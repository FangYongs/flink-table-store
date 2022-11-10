/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.store.hive;

/** Sql create hive table. */
public class SqlCreateHiveTable {

    public static final String TABLE_LOCATION_URI = "hive.location-uri";
    public static final String TABLE_IS_EXTERNAL = "hive.is-external";
    public static final String PK_CONSTRAINT_TRAIT = "hive.pk.constraint.trait";
    public static final String NOT_NULL_CONSTRAINT_TRAITS = "hive.not.null.constraint.traits";
    public static final String NOT_NULL_COLS = "hive.not.null.cols";

    /** To represent ROW FORMAT in CREATE TABLE DDL. */
    public static class HiveTableRowFormat {

        public static final String SERDE_LIB_CLASS_NAME = "hive.serde.lib.class.name";
        public static final String SERDE_INFO_PROP_PREFIX = "hive.serde.info.prop.";
        public static final String FIELD_DELIM = SERDE_INFO_PROP_PREFIX + "field.delim";
        public static final String COLLECTION_DELIM = SERDE_INFO_PROP_PREFIX + "collection.delim";
        public static final String ESCAPE_CHAR = SERDE_INFO_PROP_PREFIX + "escape.delim";
        public static final String MAPKEY_DELIM = SERDE_INFO_PROP_PREFIX + "mapkey.delim";
        public static final String LINE_DELIM = SERDE_INFO_PROP_PREFIX + "line.delim";
        public static final String SERIALIZATION_NULL_FORMAT =
                SERDE_INFO_PROP_PREFIX + "serialization.null.format";
    }

    /** To represent STORED AS in CREATE TABLE DDL. */
    public static class HiveTableStoredAs {

        public static final String STORED_AS_FILE_FORMAT = "hive.storage.file-format";
        public static final String STORED_AS_INPUT_FORMAT = "hive.stored.as.input.format";
        public static final String STORED_AS_OUTPUT_FORMAT = "hive.stored.as.output.format";
    }
}
