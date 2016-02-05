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
    public String getAppInfo(int appNumber) throws MalformedURLException, ClientSslException, ResponseException {
        return getInfo("apps/"+appNumber);
    }

    @Override
    public String getAppInfo() throws MalformedURLException, ClientSslException, ResponseException {
       return getInfo("apps");
    }

    @Override
    public String getAppInfo(String url) throws MalformedURLException, ClientSslException, ResponseException {
       URL u = new URL(url);
        GetProcessor gp = new GetProcessorImpl(new URL("https://" + host), user, apiKey);
        String response = gp.getResponse(u);
       return response;
    }
}
