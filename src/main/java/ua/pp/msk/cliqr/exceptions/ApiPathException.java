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
public class ApiPathException extends ResponseException {

    public ApiPathException() {
    }

    public ApiPathException(String message) {
        super(message);
    }

    public ApiPathException(String message, Throwable cause) {
        super(message, cause);
    }

}
