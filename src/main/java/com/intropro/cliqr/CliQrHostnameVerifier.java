/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.intropro.cliqr;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class CliQrHostnameVerifier implements HostnameVerifier {

    private Logger logger = LoggerFactory.getLogger(CliQrHostnameVerifier.class); 
    
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
    
    public final void verify(String host, X509Certificate cert) throws SSLException{
        Principal subjectDN = cert.getSubjectDN();
        try {
        Collection<List<?>> subjectAlternativeNames = cert.getSubjectAlternativeNames();
        if (subjectAlternativeNames != null) {
        for (List<?> subList : subjectAlternativeNames) {
            logger.debug("Processing alternative");
            StringBuilder sb = new StringBuilder();
            for (Object o : subList) {
                sb.append(o.toString()).append(", ");
            }
            logger.debug(sb.toString());
        }
        }
        } catch (CertificateParsingException ex) {
            logger.info("It is useful to ignore such king of exceptions", ex);
        }
        logger.debug("Subject distiguished name: " + subjectDN.getName());
    }
    
}
