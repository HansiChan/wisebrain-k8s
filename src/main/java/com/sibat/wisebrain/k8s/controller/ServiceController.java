package com.sibat.wisebrain.k8s.controller;

import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "k8s-Service信息查询接口")
public class ServiceController {
    @Autowired
    private K8sInit k8sInit;

    @ApiOperation(value = "ListService", notes = "Service")
    @GetMapping(value = "/service/list")
    public ServiceList listK8sService(){


        KubernetesClient client= k8sInit.getFabric8Connection();
        ServiceList services = client.services().list();
        client.close();
        return services;
    }



    @ApiOperation(value = "submitService", notes = "submitService")
    @PostMapping(value = "/service/submit")
    @ResponseBody
    public Service createService (@RequestBody String  deploymentJson ) throws IOException {


        KubernetesClient client= k8sInit.getFabric8Connection();

        client.close();
        return null;
    }
}
