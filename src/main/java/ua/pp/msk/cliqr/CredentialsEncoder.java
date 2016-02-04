/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class CredentialsEncoder {

    // getRequest.setHeader(new BasicHeader("Authorization", "Basic " + getEncodedPassword("cicdgroup_5", "16EE49C5337B327D")));
    private static String getEncodedPassword(String username, String password) {
        String authStrign = username + ":" + password;
        byte[] encodeBase64 = Base64.encodeBase64(authStrign.getBytes());
        return new String(encodeBase64);
    }

}
