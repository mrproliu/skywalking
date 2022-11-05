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

package org.apache.skywalking.oap.server.storage.plugin.jdbc.h2.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.oap.server.core.analysis.manual.spanattach.SpanAttachedEventRecord;
import org.apache.skywalking.oap.server.core.storage.query.ISpanAttachedEventQueryDAO;
import org.apache.skywalking.oap.server.library.client.jdbc.hikaricp.JDBCHikariCPClient;
import org.apache.skywalking.oap.server.library.util.StringUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
public class H2SpanAttachedEventQueryDAO implements ISpanAttachedEventQueryDAO {
    private JDBCHikariCPClient h2Client;

    public H2SpanAttachedEventQueryDAO(JDBCHikariCPClient h2Client) {
        this.h2Client = h2Client;
    }

    @Override
    public List<SpanAttachedEventRecord> querySpanAttachedEvents(String traceId) throws IOException {
        StringBuilder sql = new StringBuilder("select * from " + SpanAttachedEventRecord.INDEX_NAME + " where ");
        List<Object> parameters = new ArrayList<>(1);

        sql.append(" ").append(SpanAttachedEventRecord.TRACE_ID).append(" = ?");
        parameters.add(traceId);

        sql.append(" order by ").append(SpanAttachedEventRecord.START_TIME_SECOND)
                .append(",").append(SpanAttachedEventRecord.START_TIME_NANOS).append(" ASC ");

        List<SpanAttachedEventRecord> results = new ArrayList<>();
        try (Connection connection = h2Client.getConnection()) {
            try (ResultSet resultSet = h2Client.executeQuery(
                    connection, sql.toString(), parameters.toArray(new Object[0]))) {
                while (resultSet.next()) {
                    SpanAttachedEventRecord record = new SpanAttachedEventRecord();
                    record.setStartTimeSecond(resultSet.getLong(SpanAttachedEventRecord.START_TIME_SECOND));
                    record.setStartTimeNanos(resultSet.getInt(SpanAttachedEventRecord.START_TIME_NANOS));
                    record.setEvent(resultSet.getString(SpanAttachedEventRecord.EVENT));
                    record.setEndTimeSecond(resultSet.getLong(SpanAttachedEventRecord.END_TIME_SECOND));
                    record.setEndTimeNanos(resultSet.getInt(SpanAttachedEventRecord.END_TIME_NANOS));
                    record.setTraceRefType(resultSet.getInt(SpanAttachedEventRecord.TRACE_REF_TYPE));
                    record.setTraceId(resultSet.getString(SpanAttachedEventRecord.TRACE_ID));
                    record.setTraceSegmentId(resultSet.getString(SpanAttachedEventRecord.TRACE_SEGMENT_ID));
                    record.setTraceSpanId(resultSet.getString(SpanAttachedEventRecord.TRACE_SPAN_ID));
                    String dataBinaryBase64 = resultSet.getString(SpanAttachedEventRecord.DATA_BINARY);
                    if (StringUtil.isNotEmpty(dataBinaryBase64)) {
                        record.setDataBinary(Base64.getDecoder().decode(dataBinaryBase64));
                    }
                    results.add(record);
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }

        return results;
    }
}
