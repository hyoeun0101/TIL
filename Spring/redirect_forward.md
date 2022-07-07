# Redirect
:재요청   
   
처음 요청: "/save" -> [DS] -> [Controller] : "redirect:/home" -> [DS] -> [RedirectView] : 응답 헤더에 붙이기 (Http/1.1 302   Location: /home) -> 응답   
    
자동으로 재요청 "/home" -> [DS] -> [Controller]: "home" -> [DS] -> [ViewResolver] :진짜 경로 -> [DS] -> [JSTLView] (+Model)-> [home.jsp] -> 응답.  
   
결론: 두 번 요청하는 것임. 그러니 처음 요청과 두번 째 요청에서 Model은 같은 게 아님.   
   

# Forward
: 내부에서 재요청 , 한번만 요청   

처음 요청: "/save" -> [DS] -> [Controller] : "forward:/home" -> [DS] -> [IntercalResourceView] :"/home" 요청-> [DS] -> [Controller]:"home" -> [DS] -> [ViewResolver]:진짜 경로 -> [DS] -> [JStLView] -> [home.jsp] -> 응답   

예시로 pdf, csv, excel 파일 다운로드 
