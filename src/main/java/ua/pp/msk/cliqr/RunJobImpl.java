/*
 * Copyright 2016 Maksym Shkolnyi self project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.pp.msk.cliqr;

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
            properties.put(Validator.SERVICETIERID, "" + serviceTierId);
            properties.put(APPID, "" + jobId);
            startJob(properties);
        } catch (FileNotFoundException ex) {
            throw new RunJobException("Cannot run the job, because specified file " + file.getAbsolutePath() + "was not found", ex);
        } catch (IOException ex) {
            throw new RunJobException("Cannot run the job due to IO exception ", ex);
        }
    }

    @Override
    public String startJob(Map<String, String> options) throws MissingParameterException {

        if (options.isEmpty()) {
            logger.warn("We received an empty options map");
        } else {
            logger.debug("These options have been passed to the startJob method");
            options.forEach((k, v) -> {
                logger.debug(k + " = " + v);
            });
        }
        JsonObject jo = new JsonObject();

        if (!options.containsKey(APPNAME)) {
            throw new MissingParameterException("You should provide application name ");
        }
        if (!options.containsKey(APPID)) {
            throw new MissingParameterException("You should provide application id ");
        }
        String sti = (options.containsKey(Validator.SERVICETIERID)) ? options.get(Validator.SERVICETIERID) : String.format("%s-%d", options.get(APPNAME), Integer.parseInt(options.get(APPID)));;
        jo.addProperty(Validator.SERVICETIERID, sti);
        options.put(Validator.SERVICETIERID, sti);

        if (!options.containsKey(Validator.VERSION)) {
            throw new MissingParameterException("You should provide application Version id");
        }
        if (!options.containsKey(Validator.CLOUD)) {
            throw new MissingParameterException("You must provide cloud name");
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

        String generatedName = String.format("%s-%s-%s-%d", options.get(APPNAME) , options.get(APPID), (options.get(Validator.ENVNAME) == null) ? "GeneratedEnvName": options.get(Validator.ENVNAME), System.currentTimeMillis()/1000);
        jo.addProperty(Validator.NAME, generatedName);
        jo.addProperty(Validator.ENVIRONMENT, options.get(Validator.ENVIRONMENT));
        jo.addProperty(Validator.VERSION, options.get(Validator.VERSION));

        JsonArray cloudProperties = new JsonArray();
        JsonArray applicationParameters = new JsonArray();
        JsonObject cloudParameters = new JsonObject();

        cloudProperties.add(getJsonPairObject(Validator.USERCLUSTERNAME, options.get(Validator.USERCLUSTERNAME)));
        cloudProperties.add(getJsonPairObject(Validator.USERDATACENTERNAME, options.get(Validator.USERDATACENTERNAME)));
        cloudProperties.add(getJsonPairObject(Validator.USERDATASTORECLUSTER, options.get(Validator.USERDATASTORECLUSTER)));
        cloudProperties.add(getJsonPairObject(PIP, options.get(PIP)));
        cloudProperties.add(getJsonPairObject(NETWORK, options.get(NETWORK)));
        cloudParameters.addProperty(Validator.CLOUD, options.get(Validator.CLOUD));
        cloudParameters.addProperty(Validator.INSTANCE, options.get(Validator.INSTANCE));
        cloudParameters.add(Validator.CLOUDPROPERTIES, cloudProperties);

        //Will remove this
//        options.entrySet().stream().filter((entry) -> (isSecondaryKey(entry.getKey()))).forEach((entry) -> {
//            applicationParameters.add(getJsonPairObject(entry));
//        });
        options.forEach((k, v) -> {
            applicationParameters.add(getJsonPairObject(k, v));
        });

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
            String jsti = String.format("%s-%d", options.get(APPNAME), Integer.parseInt(options.get(APPID)) + 1);
            jobObj.addProperty(Validator.SERVICETIERID, jsti);
        }
        jobs.add(jobObj);
        jo.add(Validator.JOBS, jobs);

        jo.add(Validator.PARAMETERS, parameters);
        logger.debug("Using given options map we construct this json object\n" + new GsonBuilder().setPrettyPrinting().create().toJson(jo));
        return launchJob(jo);
    }

    private String launchJob(JsonObject jo) {
        String response = null;
        if (processor != null) {
            try {
                response = processor.getResponse(true, API_PREFIX, jo);
                if (processor.getLocation()!=null){
                    if (response!=null){
                        response = response + processor.getLocation().toString();
                    } else {
                        response = processor.getLocation().toString();
                    }
                }
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

    @Deprecated
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
        boolean result;
        switch (key) {
            case Validator.SERVICETIERID:
            case Validator.NAME:
            case Validator.ENVIRONMENT:
            case Validator.USERCLUSTERNAME:
            case Validator.USERDATACENTERNAME:
            case Validator.USERDATASTORECLUSTER:
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
    public String startJob(int appId, String serviceTierId, String appName, String appVersion,
            String cloudName, String network, String env,
            String inst, boolean apip, Map<String, String> envPairs) throws MissingParameterException {

        if (!(serviceTierId != null && serviceTierId.length() > 0)) {
            envPairs.put(Validator.SERVICETIERID, serviceTierId);
        }
        envPairs.put(Validator.NAME, appName);
        envPairs.put(Validator.CLOUD, cloudName);
        envPairs.put(NETWORK, network);
        envPairs.put(Validator.ENVIRONMENT, env);
        envPairs.put(Validator.INSTANCE, inst);
        envPairs.put(PIP, "" + apip);
        envPairs.put(Validator.VERSION, appVersion);
        envPairs.put(APPID, "" + appId);
        envPairs.put(APPNAME, appName);
        return startJob(envPairs);
    }
}
