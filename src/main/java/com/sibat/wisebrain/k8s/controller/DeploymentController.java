package com.sibat.wisebrain.k8s.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.api.model.apps.DoneableDeployment;
import io.fabric8.kubernetes.client.AppsAPIGroupClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


/**
 * @author chenkai
 */
@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "k8s-Depolyment接口")
public class DeploymentController {

    @Autowired
    private K8sInit k8sInit;

    @ApiOperation(value = "CreatDeployment", notes = "Deployment")
    @GetMapping(value = "/deployment/list")
    @ResponseBody
    public DeploymentList deploymentList() {
        KubernetesClient client = k8sInit.getFabric8Connection();
        AppsAPIGroupClient oClient = client.adapt(AppsAPIGroupClient.class);
        MixedOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>> operation1
                = oClient.deployments();
        DeploymentList deploymentList = operation1.list();
        oClient.close();
        return deploymentList;
    }

    @ApiOperation(value = "submitDeployment", notes = "Deployment")
    @PostMapping(value = "/deployment/submit", produces = "application/json")
    @ResponseBody
    public String createDeployment(@RequestBody String deploymentJson) throws IOException {
        KubernetesClient client = k8sInit.getFabric8Connection();
        // parse JSON
        String filename = "./test.yaml";
        YamlController.createYaml(filename, deploymentJson);
        // fileName为一个外部的pod.yaml文件
        List<HasMetadata> resources = client.load(new FileInputStream(filename)).get();
        if (resources.isEmpty()) {
            System.err.println("No resources loaded from file: ");
            return CommonController.response("200", "depolument资源创建失败", "");
        }
        HasMetadata resource = resources.get(0);
        //String namespace = "default";

        Deployment deployment = (Deployment) resource;
        AppsAPIGroupClient oClient = client.adapt(AppsAPIGroupClient.class);
        MixedOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>> deployments = oClient.deployments();
        // 此处创建deployment资源！！！
        Deployment result = deployments.create(deployment);
        System.out.println("Created deployment " + result.getMetadata().getName());


        oClient.close();
        return CommonController.response("200", "depolymemt-" + result.getMetadata().getName() + "创建成功", "");
    }


    @ApiOperation(value = "DeleteDeployment", notes = "Deployment")
    @PostMapping(value = "/deployment/delete", produces = "application/json")
    @ResponseBody
    public String deleteDeployment(@RequestBody String deploymentJson) {

        JSONObject deploymentObject = JSONUtil.parseObj(deploymentJson);
        String namespace = deploymentObject.getStr("namespace");
        String deploymentName = deploymentObject.getStr("deploymentName");

        // oClient
        KubernetesClient client = k8sInit.getFabric8Connection();
        AppsAPIGroupClient oClient = client.adapt(AppsAPIGroupClient.class);
        System.out.println(namespace);
        System.out.println(deploymentName);

        // delete depolyment
        //oClient.deployments().inNamespace(namespace).withName(deploymentName).delel

        MixedOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>>
                temp = oClient.deployments();
        Deployment deployment = temp.inNamespace(namespace).withName(deploymentName).get();
        if (CommonController.isNull(deployment) == true){
            return CommonController.response("400", deploymentName + "不存在", "");
        }
        //boolean result = deployment.
        System.out.println(deployment);
        //System.out.println("delete deployment " + result);
        oClient.close();
        return CommonController.response("200", "depolymemt-" + deploymentName + "删除成功", "");
    }


    @ApiOperation(value = "StatusDeployment", notes = "Deployment")
    @PostMapping(value = "/deployment/status", produces = "application/json")
    @ResponseBody
    public String statusDeployment(@RequestBody String deploymentJson) {

        JSONObject deploymentObject = JSONUtil.parseObj(deploymentJson);
        String namespace = deploymentObject.getStr("namespace");
        String deploymentName = deploymentObject.getStr("deploymentname");

        // oClient
        KubernetesClient client = k8sInit.getFabric8Connection();
        AppsAPIGroupClient oClient = client.adapt(AppsAPIGroupClient.class);


        // status depolyment
        DeploymentStatus status = oClient.deployments().inNamespace(namespace).withName(deploymentName).get().getStatus();
        System.out.println("Status deployment " + status);
        oClient.close();
        return CommonController.response("200", "depolymemt-" + deploymentName + "删除成功", "");
    }

    @ApiOperation(value = "logDeployment", notes = "Deployment")
    @PostMapping(value = "/deployment/log", produces = "application/json")
    @ResponseBody
    public String logDeployment(@RequestBody String deploymentJson) {
        JSONObject deploymentObject = JSONUtil.parseObj(deploymentJson);
        String namespace = deploymentObject.getStr("namespace");
        String deploymentName = deploymentObject.getStr("deploymentName");

        // oClient
        KubernetesClient client = k8sInit.getFabric8Connection();
        AppsAPIGroupClient oClient = client.adapt(AppsAPIGroupClient.class);


        // delete depolyment
        String result = oClient.deployments().inNamespace(namespace).withName(deploymentName).getLog();
        System.out.println("Log deployment " + result);
        oClient.close();
        return CommonController.response("200", "depolymemt-" + deploymentName + "删除成功", "");
    }
}
