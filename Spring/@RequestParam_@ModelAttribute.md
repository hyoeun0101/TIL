# @RequestParam
:요청의 파라미터가 기본형이나 String 형일 때 요청의 파라미터와 매개변수를 연결시켜주는 애노테이션   

### ex>   
@RequestParam 생략
```java
@GetMapping("/requestParam")
public String main(String year, String month, String day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}

```
@RequestParam 을 생략하면 @RequestParam(name="year", required=false) 와 같음.      
/requestParam  -> year=null      
/requestParam/year  -> year=""      
   
@ReuqestParam 작성   
```java
@GetMapping("/requestParam")
public String main(@RequestParam String year, 
                @RequestParam String month, 
                @RequestParam String day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}

```
@RequestParam 은 @RequestParam(name="year", required=true) 와 같음.   
/requestParam  -> 400 Bad Request ,, 필수기 때문   
/requestParam/year  -> year=""   
   
   
매개변수 타입이 기본형일 경우
```java
@GetMapping("/requestParam")
public String main(int year, int month, int day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}
```
/requestParam  -> year=null  -> 500 Error   
/requestParam/year  -> year="" -> 400Error   



```java
@GetMapping("/requestParam")
public String main(@RequestParam int year, 
                @RequestParam int month, 
                @RequestParam int day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}

```
/requestParam  -> year=null  -> 400 Error       
/requestParam/year  -> year="" -> 400Error    
   
   
defaultValue

```java
@GetMapping("/requestParam")
public String main(@RequestParam(required=false, defaultValue="2022") int year, 
                @RequestParam int month, 
                @RequestParam int day ,Model m){
    m.addAttribute("year",year);
    m.addAttribute("month",month);
    m.addAttribute("day",day);
    return "yoil";
}

```

/requestParam  -> year=1   
/requestParam/year  -> year=1   
   
   
=> required=true 이면 값이 들어왔을 때 발생할 수 있는 예외 처리 해주고, reuqired=false 이면 defaultValue로 디폴트 값 지정하는 게 좋음.   


# @ModelAttribute
: 요청의 파라미터가 참조형 타입일 때, 자동으로 Model에 저장해주는 애노테이션   

### ex>
year, month, day를 Mydate 클래스로 묶어줌.
```java
@GetMapping("/requestParam")
public String main(Mydate date ,Model m){
    String yoil = getYoil(date);
//    m.addAttribute("mydate",date);
    m.addAttribute("yoil",yoil);
    return "yoil";
}

```
@ModelAttribute 생략은 @ModelAttribute("mydate") 와 같음.   
모델에 자동으로 저장되는 속성의 이름은 타입의 맨 앞 글자 소문자로 바꾼 것임.   
   
메서드 타입 앞에 작성해서 메서드의 결과값이 Model에 자동 저장할 수 있다.


### 
기본형, String형의 매개변수는 @RequestParam가 생략되어있고,
참조형 매개변수 앞에는 @ModelAttribute가 생략되어 있다.