package com.sibat.wisebrain.k8s.controller;


import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/test")
@Api(tags = "k8s信息查询接口")
public class HelloController {

    @Autowired
    private K8sInit k8sInit;

    @ApiOperation("hello world")
    @GetMapping(value = "/test")
    public NamespaceList test() {

        KubernetesClient client = k8sInit.getFabric8Connection();
//        KubernetesClient client= k8sInit.getFabric8Connection();
//        ServiceList services = client.services().inNamespace("nginx-impress").list();
//        Service service = client.services().inNamespace("kube-system").withName("kuboard").get();
        NamespaceList nodeList = client.namespaces().list();

//        log.info("this is " + nodeList);
        client.close();
        return nodeList;
    }

    private static String getType(Object a) {
        return a.getClass().toString();
    }

}
