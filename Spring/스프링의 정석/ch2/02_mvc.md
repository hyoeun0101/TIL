# 🍎 MVC 패턴

![img](/image/mvc.PNG))  
`DispatcherServlet`이 입력을 처리한 후 `Controller` 넘겨주면 처리한 결과를 `Model`에 저장하고 `Model`을 `View`에게 전달한다. `View`에선 Model에 담긴 데이터를 처리한 후 클라이언트에게 응답한다.

## 🍎 관심사의 분리

- 관심사의 분리
- 변하는 것과 변하지 않는 것 분리
- 공통 코드 분리

## 🍎 컨트롤러 메서드의 반환타입

- **String** -> return "jsp 이름"
- **void** -> 맵핑된 url의 끝단어가 뷰 이름으로 자동 설정
- **ModelAndView** -> ModelAndView 객체를 생성하여 반환한다.  
  [ModelAndView 예시]

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
