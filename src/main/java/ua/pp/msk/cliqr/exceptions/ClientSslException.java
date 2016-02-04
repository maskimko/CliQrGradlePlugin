/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.cliqr.exceptions;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class ClientSslException extends Exception{

    public ClientSslException() {
    }

    public ClientSslException(String message) {
        super(message);
    }

    public ClientSslException(String message, Throwable cause) {
        super(message, cause);
    }

}
