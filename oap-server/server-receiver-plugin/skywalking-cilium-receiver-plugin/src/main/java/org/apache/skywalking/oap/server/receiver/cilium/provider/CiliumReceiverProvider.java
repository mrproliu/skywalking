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

package org.apache.skywalking.oap.server.receiver.cilium.provider;

import io.cilium.api.flow.FlowFilter;
import io.cilium.api.flow.TrafficDirection;
import io.cilium.api.observer.GetFlowsRequest;
import io.cilium.api.observer.GetFlowsResponse;
import io.cilium.api.observer.GetNamespacesRequest;
import io.cilium.api.observer.GetNamespacesResponse;
import io.cilium.api.observer.GetNodesRequest;
import io.cilium.api.observer.GetNodesResponse;
import io.cilium.api.observer.Namespace;
import io.cilium.api.observer.ObserverGrpc;
import io.cilium.api.peer.ChangeNotification;
import io.cilium.api.peer.NotifyRequest;
import io.cilium.api.peer.PeerGrpc;
import io.grpc.ManagedChannel;
import org.apache.skywalking.oap.server.library.client.grpc.GRPCClient;
import org.apache.skywalking.oap.server.library.module.ModuleConfig;
import org.apache.skywalking.oap.server.library.module.ModuleDefine;
import org.apache.skywalking.oap.server.library.module.ModuleProvider;
import org.apache.skywalking.oap.server.library.module.ModuleStartException;
import org.apache.skywalking.oap.server.library.module.ServiceNotProvidedException;
import org.apache.skywalking.oap.server.receiver.cilium.module.CiliumReceiverModule;

import java.util.Iterator;

public class CiliumReceiverProvider extends ModuleProvider {

    private CiliumReceiverModuleConfig config;
    private ManagedChannel channel;
    @Override
    public String name() {
        return "default";
    }

    @Override
    public Class<? extends ModuleDefine> module() {
        return CiliumReceiverModule.class;
    }

    @Override
    public ConfigCreator<? extends ModuleConfig> newConfigCreator() {
        return new ConfigCreator<CiliumReceiverModuleConfig>() {
            @Override
            public Class<CiliumReceiverModuleConfig> type() {
                return CiliumReceiverModuleConfig.class;
            }

            @Override
            public void onInitialized(CiliumReceiverModuleConfig moduleConfig) {
                config = moduleConfig;
            }
        };
    }

    @Override
    public void prepare() throws ServiceNotProvidedException, ModuleStartException {
        GRPCClient client = new GRPCClient(config.getPeerHost(), config.getPeerPort());
        client.connect();
        this.channel = client.getChannel();
    }

    private void testingGetNodes(ManagedChannel channel) {
        final PeerGrpc.PeerBlockingStub stub = PeerGrpc.newBlockingStub(channel);
        final Iterator<ChangeNotification> notify = stub.notify(NotifyRequest.newBuilder().build());
        while (notify.hasNext()) {
            ChangeNotification changeNotification = notify.next();
            System.out.println(changeNotification);
        }
    }

    private void testingObserveData(ManagedChannel channel) {
        final ObserverGrpc.ObserverBlockingStub stub = ObserverGrpc.newBlockingStub(channel);
        final Iterator<GetFlowsResponse> flows = stub.getFlows(GetFlowsRequest.newBuilder()
            .setFollow(true)
            .build());
        while (flows.hasNext()) {
            final GetFlowsResponse next = flows.next();
            System.out.println(next);
        }
    }

    @Override
    public void start() throws ServiceNotProvidedException, ModuleStartException {
        // for getting nodes notification(kubectl port-forward -n kube-system   hubble-peer 80)
        // following this API, you can get the (obs)nodes notification
//        testingGetNodes(channel);
        // for getting observe data from single obs node(kubectl port-forward -n kube-system kube-proxy-xxx 4244)
        testingObserveData(channel);
    }

    @Override
    public void notifyAfterCompleted() throws ServiceNotProvidedException, ModuleStartException {

    }

    @Override
    public String[] requiredModules() {
        return new String[0];
    }
}
