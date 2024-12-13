## 🔴 Transaction ACID
### 1. Atomicity(원자성)
- 나눌 수 없는 하나의 작업으로 이루어진다.
- 모두 성공해야 커밋, 하나라도 실패하면 롤백
- ex) 계좌이체는 출금 + 입급이 하나의 작업

### 2. Consistency(일관성)
- 트랜잭션 수행 전과 수행 후가 일관된 상태를 유지해야 한다.

- ex) A가 B에게 100원을 주면 A의 잔고는 -100, B의 잔고는 +100이 되어야 한다.

### 3. Isolation(고립성)
- 각 트랜잭션은 독립적으로 수행되어야 하낟.

- 트랜잭션1의 작업이 트랜잭션2의 작업에 영향을 미치면 안된다.

- Isolation level을 적절하게 선택해서 사용함.

### 4. Durability(영속성)
- 성공한 트랜잭션의 결과는 유지되어야 한다.

- 계좌 이체가 성공하면 그 결과는 계속 유지된다.

## 🔴 Commit & Rollback
### 커밋
- 작업 내용을 DB에 영구적으로 저장

### 롤백
- 최근 변경사항을 취소. 마지막 커밋으로 복귀

### Auto Commit
- 자동 커밋: 명령 실행 후, 자동으로 커밋. 롤백 불가  

- 수동 커밋: 명령 실행 후, 수동으로 커밋, 롤백.

## 🔴 Isolation level

### 1. Read Uncommited
- 커밋되지 않은 데이터도 읽기 가능

### 2. Read Commited
- 커밋된 데이터만 읽기 가능

- Tx1 실행 중 Tx2에서 insert를 하고 커밋을 하면 Tx1에서 읽기 가능

### 3. Repeatable Read (default)
- 트랜잭션1이 시작되면 다른 트랜잭션의 영향을 받지 않음.

- Tx1에서 작업 중 Tx2가 insert해도 Tx1에선 영향받지 않음.즉 같은 결과가 나오며 이를 반복해서 읽기 가능이라고 함.

### 4. Serializable
- 트랜잭션 직렬로 처리. 한번에 하나의 트랜잭션만 수행

- Tx1에서 select 작업 중에 Tx2에서 select는 가능하지만 insert는 Tx1 작업 끝나고 실행. 즉 무한대기

- Tx1에서 insert 작업 중이면 Tx에서 select도 할 수 없음.

## 🔴 Spring에서 Transaction 사용하기기

- Transaction 사용법을을 이해하기 위해 다음의 코드를 살펴보자.

### 기존 코드
```java
@Repository
public class UserDaoImpl implments UserDao {
    @Autowired
    private Datasource datasource;

    public int insertUser(User user) {
        Connection connection = null;
        PreparedStatement pstate = null;
        Stirng sql = "inert into user_info values (?,?,?,?)";

        try {
            connection = datasource.getConnection(); // get connection...
            pstate = connection.prepareStatement(sql);
            pstate.setString(1, id); 
            // ...생략

            return pstate.executeUpdate();
        } 
        //... 생략
    }
    

    public int deleteUser(String id) {
        Connection connection = null;
        PreparedStatement pstate = null;
        Stirng sql = "delete from user_info where id = ?";

        try {
            connection = datasource.getConnection(); // get connection...
            pstate = connection.prepareStatement(sql);
            pstate.setString(1, id);

            return pstate.executeUpdate();
        } 
        //... 생략
    }
}
```
- 메서드마다 connection을 얻어서 사용하고 있다. 즉 메서드마다 connection이 다르다.

- 트랜잭션은 하나의 Connection에 대해 동작하기 때문에 위에 처럼 작성하면 메서드를 하나의 트랜잭션으로 묶을 수 없다. 

### 변경

```java
@Repository
public class UserDaoImpl implments UserDao {
    @Autowired
    private Datasource datasource;

    public int deleteUser(String id) {
        Connection connection = null;
        PreparedStatement pstate = null;
        Stirng sql = "delete from user_info where id = ?";

        try {
            // 하나의 커넥션으로 작업하도록 함.
            connection = DatasourceUtils.getConnection(datasource);
            pstate = connection.prepareStatement(sql);
            pstate.setString(1, id);

            return pstate.executeUpdate();
        } 
        //... 생략
    }
}
```
- DatasourceUtils를 이용해서 connection을 얻었다. 내부적으로 connection 관리를 하여 현재 사용 중인 connection을 반환하는 듯? (나의 뇌피셜..)

### TransactionManager

```java
@Service
public class UserServiceImpl implments UserService {
    @Autowired
    private Datasource datasource;

    @Autowired
    private UserDao userDao;

    public int insertUser(User user) {
        PlatformTransactionManager txManager = new DataSourceTransactionManager(datasource);
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userDao.insertUser(user);
            userDao.inserUserHist(user);

            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status)
        }
    }
}
```
- TransactionManager가 자동으로 connection 관리를 해준다. 메서드를 하나의 커넥션으로 묶어 트랜잭션 처리가 가능하도록 해준다.

- TransactionManager는 빈으로 등록한 후 사용한다.
    - jdbc 라이브러리가 기본으로 `JdbcTransactionManager`를 빈으로 등록해준다.

### @Transactional
- 메서드, 클래스 위에 붙이면 AOP로 트랜잭션 관련 코드 넣어줘서 트랜잭션 처리를 할 수 있다.

