/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.intropro.cliqr;

import com.intropro.cliqr.exceptions.ClientSslException;
import com.intropro.cliqr.exceptions.ResponseException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public abstract class InfoImpl {

    protected final String host;
    protected final String user;
    protected final String apiKey;

    public InfoImpl(String host, String user, String apiKey) {
        this.host = host;
        this.user = user;
        this.apiKey = apiKey;
    }
    
    protected String getInfo(String suffix) throws MalformedURLException, ClientSslException, ResponseException {
        URL targetUrl = new URL("https://" + host);
        GetProcessor gp = new GetProcessorImpl(targetUrl, user, apiKey);
        String response = gp.getResponse(true, "/v1/"+ suffix);
        return response;
    }
}
