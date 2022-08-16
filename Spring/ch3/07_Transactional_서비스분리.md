# 서비스 계층의 분리

![img](/image/service.png)
Controller는 UserService만 주입 받아 사용.   
UserDao는 직접 DB를 CRUD하는 코드. selectUser()   
UserService는 비지니스 로직을 다루고 Tx적용하기 적합. registerUser{ userdao.selectUser()}

## 1. TransactionManage란?
- Tx는 한 개의 Connection에서 실행.
- DAO의 각 메서드는 개별 Connection 사용. 그러면 deleteUser() selectUser() 이걸 하나의 Tx로 묶을 수가 없음.
- 그래서 DAO의 메서드를 하나의 Tx로 묶기 위해 TransactionManager 사용

- 같은 Tx내에서 같은 Connection을 사용할 수 있게 관리.

- DAO에서 Connection을 얻거나 반환할 때 DataSourceUtils를 사용해야함.
```java
conn = ds.getConnection();
//...
try{ if(conn!=null) conn.close(); }
catch{SQLException e {e.printStackTrace();}}
```
기존 코드를 아래로 변경
```
conn = DataSourceUtils.getConnection(ds);
DataSourceUtils.releaseConnection(conn, ds);
```

```java
PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());//Tx의 부가기능
// tx시작
try{
    a1Dao.insert(1,100);//핵심기능 두줄!
    a1Dao.insert(2,200);
    tm.commit(status);// tx 끝- 성공(커밋)
}catch(Exception ex){
    tm.rollback(status); //tx 끝 - 실패(롤백)
}

```
dao의 메서드는 원래 개별 connection인데  TxManager가 같은 Connection쓰게 해줌.

## 2.TxManager 생성 방법
1. 직접 생성
```java
PlatformTransactionManager tm = new DataSourceTransactionManager(ds);// TxManager 생성
TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());// Tx의 속성을 정의
```
2. 빈 등록
```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<tx:annotation-driven/> 
<!-- 이건 @Transactional 사용 가능 -->
```

## 3.@Transactional
: AOP를 이용해서 핵심 기능과 부가 기능을 분리하기   

```java
@Transactional
public void insertWithTx() throws Exception{
    a1Dao.insert(1,100);
    a1Dao.insert(2,200);
}
```
- 클래스(또는 인터페이스)에 붙이면 클래스(인터페이스) 내의 모든 메서드에 적용

## 4. Transaction 실습
[A1Dao.java]
```java
public int insert(int key, int value) throws Exception{
    Connection conn = null;
    PreparedStatement pstmt = null;

    try{
        //conn = ds.getConnection();
        conn = DataSourceUtils.getConnection(ds);
        System.out.println("conn="+conn);
        pstmt = conn.prepareStatement("insert into a1 values(?,?)");
        pstmt.setInt(1,key);
        pstmt.setInt(2,value);

        return pstmt.executeUpdate();
    }catch(SQLException e){
        e.printStackTrace();
    }finally{
        //close(pstmt, conn);
        close(pstmt);
        DataSourceUtils.releaseConnection(conn, ds);
    }
    return 0;
}
```

[A1DaoTest.java]
```java
@Test
public void isnertTest() throws Exception{
    PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
    TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());
    //tx 시작
    try{
        a1Dao.deleteAll();
        a1Dao.insert(1,100);//성공
        a1Dao.insert(1,100);//실패
        tm.commit(status);
    }catch(Exception e){
        e.printStackTrace();
        tm.rollback(status);
    }finally{
    }
}
```
- 키 충돌이라 실패. 둘 다 들어가면 안됨. 
- connection 출력 보면 같은 connection인걸 알 수 있음.


## 4. @Transactional 사용
위에서 핵심 기능 빼고 before, after 코드는 AOP로 처리함. @Transactional 쓰면 됨.   
```java
@Transactional(rollbackFor=Exception.class)
public void insertWithTx throws Exception{
    a1Dao.insert(1,100);
    a1Dao.insert(2,200);
}
```
@Transactional은 RuntimeException, Error만 롤백을 함.   
그래서 rollbackFor 써줘야 롤백함.    

- @Transactional의 속성
|속성|설명|
|---|----|
|propagation| Tx의 경계를 설정하는 방법을 지정|
|isolation|Tx이 isolation level을 지정|
|readOnly|Tx이 데이터를 읽기만 하는 경우, true로 지정하면 성능 향상|
|rollbackFor|지정된 예외가 발생하면, Tx을 rollback. RuntimeException과 Error는 자동 롤백.|
|noRollbackFor|지정된 예외가 발생해도 ,롤백X|
|timeout|지정된 시간 내에 Tx 종료하지 않으면 강제 종료|

## 5. propagation속성의 값
|값|설명|
|---|----|
|REQUIRED| Tx이 진행 중이면 참여하고, 없으면 새로운 Tx시작(디폴트)|
|REQUIRES_NEW|Tx이 진행 중이건 아니건 새로운 Tx 시작|
|NESTED|Tx이 진행 중이면 Tx의 내부 Tx로 실행|
|MANDATORY|반드시 진행 중인 Tx내에서만 실행 가능. 아니면 예외발생|
|SUPPORTS|Tx이 진행 중이건 아니건 상관없이 실행|
|NOT_SUPPORTED|Tx없이 처리. Tx이 진행 중이면 잠시 중단|
|NEVER|Tx없이 처리. Tx이 진행 중이면 예외 발생|

- REQUIRED(디폴트)
```java
@Transactional(propagation = Propagation.REQUIRED)
public void insertA1WithTx() throws Exception{
    a1Dao.insert(1,100);
    insertB1WithTx();
    a1Dao.inesrt(1,200);
}

@Transactional(propagation = Propagation.REQUIRED){
    b1Dao.insert(1,100);
    b1Dao.insert(1,200);
}
```
![img](/image/tx_propagation_required.png)   
실행 중인 Tx가 있으면 참여하고, 에러가 났을 경우 모두 롤백. 하나의 Connection 사용.   

- REQUIRES_NEW
```java
@Transactional(propagation = Propagation.REQUIRED)
public void insertA1WithTx() throws Exception{
    a1Dao.insert(1,100);
    insertB1WithTx();
    a1Dao.inesrt(1,200);
}

@Transactional(propagation = Propagation.REQUIRES_NEW){
    b1Dao.insert(1,100);
    b1Dao.insert(1,200);
}
```
![img](/image/tx_propagation_requires_new.png)   
새로운 Tx 시작. A2에서 에러가 났으면 A1으로 롤백. B1, B2는 성공.    
첫번째 네 번째 Connection이 같고, 두번째 세번째 Connection이 같음.  
같은 메서드끼리의 내부 호출을 하게 되면 @Transactional 동작하지 않음.   
프록시 방식의 AOP는 내부 호출인 경우, Advice가 적용되지 않음. 그래서 Tx가 적용되지 않음.  
그래서 그냥 TxManager 생성해서 작성함.     


