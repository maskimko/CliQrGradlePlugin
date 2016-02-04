/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;
import com.intropro.cliqr.exceptions.BadCredentialsException;
import com.intropro.cliqr.exceptions.ClientSslException;
import com.intropro.cliqr.exceptions.ResponseException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.ClientContextConfigurer;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class PostProcessorImpl implements PostProcessor {

    private URL targetUrl;
    private final Logger logger;
    private HttpClient client;
    private final ResponseHelper responseHelper;
    private HttpContext context;

    {
        logger = LoggerFactory.getLogger(this.getClass());
        responseHelper = new ResponseHelper();
    }

    public PostProcessorImpl(URL url) throws BadCredentialsException, ClientSslException {
        if (url.getAuthority() != null) {
            logger.debug("Analyzing URL authority " + url.getAuthority());
            String[] credsAndHost = url.getAuthority().split("@");
            if (credsAndHost.length != 2) {
                throw new BadCredentialsException("Cannot get credentials from string " + url.getAuthority());
            }
            String[] creds = credsAndHost[0].split(":");
            if (creds.length != 2) {
                throw new BadCredentialsException("Cannot get username and password from string " + credsAndHost[0]);
            }
            logger.debug("Got username: " + creds[0]);

            URL cliqrUrl = responseHelper.normalizeUrl(url);
            init(cliqrUrl, creds[0], creds[1]);
        } else {
            throw new BadCredentialsException("You must provide credentials in your URL to be able to tuse this constructor");
        }
    }

    public PostProcessorImpl(URL url, String username, String password) throws ClientSslException {
        init(responseHelper.normalizeUrl(url), username, password);
    }

    public PostProcessorImpl(String host, int port, String username, String password) throws ClientSslException, MalformedURLException {
        URL cliqrUrl = new URL("https://" + host + ":" + port);
        init(cliqrUrl, username, password);
    }

    private void init(URL url, String user, String password) throws ClientSslException {
        this.targetUrl = url;
        HttpHost htHost = new HttpHost(targetUrl.getHost(), targetUrl.getPort(), targetUrl.getProtocol());

        BasicAuthCache aCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme(ChallengeState.TARGET);
        aCache.put(htHost, basicAuth);
        

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);
        BasicCredentialsProvider cProvider = new BasicCredentialsProvider();
        cProvider.setCredentials(new AuthScope(htHost), creds);
        logger.debug("Credential provider: " + cProvider.toString());

        context = new BasicHttpContext();
        ClientContextConfigurer cliCon = new ClientContextConfigurer(context);
        context.setAttribute(ClientContext.AUTH_CACHE, aCache);
        //context.setAuthCache(aCache);
        cliCon.setCredentialsProvider(cProvider);
        //context.setCredentialsProvider(cProvider);
        SSLSocketFactory sslSocketFactory = null;
        try {
            //SSLContext trustySslContext = SSLContextBuilder.create().loadTrustMaterial( new TrustSelfSignedStrategy()).build();
            //sslConnectionSocketFactory = new SSLConnectionSocketFactory(trustySslContext, new CliQrHostnameVerifier());
            sslSocketFactory = new SSLSocketFactory(new TrustSelfSignedStrategy(), new CliQrHostnameVerifier());
       } catch (KeyManagementException ex) {
            logger.error("Cannot manage secure keys", ex);
            throw new ClientSslException("Cannot manage secure keys", ex);
        } catch (KeyStoreException ex) {
            logger.error("Cannot build SSL context due to KeyStore error", ex);
            throw new ClientSslException("Cannot build SSL context due to KeyStore error", ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Unsupported security algorithm", ex);
            throw new ClientSslException("Unsupported security algorithm", ex);
        } catch (UnrecoverableKeyException ex) {
            logger.error("Unrecoverable key", ex);
            throw new ClientSslException("Unrecoverrable key", ex);
        }
        
        DefaultHttpClient defClient = new DefaultHttpClient();
        defClient.setRedirectStrategy(new CliQrRedirectStrategy());
        defClient.setCredentialsProvider(cProvider);
        Scheme https = new Scheme("https", 443, sslSocketFactory);
        defClient.getConnectionManager().getSchemeRegistry().register(https);
        defClient.setTargetAuthenticationStrategy(new TargetAuthenticationStrategy());
//        client = HttpClientBuilder.create()
//                .setSSLSocketFactory(sslSocketFactory)
//                .setRedirectStrategy(new CliQrRedirectStrategy())
//                .setDefaultCredentialsProvider(cProvider)
//                .setTargetAuthenticationStrategy(TargetAuthenticationStrategy.INSTANCE)
//                .build();
        client = defClient;
    }

    @Override
    public String getResponse(boolean pretty, String apiPath, JsonObject json) throws ResponseException {
        String response = null;
        try {
            HttpPost httpPost = new HttpPost(targetUrl.toString() + apiPath);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("X-CLIQR-API-KEY-AUTH", "true");
            StringEntity sEntity = new StringEntity(responseHelper.jsonToString(json));
            httpPost.setEntity(sEntity);
            HttpResponse  postResponse = client.execute(httpPost, context);
                logger.debug("Status line: " + postResponse.getStatusLine().toString());
                int statusCode = postResponse.getStatusLine().getStatusCode();
                switch (statusCode) {
                    case 200:
                        logger.debug("Got a successful http response " + postResponse.getStatusLine());
                        break;
                    case 201:
                        logger.debug("Created! Got a successful http response " + postResponse.getStatusLine());
                        break;
                    case 401:
                        throw new BadCredentialsException("Bad credentials. Response status: " + postResponse.getStatusLine());
                    default:
                        throw new ResponseException("Response is not OK. Response status: " + postResponse.getStatusLine());
                }
                HttpEntity entity = postResponse.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity);
         
        } catch (UnsupportedEncodingException ex) {
            logger.error("Encoding is unsuported ", ex);
        } catch (IOException ex) {
            logger.error("Got IO excepption ", ex);
        }
        return response;
    }

    @Override
    public String getResponse(String apiPath, String json) throws ResponseException {
        return getResponse(false, apiPath, json);
    }

    @Override
    public String getResponse(boolean pretty, String apiPath, String entity) throws ResponseException {
        String result = null;
        try {
            JsonObject jo = responseHelper.readJson(entity);
            result = getResponse(pretty, apiPath, jo);
        } catch (MalformedJsonException ex) {
            throw new ResponseException("Malformed Json", ex);
        }
        return result;
    }
}
