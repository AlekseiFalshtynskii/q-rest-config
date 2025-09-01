package ru.b.q.rest.config;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@NoArgsConstructor(access = PRIVATE)
public class RestUtils {

  public static RestTemplate restTemplate(String url) {
    var restTemplate = new RestTemplate(requestFactory());
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));
    var interceptors = restTemplate.getInterceptors();
    interceptors.add(new QRestClientRequestInterceptor());
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }

  public static RestClient restClient(String url) {
    return RestClient.builder(restTemplate(url)).build();
  }

  @SneakyThrows
  private static ClientHttpRequestFactory requestFactory() {
    var httpClient = HttpClients.custom()
        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
            .setTlsSocketStrategy(DefaultClientTlsStrategy.createDefault())
            .build())
        .build();
    var requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout(1_000_000);
    requestFactory.setConnectionRequestTimeout(1_000_000);
    requestFactory.setHttpClient(httpClient);
    return requestFactory;
  }
}
