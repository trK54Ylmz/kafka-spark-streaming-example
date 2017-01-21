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
package com.test.db;

import com.test.config.ConfigurationFactory;
import com.test.config.objects.Config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnection {
    private static final Logger LOGGER = Logger.getLogger(MySqlConnection.class);

    private static final Config CONFIG = ConfigurationFactory.load();

    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(getUrl());
        ds.setUsername(CONFIG.getStreaming().getDb().getUser());
        ds.setPassword(CONFIG.getStreaming().getDb().getPass());
    }

    private MySqlConnection() {
        // IGNORE THIS BLOCK
    }

    public static String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s?rewriteBatchedStatements=true",
                CONFIG.getStreaming().getDb().getHost(),
                CONFIG.getStreaming().getDb().getPort(),
                CONFIG.getStreaming().getDb().getDb());
    }

    /**
     * Creates new MySQL connection by using connection pool
     *
     * @return Returns active database connection
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Closes active MySQL connection
     *
     * @param connection the active connection
     */
    public static void close(Connection connection) {
        if (connection == null) return;

        try {
            if (connection.isClosed()) return;

            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Closes active MySQL connection
     *
     * @param resultSet the active connection
     */
    public static void close(ResultSet resultSet) {
        if (resultSet == null) return;

        try {
            if (resultSet.isClosed()) return;

            resultSet.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Closes active MySQL connection
     *
     * @param statement the active connection
     */
    public static void close(Statement statement) {
        if (statement == null) return;

        try {
            if (statement.isClosed()) return;

            statement.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Closes connection pool
     */
    public static void close() {
        if (ds.isClosed()) return;

        try {
            ds.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}