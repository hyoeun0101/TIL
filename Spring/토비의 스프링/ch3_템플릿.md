### 정리

- `pool 방식` : 공유 리소스 반환이 필요한 코드는 반드시` try/catch/finally 블록`으로 관리해야한다.
- `전략 패턴` : 변하지 않는 부분은 `컨텍스트`(Context)로, 변하는 부분은 `전략`(Strategy)으로 한다. `클라이언트`에서 `전략`을 선택 및 생성 후 `컨텍스트`에게 넘겨준다. 이 때 `전략`은 인터페이스를 통해 유연하게 변경할 수 있어야 한다. 이는 변하지 않는 패턴을 가지고, 그 중 일부분만 바꿔서 사용해야하는 경우에 적합한 구조이다.

- 같은 애플리케이션에서 여러 `전략`을 사용한다면, `컨텍스트`를 사용하는 `클라이언트` 메서드에서 직접 `전략`을 관리한다.
- `클라이언트`에서 `전략`을 생성할 때 익명 내부 클래스로 작성할 수 있다. `전략`의 인터페이스는 functional interface여야 한다.
- `컨텍스트`가 하나 이상의 `클라이언트` 인스턴스에서 사용되면 `컨텍스트`를 클래스로 분리하자.
- 이 때 `컨택스트`와 `클라이언트`가 높은 결합도를 가지는데, `컨텍스트`를 `클라이언트`에게 DI 하는 방법은 두 가지가 있다. 자동 DI, 수동 DI(클라이언트에서 직접 생성)

- 템플릿/콜백 패턴 : 단일 전략 메소드를 갖는 전략 패턴이면서, 익명 내부 클래스를 사용해서 매번 전략을 새로 생성하여 컨텍스트를 호출하는 방식
- `콜백`의 코드에서 일정한 패턴이 반복되면 `콜백`을 `템플릿`에 넣고 재활용하자.
- `템플릿`과 `콜백`에 다양한 타입이 들어갈 수 있도록 제네릭을 활용하자.
- 템플릿/콜백을 설계할 때는 `템플릿`과 `콜백`이 서로 주고 받는 정보에 관심을 둬야한다. `템플릿`이 `콜백`에게 어떤 값을 넘겨 줄 것인지, `콜백`은 어떤 값을 리턴해 줄 것인지.

---

## 🍎 템플릿

- `템플릿`이란 변하는 것과 변하지 않는 것을 분리하여 변하지 않는 것은 템플릿으로 관리하는 것을 말한다.

다음 예제를 리펙토링해가며 스프링에 적용된 템플릿 기법을 알아보자.

```java
@Component
public class UserDao {
    // DataSource 역시 빈임! 자동 주입하는 것!
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void deleteAll() throws Exception{
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            // 변하는 것
            ps = c.prepareStatement("delete from users");
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}

        }
    }
}
```

- Connection과 PrepareStatement는 사용 후 반드시 리소스를 반환해야한다.

  - `pool 방식` : 서버는 pool 안에 제한된 자원을 미리 생성하고, 재사용하여 사용한다. 요청이 매우 많은 환경에서 매번 자원을 생성하는 것이 아니라, pool의 자원을 사용하고, 자원을 반환하여 재사용하는 방식으로 운영된다. 대신 사용한 자원은 빠르게 반환하는 것이 중요하다.

- 자원을 반환하는지 어떻게 테스트할까? 일일히 자원을 사용하는 코드를 작성할 수도 없고.

### 리펙토링1 - 변하는 것 vs 변하지 않는 것 분리

- 위의 예제에서 변하는 부분은 메소드 추출을 통해 분리했다.

```java
@Component
public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void deleteAll() throws Exception{
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = makeStatement(c);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}

        }
    }

    private PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return ps;
    }
}
```

- 하지만 추출한 메서드는 계속 변하는 부분이기 때문에 재사용할 수 없다. 이는 올바른 방법이 아니다.

### 리펙토링2 - 탬플릿 메서드 패턴 적용

- 변하지 않는 부분을 상위 클래스로, 변하는 부분은 추상 메서드로!

```java
public abstract class UserDao2 {
    private final DataSource dataSource;

    public UserDao2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteAll() throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = makeStatement(c);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}
        }
    }

    abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;

}
```

```java
@Component
public class UserDaoDeleteAll extends UserDao2{

    public UserDaoDeleteAll(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }

}
```

