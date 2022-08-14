데이터가 문자열로 요청이 들어오면, `WebDataBinder`에서 타입 변환 후 BindingReuslt에 저장,   
두 번째로 데이터 검증. 에러가 없으면 값 저장하고, 에러가 있으면 BindingReuslt 에 저장.   
   


### __[실습] RegisterController 회원가입__   

# 1. 타입 변환
입력 받으면 WebDataBinder 거쳐 타입 변환해줌.
- 생일 필드를 String -> Date
- SNS 필드값을 여러 개 받으면, String[]로 들어옴. 
- User의 SNS필드가 String이면, String[]-> String 으로 자동 변환
- "카카오톡, 페이스북, 인스타그램" 이렇게.
```java
@InitBinder
public void toDate(WebDataBinder binder){
    //포맷 형식
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //Date 타입으로 변환. CustomDateEditor 사용
    binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false));
}
```
또는
```java
 public class User{
    private String id;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birth;
    private String name;
 }
```

#####  - 취미 필드 추가
- private String[] hobby;
- 입력 : Tennis#piano#swimming
- 이러면 [Tennis#piano#swimming] 이렇게 들어감.
- 
```java
@InitBinder
public void toDate(WebDataBinder binder){
    //포맷 형식
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //Date 타입으로 변환. CustomDateEditor 사용
    binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false));
    //binder.registerCustomEditor(String[].class,"hobby", new StringArrayPropertyEditor("#"));
    binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor("#"));
}
```
- [Tennis,piano, swimming] 이렇게 들어옴.
### 타입 변환
1. PropertyEditor
    - 양방향으로 타입 변환.
    - 특정 타입이나 특정 필드에 적용 가능
    - 디폴트 PropertyEditor는 스프링이 기본 제공함.
    - 커스텀 PropertyEditor는 사용자가 직접 구현. PropertyEditorSupport를 상속하면 편리 

    - propertydeitors 서칭해서 필요할 때 찾아보기.
    - 모든 컨트롤러 내에서 변환하려면 WebBindingInitializer를 구현 후 등록
    - 특정 컨트롤러 내에서 변환은 메서드에 @InitBinder 붙여주기

2. Converter
    - 단방향 타입 변환.
    - PropertyEditor 단점을 개선. 인스턴스 변수를 씀.(stateful)-> 싱글톤으로 사용 불가. 즉, 변환할 때마다 새로운 객체 계속 생성
    - WebDataBinder에 DefaultFormattingConversionService가 기본 등록
    - 모든 컨트롤러 변환- ConfigurableWebBindingInitializer를 설정
    - 특정 컨트롤러 변환 - 컨트롤러에 @InitBinder 붙은 메서드 작성
```java
public class StringToStringArrayConverter impements Converter<String,String[]>{
    @Override
    public String[] convert(String source){
        return source.split("#");
    }
}
```
String -> String[]
위의 Converter를 ConversionService에 등록. 타입 변환 서비스를 제공. 여러 Converter를 등록 가능   


3. Formmater
- 양방향 타입 변환
- 바인딩할 필드에 적용 - @NumberFormat, @DateTimeFormat
```java
@DateTimeFormat(pattern="yyyy/MM/dd")
Date birth;

//123,456 이렇게 들어오면 숫자로
@NumberFormat(parttern="###,###")
BigDecimal salary;
```


- 등록되어 있는 ConversionService 출력하기
```java
@InitBinder
public void toDate(WebDataBinder binder){
    ConversionService cs = binder.getConversionService();
    
    //포맷 형식
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //Date 타입으로 변환. CustomDateEditor 사용
    binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false));
    //binder.registerCustomEditor(String[].class,"hobby", new StringArrayPropertyEditor("#"));
    binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor("#"));
}
```
- 우선 순위
    - 커스텀 PropertyEditor
    - ConversionService
    - 디폴트 PropertyEditor

# 2. 데이터 검증
- - 검증이란 관심사를 분리

### - Validator : 객체를 검증하기 위한 인터페이스.
```java
public interface Validator{
    //검증 가능한 객체인가
    boolean supports (Class<?> clazz);
    //객체 검증 - target: 검증할 객체, errers: 검증시 발생한 에러 저장소
    void validate(@Nullable Object target, Errors errors);
}
```
### - Errors 인터페이스
```java
public interface Errors{
    //객체 전체에 대한 에러 "id 또는 pwd 일치하지 않습니다."
    void reject(String errorCode);
    //필드: 에러 , id-"유효하지 않습니다."
    void rejectValue(String field, String errorCode);
}
```
### - UserValidator 작성
```java
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		//clazz가 User또는 자손인지 검증
		return User.class.isAssignableFrom(clazz);
        //return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		System.out.println("UserValidator.validate() is called");
		
		User user= (User)target;// 검증할 User 객체 들어옴. 사실 supports에서 검증해주기 때문에 (User) 필요없긴 함.
		String id = user.getId();
		//id가 비었거나 공백이면,"id"필드에서 "required"라는 에러 코드를 저장.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "required");
		ValidationUtils.rejectIfEmpty(errors, "pwd", "required");
		 //id가 null, 길이가 5~12사이가 아니라면, "id"필드에 "invalidLength"라는 에러 코드로 저장.
		if(id==null|| id.length()<5 || id.length()>12) {
			errors.rejectValue("id","invalidLegnth",new String[]("5","12"));
		}
	}
}
```
### - 검증 방법
1. 수동으로 검증 - 메서드 내에서 UserValidator()호출
```java
UserValidator uv = new UserValidator();
uv.validate(user,result);
if(result.hasErrors())//에러가 있으면,
    return "registerForm";
```

2. 자동으로 검증 - @InitBinder
```java
@InitBinder
public void toDate(WebDataBinder binder){
    //setValidator로 WebDataBinder에 UserValidator를 등록
    binder.setValidator(new UserValidator());
}

@PostMapping("register/add")
public String save(@Valid User user,BindingResult result){}

```
- @InitBinder에 검증 객체 등록하고, 검증할 객체 앞에 @Valid 붙여주기
- maven repository-  Bean Validation API

### - 글로벌 Validator
- 하나의 Validator로 여러 객체를 검증할 때, 글로벌 Validator로 등록.
1. servlet-context.xml에 추가 - 글로벌 Validator 등록
```xml
<annotation-driven validator="globalValidator" />
<!-- <beans:bean id="globalValidator" class="GlobalValidator 위치" /> -->
<beans:bean id="globalValidator" class="com.hyoding.ch2.GlobalValidator" />
```
2. 글로벌 Validator와 로컬 Validator를 동시에 적용
```java
@InitBinder
public void toDate(WebDataBinder binder){
    //로컬 Validator 추가
    binder.addValidators(new UserValidator());
}
```

### - MessageSource
- 다양한 리소스(파일, 배열 등)에서 메시지를 읽기 위한 인터페이스.

- 프로퍼티 파일을 메세지 소스로 하는 ResourceBundleMessageSource를 등록.
-> servlet-context.xml에 추가
```xml
<beans:bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
		<beans:property name="basenames">
			<beans:list>
				<beans:value>error_message</beans:value> <!-- /src/main/resources/error_message.properties -->
			</beans:list>
		</beans:property>
		<beans:property name="defaultEncoding" value="UTF-8"/>
	</beans:bean>
```

- src/main/resources/error_message.properties 추가
```
required=필수 항목 입니다.
required.user.pwd=비밀번호는 필수 항목입니다.
invalidLength.id=아이디 길이는 {0}~{1}사이여야 합니다.
```
"id"필드에 에러가 발생하면, "required"     
1순위 required.user.id      
2순위 required.id   
3순위 required.java.lang.String   
4순위 required   

### - 검증 메세지 출력
```html
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form modelAttribute="user">
<!-- 이렇게 바뀜.<form id="user" action="/ch2/register/add" method="post"> -->

<form:errors path="id" cssClss="msg"/>
<!-- 이렇게 바뀜.<span id="id.errors" class="msg">필수 입력 항목입니다.</span> -->

</form:form>
```