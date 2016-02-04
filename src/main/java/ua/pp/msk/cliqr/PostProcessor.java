/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import com.google.gson.JsonObject;
import ua.pp.msk.cliqr.exceptions.ResponseException;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public interface PostProcessor {

    public String getResponse(boolean pretty, String apiPath, JsonObject json) throws ResponseException;

    public String getResponse(boolean pretty, String apiPath, String entity) throws ResponseException;

    /**
     * By default response should be not pretty (one line String)
     *
     * @param apiPath
     * @param entity JSON String
     * @return JSON string
     * @throws ua.pp.msk.cliqr.exceptions.ResponseException General response exception
     */
    public String getResponse(String apiPath, String entity) throws ResponseException;
}
