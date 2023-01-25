## 🍎 @RequestParam

: 요청의 파라미터와 연결할 매개변수 앞에 붙이는 애노테이션

**<속성>**

- name : 파라미터 이름
- required : 파라미터 값이 필수이면 true, 아니면 false
- defaultValue : 파라미터 기본값 설정

### 1. @RequestParam 생략하, 매개변수 타입이 String

```java
@GetMapping("/rp")
//                @RequestParam(name="year", required=false)
public String main(String year, String month, String day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}
```

- /rp 로 요청시 year=null
- /rp?year 요청시 year은 빈문자열

### 2. @RequestParam 작성 ,매개변수 타입이 String

```java
@GetMapping("/rp")
//                  @RequestParam(name="year", required=true)
public String main(@RequestParam String year,
                    @RequestParam String month,
                    @RequestParam String day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}
```

- /rp 요청시 반드시 줘야할 데이터를 클라이언트에서 안줬기 때문에 400err
- /rp?year 요청시 year은 빈문자열

### 3. @RequestParam(required=false), 타입은 int

```java
@GetMapping("/rp")
public String main(@RequestParam(required=false) int year){

    return "yoil";
}
```

- /rp 요청 시 year은 null인데 int로 처리할 수 없으니 서버 에러 500
- /rp?year 요청 시 year은 빈문자열이고, 값을 잘 못 줬으니 클라이언트 에러 400

- required=false 일 땐 defaultValue 작성하자.

### 4. @RequestParam(required=true)이고 타입은 int

```java
@GetMapping("/rp")
public String main(@RequestParam(required=true) int year){

    return "yoil";
}
```

- /rp 요청 시 year은 필수값이라 클라이언트 에러
- /rp?year 요청 시 year은 빈문자열이고, 값 잘못줬으니 클라이언트 에러

- required=true일때는 사용자가 잘못된 값을 전달할 경우를 대비하여 예외처리하자.

## 🍎 @ModelAttribute

- 매개변수 앞에 쓰면 매개변수의 값을 model에 저장함.
- 반환 타입 앞에 쓰면 반환값을 Model에 저장함.
- 참조형 매개 변수 앞에 @ModelAttribute 생략 가능.

```java
@Controller
public class YoilTellerMVC{
    @RequestMapping("/getYoil")
    //                  @ModelAttribute("myDate")
    public String main(@ModelAttribute MyDate date, Model m){
        char yoil=getYoil(date)
        return "yoil";
    }

    private @ModelAttribute("yoil") char getYoil(MyDate date){
        ...
    }
}
```

---

# 🍎정리

#### 컨트롤러 매개변수에 붙일 수 있는 애노테이션

- @RequestParam : 타입이 기본형, String일때 생략돼있음.
  - 기본형이나 String 일 경우 Model에 저장할 필요 없음. 뷰에서 `${param.파라미터 이름}`으로 바로 참조 가능
- @ModelAttribute : 타입이 참조형일때 생략돼있음.
