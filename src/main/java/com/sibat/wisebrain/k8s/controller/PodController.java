package com.sibat.wisebrain.k8s.controller;

import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "k8s-Pod信息接口")
public class PodController {

    @Autowired
    private K8sInit k8sInit;

    @ApiOperation("hello world")
    @GetMapping(value = "/pod/list")
    public PodList listPod() {
        KubernetesClient client = k8sInit.getFabric8Connection();
        PodList podList = client.pods().list();
        client.close();
        return podList;
    }

    @ApiOperation(value = "CreatePod", notes = "CreatePod")
    @PostMapping(value = "/pod/create")
    @ResponseBody
    public Pod createk8spod(@RequestParam(value = "NameSpaceName") String nsName,
                            @RequestParam(value = "PodName") String pdName,
                            @RequestParam(value = "ContainerName") String ctName,
                            @RequestParam(value = "ImageName") String imName,
                            @RequestParam(value = "ContainerPort") int cnPort,
                            @RequestParam(value = "HostPort") int htPort) {
        System.out.println(nsName);
        KubernetesClient client = k8sInit.getFabric8Connection();
        ObjectMeta objectMeta = new ObjectMetaBuilder().
                withName(pdName).
                withNamespace(nsName).
                build();
        //Container 端口配置
        ContainerPort containerPort = new ContainerPortBuilder().
                withContainerPort(cnPort).
                withHostPort(htPort).
                build();
        //Container 配置
        Container container = new ContainerBuilder().
                withName(ctName).
                withImage(imName).
                withPorts(containerPort).
                build();
        //Spec 配置
        PodSpec podSpec = new PodSpecBuilder().
                withContainers(container).
                build();
        //Pod 配置
        Pod pod = new PodBuilder().
                withApiVersion("v1").
                withKind("Pod").
                withMetadata(objectMeta).
                withSpec(podSpec).
                build();
        client.pods().create(pod);
        client.close();
        return pod;
    }
}
