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
package com.test;

import com.test.db.MySqlConnection;
import com.test.http.service.WebServer;

import org.apache.log4j.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        final WebServer server = new WebServer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Web server being stopped ...");

            // shut down connection pool
            MySqlConnection.close();

            server.stop();
        }));

        LOGGER.info("Web server starting ...");

        server.start();
    }
}