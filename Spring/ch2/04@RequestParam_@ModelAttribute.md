# @RequestParam
: 요청의 파라미터와 연결할 매개변수 앞에 붙이는 애노테이션     
      
__<속성>__      
- name : 파라미터 이름
- required : 파라미터 값이 필수이면 true, 아니면 false
- defaultValue : 파라미터 기본값 설정

1. @RequestParam 생략하고, 매개변수 타입이 String일 경우

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
/rp 로 요청시 year=null    
/rp?year 로 요청시 year=""    

2. @RequestParam 작성하고 ,매개변수 타입이 String 일 경우
:required=true 즉 파라미터 값 반드시 있어야함.   
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
/rp 요청시 반드시 줘야할 데이터를 클라이언트에서 안줬기 때문에 400err   
/rp?year 요청시 year은 빈문자열   
   
3. @RequestParam(required=false)이고 타입은 int   
```java
@GetMapping("/rp")
public String main(@RequestParam(required=false) int year){

    return "yoil";
}
```
/rp 요청 시 year은 null인데 int로 처리할 수 없으니 서버 에러 500   
/rp?year 요청 시 year은 빈문자열이고, 값을 잘 못 줬으니 클라이언트 에러 400    
   
required=false 일 땐 defaultValue="1"를 준다.   
아무 값도 안줘도 디폴트값으로 설정.   

4. @RequestParam(required=true)이고 타입은 int

```java
@GetMapping("/rp")
public String main(@RequestParam(required=true) int year){

    return "yoil";
}
```
/rp 요청 시 year은 필수값이라 클라이언트 에러   
/rp?year 요청 시 year은 빈문자열이고, 값 잘못줬으니 클라이언트 에러    
     
required=true일때는 사용자가 잘못된 값을 전달할 경우를 대비하여 예외처리해주기.   
```java
@Controller
public class YoilTellerMVC{
    @ExceptionHandler(Exception.class)
    public String catcher(Exception ex){
        ex.printStackTrace();
        return "yoilError";
    }

    @RequestMapping("/getYoil")
    public String main(@RequestParam(required=true) int year,
    @RequestParam(required=true) int month,
    @RequestParam(required=true) int day, Model m){
        char yoil=getYoil(year,month,day)
        m.addAttribute("year",year);
        m.addAttribute("month",month);
        m.addAttribute("day",day);
        m.addAttribute("yoil",yoil);

        return "yoil";
    }
}
```
/getYoil 로 요청시 파라미터 값을 안줬기 때문에 예외 발생하고, 발생한 예외를 catcher에서 처리.   

# 여러 데이터를 입력받을 때는 받을 데이터를 하나의 클래스(타입)으로 합치기
```java
@Controller
public class YoilTellerMVC{
    @ExceptionHandler(Exception.class)
    public String catcher(Exception ex){
        ex.printStackTrace();
        return "yoilError";
    }

    @RequestMapping("/getYoil")
    public String main(MyDate date, Model m){
        char yoil=getYoil(date)
        m.addAttribute("myDate",date);
        m.addAttribute("yiol",yoil);


        return "yoil";
    }
}
```
[SetterCall]    
파라미터로 year, month, day 값이 들어왔을 때 이를 어떻게 MyDate와 매칭시키는지 구현한 코드   
```java
package com.fastcampus.ch2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public class SetterCall {
	public static void main(String[] args) throws Exception{
		Map<String, String> map = new HashMap<>();
		map.put("year", "2021");
		map.put("month", "10");
		map.put("day", "1");
		
		Class<?> type = Class.forName("com.fastcampus.ch2.MyDate");

		// MyDate인스턴스를 생성하고, map의 값으로 초기화한다. 
		Object obj = dataBind(map, type);
		System.out.println("obj="+obj); // obj=[year=2021, month=10, day=1]
	} // main

	private static Object dataBind(Map<String, String> map, Class<?> clazz) throws Exception {
		// 1. MyDate인스턴스 생성
//		Object obj = clazz.newInstance(); // deprecated method
		Object obj = clazz.getDeclaredConstructor().newInstance(new Object[0]);

		// 2. MyDate인스턴스의 setter를 호출해서, map의 값으로 MyDate를 초기화
		// 	 2-1. MyDate의 모든 iv를 돌면서 map에 있는지 찾는다.
		// 	 2-2. 찾으면, 찾은 값을 setter로 객체에 저장한다.
		Field[] ivArr = clazz.getDeclaredFields();
		
		for(int i=0;i<ivArr.length;i++) {
			String name = ivArr[i].getName();
			Class<?>  type = ivArr[i].getType();
			
			// map에 같은 이름의 key가 있으면 가져와서 setter호출 
			Object value = map.get(name); // 못찾으면 value의 값은 null
			Method method = null;
			
			try {   // map에 iv와 일치하는 키가 있을 때만, setter를 호출
				if(value==null) continue;
				
				method = clazz.getDeclaredMethod(getSetterName(name), type); // setter의 정보 얻기	
				System.out.println("method="+method);
				method.invoke(obj, convertTo(value, type)); // obj의 setter를 호출
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(Arrays.toString(ivArr));
		
		return obj;
	}

	private static Object convertTo(Object value, Class<?> type) {
		// value의 타입과 type의 타입이 같으면 그대로 반환
		if(value==null || type==null || type.isInstance(value))
			return value;
		
		// value의 타입과 type이 다르면, 변환해서 반환
		if(String.class.isInstance(value) && type==int.class) // String -> int
			return Integer.valueOf(""+value);

		return value;
	}

	// iv의 이름으로 setter의 이름을 만들어서 반환하는 메서드("day" -> "setDay")
	private static String getSetterName(String name) {
//		return "set"+name.substring(0,1).toUpperCase()+name.substring(1);
		return "set" + StringUtils.capitalize(name); // org.springframework.util.StringUtils
	}
}

```

# @ModelAttribute
: 자동으로 Model에 저장해주는 애너테이션. 매개변수, 반환 타입에 적용 가능   
```java
@Controller
public class YoilTellerMVC{
    @ExceptionHandler(Exception.class)
    public String catcher(Exception ex){
        ex.printStackTrace();
        return "yoilError";
    }

    @RequestMapping("/getYoil")
    //                  @ModelAttribute("myDate")
    public String main(@ModelAttribute MyDate date, Model m){
        char yoil=getYoil(date)
        
        //m.addAttribute("myDate",date);
        //m.addAttribute("yiol",yoil);
        return "yoil";
    }

    private @ModelAttribute("yoil") char getYoil(MyDate date){
        ...
    }
}
```
참조형 매개 변수 앞에 @ModelAttribute 생략 가능.   
    
#### 컨트롤러 매개변수에 붙일 수 있는 애노테이션   
- @RequestParam : 타입이 기본형, String일때 생략돼있음.     
                 기본형이나 String 일 경우 Model에 저장할 필요 없음. 뷰에서 `${param.파라미터 이름}`으로 바로 참조 가능    
- @ModelAttribute : 타입이 참조형일때 생략돼있음.   

# WebDataBinder

1. 타입 변환
String 을 int 로 변환 후 BindingResult에 결과를 저장.   
   
2. 데이터 검증   
month는 1~12, day는 1~31 값을 검증 후 BindingResult에 결과 저장.      
  
BindingResult를 Controller에 넘겨줌.   
    
```java
@RequestMapping("/getYoil")
public String main(@ModelAttribute MyDate date, BindingResult result){}
```
BindingResult는 바인딩할 객체 바로 뒤에 와야함.  