- 이는 생성, 수정, 삭제 Dao 로직마다 계속 하위 클래스를 두어야한다. UserDao의 메소드가 4개면 4개의 하위 클래스를 만들어야하는 셈이다. 단점이 더 많다.

### 리펙토링3 - 전략 패턴 적용

- 클래스를 아예 둘로 분리하고, 이 둘은 인터페이스를 통해서만 의존하도록 한다.
- 변하지 않는 것은 컨텍스트(Context)로, 변하는 것은 전략(Strategy)으로 분리한다.
- 전략은 인터페이스를 통해 호출된다.
- 클라이언트에서 전략을 선택하고, 생성한 후 컨텍스트에게 전략을 제공한다.

```java
@Component
public class UserDao3 {
    private final DataSource dataSource;

    public UserDao3(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    // 클라이언트
    // 클라이언트에서 전략을 선택 및 생성 후 컨텍스트에게 전달한다.
    public void deleteAll() throws SQLException{
        StatementStrategy strategy = new DeleteAllStatement();
        jdbcMakeStatement(strategy);
    }
    // 컨텍스트 : 변하지 않는 부분.
    private void jdbcMakeStatement(StatementStrategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try{
            c = dataSource.getConnection();
            ps = strategy.makeStatement(c);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}
        }
    }
}

```

```java
public interface StatementStrategy {
    PreparedStatement makeStatement(Connection c) throws SQLException;
}
```

```java
public class DeleteAllStatement implements StatementStrategy{

    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return ps;
    }
}

```

### 리펙토링4 - 익명 내부 클래스로 선언

```java
@Component
public class UserDao4 {
    private final DataSource dataSource;

    public UserDao4(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void deleteAll() throws SQLException{
        jdbcMakeStatement(
            new StatementStrategy() {
                @Override
                public PreparedStatement makeStatement(Connection c) throws SQLException {
                    return c.prepareStatement("delete from users");
                }

            }
        );
    }

    private void jdbcMakeStatement(StatementStrategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try{
            c = dataSource.getConnection();
            ps = strategy.makeStatement(c);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}
        }
    }
}
```

## 🍎 컨텍스트와 DI

- 위의 예제에서 컨텍스트인 jdbcMakeStatement() 메서드 부분을 JdbcContext 클래스로 분리해주자.

### JdbcContext와 UserDao의 관계

- JdbcContext 클래스는 내용이 변하지 않기 때문에 인터페이스가 필요없다.
- 따라서 UserDao에서 DI하여 사용할 때, 인터페이스를 거치지 않고 구체 클래스와 `직접 의존`하게 된다. 이는 두 오브젝트가 `긴밀한 관계`를 가지고 있으며 `높은 결합도`를 갖고있음을 의미한다.

### 1. 인터페이스를 거치지 않고 직접 DI하기- 자동 DI

`UserDao` <- `JdbcContext` <- `DataSource` (`<-` 화살표는 의존하고 있음을 의미한다.)

```java
@Component
public class UserDao5 {
    // 인터페이스를 거치지 않고, 구체 클래스와 의존하고 있다.
    private final JdbcContext jdbcContext;
    public UserDao5(JdbcContext jdbcContext ) {
        this.jdbcContext = jdbcContext;
    }
    public void deleteAll() throws Exception {
        jdbcContext.workWithStatementStrategy(
            new StatementStrategy() {
                @Override
                public PreparedStatement makeStatement(Connection c) throws SQLException {
                    return c.prepareStatement("delete from users");
                }
            }
        );
    }
}
```

```java
@Component
public class JdbcContext {
    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws Exception{
        Connection c = null;
        PreparedStatement ps = null;
                try {
            c = dataSource.getConnection();

            ps = stmt.makeStatement(c);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}
        }
    }
}
```

- 장점 : 의존관계가 명확하게 드러난다.
- 단점 : 기존의 DI 원칙(인터페이스를 통한 느슨한 결합, 런타임 시 오브젝트의 관계가 정의됨)에는 부합한다.

### 2. UserDao 내부에서 JdbcContext를 생성하기- 수동 DI

`UserDao` <- `DataSource`

```java
@Component
public class UserDao6 {
    private final DataSource dataSource;
    private JdbcContext jdbcContext;

    public UserDao6(DataSource dataSource) {
        this.jdbcContext = new JdbcContext(dataSource);
        this.dataSource = dataSource;
    }

    public void deleteAll() throws Exception {
        this.jdbcContext.workWithStatementStrategy(
            new StatementStrategy() {
                @Override
                public PreparedStatement makeStatement(Connection c) throws SQLException {
                    return c.prepareStatement("delete from users");
                }
            }
        );
    }

}
```

