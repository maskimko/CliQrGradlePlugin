/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr.validators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.pp.msk.cliqr.Environment;
import ua.pp.msk.cliqr.exceptions.MissingParameterException;
import ua.pp.msk.cliqr.exceptions.ParameterException;
import ua.pp.msk.cliqr.exceptions.ParseException;
import ua.pp.msk.cliqr.exceptions.WrongParameterException;
import java.util.Enumeration;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobValidator extends Validator {

    private Logger logger;

    public JobValidator() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

        @Override
    public boolean validate(String json) throws ParseException, ParameterException {
        
        JsonParser jsonParser = new JsonParser();
        JsonElement parsedElement = jsonParser.parse(json);
        if (parsedElement == null) {
            throw new MissingParameterException("Cannot parse string as a json element. It is null." + json);
        }
        if (!parsedElement.isJsonObject()){
            throw new WrongParameterException("Seems to be that privided json string cannot be parsed as a json object. " + json);
        }
        JsonObject jo = parsedElement.getAsJsonObject();
        return validate(jo);
        
    }

    @Override
    public boolean validate(JsonObject json) throws ParameterException {
        if (!json.has(SERVICETIERID)) {
            throw new MissingParameterException("Missing service tier id element");
        } else {
            if (json.get(SERVICETIERID).isJsonNull() || json.get(SERVICETIERID).getAsString().isEmpty()) {
                throw new MissingParameterException("Service tier id element is empty or null value");
            }
        }
        if (!json.has(NAME)) {
            throw new MissingParameterException("Missing name element");
        } else {
            if (json.get(NAME).isJsonNull() || json.get(NAME).getAsString().isEmpty()) {
                throw new MissingParameterException("Name element is empty or null value");
            }
        }
        if (!json.has(ENVIRONMENT)) {
            throw new MissingParameterException("Missing environment element");
        } else {
            if (json.get(ENVIRONMENT).isJsonNull() || json.get(ENVIRONMENT).getAsString().isEmpty()) {
                throw new MissingParameterException("Environment element is empty or null value");
            }
            Environment environment = Environment.getEnvironment();
            String env = json.get(ENVIRONMENT).getAsString();
            if (!environment.contains(env)) {
                StringBuilder sb = new StringBuilder("{");
                Enumeration<String> environments = environment.getEnvironments();
                while (environments.hasMoreElements()) {
                    sb.append(environments.nextElement());
                    if (environments.hasMoreElements()) {
                        sb.append(", ");
                    }
                }
                String supportedEnvs = sb.append("}").toString();
                throw new MissingParameterException("Wrong environment name: " + env + " Supported environments are: " + supportedEnvs);
            }
        }
        if (!json.has(PARAMETERS)) {
            throw new MissingParameterException("Missing parameters element");
        } else {
            if (json.get(PARAMETERS).isJsonNull()) {
                throw new MissingParameterException("Parameters element is empty or null value");
            }
            if (json.has(CLOUDPARAMETERS)) {
                validateCloudParams(json.get(CLOUDPARAMETERS));
            } else {
                throw new MissingParameterException("Parameters element must contain here " + CLOUDPARAMETERS + " property");
            }
            if (json.has(APPLICATIONPARAMENTERS)) {
                validateAppParams(json.get(APPLICATIONPARAMENTERS));
            } else {
                throw new MissingParameterException("Parameters element must contain here " + APPLICATIONPARAMENTERS + " property");
            }
        }
        if (!json.has(JOBS)) {
            throw new MissingParameterException("Missing jobs element");
        } else {
            if (json.get(JOBS).isJsonNull()) {
                throw new MissingParameterException("Jobs element is empty or null value");
            }
        }
        return true;
    }

    private boolean validateInstance(String instance) {
        //TODO implement this 
        logger.debug("Validating vm instance size: " + instance);
        return true;
    }

    private boolean validateNVJson(JsonElement je) throws ParameterException {
        if (!je.isJsonObject()) {
            throw new WrongParameterException("This json element " + je.getAsString()
                    + "\nshould look like a json object with 'name' and 'value' String parameters");
        } else {
            JsonObject jo = je.getAsJsonObject();
            if (!jo.has(NAME)) {
                throw new WrongParameterException("This object:" + jo.getAsString() + " should contain a name property");
            } else {
                if (!jo.get(NAME).isJsonPrimitive()) {
                    throw new WrongParameterException("This: " + jo.get(NAME).getAsString() + " should be a json primitive");
                }
            }
            if (!jo.has(VALUE)) {
                throw new WrongParameterException("This object:" + jo.getAsString() + " should contain a value property");
            } else {
                if (!jo.get(VALUE).isJsonPrimitive()) {
                    throw new WrongParameterException("This: " + jo.get(VALUE).getAsString() + " should be a json primitive");
                }
            }
        }
        return true;
    }

    private boolean validateCloudParams(JsonElement je) throws ParameterException {
        if (je.isJsonObject()) {
            JsonObject jo = je.getAsJsonObject();
            if (!jo.has(CLOUD)) {
                throw new MissingParameterException("Missing Cloud propery in cloud parameters");
            }
            if (!jo.has(INSTANCE)) {
                throw new MissingParameterException("Missing instance property in cloud parameters");
            } else {
                validateInstance(jo.get(INSTANCE).getAsString());
            }
            if (!jo.has(CLOUDPROPERTIES)) {
                if (!jo.get(CLOUDPROPERTIES).isJsonArray()) {
                    throw new WrongParameterException("Cloud properties  should be a json array");
                } else {
                    JsonArray ja = jo.get(CLOUDPROPERTIES).getAsJsonArray();
                    Iterator<JsonElement> iterator = ja.iterator();
                    while (iterator.hasNext()) {
                        validateNVJson(iterator.next());
                    }
                }
            }

        } else {
            throw new WrongParameterException("Cloud parameters should be a JSON Object");
        }
        return true;
    }

    private boolean validateAppParams(JsonElement je) throws ParameterException {
        if (!je.isJsonArray()) {
            throw new WrongParameterException("Application parameters should be a json array");
        } else {
            JsonArray ja = je.getAsJsonArray();
            Iterator<JsonElement> iterator = ja.iterator();
            while (iterator.hasNext()) {
                validateNVJson(iterator.next());
            }
        }
        return true;
    }

    private boolean validateJobs(JsonElement je) throws ParameterException {
        if (!je.isJsonArray()) {
            throw new WrongParameterException("Jobs property  should be a json array");
        } else {
            JsonArray ja = je.getAsJsonArray();
            Iterator<JsonElement> iterator = ja.iterator();
            while (iterator.hasNext()) {
                validateJob(iterator.next());
            }
        }
        return true;
    }

    private boolean validateJob(JsonElement je) throws ParameterException{
        if (je.isJsonObject()) {
            JsonObject jo = je.getAsJsonObject();
            if (!jo.has(SERVICETIERID)) {
                throw new MissingParameterException("Missing service tier id element");
            } else {
                if (jo.get(SERVICETIERID).isJsonNull() || jo.get(SERVICETIERID).getAsString().isEmpty()) {
                    throw new MissingParameterException("Service tier id element is empty or null value");
                }
            }

            if (!jo.has(PARAMETERS)) {
                throw new MissingParameterException("Missing parameters element in job " + jo.getAsString());
            } else {
                if (jo.get(PARAMETERS).isJsonNull()) {
                    throw new MissingParameterException("Parameters element is empty or null value");
                }
                if (jo.has(CLOUDPARAMETERS)) {
                    validateCloudParams(jo.get(CLOUDPARAMETERS));
                } else {
                    throw new MissingParameterException("Parameters element must contain here " + CLOUDPARAMETERS + " property");
                }
                if (jo.has(APPLICATIONPARAMENTERS)) {
                    validateAppParams(jo.get(APPLICATIONPARAMENTERS));
                } 
            }
        } else {
            throw new WrongParameterException("Job must be a JsonObject");
        }

        return true;
    }

}
