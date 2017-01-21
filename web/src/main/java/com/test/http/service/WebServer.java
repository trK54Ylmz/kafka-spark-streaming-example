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
package com.test.http.service;

import com.test.config.ConfigurationFactory;
import com.test.config.objects.Config;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import javax.ws.rs.core.UriBuilder;

import static java.util.logging.Logger.getLogger;

public class WebServer {
    private static final Logger LOGGER = Logger.getLogger(WebServer.class);

    private final Config config;

    private HttpServer server;

    private URI getUri() {
        return UriBuilder.fromUri("http://localhost/").port(config.getWeb().getPort()).build();
    }

    public WebServer() {
        config = ConfigurationFactory.load();
    }

    public WebServer(Config config) {
        this.config = config;
    }

    public void start() {
        final ResourceConfig config = new ResourceConfig();

        // define resources package
        config.packages("com.test.http.resources");

        server = GrizzlyHttpServerFactory.createHttpServer(getUri(), config);

        java.util.logging.Logger logger = getLogger("org.glassfish.grizzly.http.server.HttpHandler");
        logger.setLevel(Level.FINE);
        logger.setUseParentHandlers(false);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        logger.addHandler(ch);

        try {
            server.start();

            // grizzly uses executor so does not stop http server
            Thread.currentThread().join();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void stop() {
        // skip the shutdown if already stopped or never started
        if (server == null || !server.isStarted()) return;

        server.shutdown();
    }
}