/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

import com.intropro.cliqr.exceptions.ClientSslException;
import com.intropro.cliqr.exceptions.ResponseException;
import java.net.MalformedURLException;

/**
 *
 * @author maskimko
 */
public interface AppInfo {

    public void getAppInfo(int appNumber) throws MalformedURLException, ClientSslException, ResponseException;
    public void getAppInfo() throws MalformedURLException, ClientSslException, ResponseException;
    public void getAppInfo(String url) throws MalformedURLException, ClientSslException, ResponseException;
}
