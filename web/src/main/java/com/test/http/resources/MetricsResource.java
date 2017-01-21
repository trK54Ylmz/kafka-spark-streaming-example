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
package com.test.http.resources;

import com.test.db.MySqlConnection;
import com.test.http.responses.MessageResponse;
import com.test.http.responses.MetricResponse;
import com.test.http.utils.ResponseUtils;
import com.test.http.utils.Types;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/metrics")
public class MetricsResource {
    private static final Logger LOGGER = Logger.getLogger(MetricsResource.class);

    @GET
    @Produces(Types.JSON)
    public Response getDashboardMetric(
            @QueryParam("start") long start,
            @QueryParam("end") long end,
            @QueryParam("delay") int millis) {
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            if (start == 0 || end == 0) {
                final Timestamp now = new Timestamp(System.currentTimeMillis());

                end = now.getTime();
                start = end - millis;
            }

            final Timestamp startDate = new Timestamp(start);
            final Timestamp endDate = new Timestamp(end);

            connection = MySqlConnection.getConnection();

            String sql = "SELECT market, AVG(rate) AS rate, AVG(dt) AS dt " +
                    "FROM events WHERE dt BETWEEN ? AND ? GROUP BY market ORDER BY market ASC";

            st = connection.prepareStatement(sql);
            st.setTimestamp(1, startDate);
            st.setTimestamp(2, endDate);

            rs = st.executeQuery();

            final MetricResponse response = new MetricResponse();

            Timestamp lastTime = null;
            while (rs.next()) {
                MetricResponse.Metric metric = new MetricResponse.Metric();

                metric.setMarket(rs.getString("market"));
                metric.setRate(rs.getFloat("rate"));
                metric.setDate(rs.getTimestamp("dt").getTime() / 1000);

                response.addMetric(metric);

                if (lastTime == null || lastTime.getTime() < rs.getTimestamp("dt").getTime()) {
                    lastTime = rs.getTimestamp("dt");
                }
            }

            if (lastTime == null) {
                lastTime = endDate;
            }

            response.setLastTime(lastTime);

            return Response.ok().entity(ResponseUtils.toJson(response)).build();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);

            return Response.status(500)
                    .entity(ResponseUtils.toJson(new MessageResponse(false, "connection error")))
                    .build();
        } finally {
            MySqlConnection.close(rs);
            MySqlConnection.close(st);
            MySqlConnection.close(connection);
        }
    }
}