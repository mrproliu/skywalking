## 10.2.0

#### OAP Server

* Skip processing OTLP metrics data points with flag `FLAG_NO_RECORDED_VALUE`, which causes exceptional result.
* Add self observability metrics for GraphQL query, `graphql_query_latency`.
* Reduce the count of process index and adding time range when query process index.
* Bump up Apache commons-io to 2.17.0.
* Polish eBPF so11y metrics and add error count for query metrics.
* Support query endpoint list with duration parameter(optional).
* Change the endpoint_traffic to updatable for the additional column `last_ping`.


#### UI
* Support the endpoint list widget query with duration parameter.


#### Documentation
* Update release document to adopt newly added revision-based process.

All issues and pull requests are [here](https://github.com/apache/skywalking/milestone/224?closed=1)
