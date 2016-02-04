/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.pp.msk.cliqr.exceptions.MissingParameterException;
import ua.pp.msk.cliqr.exceptions.ParseException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.cliqr.exceptions.RunJobException;
import ua.pp.msk.cliqr.validators.Validator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class RunJobImpl implements RunJob {

    public static final String API_PREFIX = "/v1/jobs";
    private final String host;
    private final String user;
    private final String apiKey;
    private PostProcessor processor;
    private final Logger logger;

    public RunJobImpl(String host, String user, String apiKey) {
        this.host = host;
        this.user = user;
        this.apiKey = apiKey;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void startJob() {
        Gson gson = new GsonBuilder().create();
        JsonParser jp = new JsonParser();
        JsonElement parsedJson = jp.parse(new InputStreamReader(System.in));
        launchJob(parsedJson.getAsJsonObject());
    }

    @Override
    public void startJob(int jobId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startJob(String file) {
        Path jsonFPath = Paths.get(file);
        if (!Files.exists(jsonFPath)) {
            logger.error("File you provide does not exist");
            return;
        }
        if (!Files.isReadable(jsonFPath)) {
            logger.error("File " + file + " is not readable");
            return;
        }
        startJob(jsonFPath.toFile());
    }

    @Override
    public void startJob(File file) {
        try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
            JsonParser jp = new JsonParser();
            JsonElement parsedElement = jp.parse(reader);
            launchJob(parsedElement.getAsJsonObject());
        } catch (FileNotFoundException ex) {
            logger.error("Cannot find the file " + file.getAbsolutePath(), ex);
        } catch (IOException ex) {
            logger.error("Got IO exception ", ex);
        }
    }

    @Override
    public void startJob(int jobId, int serviceTierId, String file) throws RunJobException, MissingParameterException {
        Path filePath = Paths.get(file);
        startJob(jobId, serviceTierId, filePath.toFile());
    }

    @Override
    public void startJob(int jobId, int serviceTierId, File file) throws RunJobException, MissingParameterException {
        if (!file.exists()) {
            logger.error("File you provide does not exist");
            return;
        }
        if (!file.canRead()) {
            logger.error("File " + file + " is not readable");
            return;
        }
        Map<String, String> properties = new HashMap<>();
        String line = null;
        int counter = 1;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                try {
                    Map.Entry<String, String> pair = parseKV(line);
                    properties.put(pair.getKey(), pair.getValue());
                } catch (ParseException ex) {
                    logger.warn("Cannot parse line number " + counter + ": " + line);
                }
                counter++;
            }
           properties.put(Validator.SERVICETIERID, ""+serviceTierId);
            startJob(jobId, properties);
        } catch (FileNotFoundException ex) {
            throw new RunJobException("Cannot run the job, because specified file " + file.getAbsolutePath() + "was not found", ex);
        } catch (IOException ex) {
            throw new RunJobException("Cannot run the job due to IO exception ", ex);
        }
    }

    @Override
    public void startJob(int jobId, Map<String, String> options) throws MissingParameterException {
        //Logging
        if (logger.isDebugEnabled()) {
            if (options.isEmpty()) {
                logger.warn("We received an empty options map");
            } else {
                logger.debug("These options have been passed to the startJob method");
                options.forEach((k, v) -> {
                    logger.debug(k + " = " + v);
                });
            }
        }
        //End of logging
        JsonObject jo = new JsonObject();

        String sti = (options.containsKey(Validator.SERVICETIERID)) ? options.get("serviceTierId") : options.get(APPNAME) + "-" + options.get(APPID);
        jo.addProperty(Validator.SERVICETIERID, sti);
        options.put(Validator.SERVICETIERID, sti);

        if (!options.containsKey(APPNAME) && !options.containsKey(Validator.SERVICETIERID)) {
            throw new MissingParameterException("You should provide application name or service tier id");
        }
        if (!options.containsKey(APPID) && !options.containsKey(Validator.SERVICETIERID)) {
            throw new MissingParameterException("You should provide application id or service tier id");
        }

        if (!options.containsKey(Validator.CLOUD)) {
            throw new MissingParameterException("You must provide cloud name");
        }
        if (!options.containsKey(VPCID)) {
            throw new MissingParameterException("You must provide VPC ID");
        }

        if (!options.containsKey(NETWORK)) {
            throw new MissingParameterException("You must provide subnet id");
        }

        if (!options.containsKey(Validator.NAME)) {
            throw new MissingParameterException("You must provide job name");
        }
        if (!options.containsKey(Validator.ENVIRONMENT)) {
            throw new MissingParameterException("You must provide environment name");
        }

        if (!options.containsKey(Validator.INSTANCE)) {
            logger.warn("Instance ID is not defined. Will use \"m1.small\"");
        }

        if (!options.containsKey(PIP)) {
            logger.warn("Will not attach public IP");
        }

        jo.addProperty(Validator.NAME, options.get(Validator.NAME));
        jo.addProperty(Validator.ENVIRONMENT, options.get(Validator.ENVIRONMENT));
        JsonArray cloudProperties = new JsonArray();
        JsonArray applicationParameters = new JsonArray();
        JsonObject cloudParameters = new JsonObject();

        cloudProperties.add(getJsonPairObject(VPCID, options.get(VPCID)));
        cloudProperties.add(getJsonPairObject(PIP, options.get(PIP)));
        cloudProperties.add(getJsonPairObject(NETWORK, options.get(NETWORK)));

        cloudParameters.addProperty(Validator.CLOUD, options.get(Validator.CLOUD));
        cloudParameters.addProperty(Validator.INSTANCE, options.get(Validator.INSTANCE));
        cloudParameters.add(Validator.CLOUDPROPERTIES, cloudProperties);

        for (Entry<String, String> entry : options.entrySet()) {
            if (isSecondaryKey(entry.getKey())) {
                applicationParameters.add(getJsonPairObject(entry));
            }
        }

        JsonObject parameters = new JsonObject();
        parameters.add(Validator.CLOUDPARAMETERS, cloudParameters);
        parameters.add(Validator.APPLICATIONPARAMENTERS, applicationParameters);

        JsonArray jobs = new JsonArray();

        //TODO get job id
        //Requires passing job id
        JsonObject jobObj = new JsonObject();
        JsonObject jobParam = new JsonObject();
        jobParam.add(Validator.CLOUDPARAMETERS, cloudParameters);
        jobObj.add(Validator.PARAMETERS, jobParam);
        if (options.containsKey(JOBSERVICETIERID)) {
            jobObj.addProperty(Validator.SERVICETIERID, options.get(JOBSERVICETIERID));
        } else {
            jobObj.addProperty(Validator.SERVICETIERID, options.get(Validator.SERVICETIERID));
        }
        jobs.add(jobObj);
        jo.add(Validator.JOBS, jobs);

        jo.add(Validator.PARAMETERS, parameters);
        logger.debug("Using given options map we construct this json object\n" + new GsonBuilder().setPrettyPrinting().create().toJson(jo));
        launchJob(jo);
    }

    private String launchJob(JsonObject jo) {
        String response = null;
        if (processor != null) {
            try {
                response = processor.getResponse(true, API_PREFIX, jo);
            } catch (ResponseException ex) {
                logger.error("Cannot execute request", ex);
            }
        } else {
            throw new IllegalStateException("Post Porcessor has been not initialized yet");
        }
        return response;
    }

    public void setProcessor(PostProcessor processor) {
        this.processor = processor;
    }

    Map.Entry<String, String> parseKV(String line) throws ParseException {
        String[] pair = line.split("=");
        if (pair.length != 2) {
            throw new ParseException("Cannot split line " + line + "to key value pair using = separator");
        }
        return new AbstractMap.SimpleEntry<>(pair[0], pair[1]);
    }

    private JsonObject getJsonPairObject(Map.Entry<String, String> entry) {
        return getJsonPairObject(entry.getKey(), entry.getValue());
    }

    private JsonObject getJsonPairObject(String key, String value) {
        JsonObject jsonPair = new JsonObject();
        jsonPair.addProperty(Validator.NAME, key);
        jsonPair.addProperty(Validator.VALUE, value);
        return jsonPair;
    }

    private boolean isSecondaryKey(String key) {
        boolean result = true;
        switch (key) {
            case Validator.SERVICETIERID:
            case Validator.NAME:
            case Validator.ENVIRONMENT:
            case VPCID:
            case PIP:
            case NETWORK:
            case JOBSERVICETIERID:
            case Validator.CLOUD:
            case Validator.INSTANCE:
                result = false;
                break;
            default:
                result = true;
        }
        return result;
    }

    @Override
    public void startJob(int jobId, String serviceTierId, String appName, 
            String cloudName, String vpcId, String network, String env, 
            String inst, boolean apip, Map<String, String> envPairs) throws  MissingParameterException {
        
        envPairs.put(Validator.SERVICETIERID, serviceTierId);
        envPairs.put(Validator.NAME, appName);
        envPairs.put(Validator.CLOUD, cloudName);
        envPairs.put(VPCID, vpcId);
        envPairs.put(NETWORK, network);
        envPairs.put(Validator.ENVIRONMENT, env);
        envPairs.put(Validator.INSTANCE, inst);
        envPairs.put(PIP, ""+apip);
        startJob(jobId, envPairs);
    }
}
