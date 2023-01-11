root-content.xml - 웹 노관련  
servlet-context.xml - 웹 상관

---

sns=facebook&sns=kakao 이렇게 여러개 요청 들어오면 배열로 들어옴.  
request.getParamValues("sns");  
el에서는 `${paramValues.sns[0]},  ${paramValues.sns[1]}`

---

js에서 ${} 이건 template literal 인데, el은 서버에서 사용해서 우선순위가 더 높아 el로 인식하게 됨.   
그럴땐 한번더 감싸주기   
`${'${msg}'}` 이렇게!!

---

**JSTL**

```html
<form action="<c:url value='login'/>"></form>
```

### <c:url>의 역할

1. context root 자동 추가 (ch1/login 이렇게)
2. session id 자동 추가

---

### maven 모듈들의 실제 위치

사용자 > 사용자 > .m2 > repository  
모듈 충돌나거나 오류 생기면 repository 삭제하고 maven update 하면 됨.

---

### jsp가 서블릿으로 변환 되고, 컴파일된 결과 보기

이클립스  
Run>run configurations> Arguments> deploy 경로 복사 > 들어가기 > 한 단계 위로 tmp0 > work 들어가기

---

src/main/resources > log4j.xml > org.springframework.web <level value="trace"> 아니면 info

### XML의 특수 문자 처리

특수 문자 <, >,& 는 변환 필요.

ex)

```xml
<update id="update" parameterType="BoardDto">
    UPDATE board
    SET title = #{title},
        content = #{content},
        up_date = now()
    WHERE bno = #{bno} and bno <> 0
</update>
```

1. 변환하기 <> -> `&lt; &gt;`
2. `<![CDATA[ 여기 내용 ]]>` 으로 감싸기
   - 여기 내용에는 XML 태그가 없다는 뜻.

---

### mapper.xml에서 #{} ${} 태그 차이

1. #{title} 는 PreparedStatement를 사용. #{title} -> ?로 바뀐다.
   문자열(varchar)일 경우 알아서 따옴표 붙여줌.  
   값에만 쓸 수 있음. SQL Injection 방지할 수 있다.

```java
String sql = "INSERT INTO board VALUES (?,?,?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
int result = pstmt.executeUpdate();
```

2. '${title}' 는 Statement를 사용. 따옴표를 붙여서 바뀐다.
   더 유연하다. 왜? 테이블 이름에도 쓸 수 있음.
   유연하기 때문에 내부의 값을 주입할 때만 사용.

```java
String sql = "INSERT INTO board VALUES ('"+title+"')";
Statement stmt = conn.createStatement();
int result = stmt.executeUpdate(sql);
```
