# 1.  Dao
1. DB테이블 생성
2. Mapper XML & DTO 작성
3. DAO 인터페이스 작성
4. DAO 인터페이스 구현& 테스트

# 2. DTO
: Data Transfer Object 
- 계층 간의 데이터를 주고받기 위해 사용되는 객체

![img](/image/dto.png)    

각 계층을 나눠놨는데 서로 주고 받는 데이터가 DTO

`C` -> `S` -> `R`    
R에서 예외발생하면 S에게 넘기기.   
S에서 처리할 건 하고 예외를 C에게 넘기기.   
C에서 예외처리  

+ BoardDao 인터페이스로 추출하는 이유?
-> 나중에 Dao가 바뀔 때, BoardOracleDao 이렇게 바꿀 수도 있기 때문. DB 종류에 따라.   
