# 서비스 계층의 분리

![img](/image/service.png)
Controller는 UserService만 주입 받아 사용.   
UserDao는 직접 DB를 CRUD하는 코드. selectUser()   
UserService는 비지니스 로직을 다루고 Tx적용하기 적합. registerUser{ userdao.selectUser()}

## TransactionManage란?
- Tx는 한 개의 Connection에서.
- DAO의 각 메서드는 개별 Connection 사용. 그러면 deleteUser() selectUser() 이걸 하나의 Tx로 묶을 수가 없음.
- 그래서 DAO의 메서드를 하나의 Tx로 묶기 위해 TransactionManager 사용

- 같은 Tx내에서 같은 Connection을 사용할 수 있게 관리.

- DAO에서 Connection을 얻거나 반환할 때 DataSourceUtils를 사용해야함.
```java
conn = DataSourceUtils.getConnection(ds);
DataSourceUtils.releaseConnection(conn, ds);
```

```java
PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());// tx시작

try{
    a1Dao.insert(1,100);
    a1Dao.insert(2,200);
    tm.commit(status);// tx 끝- 성공(커밋)
}catch(Exception ex){
    tm.rollback(status); //tx 끝 - 실패(롤백)
}

```
dao의 메서드는 원래 개별 connection인데  TxManager가 같은 Connection쓰게 해줌.

### TxManager 생성
- 직접 생성
```java
PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());// tx시작
```
- 빈 등록
```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<tx:annotation-driven/> 
<!-- 이건 @Transactional 사용 가능 -->
```

### @Transactional
: AOP를 이용한 핵심 기능과 부가 기능을 분리   
클래스나 인터페이스에도 붙일 수 있음.   
붙어있으면 클래스 내의 모든 메서드에 적용.   


# tx 실습하기
둘 다 성공, 하나만 성공하면 실패하도록 하기


결론 : Dao의 메서드들 (select, update, delete, insert)은 각각 서로 다른 Connection이다.   
하나의 Tx로 묶기 위해서 TransactionManager를 사용한다.
사용하는 방법은 선언 또는 빈 등록해서 주입받기.    


### @Transactional의 속성
|속성|설명|
|---|----|
|propagation| Tx의 경계를 설정하는 방법을 지정|
|isolation|Tx이 isolation level을 지정|
|readOnly|Tx이 데이터를 읽기만 하는 경우, true로 지정하면 성능 향상|
|rollbackFor|지정된 예외가 발생하면, Tx을 rollback. RuntimeException과 Error는 자동 롤백.|
|noRollbackFor|지정된 예외가 발생해도 ,롤백X|
|timeout|지정된 시간 내에 Tx 종료하지 않으면 강제 종료|

__propagation속성의 값__
![이미지](/image/propagation.png)   
ch3-20강 23분 보기   