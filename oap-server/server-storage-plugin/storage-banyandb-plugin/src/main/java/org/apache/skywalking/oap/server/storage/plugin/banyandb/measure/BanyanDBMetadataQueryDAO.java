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

package org.apache.skywalking.oap.server.storage.plugin.banyandb.measure;

import org.apache.skywalking.oap.server.core.query.type.Endpoint;
import org.apache.skywalking.oap.server.core.query.type.Process;
import org.apache.skywalking.oap.server.core.query.type.Service;
import org.apache.skywalking.oap.server.core.query.type.ServiceInstance;
import org.apache.skywalking.oap.server.core.storage.query.IMetadataQueryDAO;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BanyanDBMetadataQueryDAO implements IMetadataQueryDAO {
    @Override
    public List<Service> listServices(String layer, String group) throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<Service> getServices(String serviceId) throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<ServiceInstance> listInstances(long startTimestamp, long endTimestamp, String serviceId) throws IOException {
        return Collections.emptyList();
    }

    @Override
    public ServiceInstance getInstance(String instanceId) throws IOException {
        return null;
    }

    @Override
    public List<Endpoint> findEndpoint(String keyword, String serviceId, int limit) throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<Process> listProcesses(String serviceId, String instanceId) throws IOException {
        return Collections.emptyList();
    }

    @Override
    public Process getProcess(String processId) throws IOException {
        return null;
    }
}