```java
public class JdbcContext {
    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws Exception{
        Connection c = null;
        PreparedStatement ps = null;
                try {
            c = dataSource.getConnection();

            ps = stmt.makeStatement(c);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(ps != null) { try{ps.close();} catch(SQLException e){}}
            if(c != null) { try{c.close();} catch(SQLException e){}}
        }
    }
}
```

- JdbcContext는 빈으로 등록하지 않고, UserDao에서 직접 생성해준다.
- UserDao와 DataSource는 실제론 의존적인 관계가 아니지만 JdbcContext에 DataSource를 주입하기 위한 용도로 의존관계를 가지고 있다.
- 장점 : 긴밀한 관계를 가진 두 오브젝트(UserDao와 JdbcContext)의 관계를 외부에 드러내지 않는다.
- 단점 : JdbcContext를 빈으로 등록하지 않기 때문에 싱글톤으로 만들 수 없다. 수동 DI으로 인한 부가적인 코드가 필요한다.

### 두 가지 방법 중 어느 것을 선택하나?

- 의존관계를 명확하게 드러내야하는 경우 1번, 의존관계를 숨겨야 할 때는 2번.
- 위의 두 가지 방법 중 어느 것이 낫다고 말할 수는 없다. 상황에 따라 적절하다고 판단되는 방법을 선택해서 사용하라.
- 다만 왜 그렇게 선택했는지에 대한 분명한 이유는 말할 수 있어야한다.
- 분명하게 설명할 자신이 없다면 차라리 인터페이스를 만들어 평범한 DI 구조로 만드는게 나을 수도 있다.

## 🍎 템플릿/콜백

- 전략 패턴 + 익명 내부 클래스 활용
- 전략 패턴의 Context가 템플릿, 익명 내부 클래스의 오브젝트를 콜백이라고 한다.
- 콜백은 일반적으로 functional interface를 사용하여 익명 내부 클래스로 구현된다.
- 매번 사용할 콜백 오브젝트를 새롭게 전달한다.

### 동작 방식

1. 클라이언트가 콜백 오브젝트를 생성하고, 템플릿의 메소드를 호출한다. 이 때 콜백의 오브젝트는 템플릿의 메소드의 파라미터로 넘긴다.
2. 템플릿은 콜백 오브젝트를 받아서 콜백 오브젝트의 메소드를 실행한다.
3. 콜백은 작업을 수행 후 템플릿에게 결과를 반환해준다.
4. 템플릿은 콜백의 작업 결과를 받아 나머지 작업을 마저 수행한다. 경우에 따라 처리 결과를 클라이언트에게 반환한다.

### 리팩토링 순서 정리

1. 중복된 코드는 먼저 메서드로 분리하기
2. 인터페이스를 사이에 두어 전략 패턴을 적용하고, DI로 의존관계 관리하기
3. 바뀌는 부분이 한 어플리케이션에서 여러 개 생성되면 템플릿/콜백 패턴 고려하기

## 🍎 JdbcTempalte

- 여러 템플릿을 제공하며, 템플릿 호출 시 익명 내부 클래스로 콜백을 제공해야한다.
- `UserDao` <- `JdbcTemplate`
  - JDBC API를 사용하는 방식, 예외처리, 리소스 반납, DB 연결 등에 대한 관심은 오직 JdbcTemplate에 있다.
  - JdbcTemplate은 DAO안에서 직접 생성하는 것이 관례이지만, 원한다면 독립된 싱글톤 빈으로 등록 후 DI 받아 인터페이스(JdbcOperations)를 통해 사용할 수 있다.

```java
/*
 * JdbcTemplate 적용하기
 */
public class UserDao8 {
    private final JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public UserDao8(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void deleteAll() throws Exception {
        this.jdbcTemplate.update("delete from users");
    }

    public void addUser(final User user) {
        String sql = "insert into users(id, name, password) values (?,?,?)";
        this.jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
    }

    public int getCount() {
        return this.jdbcTemplate.query(
            new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    return con.prepareStatement("select count(*) from users");
                }

            },
            new ResultSetExtractor<Integer>() {

                @Override
                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    rs.next();
                    return rs.getInt(1);
                }

            });
    }

    public User getUser(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", userMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query(
            "select * from users order by id", userMapper);
    }
}

```
