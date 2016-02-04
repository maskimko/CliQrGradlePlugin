/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import java.net.MalformedURLException;

/**
 *
 * @author maskimko
 */
public interface AppInfo {

    public String getAppInfo(int appNumber) throws MalformedURLException, ClientSslException, ResponseException;
    public String getAppInfo() throws MalformedURLException, ClientSslException, ResponseException;
    public String getAppInfo(String url) throws MalformedURLException, ClientSslException, ResponseException;
}
