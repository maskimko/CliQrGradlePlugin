/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.intropro.cliqr.exceptions.BadCredentialsException;
import com.intropro.cliqr.exceptions.ClientSslException;
import com.intropro.cliqr.exceptions.ResponseException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.ClientContextConfigurer;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class GetProcessorImpl implements GetProcessor {

    private URL targetUrl;
    private final Logger logger;
    private HttpClient client;
    private HttpContext context;
    private final ResponseHelper responseHelper;

    {
        logger = LoggerFactory.getLogger(this.getClass());
        responseHelper = new ResponseHelper();
    }

    public GetProcessorImpl(String host, int port, String user, String password) throws MalformedURLException, ClientSslException {
        URL cliqrUrl = new URL("https://" + host + ":" + port);
        init(cliqrUrl, user, password);
    }

    public GetProcessorImpl(URL url, String user, String password) throws ClientSslException {
        init(responseHelper.normalizeUrl(url), user, password);
    }

    public GetProcessorImpl(URL url) throws BadCredentialsException, ClientSslException {

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

    private void init(URL targetURL, String user, String password) throws ClientSslException {
        this.targetUrl = targetURL;
        logger.debug("Initializing " + this.getClass().getName() + " with target URL " + targetURL.toString());
        HttpHost htHost = new HttpHost(targetUrl.getHost(), targetUrl.getPort(), targetUrl.getProtocol());

        AuthCache aCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        aCache.put(htHost, basicAuth);

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user, password);
        BasicCredentialsProvider cProvider = new BasicCredentialsProvider();
        cProvider.setCredentials(new AuthScope(htHost), creds);
        logger.debug("Credential provider: " + cProvider.toString());

        context = new BasicHttpContext();
        ClientContextConfigurer cliCon = new ClientContextConfigurer(context);
        cliCon.setCredentialsProvider(cProvider);
        context.setAttribute(ClientContext.AUTH_CACHE, aCache);
        SSLSocketFactory sslConnectionSocketFactory = null;
        try {
            sslConnectionSocketFactory = new SSLSocketFactory(new TrustSelfSignedStrategy(), new CliQrHostnameVerifier());
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
        Scheme https = new Scheme("https", 443, sslConnectionSocketFactory);
        defClient.getConnectionManager().getSchemeRegistry().register(https);
        defClient.setTargetAuthenticationStrategy(new TargetAuthenticationStrategy());
        client = defClient;
    }

    @Override
    public String getResponse(boolean pretty, String apiPath) throws ResponseException {
        responseHelper.validateApiPath(apiPath);
        String result = null;
        logger.debug("Creating GET request to the URL " + targetUrl.toString() + apiPath);
        HttpGet getRequest = new HttpGet(targetUrl.toString() + apiPath);
        getRequest.setHeader(new BasicHeader("X-CLIQR-API-KEY-AUTH", "true"));
        getRequest.setHeader(new BasicHeader("Content-Type", "application/json"));
        getRequest.setHeader(new BasicHeader("Accept", "application/json"));
        try {
            HttpResponse getResponse = client.execute(getRequest, context);
            HttpEntity entity = getResponse.getEntity();
            logger.debug("Content-type: " + entity.getContentType().getName() + "=" + entity.getContentType().getValue());
            logger.debug("Status line: " + getResponse.getStatusLine());
            int statusCode = getResponse.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200:
                    logger.debug("Got a successful http response " + getResponse.getStatusLine());
                    break;
                case 401:
                    throw new BadCredentialsException("Bad credentials. Response status: " + getResponse.getStatusLine());
                default:
                    throw new ResponseException("Response is not OK. Response status: " + getResponse.getStatusLine());
            }
            if (getResponse.getStatusLine().getStatusCode() != 200) {
                throw new BadCredentialsException("Bad credentials. Response status: " + getResponse.getStatusLine());
            }
            result = responseHelper.readJson(entity.getContent(), pretty);
            logger.debug("Got a response " + result);
            EntityUtils.consume(entity);

        } catch (JsonSyntaxException | MalformedJsonException ex) {
            logger.error("Cannot read resulting JSON ", ex);
        } catch (IOException ex) {
            logger.error("Cannot execute get request ", ex);
        }

        return result;
    }

    @Override
    public String getResponse(String apiPath) throws ResponseException {
        return getResponse(false, apiPath);
    }

    @Override
    public String getResponse(URL url) throws ResponseException {
        String apiPath = url.getPath();
        return getResponse(true, apiPath);
    }

}
