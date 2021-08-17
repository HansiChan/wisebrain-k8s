package com.sibat.wisebrain.k8s.config;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * @author chenkai
 */
@Configuration
public class K8sInit {

    @Value("${k8s.url}")
    private String apiHttps;
    /**
     * 安全证书相关
     **/
    @Value("${k8s.certificate-authority-data}")
    private String caCert;

    @Value("${k8s.client-certificate-data}")
    private String clientCert;

    @Value("${k8s.client-key-data}")
    private String clientKey;

    public KubernetesClient getFabric8Connection() {
        Config config = new ConfigBuilder().withMasterUrl(apiHttps).withTrustCerts(true)
                .withCaCertData(caCert)
                .withClientCertData(clientCert)
                .withClientKeyData(clientKey)
                .build();
        return new DefaultKubernetesClient(config);
    }
}
