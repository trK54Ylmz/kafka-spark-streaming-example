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

import com.test.http.responses.MessageResponse;
import com.test.http.utils.ResponseUtils;
import com.test.http.utils.Types;
import com.test.http.utils.ViewUtils;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class MainResource {
    @GET
    @Produces(Types.HTML)
    public Response getMainPage() {
        try {
            return Response.ok().entity(ViewUtils.render("main")).build();
        } catch (IOException e) {
            return Response.serverError()
                    .entity(ResponseUtils.toJson(new MessageResponse(false, e.getMessage())))
                    .build();
        }
    }
}