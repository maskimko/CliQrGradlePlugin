/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.intropro.cliqr.exceptions.ApiPathException;
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
