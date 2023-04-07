## 정리

- 모든 예외는 적절하게 복구되든지, 작업을 중단시키고 개발자에게 분명하게 통보되어야 한다.
- 의미없는 throws 선언 남발하지 마라.
- 예외는 의도적으로 던지거나, 적절한 예외로 전환해야 한다.
- 좀 더 의미있는 예외로 변경하거나, 불필요한 catch/throws를 피하기 위해 런타임 예외로 포장하는 두 가지의 방법의 예외 전환이 있다.
  - 애플리케이션에서 컨트롤할 수 없는 예외는 가능한 빨리 런타임 예외로 전환해야 한다.
- 애플리케이션의 로직을 처리하기 위한 예외는 체크 예외로 만든다.
- `SQLException`은 대부분 복구할 수 없는 예외이므로 런타임 예외로 포장해야 한다. 그 런타임 예외가 `DataAccessException`이다.
  - DataAccessException을 통해 DB에 독립적으로 적용 가능한 추상화된 런타임 예외 계층을 제공한다.
- DAO를 데이터 액세스 기술에서 독립시키려면 인터페이스 도입, 런타임 예외 전환, 기술에 독립적인 추상화된 예외로 전환이 필요하다.

---

## 🍎 예외처리 방법

### 1. 예외 복구

- 예외 발생하면 다시 정상 상태로 돌리기
- 예외를 복구할 가능성이 있는 경우 `checked exception`을 사용한다.
- ex) 네트워크 접속이 원할하지 않으면 일정 시간 대기했다가 최대 횟수만큼 다시 접속 시도하기

### 2. 예외처리 회피

- throws문으로 예외 던지기
- 예외 처리하는 로직은 Controller에서 한다고 하면 service에서는 예외를 controller에게 던져줘야 한다.

### 3. 예외 전환

- 예외가 발생하면 다른 예외를 발생시키기
  **\*\* 예외 전환하는 이유 두 가지**

1. 의미를 분명하게 해주기 위해
   - ex) SQLException의 정보를 해석해서 DuplicateUserIdException과 같이 구체적인 예외로 바꿔 던져주기.
   - 전환하는 예외는 중첩 예외로 만드는 것이 좋다. 그럼 getCause() 메서드를 통해 처음 발생한 예외를 확인할 수 있다.

```java
public void add(User user) throws DuplicateUserIdException, SQLException {
    try {
        //JDBC를 이용해 user정보를 DB에 추가하는 코드
    } catch(SQLException e) {
        if(e.getErrorcode() == MysqlErrorNumbers.ER_DUP_ENTRY)
            throw DuplicateUserIdException(e);
            // 또는
            // throw DuplicateUserIdException().initCause(e);
        else
            throw e;
    }
}
```

2. `checked Exception`을 `unchecked Exception`으로 바꾸기 위해
   - 일반적으로 `checked Exception`을 계속 throws로 넘기는 건 무의미하다. 복구가 불가한 예외라면 `unckecked Exception`으로 포장해서 불필요한 throws 선언을 없애줘야 한다.
   - ex) EJB 컴포넌트 코드에서 발생하는 체크 예외는 비즈니스 로직으로 볼 때 의미 있는 예외가 아니다. 이런 경우 런타임 예외인 EJBException으로 포장한다.

```java
try {
    OrderHome orderHome = EJBHomeFactory.getInstance().getOrderHome();
    Order order = orderHome.findByPrimaryKey(Integer id);
} catch (NamingException | SQLException | RemoteException e) {
    throw new EJBException(e);
}
```

### 예외 처리 시 `체크 예외`, `언체크 예외` 중 무엇으로 만드는 게 나을까?

- 우선 애플리케이션 차원에서 발생할 예외를 예상하고, 예외가 발생하지 않도록 차단하는 것이 우선이다.
- 대응이 불가한 `체크 예외`라면 `런타임 예외`로 전환해서 던지는 것이 좋다. `언체크 예외`도 필요하다면 catch 블록으로 잡을 수 있고, `체크 예외` 시 불필요한 `예외 던지기`를 없애기 위해서다.
- 또한 어디에서든 예외를 처리할 수 있다면 굳이 `체크 예외`로 만들지 않고, `언체크 예외`로 만드는 것이 낫다. 대신 메서드에 발생하는 예외를 throws 선언을 통해 명시해줘야 한다.
  - 예외를 처리하는 곳에선 `런타임 예외`를 처리할 수도, 안할 수도 있기 때문에 API 문서를 통해 메소드 사용 시 발생할 수 있는 예외의 종류와 원인, 활용 방법을 자세히 설명해두자.

```java
public void add() throws DuplicateKeyException {
    String sql = "insert into users(id, name, password) values (?,?,?)";
    this.jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
}

```

- 반면에 애플리케이션 자체의 로직에 의해 의도적으로 발생시키는 예외는 `체크 예외`여야 한다. 개발자가 잊지 않고 처리 로직을 구현하기 위함이다.

```java
public void add() throws DuplicationUserIdException {
    try {
        String sql = "insert into users(id, name, password) values (?,?,?)";
        this.jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
    } catch (DuplicationKeyException e) {
        throw new DuplicationUserIdException(e); // 의도적으로 로직 구현을 위해 사용자 정의 예외(체크 예외)를 발생시킨다.
    }
}
```

## 🍎 SQLException, DataAccessException

### SQLException

- 대부분의 `SQLException`은 코드 레벨에서 복구할 방법이 없다. SQL 문법이 틀렸거나, 제약조건을 위반했거나, DB 서버가 다운됐거나, DB 커넥션 풀이 꽉 차서 DB 커넥션을 가져올 수 없을 때 발생하는데 이는 개발자에게 예외가 발생했다고 알리는 용도로만 쓰인다. 위에 말했듯이 이런 경우는 빨리 `런타임 예외`로 포장해줘야 한다!

### DataAccessException

- JdbcTemplate에서는 `SQLException`을 `런타임 예외`인 `DataAccessException`으로 포장해서 던져준다. 따라서 JdbcTemplate을 사용하는 메서드에서는 `DataAccessException`을 잡아도 되고, 안잡아도 된다.
- 또한 스프링에선 데이터 액세스 기술마다 다르게 발생하는 예외를 `DataAccessException`로 추상화했다. 즉 `DataAccessException`을 통해 기술에 독립적인 예외를 정의하고 사용할 수 있는 것이다!

```java
// 기술마다 발생하는 예외가 다르다.
public void add(User user) throws PersistentException; //JPA
public void add(User user) throws JdoException; //JDO
public void add(User user) throws HibernateException; //Hiberate
```

```java
public interface UserDao {
    // DataAccessException를 통해 추상화. 런타임 예외이므로 throws 선언하지 않아도 됨.
    public void add(User user);

}
```

- 좀 더 구체적인 여러 DataAccessException의 하위 클래스를 제공한다. ex) `DuplicateKeyException`,`DataAccessResourceFailureException`, `DataIntegrityViolationException`,`ObjectOptimisticLockingFailureException`, `InvalidDataAccessResourceUsageException` 등
