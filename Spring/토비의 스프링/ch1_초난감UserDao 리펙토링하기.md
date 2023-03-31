## 🍎 초난감 DAO

- 초난감 DAO를 만들어보고, 리팩토링하기.

### \*\* JDBC 이용하는 법

1. DB 연결을 위한 Connection 가져오기.
2. SQL을 담은 Statement(또는 PreparedStatement) 생성 및 실행
3. SQL 쿼리의 결과를 ResultSet으로 받는다.
4. 작업 중에 생성한 Connection, Statement, ResultSet을 닫아준다.

```java
public class UserDao {
    public void add(User user) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook","userId","pwd123");

        PrepareStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

        public User get(String id) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook","userId","pwd123");

        PrepareStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, user.getId());

        ResultSet rs = ps.executeUpdate();
        rs.next();
        User user new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }
}
```

### 1. 초난감 DAO 리팩토링 - 관심사의 분리

- 초난감 DAO에서 JDBC 사용하는 부분이 모든 메서드에 공통적으로 들어가있다.
- 관심사의 분리를 통해 Connection을 얻는 공통 부분을 메서드로 분리했다.

```java
public class UserDao {

    public void add(User user) throws Exception {
        Connection c = getConnection();
        PrepareStatement ps = c.prepareStatement("insert into users(id,name,password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public User get(String id) throws Exception {
        Connection c = getConnection();
        PrepareStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, user.getId());
        ResultSet rs = ps.executeUpdate();
        rs.next();
        User user new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        rs.close();
        ps.close();
        c.close();
        return user;
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook","userId","pwd123");
        return c;
    }
}
```

### 2. 초난감 DAO 리팩토링 - 상속을 통한 확장

- 다른 종류의 DB Connection을 얻고 싶다. 유연하게 다른 종류의 DB Connection을 얻는 부분을 원하는대로 갈아끼울 수 있도록 리팩토링 해보자.
- UserDao의 getConnection을 추상 메서드로 만들고, UserDao를 상속한 하위 클래스에서 getConnection을 구현한다.

```java
public class UserDao {
    public void add(User user) throws Exception { ... }
    public User get(String id) throws Exception { ... }

    public abstract Connection getConnection() throws Exception;
}
```

```java
public class NUserDao extends UserDao {
    @Override
    public Connection getConnection() throws Exception {
        // 원하는 대로 구현.
    }
}
```

---

** 깨알지식 **  
**템플릿 메소드 패턴 (template method pattern)**

- 위처럼 add(), get() 과 같은 기본적인 로직은 상위 클래스에 만들고, getConnection()과 같이 일부기능은 추상 메서드나 protected 메서드로 만든 뒤 하위 클래스에서 구현하는 패턴.

**팩토리 메소드 패턴 (factory method pattern)**  
![s](https://user-images.githubusercontent.com/96059261/229060190-1ee029e3-621f-4fcf-93c3-e5ec803dfce6.jpg)

- `Connection getConnection()` 메서드에서 Connection은 인터페이스이다. 하위 클래스에서 실제 오브젝트의 타입을 지정한다.
- 이와 같이 하위 클래스에서 구체적인 오브젝트 생성 방법을 지정하는 패턴을 팩토리 메소드 패턴이라고 한다.

---

**위의 초난감 UserDao를 상속했을 때 문제점 !!**

- `단일 상속 `: 단지 커넥션 객체를 가져오는 것 때문에 상속을 한다면, 후에 다른 목적으로 상속을 사용하기 힘들다.
- `상위 클래스 변경의 어려움` : 상위 클래스를 변경하면 하위 클래스도 변경된다. 경우에 따라서 상위 클래스의 변경에 제약을 줘야한다.
- getConnection을 다른 Dao에서도 사용해야한다면? getConnection은 `또 중복`된다.

### 3. 초난감 DAO 리팩토링 - 클래스의 분리

- 아예 커넥션을 가져오는 관심을 가진 객체로 분리하자!

```java
public class UserDao {
    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDao() {
        this.simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void add(User user) throws Exception {
        Connection c = simpleConnectionMaker.makeNewConnection();
        ...
    }

    public User get(String id) throws Exception {
        Connection c = simpleConnectionMaker.makeNewConnection();
        ...
    }

}
```

```java
public class SimpleConnectionMaker {
    public Connection makeNewConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook","userId","pwd123");
        return c;
    }
}
```

**\*\* 또 다시 문제점!!**

- `OCP 위반`, `DIP 위반`: SimpleMakerConnection을 변경하려면 UserDao를 변경해야한다.

### 4. 초난감 DAO 리팩토링 - 인터페이스의 도입

- 직접적인 관계를 맺는 UserDao와 SimpleMakerConnection 사이에 인터페이스를 도입하자!

![ｇ](https://user-images.githubusercontent.com/96059261/229068563-44afb9f3-5ddb-47fd-a204-035a5238c6c0.jpg)

```java
public class UserDao {
    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws Exception {
        Connection c = connectionMaker.makeNewConnection();
        ...
    }

    public User get(String id) throws Exception {
        Connection c = connectionMaker.makeNewConnection();
        ...
    }

}
```

```java
public interface ConnectionMaker {
    public Connection makeNewConnection() throws Exception;
}
```

```java
public class NConnectionMaker implements ConnectionMaker{
    @Override
    public Connection makeNewConnection() throws Exception {
        // Connection 구체적으로 구현
    }

}
```

## 정리

- 총 4단계를 거쳐 리펙토링을 해봤다. 이제 클래스를 설계할 때 다음 질문을 던져보며 확인하자.

1. 메서드로 분리 가능한가?
2. 구현이 계속 바뀌어야 되진 않은가?
3. 클래스로 분리해야 되진 않은가?
4. 인터페이스가 필요하진 않은가?
