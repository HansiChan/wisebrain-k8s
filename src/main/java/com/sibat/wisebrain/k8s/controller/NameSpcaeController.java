package com.sibat.wisebrain.k8s.controller;

import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenkai
 */
@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "k8s-NameSpace接口")
public class NameSpcaeController {
    @Autowired
    private K8sInit k8sInit;
    @ApiOperation(value = "ListNamespace", notes = "ListNamespace")
    @GetMapping(value = "/namespace/list")
    public NamespaceList listNamespace(){
        KubernetesClient client = k8sInit.getFabric8Connection();
        NamespaceList namespaceList = client.namespaces().list();
        client.close();
        return namespaceList;
    }
}
