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
package ua.pp.msk.cliqr;

import com.google.gson.JsonObject;
import ua.pp.msk.cliqr.exceptions.ResponseException;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public interface PostProcessor {

    public String getResponse(boolean pretty, String apiPath, JsonObject json) throws ResponseException;

    public String getResponse(boolean pretty, String apiPath, String entity) throws ResponseException;

    /**
     * By default response should be not pretty (one line String)
     *
     * @param apiPath
     * @param entity JSON String
     * @return JSON string
     * @throws ua.pp.msk.cliqr.exceptions.ResponseException General response exception
     */
    public String getResponse(String apiPath, String entity) throws ResponseException;
}
