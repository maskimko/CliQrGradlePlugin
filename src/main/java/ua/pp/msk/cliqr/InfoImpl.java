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
