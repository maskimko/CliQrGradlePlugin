/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr.validators;

import com.google.gson.JsonObject;
import com.intropro.cliqr.exceptions.ParameterException;
import com.intropro.cliqr.exceptions.ParseException;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public abstract class Validator {

    public static final String SERVICETIERID = "serviceTierId";
    public static final String ENVIRONMENT = "environment";
    public static final String PARAMETERS = "parameters";
    public static final String CLOUDPARAMETERS = "cloudParams";
    public static final String APPLICATIONPARAMENTERS = "appParams";
    public static final String JOBS = "jobs";
    public static final String CLOUD = "cloud";
    public static final String CLOUDPROPERTIES = "cloudProperties";
    public static final String INSTANCE = "instance";
    public static final String NAME = "name";
    public static final String VALUE = "value";

    public abstract boolean validate(String json) throws ParseException, ParameterException;

    public abstract boolean validate(JsonObject json) throws ParameterException;
}
