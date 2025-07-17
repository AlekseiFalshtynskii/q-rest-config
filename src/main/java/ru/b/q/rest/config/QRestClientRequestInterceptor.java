package ru.b.q.rest.config;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import lombok.SneakyThrows;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class QRestClientRequestInterceptor implements ClientHttpRequestInterceptor {

  @SneakyThrows
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
    var authentication = getContext().getAuthentication();
    if (nonNull(authentication) && isNotEmpty(authentication.getCredentials())) {
      request.getHeaders().add(AUTHORIZATION, (String) authentication.getCredentials());
    }
    return execution.execute(request, body);
  }
}
