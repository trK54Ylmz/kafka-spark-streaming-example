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
package com.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Returns object as JSON encoded string
     *
     * @param input the bean instance
     * @return Returns single line JSON string
     */
    public static String serialize(Object input) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(input);
    }

    /**
     * Returns JSON encoded string as real object
     *
     * @param input the encoded JSON string
     * @return Returns PoJo
     */
    public static <T> T deserialize(String input, Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(input, tClass);
    }
}