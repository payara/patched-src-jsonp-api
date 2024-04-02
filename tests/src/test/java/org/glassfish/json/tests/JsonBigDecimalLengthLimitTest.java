/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.json.tests;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonReader;
import javax.json.JsonValue;
import junit.framework.TestCase;
import org.glassfish.json.api.JsonConfig;

/**
 * Test maxBigDecimalLength limit set from System property.
 */
public class JsonBigDecimalLengthLimitTest extends TestCase  {

    public JsonBigDecimalLengthLimitTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() {
        System.setProperty(JsonConfig.MAX_BIGDECIMAL_LEN, "500");
    }

    @Override
    protected void tearDown() {
        System.clearProperty(JsonConfig.MAX_BIGDECIMAL_LEN);
    }

    // Test BigDecimal max source characters array length using length equal to system property limit of 500.
    // Parsing shall pass and return value equal to source String.
    public void testLargeBigDecimalBellowLimit() {
        JsonReader reader = Json.createReader(new StringReader(JsonNumberTest.Π_500));
        JsonNumber check = Json.createValue(new BigDecimal(JsonNumberTest.Π_500));
        JsonValue value = reader.readValue();
        assertEquals(value.getValueType(), JsonValue.ValueType.NUMBER);
        assertEquals(value, check);
    }

    // Test BigDecimal max source characters array length using length above system property limit of 500.
    // Parsing shall pass and return value equal to source String.
    public void testLargeBigDecimalAboveLimit() {
        JsonReader reader = Json.createReader(new StringReader(JsonNumberTest.Π_501));
        try {
            reader.readValue();
            fail("No exception was thrown from BigDecimal parsing with source characters array length over limit");
        } catch (UnsupportedOperationException e) {
            // UnsupportedOperationException is expected to be thrown
            assertEquals(
                    "Number of BigDecimal source characters 501 exceeded maximal allowed value of 500",
                    e.getMessage());
        }
    }

}
