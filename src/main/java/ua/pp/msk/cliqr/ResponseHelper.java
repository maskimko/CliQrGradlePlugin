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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import ua.pp.msk.cliqr.exceptions.ApiPathException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class ResponseHelper {

    private final Logger logger;

    public ResponseHelper() {
        logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("Created " + this.getClass().getCanonicalName());
    }

    void validateApiPath(String path) throws ApiPathException {
        logger.debug("Validating path " + path);
        Pattern apiPattern = Pattern.compile("^\\/v1\\/[a-zA-Z0-9?&/]+", Pattern.DOTALL);
        Matcher matcher = apiPattern.matcher(path);
        if (!matcher.matches()) {
            throw new ApiPathException("Bad API REST path: " + path);
        }
    }

    JsonObject readJson(String jsonString) throws MalformedJsonException, JsonSyntaxException{
        JsonParser jp = new JsonParser();
        JsonElement parsedJson = jp.parse(jsonString);
        return parsedJson.getAsJsonObject();
    }
    
    String readJson(InputStream is, boolean pretty) throws MalformedJsonException, JsonSyntaxException {
        Gson gson = null;
        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            new GsonBuilder().create();
        }
        JsonParser jp = new JsonParser();
        JsonElement parsedJson = jp.parse(new InputStreamReader(is));
        return (gson == null) ? null : gson.toJson(parsedJson);
    }
    
    String jsonToString(JsonObject json) {
         Gson gson = new GsonBuilder().create();
       return gson.toJson(json);
    }

    URL normalizeUrl(URL url) {
        logger.debug("Normalizing URL " + url.toString());
        URL normalUrl = null;
        try {
            String protocol = url.getProtocol();
            int port = url.getPort();
            if (protocol.equals("https") && port == -1) {
                port = 443;
            }
            normalUrl = new URL(protocol + "://" + url.getHost() + ":" + port);
        } catch (MalformedURLException ex) {
            logger.error("You should never see this", ex);
        }
        return normalUrl;
    }

}
