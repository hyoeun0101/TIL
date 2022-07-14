root-content.xml - 웹 노관련   
servlet-context.xml - 웹 상관   
 
------

sns=facebook&sns=kakao 이렇게 여러개 요청 들어오면 배열로 들어옴.      
request.getParamValues("sns");       
el에서는 `${paramValues.sns[0]},  ${paramValues.sns[1]}`       

-------
js에서 ${} 이건 template literal 인데, el은 서버에서 사용해서 우선순위가 더 높아 el로 인식하게 됨.   
그럴땐 한번더 감싸주기   
`${'${msg}'}` 이렇게!!   

----
__JSTL__
```html
<form action= "<c:url value='login'/>" >
</form>
```
### <c:url>의 역할
1. context root 자동 추가 (ch1/login 이렇게)
2. session id 자동 추가
   
----
### maven 모듈들의 실제 위치   
사용자 > 사용자 > .m2 > repository   
모듈 충돌나거나 오류 생기면 repository 삭제하고 maven update 하면 됨.
----
### jsp가 서블릿으로 변환 되고, 컴파일된 결과 보기
이클립스   
Run>run configurations> Arguments> deploy 경로 복사 > 들어가기 > 한 단계 위로 tmp0 >  work 들어가기