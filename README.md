# Библиотека единой конфигурации RestTemplate

#### 3.0.0

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
import static ru.b.q.rest.config.DefaultRestTemplateConfig.defaultRestTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RestTemplateConfig {

  private final AuthApi authApi;

  @Bean
  public RestTemplate authRestTemplate() {
    return restTemplate(authApi);
  }

  private RestTemplate restTemplate(Api api) {
    var restTemplate = defaultRestTemplate();
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(api.getServiceUrl()));
    return restTemplate;
  }
}
~~~

