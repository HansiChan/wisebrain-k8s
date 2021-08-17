package com.sibat.wisebrain.k8s.controller;

import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "k8s-Node接口")
public class NodeController  {
    @Autowired
    private K8sInit k8sInit;
    //k8s node list
    @ApiOperation(value = "ListNode", notes = "ListNode")
    @GetMapping(value = "/node/list")
    public NodeList listk8snode() {
        KubernetesClient client = k8sInit.getFabric8Connection();
        NodeList nodeList = client.nodes().list();
        client.close();
        return nodeList;
    }
}