- 메서드 위에 붙이면 메서드 내의 작업에 대해 트랜잭션 처리를 한다.

- 클래스(또는 인터페이스)에 붙이면 클래스(또는 인터페이스) 내의 모든 메서드에 적용된다.

- 단순 @Transactional은 RuntimeException, Error 만 롤백을 한다.

    - Exception도 롤백하려면 rollbackFor 속성을 명시해야 한다!


```java
@Transactional(rollbackFor = "Exception.class")
```


|@Transactional 속성|설명|
|-------------------|------|
|isolation| isolation level을 지정|
|propagation      | 트랜잭션의 경계를 설정하는 방법을 지정|
|readOnly| 트랜잭션에서 read만 할 경우, true로 지정하면 성능 향상|
|rollbackFor|지정된 예외가 발생하면 rollback.|
|noRollbackFor|지정된 예외가 발생해도 롤백하지 않음.|
|timeout|지정된 시간 내에 트랜잭션이 종료하지 않으면 트랜잭션 강제 종료|

## 🔴 Propagation
### Requried(default) ⭐
- 진행 중인 트랜잭션이 있으면 그 트랜잭션에 참여

- 없으면 새로운 트랜잭션 실행


```java
@Service
public TxService {
    @Autowired
    private A1Dao a1Dao;

    @Autowired
    private TxService2 txServcie2;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertA1WithTransaction() throws Exception {
        a1Dao.insert(1, 100); //1. 성공

        txService2.insertB1WithTransaction(); // Exception 발생!

        a1Dao.insert(4, 100);
    }
}
```
```java
@Service
public TxService2 {
    
    @Autowired
    private B1Dao b1Dao;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertB1WithTransaction() throws Exception {
        b1Dao.insert(2, 100); //2. 성공
        a1Dao.insert(2, 100); // 3. 실패
    }
}
```

- 설명
    - 1번 작업 성공
    - insertB1WithTransaction에서 현재 실행 중인 트랜잭션에 참여.
    - 2번 작업 성공
    - 3번 작업 실패
    - 1, 2번 작업 모두 롤백!
    - 1,2,3,4번의 커넥션이 모두 같다.


### Requires_new ⭐
- 무조건 새로운 트랜잭션 실행


```java
public TxService {
    @Autowired
    private A1Dao a1Dao;

    @Autowired
    private B1Dao b1Dao;
    
    @Transactional(propagation = Propagation.REQUIRED_NEW, rollbackFor = Exception.class)
    public void insertA1WithTransaction() throws Exception {
        a1Dao.insert(1, 100); //1. 성공

        insertB1WithTransaction(); // Exception 발생!

        a1Dao.insert(4, 100); //4. 성공
    }


    @Transactional(propagation = Propagation.REQUIRED_NEW, rollbackFor = Exception.class)
    public void insertB1WithTransaction() throws Exception {
        b1Dao.insert(2, 100); //2. 성공
        a1Dao.insert(2, 100); // 3. 실패
    }
}

```
- 설명
    - 1번 작업 성공
    - insertB1WithTransaction에서 새로운 트랜잭션으로 실행
    - 2번 작업 성공
    - 3번 작업 실패
    - 2번 작업만 롤백
    - 4번 작업 성공 후 1번, 4번 커밋
    - 1번, 4번의 connection이 같고, 2번,3번의 connection이 같다.

### Nested
- 진행 중인 트랜잭션이 있으면 그 트랜잭션의 내부적으로 새로운 트랜잭션(sub transaction)으로 실행.

- 하나의 트랜잭션의 작업이 클 때 sub transaction을 만들어서 작업을 쪼갤 수 있다.
    - sub transaction들 사이에 savepoint를 찍으면 그 지점으로 롤백할 수 있다.


### Mandatory
- 반드시 진행 중인 트랜잭션에서만 실행 가능. 아니면 예외 발생

### Supports
- Tx이 진행 중이건 아니건 상관없이 실행

### Not_supported
- Tx없이 처리. Tx이 진행 중이면 잠시 중단

### Never
- Tx없이 처리. Tx이 진행 중이면 예외 발생

### 주의할 점
- @Transactional은 프록시 방식의 AOP를 사용한다.
- 같은 클래스에 속한 메서드끼리 내부 호출인 경우, Advice가 적용하지 않는다.

```java
public TxService {
    @Autowired
    private A1Dao a1Dao;

    @Autowired
    private B1Dao b1Dao;
    
    @Transactional(propagation = Propagation.REQUIRED_NEW, rollbackFor = Exception.class)
    public void insertA1WithTransaction() throws Exception {
        a1Dao.insert(1, 100); //1. 성공

        // ⭐ 여기서 insertB1WithTransaction 메서드에선 adviㄴc(트랜잭션 코드)가 적용되지 않는다. 따라서 동일한 트랜잭션에서 수행
        insertB1WithTransaction(); // Exception 발생!

        a1Dao.insert(4, 100); //4. 성공
    }


    @Transactional(propagation = Propagation.REQUIRED_NEW, rollbackFor = Exception.class)
    public void insertB1WithTransaction() throws Exception {
        b1Dao.insert(2, 100); //2. 성공
        a1Dao.insert(2, 100); // 3. 실패
    }
}


```