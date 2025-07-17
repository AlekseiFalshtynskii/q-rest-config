# Библиотека единой конфигурации RestTemplate

#### 3.0.1

Поддерживает Spring boot 3.3.4, JDK 21

## Описание работы

Предоставляет метод конфигурации дефолтного RestTemplate для создания оберток темплейтов конкретных сервисов\
Прокидывает необходимые заголовки дальше в вызовы

#### 1. Добавить в зависимости библиотеку

~~~
implementation "ru.b.q:q-rest-config"
~~~

#### 2. Реализовать конфиг бинов темплейтов с применением дефолтной конфигурации

~~~
import static ru.b.q.rest.config.RestUtils.restTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RestTemplateConfig {

  private final AuthApi authApi;

  @Bean
  public RestTemplate authRestTemplate() {
    return restTemplate(authApi.getServiceUrl());
  }
}
~~~

