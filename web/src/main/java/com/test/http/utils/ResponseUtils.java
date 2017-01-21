/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.http.utils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.http.responses.MessageResponse;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ResponseUtils {
    private static final Logger LOGGER = Logger.getLogger(ResponseUtils.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(Object entity) {
        try {
            return MAPPER.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);

            try {
                return MAPPER.writeValueAsString(new MessageResponse(false, "json error"));
            } catch (JsonProcessingException p) {
                // ignore this block

                return null;
            }
        }
    }

    /**
     * Loads content from local resources directories
     *
     * @param path resources directory path
     * @return Returns content of file
     */
    public static String loadFromResource(String path) {
        InputStream is = ResponseUtils.class.getResourceAsStream(path);

        try {
            return CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            return toJson(new MessageResponse(false, "invalid content"));
        }
    }
}