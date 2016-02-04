/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.cliqr;

import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class CliQrRedirectStrategy extends DefaultRedirectStrategy {

    private Logger logger = LoggerFactory.getLogger(CliQrRedirectStrategy.class);
            
    
    @Override
    protected boolean isRedirectable(String method) {
        boolean redirectable = super.isRedirectable(method); //To change body of generated methods, choose Tools | Templates.
        logger.debug("Method " + method + " is redirectable " + redirectable);
        logger.debug("Ignoring and returning true");
        return true;
    }

    
}
