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
package com.test.beans;

import java.io.Serializable;

public class RecordBean implements Serializable {
    public enum Types {
        africa, asia, europe, north_america, oceania, south_america;

        public static Types fromNumeric(int index) {
            switch (index) {
                default:
                    return null;
                case 0:
                    return Types.africa;
                case 1:
                    return Types.asia;
                case 2:
                    return Types.europe;
                case 3:
                    return Types.north_america;
                case 4:
                    return Types.oceania;
                case 5:
                    return Types.south_america;
            }
        }
    }

    private Types type;

    private float value;

    public RecordBean() {
        // default constructor for default instantiate
    }

    public RecordBean(Types type, float value) {
        this.type = type;
        this.value = value;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}