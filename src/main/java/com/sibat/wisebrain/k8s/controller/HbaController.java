package com.sibat.wisebrain.k8s.controller;

import com.sibat.wisebrain.k8s.config.K8sInit;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.AutoscalingAPIGroupClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenkai
 */
@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = "k8s-hba接口")
public class HbaController {
    @Autowired
    private K8sInit k8sInit;

    @ApiOperation(value = "ListHpa", notes = "ListHpa")
    @GetMapping(value = "/hpa/list")
    @ResponseBody
    public HorizontalPodAutoscalerList horizontalPodAutoscalerList() {
        KubernetesClient client = k8sInit.getFabric8Connection();
        AutoscalingAPIGroupClient oClient =client.adapt(AutoscalingAPIGroupClient.class);
        MixedOperation<HorizontalPodAutoscaler, HorizontalPodAutoscalerList, DoneableHorizontalPodAutoscaler, Resource<HorizontalPodAutoscaler, DoneableHorizontalPodAutoscaler>> operation1
                =oClient.horizontalPodAutoscalers();
        HorizontalPodAutoscalerList horizontalPodAutoscalerList =operation1.list();
        oClient.close();
        return horizontalPodAutoscalerList;
    }
    @ApiOperation(value = "CreateHpa", notes = "CreateHpa")
    @PostMapping(value="/hpa/create")
    public HorizontalPodAutoscaler horizontalPodAutoscaler (@RequestParam(value = "hpaname") String hpaName,
                                              @RequestParam(value = "NameSpaceName") String nameSpaceName,
                                              @RequestParam(value = "ResourceName") String resourceName,
                                              @RequestParam(value = "TargetType") String targetType,
                                              @RequestParam(value = "TargetAverageUtilization") int averageUtilization,
                                              @RequestParam(value = "MaxReplicas") int maxReplicas,
                                              @RequestParam(value = "MinReplicas") int minReplicas){

        KubernetesClient client = k8sInit.getFabric8Connection();
        //变量默认值


        //mataData 数据组装
        ObjectMeta mataData = new ObjectMeta();
        mataData.setName(hpaName+ "-deployment-hpa");
        mataData.setNamespace(nameSpaceName);


        //Spec 数据组装
        HorizontalPodAutoscalerSpec hpaSpec = new HorizontalPodAutoscalerSpec();
        hpaSpec.setMaxReplicas(maxReplicas);
        hpaSpec.setMinReplicas(minReplicas);

        //2.scaleTargetRef
        CrossVersionObjectReference crossVersionObjectReference = new CrossVersionObjectReference();
        crossVersionObjectReference.setName(hpaName+ "-deployment-hpa");
        crossVersionObjectReference.setApiVersion("app/v1");
        crossVersionObjectReference.setKind("Depolyment");

        hpaSpec.setScaleTargetRef(crossVersionObjectReference);

        //metric  数据组装
        MetricSpec metricSpec=new MetricSpec();
        ResourceMetricSource resourceMetricSource=new ResourceMetricSource();
        resourceMetricSource.setName(resourceName);
        MetricTarget metricTarget = new MetricTarget();
        metricTarget.setType(targetType);
        metricTarget.setAverageUtilization(averageUtilization);
        resourceMetricSource.setTarget(metricTarget);
        metricSpec.setResource(resourceMetricSource);
        List<MetricSpec> metricSpecs = new ArrayList<>();
        metricSpecs.add(metricSpec);
        hpaSpec.setMetrics(metricSpecs);



        HorizontalPodAutoscaler horizontalPodAutoscaler = new HorizontalPodAutoscaler();
        horizontalPodAutoscaler.setApiVersion("apps/v1");
        horizontalPodAutoscaler.setKind("Deployment");
        horizontalPodAutoscaler.setMetadata(mataData);
        horizontalPodAutoscaler.setSpec(hpaSpec);



        //将基础Client转换为AppsAPIGroupClient，用于操作deployment
        AutoscalingAPIGroupClient oclient =client.adapt(AutoscalingAPIGroupClient.class);
        oclient.horizontalPodAutoscalers().create(horizontalPodAutoscaler);
        oclient.close();
        client.close();
        return horizontalPodAutoscaler;


    }

}
