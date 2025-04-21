package ru.b.q.rest.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DefaultRestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    return defaultRestTemplate();
  }

  public static RestTemplate defaultRestTemplate() {
    var restTemplate = new RestTemplate(requestFactory());
    var interceptors = restTemplate.getInterceptors();
    interceptors.add(new RestTemplateDefaultInterceptor());
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }

  @SneakyThrows
  private static ClientHttpRequestFactory requestFactory() {
    var httpClient = HttpClients.custom()
        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(SSLContextBuilder.create()
                    .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                    .build())
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build())
            .build())
        .build();
    var requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout(1_000_000);
    requestFactory.setConnectionRequestTimeout(1_000_000);
    requestFactory.setHttpClient(httpClient);
    return requestFactory;
  }
}
