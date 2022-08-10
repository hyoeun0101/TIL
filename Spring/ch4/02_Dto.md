# DTO
: Data Transfer Object   
db 테이블과 같음.

![img](/image/dto.png)    

각 계층을 나눠놨는데 서로 주고 받는 데이터가 DTO

`C` -> `S` -> `R`    
R에서 예외발생하면 S에게 넘기기.   
S에서 처리할 건 하고 예외를 C에게 넘기기.   
C에서 예외처리   

### ${}와 #{} 차이
#{title} 는 PreparedStatement    
${title} 는 Statement   
  
PreparedStatement는 값에만 사용      
   
xml 내 특수 문자 처리   
<![CDATA[                    내용           ]]>   
   
   
이걸로 감싸기   