# JSON
: Java Script Object Notation - 자바 스크립트 객체 표기법   
{key : value, key : value,...}    

### stringify()와 parse()
JS객체를 서버로 전송하려면 text(문자열)로 변경.직렬화(serialize)가 필요.       
     
HTTP는 Text 기반. 주고 받을 때 text여야함. JSON이 text임.     

객체 -> JSON 직렬화이고, JSON.stringify()      
JSON -> 객체 역직렬화이고, JSON.parse()     
    
{name: "John", age:30}  -> `JSON.stringify` -> '{"name": "John", "age" : 30}'   

# AJAX
: Asynchronous javascript and XML - 요즘은 JSON 사용   
비동기 통신으로 데이터를 주고 받는 기술   
웹페이지 전체가 아닌 일부만 업데이트 가능   

비동기는 요청을 보내면 응답을 기다리지 않고, 다음 코드를 실행하는 것. 다만 처리가 언제 끝나는지 알지 못한다. 그래서 언제 끝나는지 알려주는 것이 콜백 함수.     

  
```javascript
$(document).ready(function(){
    let person = {name:"abc", age:10};
    let person2 = {};

    $("#sendBtn").click(function(){
      $.ajax({
        type:'POST',       // 요청 메서드
        url: '/ch4/send',  // 요청 URI
        headers : { "content-type": "application/json"}, // 요청 헤더
        dataType : 'text', // 전송받을 데이터의 타입
        data : JSON.stringify(person),  // 서버로 전송할 데이터. stringify()로 직렬화 필요.
        success : function(result){
          person2 = JSON.parse(result);    // 서버로부터 응답이 도착하면 호출될 함수
          alert("received="+result);       // result는 서버가 전송한 데이터
          $("#data").html("name="+person2.name+", age="+person2.age);
        },
        error   : function(){ alert("error") } // 에러가 발생했을 때, 호출될 함수
      }); // $.ajax()

      alert("the request is sent")
    });
  });
```

`maven-repo`- Jackson Databind     

## 정리
![img](/image/ajax.png)    
1. 브라우저에서 JS객체인 데이터를 JSON.stringify()해서 요청을 보냄.   
2. 문자열인 데이터를 jackson-databind가 java객체로 만들어줌.   
3. 코드 실행 후 jackson-databind가 다시 문자열로 변환해주고, 응답.   
4. 문자열을 JSON.parse()로 JS객체로 변환   

### @ResponseBody, @RequestBody   
__@RequestBody Person p__      
위에서 요청이 POST로 들어오면, 요청 헤더 Body에 있는 데이터를 Person p 자바 객체로 만들어줌.    
   
__@ResponseBody__   
위에서 응답할 때, 객체 반환하고, 데이터를 응답 헤더 Body에 붙여서 응답함.   
   
@ResponseBody 대신 클래스에 @RestController 사용 가능.   

# Rest란?
: 웹서비스 디자인 아키텍쳐 접근 방식.   
프로토콜에 독립적이며, 주로 HTTP를 사용해서 구현.   
`리소스(/url 이런거) 중심의 API 디자인- HTTP메서드로 수행할 작업을 정의`   

POST, GET, PUT, DELETE 를 나눠서 리소스(/user)는 심플하게 가자!   
리소스는 명사로 수행할 작업(동사)는 메서드를 사용하자!   

### Rest API
: REST 규약을 지킨 API   

![img](/image//restful_api.png)     
URL은 명사로 심플하게, 동사는 HTTP메서드로 표현.   