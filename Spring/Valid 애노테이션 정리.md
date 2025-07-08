🔴 🟡


### 🟡 String 필드 검증

|애노테이션|설명|
|--------|-----|
|`@NotBlank(message = "필수")`|null or "" 안됨.|
|`@NotNull(message = "필수")`|null 안됨.|





### 🟡 숫자 필드 검증


|애노테이션|설명|
|--------|-----|
|`@DecimalMax(value = 5)`|5 이하의 실수만 가능|
|`@DecimalMin(value = 5)`|5 이상의 실수만 가능|
|`@Digits(integer=, fraction)`||
|||




### 🟡 Boolean 필드 검증


|애노테이션|설명|
|--------|-----|
|`@AssertFalse`|false만 가능|
|`@AssertTrue`|true만 가능.|

