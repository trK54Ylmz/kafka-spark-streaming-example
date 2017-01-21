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

import com.google.common.base.Strings;

import com.test.http.utils.ResponseUtils;
import com.test.http.utils.Types;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/assets")
public class AssetsResource {
    /**
     * Returns file output as Response object
     *
     * @param file input file resource path
     * @return Returns content of file
     */
    private Response getCore(String file, String path) {
        if (Strings.isNullOrEmpty(file)) {
            return Response.noContent().build();
        }

        // read content of file and return as SUCCESS response
        return Response.status(200)
                .entity(ResponseUtils.loadFromResource("/assets/" + path + "/" + file))
                .build();
    }

    @GET
    @Path("/css/{file}")
    @Produces(Types.CSS)
    public Response cssFileGet(@PathParam("file") String file) {
        return getCore(file, "css");
    }

    @POST
    @Path("/css/{file}")
    @Produces(Types.CSS)
    public Response cssFilePost(@PathParam("file") String file) {
        return getCore(file, "css");
    }

    @GET
    @Path("/js/{file}")
    @Produces(Types.JS)
    public Response jsFileGet(@PathParam("file") String file) {
        return getCore(file, "js");
    }

    @POST
    @Path("/js/{file}")
    @Produces(Types.JS)
    public Response jsFilePost(@PathParam("file") String file) {
        return getCore(file, "css");
    }

    @GET
    @Path("/images/{file}")
    @Produces(Types.IMAGE)
    public Response imageFileGet(@PathParam("file") String file) {
        return getCore(file, "css");
    }

    @POST
    @Path("/images/{file}")
    @Produces(Types.IMAGE)
    public Response imageFilePost(@PathParam("file") String file) {
        return getCore(file, "css");
    }
}