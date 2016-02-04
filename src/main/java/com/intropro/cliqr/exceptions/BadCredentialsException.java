/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.intropro.cliqr.exceptions;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class BadCredentialsException extends ResponseException{

    public BadCredentialsException() {
    }

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

}
