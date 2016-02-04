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
public class RunJobException extends Exception {

    public RunJobException() {
    }

    public RunJobException(String message) {
        super(message);
    }

    public RunJobException(String message, Throwable cause) {
        super(message, cause);
    }

}
