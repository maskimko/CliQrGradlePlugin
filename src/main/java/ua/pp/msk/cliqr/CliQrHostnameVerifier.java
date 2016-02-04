/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.cliqr;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class CliQrHostnameVerifier implements X509HostnameVerifier, HostnameVerifier {

    private final Logger logger = LoggerFactory.getLogger(CliQrHostnameVerifier.class); 
    
    @Override
    public boolean verify(String host, SSLSession sslSession) {
        try {
        Certificate[] peerCertificates = sslSession.getPeerCertificates();
        for ( Certificate cert : peerCertificates) {
        verify(host, (X509Certificate) cert);
        }
        } catch (SSLException ex) {
            logger.warn("Cannot analize SSL certificates", ex );
        }
        return true;
    }
    
    @Override
    public final void verify(String host, X509Certificate cert) throws SSLException{
        Principal subjectDN = cert.getSubjectDN();
        try {
        Collection<List<?>> subjectAlternativeNames = cert.getSubjectAlternativeNames();
        if (subjectAlternativeNames != null) {
            subjectAlternativeNames.stream().map((subList) -> {
                logger.debug("Processing alternative");
                return subList;
            }).map((subList) -> {
                StringBuilder sb = new StringBuilder();
                subList.stream().forEach((o) -> {
                    sb.append(o.toString()).append(", ");
                }); return sb;
            }).forEach((sb) -> {
                logger.debug(sb.toString());
            });
        }
        } catch (CertificateParsingException ex) {
            logger.info("It is useful to ignore such king of exceptions", ex);
        }
        logger.debug("Subject distiguished name: " + subjectDN.getName());
    }

    @Override
    public void verify(String host, SSLSocket ssls) throws IOException {
       verify(host, ssls.getSession());
    }

    @Override
    public void verify(String string, String[] strings, String[] strings1) throws SSLException {
        //Do nothing
        logger.debug("Skip verification of ssl certificate. Doing nothing.");
    }
    
}
