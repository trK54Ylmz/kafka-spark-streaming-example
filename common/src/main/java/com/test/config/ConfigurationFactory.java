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
package com.test.config;

import com.google.common.base.Strings;

import com.test.config.objects.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class ConfigurationFactory {
    /**
     * Loads configuration object by file
     *
     * @return Returns configuration objects
     */
    public static Config load() {
        if (Strings.isNullOrEmpty(System.getProperty("config"))) {
            throw new RuntimeException("Configuration file path is empty. "
                    + "Please specify the file path with using -Dconfig=[PATH]");
        }

        com.typesafe.config.Config config
                = ConfigFactory.parseFile(new File(System.getProperty("config")));

        return ConfigBeanFactory.create(config, Config.class);
    }
}