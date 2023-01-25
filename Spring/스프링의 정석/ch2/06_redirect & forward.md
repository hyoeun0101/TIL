## 🍎 redirect

= 재요청, 2번 요청

![이미지](/image/redirect.PNG)

1. 요청
2. `DS`-> `Controller`에서 redirect 반환
3. `DS`->`RedirectView`
4. `RedirectView`는 `HTTP/1.1 302 Location: /register/add` 응답헤더를 만들어 응답.

자동으로 재요청

![이미지](/image/jstlView.PNG)

1. 자동으로 재요청
2. `DS`->`Controller`에서 뷰이름 반환
3. ViewResolver가 진짜 경로 반환 후 JstmlView 에게 넘김
4. JstmlView가 모델을 해당 jsp파일에 넘겨줌
5. 응답

### \*\* 정리

- `두 번 요청하는 것`.
- 브라우저의 url은 redirect로 재요청한 url이 보여진다.
- 처음 요청의 model과 리다이렉트 후 model은 다른 것이다. 리다이렉트 시에는 모델의 데이터가 파라미터로 넘어간다.

```java
@PostMapping("/register/save")
public Strign save(User user, Model m) throws Exception{
    if(!isValid(user)){
        String msg = URLEncoder.encode("id를 잘못를 입력했습니다.","utf-8");

        m.addAttribute("msg",msg);
        return "redirect:register/add";
        //위와 동일!!!
        //return "redircet:/register/add?msg="+msg;
    }
    return "registerInfo";
}
```

## 🍎 Forward

= 내부에서 재요청 , 한번만 요청

![이미지](/image/forward.PNG)

1. 요청
2. `DS`-> `Controller`에서 forward 반환
3. `InternalResourceView`에게 전달
4. /register/add로 요청, 호출

### \*\* 정리

- 한번만 요청.
- 내부적으로 호출이 된다.
- 브라우저의 url은 처음에 요청한 경로이고, 화면은 forward로 요청한 결과를 보여준다.
- 예시로 pdf, csv, excel 파일 다운로드

### 🍎 redirect vs forward

![img](/image/redirect_forward.PNG)

## Redirect

1. 요청을 함.
2. HTTP/1.1 302 Location:/ch2/login.jsp 인 응답헤더로 응답.(300번은 리다이렉트, 바디없음)
3. 자동으로 Location에서 준 url로 GET으로 요청
4. 응답
   `리다이렉트로 재요청하는 건 무조건 GET요청.`

### Forward

1. 요청
2. forward 하면 request 를 login.jsp에게 전달(요청). response객체도.
3. 응답
