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
package com.test.config.objects;

public class Config {
    private ProducerConfig producer;

    private GeneratorConfig generator;

    private WebConfig web;

    private StreamingConfig streaming;

    public ProducerConfig getProducer() {
        return producer;
    }

    public void setProducer(ProducerConfig producer) {
        this.producer = producer;
    }

    public GeneratorConfig getGenerator() {
        return generator;
    }

    public void setGenerator(GeneratorConfig generator) {
        this.generator = generator;
    }

    public WebConfig getWeb() {
        return web;
    }

    public void setWeb(WebConfig web) {
        this.web = web;
    }

    public StreamingConfig getStreaming() {
        return streaming;
    }

    public void setStreaming(StreamingConfig streaming) {
        this.streaming = streaming;
    }
}