/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.MissingParameterException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.cliqr.exceptions.RunJobException;
import ua.pp.msk.cliqr.job.JobFilter;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class CliQrController {

    // public static final String cliqrUrl = "http://cliqrmee-msdc02.ds.dtveng.net";
    public static Logger logger = LoggerFactory.getLogger(CliQrController.class);
    private static Options opts;
    private static JobInfo ji;
    private static AppInfo ai;
    private static RunJob rj;

    private static Options configureOptions() {
        opts = new Options();

        opts.addOption("H", "host", true, "CliQr host");
        opts.addOption("u", "user", true, "user login");
        opts.addOption("U", "url", true, "URL for API call");
        opts.addOption("k", "apiKey", true, "API keys for CliQr user");
        opts.addOption("j", "job", true, "Job Id number");
        opts.addOption("jobs", false, "Get information about all available jobs");
        //TODO Added more informative description
        opts.addOption("filter", true, "key:value,... Filter the output ");
        opts.addOption("output", true, "File to store the output");
        opts.addOption("i", "info", false, "Info mode");
        opts.addOption("apps", false, "Get information about all available apps");
        opts.addOption("a", "app", true, "Application id number");
        opts.addOption("r", "run", false, "Run job mode ");
        opts.addOption("J", "jsonfile", true, "Fetch input from json file");
        opts.addOption("I", "interactive", false, "Interactively configure the job");
        opts.addOption("P", "propertyfile", true, "Fetch input from property file");
        opts.addOption("m", "map", true, "Parse key-value map of job prarmeters to apply [key=value&key=value]");
        opts.addOption("h", "help", false, "Print usage info");

        return opts;
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar CliqrController.jar <host> <user> <apiKey> [job] [help]", opts);
    }

    public static void main(String[] args) {
        //"cicdgroup_5", "16EE49C5337B327D"
        int jobNum = 0;
        opts = configureOptions();
        try {
            CommandLineParser parser = new GnuParser();
            CommandLine cmd = parser.parse(opts, args);
            if (cmd.hasOption("help")) {
                printHelp();
                System.exit(0);
            }
            if (!(cmd.hasOption("host") || cmd.hasOption("url")) || !cmd.hasOption("user") || !cmd.hasOption("apiKey")) {
                logger.error("Wrong usage");
                printHelp();
                System.exit(1);
            }

            //TODO implement POST
            if (cmd.hasOption("run")) {

                if (cmd.hasOption("job")) {
                    try {
                        jobNum = Integer.parseInt(cmd.getOptionValue("job"));
                    } catch (NumberFormatException ex) {
                        logger.error("Bad job number format: " + cmd.getOptionValue("job"), ex);
                        System.exit(1);
                    }
                }
                //Ugly stratedy pattern casting
                String hst = cmd.getOptionValue("host");
                String usr = cmd.getOptionValue("user");
                String ak = cmd.getOptionValue("apiKey");
                RunJobImpl rji = new RunJobImpl(hst, usr, ak);
                URL cqUrl = new URL("https://" + hst);
                rji.setProcessor(new PostProcessorImpl(cqUrl, usr, ak));
                rj = rji;

                if (cmd.hasOption("interactive")) {
                    if (cmd.hasOption("job")) {
                        rj.startJob(jobNum);
                    } else {
                        logger.error("--job option is mandatory for running job in interactive mode");
                        System.exit(1);
                    }
                } else if (cmd.hasOption("jsonfile")) {
                    rj.startJob(cmd.getOptionValue("jsonfile"));
                } else if (cmd.hasOption("propertyfile")) {
                    if (cmd.hasOption("job")) {
                        rj.startJob(jobNum, cmd.getOptionValue("propertyfile"));
                    } else {
                        logger.error("--job option is mandatory for running job using property file");
                        System.exit(1);
                    }
                } else if (cmd.hasOption("map")) {
                    if (cmd.hasOption("job")) {
                        String[] params = cmd.getOptionValue("map").split("&");
                        Map<String, String> optionMap = new HashMap<>();
                        for (String param : params) {
                            String[] pair = param.split("=");
                            if (pair.length == 2) {
                                optionMap.put(pair[0], pair[1]);
                            } else {
                                logger.warn("Ivalid parameter \"" + param + "\" Skipping. Must be like key=value");
                            }
                        }
                        rj.startJob(jobNum, optionMap);
                        throw new UnsupportedOperationException("it is not supported to run job with key value params for now");
                    } else {
                        logger.error("--job option is mandatory for running job in interactive mode");
                        System.exit(1);
                    }
                } else {
                    rj.startJob();
                }
            } else if (cmd.hasOption("i")) {

                if (cmd.hasOption("job")) {
                    try {
                        ji = new JobInfoImpl(cmd.getOptionValue("host"), cmd.getOptionValue("user"), cmd.getOptionValue("apiKey"));
                        jobNum = Integer.parseInt(cmd.getOptionValue("job"));
                        ji.getJobInfo(jobNum);
                    } catch (NumberFormatException ex) {
                        logger.error("Bad job number format: " + cmd.getOptionValue("job"), ex);
                        System.exit(1);
                    } catch (ResponseException | ClientSslException | MalformedURLException ex) {
                        logger.error(ex.getMessage(), ex);
                        System.exit(1);
                    }
                    System.exit(0);
                } else if (cmd.hasOption("jobs")) {
                    try {
                        ji = new JobInfoImpl(cmd.getOptionValue("host"), cmd.getOptionValue("user"), cmd.getOptionValue("apiKey"));
                        List<JobFilter> jfl = new LinkedList<>();
                        if (cmd.hasOption("filter")) {
                            String filterString = cmd.getOptionValue("filter");
                            String[] constraints = filterString.split(",");
                            for (String cstr : constraints) {
                                String[] keyVal = cstr.split(":");
                                if (keyVal.length != 2) {
                                    LoggerFactory.getLogger(CliQrController.class).error(String.format("Cannot parse filter at %2$s from %1$s Ignoring key %2$s", filterString, cstr));
                                    continue;
                                }
                                jfl.add(new JobFilter(keyVal[0], keyVal[1]));
                            }
                        }
                        if (cmd.hasOption("output")) {

                            try (PrintStream lps = new PrintStream(cmd.getOptionValue("output"))) {
                                ji.setOutputStream(lps);

                                if (cmd.hasOption("filter")) {
                                    ji.getJobInfo(jfl.toArray(new JobFilter[0]));
                                } else {
                                    ji.getJobInfo();
                                }
                            } catch (FileNotFoundException ex) {
                                logger.error(ex.getMessage(), ex);
                                System.exit(2);
                            }
                        } else {
                            if (cmd.hasOption("filter")) {
                                ji.getJobInfo(jfl.toArray(new JobFilter[0]));
                            } else {
                                ji.getJobInfo();
                            }
                        }

                    } catch (ResponseException | ClientSslException | MalformedURLException ex) {
                        logger.error(ex.getMessage(), ex);
                        System.exit(1);
                    }
                } else if (cmd.hasOption("apps")) {
                    try {
                        ai = new AppInfoImpl(cmd.getOptionValue("host"), cmd.getOptionValue("user"), cmd.getOptionValue("apiKey"));
                        ai.getAppInfo();
                    } catch (ResponseException | ClientSslException | MalformedURLException ex) {
                        logger.error(ex.getMessage(), ex);
                        System.exit(1);
                    }
                } else if (cmd.hasOption("app")) {
                    try {
                        int appId = Integer.parseInt(cmd.getOptionValue("app"));
                        ai = new AppInfoImpl(cmd.getOptionValue("host"), cmd.getOptionValue("user"), cmd.getOptionValue("apiKey"));
                        ai.getAppInfo(appId);
                    } catch (NumberFormatException ex) {
                        logger.error("Bad job number format: " + cmd.getOptionValue("job"), ex);
                        System.exit(1);
                    } catch (ResponseException | ClientSslException | MalformedURLException ex) {
                        logger.error(ex.getMessage(), ex);
                        System.exit(1);
                    }
                } else if (cmd.hasOption("url")) {
                    String urlString = cmd.getOptionValue("url");
                    try {
                        URL u = new URL(urlString);
                        ai = new AppInfoImpl(u.getHost(), cmd.getOptionValue("user"), cmd.getOptionValue("apiKey"));
                        ai.getAppInfo(urlString);
                    } catch (ResponseException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }

            }
            System.out.println("Nothing to do");
        } catch (ParseException ex) {
            logger.error("Cannot parse the command line arguments");
            printHelp();
            System.exit(1);
        } catch (MalformedURLException ex) {
            logger.error("Cannot create URL from given hostname", ex);
            System.exit(2);
        } catch (ClientSslException ex) {
            logger.error("SSL error", ex);
            System.exit(3);
        } catch (RunJobException ex) {
            logger.error("Cannot run job " + ex.getMessage(), ex);
        } catch (MissingParameterException ex) {
            logger.error("Missing required parameter " + ex.getMessage(), ex);
        }

    }
}
