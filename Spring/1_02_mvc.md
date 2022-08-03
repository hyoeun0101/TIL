# MVC 패턴
### 1. 관심사의 분리
__분리__
- 관심사의 분리
- 변하는 것과 변하지 않는 것 분리
- 공통 코드 분리   
   
[.java]
```java
@Controller
public class YoilTeller{// http://localhost/ch2/getYoil?year=2022&month=1&day=1
    @RequestMapping("/getYoil")
    public void main(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //1. 입력
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");

        int yyyy = Integer.parseInt(year);
        int mm = Integer.parseInt(month);
        int dd = Integer.parseInt(day);

        //2. 처리
        Calendar cal = Calendar.getInstance();
        cal.set(yyyy,MM-1,dd);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        char yoil = " 일월화수목금토".charAt(dayOfWeek);

        //3. 출력
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body>");
        out.println(year+"년 "+month+"월 "+day+"일은 "+yoil+"입니다.");
        out.println("</body>");
        out.println("</html>");
    }
}
```
#### 관심사
- 입력
- 처리: 요일 계산
- 출력
    
#### OOP 5대 설계 원칙- SOLID 중 SRP
1. __SRP__ (단일 책임 원칙) : 하나의 메서드는 하나의 책임(관심사)를 갖는다.
    

# 2. mvc 패턴
![img](/image/mvc.PNG))   
`DS`가 입력 처리 후 `Controller` 넘겨주면 처리한 결과를 `Model`에 저장하고 `Model`을 `View`에게 전달. `View`에선 Model에 담긴 데이터를 처리한 후 클라이언트에게 응답.    
    
- 입력 분리: 입력 데이터를 매개변수로 받기
- 처리 분리: 별도의 메서드 생성
- 출력 분리: 별도의 jsp 생성    
[.java]
```java
@Controller
public class YoilTeller{// http://localhost/ch2/getYoil?year=2022&month=1&day=1
    @RequestMapping("/getYoil")
    public String main(int year, int month, int day, Model model) throws IOException{//입력
        //처리
        char yoil = getYoil(year,month,day);

        model.addAttribute("year",year);
        model.addAttribute("month",month);
        model.addAttribute("day",day);
        model.addAttribute("yoil",yoil);

        return "yoil";
    }

    private char getYoil(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(yyyy,MM-1,dd);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return " 일월화수목금토".charAt(dayOfWeek);
    }
}
```
[WEB-INF/views/yoil.jsp]
```jsp
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<title>Home</title>
</head>
<body>
<p>${year}년 ${month}월 ${day}일은 ${yoil}요일입니다.</p>
</body>
</html>

```


[.java]
```java
@Controller
public class YoilTeller{// http://localhost/ch2/getYoil?year=2022&month=1&day=1
    @RequestMapping("/getYoil")
    public ModelAndView main(int year, int month, int day) throws IOException{

        ModelAndView mv = new ModelAndView();
        
        char yoil = getYoil(year,month,day);

        mv.addObject("year",year);
        mv.addObject("month",month);
        mv.addObject("day",day);
        mv.addObject("yoil",yoil);
        //결과를 보여줄 view 지정
        mv.setViewName("yoil");

        return mv;

    
    }

    private char getYoil(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(yyyy,MM-1,dd);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return " 일월화수목금토".charAt(dayOfWeek);
    }
}
```
    
컨트롤러 메서드의 반환타입    
- __String__ -> return "jsp 이름"
- __void__ -> 맵핑된 url의 끝단어가 뷰 이름으로 자동 설정
- __ModelAndView__     
    
[ModelAndView 사용]
```java
@Controller
public class YoilTeller{// http://localhost/ch2/getYoil?year=2022&month=1&day=1
    @RequestMapping("/getYoil")
    public ModelAndView main(int year, int month, int day) throws IOException{
        // ModelAndView 객체 생성
        ModelAndView mv = new ModelAndView();
        
        char yoil = getYoil(year,month,day);

        mv.addObject("year",year);
        mv.addObject("month",month);
        mv.addObject("day",day);
        mv.addObject("yoil",yoil);
        //결과를 보여줄 view 지정
        mv.setViewName("yoil");

        return mv;
    }
...
}
```
-----
### 정리
MVC 패턴이란?
관심사를 분리한 것.
입력은 DS처리하고, 컨트롤러에서 값을 처리하여 모델에 저장하고, 모델을 뷰에게 전달하여 결과를 출력.

OOP 5대 원칙
1. SRP(단일 책임 원칙) : 하나의 클래스, 메서드는 하나의 책임을 갖는다. - 관심사의 분리
2. OCP(개방 폐쇄 원칙) : 모든 클래스는 확장에 열려있고, 수정에 닫혀있어야한다.
-> 추상화
3. LSP(리스코프 치환 원칙) : 자식의 객체를 부모의 객체로 치환(교체)할 수 있어야한다.-> 상속
4. ISP(인터페이스 분리 원칙) : 클라이언트는 자신이 사용하지 않는 메소드에 의존 관계를 맺으면 안된다
5. DIP(의존성 역전 원칙) : 
