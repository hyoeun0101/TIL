## 필요한 설정
1. RedisConfig
2. InterceptorConfig : WebMvcConfigurer 구현.


## Interceptor
### HttpServletReqeust에서 PathVariable 값 읽기 
```java
request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
```