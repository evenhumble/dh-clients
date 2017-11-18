/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hedwig.dh.jmeter.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Value used for indexed put test.
 */
public class Person4 implements Serializable {
    /** Value 1. */
    @QuerySqlField(index = true)
    private int val1;

    /** Value 2. */
    @QuerySqlField(index = true)
    private int val2;

    /** Value 3. */
    @QuerySqlField(index = true)
    private int val3;

    /** Value 4. */
    @QuerySqlField(index = true)
    private int val4;
    /**
     * Constructs.
     *
     * @param val Indexed value.
     */
    public Person4(int val) {
        this.val1 = val;
        this.val2 = val + 1;
        this.val3 = val + 2;
        this.val4 = val + 3;
    }

    public Person4() {
    }


    /** {@inheritDoc} */
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Person4 p8 = (Person4)o;

        return val1 == p8.val1 && val2 == p8.val2 && val3 == p8.val3 && val4 == p8.val4
            ;
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        int result = val1;

        result = 31 * result + val2;
        result = 31 * result + val3;
        result = 31 * result + val4;

        return result;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Person8 [val1=" + val1 + ", val2=" + val2 + ", val3=" + val3 + ", val4=" + val4 +
             ']';
    }
}