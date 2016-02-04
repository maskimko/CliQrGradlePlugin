/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

import com.intropro.cliqr.exceptions.ResponseException;
import java.net.URL;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public interface GetProcessor {

    public String getResponse(boolean pretty, String apiPath) throws ResponseException;

    /**
     * By default response should be not pretty (one line String)
     *
     * @param apiPath
     * @return JSON string
     * @throws com.intropro.cliqr.exceptions.ApiPathException when apiPass does
     * not pass the regex validation
     * @throws com.intropro.cliqr.exceptions.BadCredentialsException when cannot
     * log in using given credentials
     */
    public String getResponse(String apiPath) throws ResponseException;

    public String getResponse(URL url) throws ResponseException;
}
