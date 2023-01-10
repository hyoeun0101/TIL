## 인터셉터

: 인터셉터는 `가로채는` 의미를 가지고 있다.  
클라이언트에서 서버로 들어온 요청(HttpRequest)을, 컨트롤러에서 Handler를 갈 때 가로채어 추가적인 작업을 할 수 있다.  
관리자 페이지에 접근하기 전에 관리자 인증을 하는 용도 등에 활용된다.  
알고있는 Filter와 비슷하다고 볼 수 있지만 Filter와는 엄연히 차이점이 존재한다.
![1](https://user-images.githubusercontent.com/96059261/211508902-fe794b85-41b1-4b42-9d45-135b1f70b76f.PNG)  
|차이|Filter|Interceptor|
|---|------|-----------|
|호출 시점|DispatcherServlet이 실행 전|DispatchServlet 실행 후|
|설정 위치|web.xml|spring-servlet.xml|
|구현 방식|web.xml에서 설정하기|설정 및 메서드 구현 필요|
