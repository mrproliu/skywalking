/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oal.rt.parser;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.storage.type.StorageDataComplexObject;

/**
 * OAL analysis result.
 */
@Getter
@Setter
public class AnalysisResult {
    /**
     * Variable name of one OAL expression.
     */
    private String varName;
    /**
     * Generated metric name.
     */
    private String metricsName;
    /**
     * Package name of generated metric class.
     */
    private String metricsClassPackage;
    /**
     * Table name for the storage.
     */
    private String tableName;
    /**
     * The package name of source class from {@link org.apache.skywalking.oap.server.core.oal.rt.OALDefine}
     */
    private String sourcePackage;
    /**
     * The class name of generated metric class.
     */
    private String metricsClassName;
    /**
     * The raw parsed result of from statement.
     */
    private FromStmt from = new FromStmt();
    /**
     * The raw parsed result of filter statements.
     */
    private FilterStmts filters = new FilterStmts();
    /**
     * The raw parsed result of aggregation function with arguments.
     */
    private AggregationFuncStmt aggregationFuncStmt = new AggregationFuncStmt();

    /**
     * Generated through {@link #aggregationFuncStmt}
     */
    private EntryMethod entryMethod;

    /**
     * Persistent columns are generated by {@link org.apache.skywalking.oap.server.core.storage.annotation.Column}
     * definition of {@link org.apache.skywalking.oap.server.core.analysis.metrics.annotation.MetricsFunction}.
     */
    private List<DataColumn> persistentFields;
    /**
     * Fields of metric class are generated by the fields annotated {@link org.apache.skywalking.oap.server.core.source.ScopeDefaultColumn.DefinedByField}
     * and class level definition through {@link org.apache.skywalking.oap.server.core.source.ScopeDefaultColumn.VirtualColumnDefinition}
     * in the {@link org.apache.skywalking.oap.server.core.source.Source}
     */
    private List<SourceColumn> fieldsFromSource;
    /**
     * Fields generated by {@link #fieldsFromSource} and {@link #persistentFields}. These fields are used in final
     * persistence.
     */
    private PersistenceColumns serializeFields;

    private String sourceDecorator;

    public void addPersistentField(String fieldName, String columnName, Class<?> type) {
        if (persistentFields == null) {
            persistentFields = new ArrayList<>();
        }
        DataColumn dataColumn = new DataColumn(fieldName, columnName, type);
        persistentFields.add(dataColumn);
    }

    public void generateSerializeFields() {
        serializeFields = new PersistenceColumns();
        for (SourceColumn sourceColumn : fieldsFromSource) {
            String type = sourceColumn.getType().getSimpleName();
            switch (type) {
                case "int":
                    serializeFields.addIntField(sourceColumn.getFieldName());
                    break;
                case "double":
                    serializeFields.addDoubleField(sourceColumn.getFieldName());
                    break;
                case "String":
                    serializeFields.addStringField(sourceColumn.getFieldName());
                    break;
                case "long":
                    serializeFields.addLongField(sourceColumn.getFieldName());
                    break;
                default:
                    throw new IllegalStateException(
                        "Unexpected field type [" + type + "] of source sourceColumn [" + sourceColumn
                            .getFieldName() + "]");
            }
        }

        for (DataColumn column : persistentFields) {
            final Class<?> columnType = column.getType();

            if (columnType.equals(int.class)) {
                serializeFields.addIntField(column.getFieldName());
            } else if (columnType.equals(double.class)) {
                serializeFields.addDoubleField(column.getFieldName());
            } else if (columnType.equals(String.class)) {
                serializeFields.addStringField(column.getFieldName());
            } else if (columnType.equals(long.class)) {
                serializeFields.addLongField(column.getFieldName());
            } else if (StorageDataComplexObject.class.isAssignableFrom(columnType)) {
                serializeFields.addObjectField(column.getFieldName(), columnType.getName());
            } else {
                throw new IllegalStateException(
                    "Unexpected field type [" + columnType.getSimpleName() + "] of persistence column [" + column
                        .getFieldName() + "]");
            }
        }
    }
}
