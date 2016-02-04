/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.cliqr;

import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class AppInfoImpl extends InfoImpl implements AppInfo{

    public AppInfoImpl(String host, String user, String apiKey) {
        super(host, user, apiKey);
    }

     @Override
    public void getAppInfo(int appNumber) throws MalformedURLException, ClientSslException, ResponseException {
        System.out.println(getInfo("apps/"+appNumber));
    }

    @Override
    public void getAppInfo() throws MalformedURLException, ClientSslException, ResponseException {
        System.out.println(getInfo("apps"));
    }

    @Override
    public void getAppInfo(String url) throws MalformedURLException, ClientSslException, ResponseException {
       URL u = new URL(url);
        GetProcessor gp = new GetProcessorImpl(new URL("https://" + host), user, apiKey);
        String response = gp.getResponse(u);
        System.out.println(response);
    }
}
