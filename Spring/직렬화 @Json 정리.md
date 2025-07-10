### 🟡 field serialize 관련 annotion 정리

- Spring에선 기본적으로 Jackson을 사용하여 직렬화, 역직렬화를 한다.


- 값이 null이면 값이 누락될 수 있다. ex) `{ "name" }`

#### 1. `@JsonInclude(JsonInclude.Include.NON_NULL)`

- 해당 필드의 값이 NULL이면 JSON에 포함하지 않는다.


#### 2. LocalDateTime 직렬화

- `@DataTimeFormat`, `@JsonFormat` 사용.


#### 3. `@JsonProperty`

- `@JsonProperty("error-codes")` : error-codes 키를 해당 필드와 매핑한다.

- `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` : 역직렬화만 가능. String -> Object만 가능. 즉 요청에서 읽기는 가능하고 응답 결과엔 표시 안된다.
