# Redirect
:재요청, 2번 요청   

![이미지](/image/redirect.PNG)

1. 요청
2. `DS`-> `Controller`에서 redirect 반환
4. `DS`->`RedirectView`
5. `RedirectView`는 `HTTP/1.1 302 Location: /register/add` 응답헤더를 만들어 응답.

자동으로 재요청

![이미지](/image/jstlView.PNG)

1. 자동으로 재요청
2. `DS`->`Controller`에서 뷰이름 반환
3. ViewResolver가 진짜 경로 반환 후 JstmlView 에게 넘김
4. JstmlView가 모델을 해당 jsp파일에 넘겨줌
5. 응답
   
결론: 두 번 요청하는 것. 그러니 처음 요청과 두번 째 요청에서 Model은 같은 게 아님. 브라우저의 url은 redirect로 재요청한 url이 보여짐.
  

# Forward
: 내부에서 재요청 , 한번만 요청     


![이미지](/image/forward.PNG)

1. 요청
2. `DS`-> `Controller`에서 forward 반환
3. `InternalResourceView`에게 전달
4. /register/add로 요청, 호출

결론: 한번만 요청. 내부적으로 호출이 됨. 브라우저의 url은 처음에 요청한 경로이고, 화면은 forward로 요청한 결과를 보여줌.
예시로 pdf, csv, excel 파일 다운로드 

![img](/image/redirect_forward.PNG)

# Redirect
1. 요청을 함.
2. HTTP/1.1 302  Location:/ch2/login.jsp 인 응답헤더로 응답.(300번은 리다이렉트, 바디없음)
3. 자동으로 Location에서 준 url로 GET으로 요청
4. 응답
리다이렉트로 재요청하는 건 무조건 GET요청.

# Forward
1. 요청
2. forward 하면 request 를 login.jsp에게 전달(요청). response객체도.
3. 응답
