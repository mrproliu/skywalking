package org.apache.skywalking.oap.server.storage.plugin.banyandb.stream;

import org.apache.skywalking.banyandb.v1.client.PairQueryCondition;
import org.apache.skywalking.banyandb.v1.client.StreamQuery;
import org.apache.skywalking.oap.server.core.analysis.TimeBucket;
import org.apache.skywalking.oap.server.core.analysis.manual.log.AbstractLogRecord;
import org.apache.skywalking.oap.server.core.analysis.manual.searchtag.Tag;
import org.apache.skywalking.oap.server.core.query.enumeration.Order;
import org.apache.skywalking.oap.server.core.query.input.TraceScopeCondition;
import org.apache.skywalking.oap.server.core.query.type.Log;
import org.apache.skywalking.oap.server.core.query.type.Logs;
import org.apache.skywalking.oap.server.core.storage.query.ILogQueryDAO;
import org.apache.skywalking.oap.server.library.util.StringUtil;
import org.apache.skywalking.oap.server.storage.plugin.banyandb.BanyanDBStorageClient;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * {@link org.apache.skywalking.oap.server.core.analysis.manual.log.LogRecord} is a stream
 */
public class BanyanDBLogQueryDAO extends AbstractBanyanDBDAO implements ILogQueryDAO {
    public BanyanDBLogQueryDAO(BanyanDBStorageClient client) {
        super(client);
    }

    @Override
    public Logs queryLogs(String serviceId, String serviceInstanceId, String endpointId,
                          TraceScopeCondition relatedTrace, Order queryOrder, int from, int limit,
                          long startTB, long endTB, List<Tag> tags, List<String> keywordsOfContent,
                          List<String> excludingKeywordsOfContent) throws IOException {
        List<Log> entities = query(Log.class, new QueryBuilder() {
            @Override
            public void apply(StreamQuery query) {
                if (StringUtil.isNotEmpty(serviceId)) {
                    query.appendCondition(PairQueryCondition.StringQueryCondition.eq("searchable", AbstractLogRecord.SERVICE_ID, serviceId));
                }

                if (startTB != 0 && endTB != 0) {
                    query.appendCondition(PairQueryCondition.LongQueryCondition.ge("searchable", AbstractLogRecord.TIMESTAMP, TimeBucket.getTimestamp(startTB)));
                    query.appendCondition(PairQueryCondition.LongQueryCondition.le("searchable", AbstractLogRecord.TIMESTAMP, TimeBucket.getTimestamp(endTB)));
                }

                if (StringUtil.isNotEmpty(serviceInstanceId)) {
                    query.appendCondition(PairQueryCondition.StringQueryCondition.eq("searchable", AbstractLogRecord.SERVICE_INSTANCE_ID, serviceInstanceId));
                }
                if (StringUtil.isNotEmpty(endpointId)) {
                    query.appendCondition(PairQueryCondition.StringQueryCondition.eq("searchable", AbstractLogRecord.ENDPOINT_ID, endpointId));
                }
                if (Objects.nonNull(relatedTrace)) {
                    if (StringUtil.isNotEmpty(relatedTrace.getTraceId())) {
                        query.appendCondition(PairQueryCondition.StringQueryCondition.eq("searchable", AbstractLogRecord.TRACE_ID, relatedTrace.getTraceId()));
                    }
                    if (StringUtil.isNotEmpty(relatedTrace.getSegmentId())) {
                        query.appendCondition(PairQueryCondition.StringQueryCondition.eq("searchable", AbstractLogRecord.TRACE_SEGMENT_ID, relatedTrace.getSegmentId()));
                    }
                    if (Objects.nonNull(relatedTrace.getSpanId())) {
                        query.appendCondition(PairQueryCondition.LongQueryCondition.eq("searchable", AbstractLogRecord.SPAN_ID, (long) relatedTrace.getSpanId()));
                    }
                }

                // TODO: if we allow to index tags?
//        if (CollectionUtils.isNotEmpty(tags)) {
//            for (final Tag tag : tags) {
//                query.appendCondition(PairQueryCondition.StringQueryCondition.eq("searchable", tag.getKey(), tag.getValue()));
//            }
//        }
            }
        });
        Logs logs = new Logs();
        logs.getLogs().addAll(entities);
        logs.setTotal(entities.size());

        return logs;
    }
}
