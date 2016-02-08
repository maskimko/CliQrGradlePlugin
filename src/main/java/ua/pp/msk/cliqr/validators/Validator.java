/*
 * Copyright 2016 Maksym Shkolnyi self project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.pp.msk.cliqr.validators;

import com.google.gson.JsonObject;
import ua.pp.msk.cliqr.exceptions.ParameterException;
import ua.pp.msk.cliqr.exceptions.ParseException;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public abstract class Validator {

    public static final String SERVICETIERID = "serviceTierId";
    public static final String ENVIRONMENT = "environment";
    public static final String PARAMETERS = "parameters";
    public static final String VERSION = "appVersion";
    public static final String CLOUDPARAMETERS = "cloudParams";
    public static final String APPLICATIONPARAMENTERS = "appParams";
    public static final String JOBS = "jobs";
    public static final String CLOUD = "cloud";
    public static final String CLOUDPROPERTIES = "cloudProperties";
    public static final String INSTANCE = "instance";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String USERDATACENTERNAME = "UserDataCenterName";
    public static final String USERCLUSTERNAME = "UserClusterName";
    public static final String USERDATASTORECLUSTER = "UserDatastoreCluster";
    public static final String VIPPOOLNAME = "VIP_POOL_NAME";
    public static final String MINCLUSTERSIZE = "minClusterSize";
    public static final String ENVNAME = "ENV_NAME";

    public abstract boolean validate(String json) throws ParseException, ParameterException;

    public abstract boolean validate(JsonObject json) throws ParameterException;
}
