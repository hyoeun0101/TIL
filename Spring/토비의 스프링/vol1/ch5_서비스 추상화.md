## 🍎 정리

- 비지니스 로직을 담은 코드(서비스)와 데이터 액세스 로직을 담은 코드(DAO)를 각각 클래스로 분리한다.
- 서비스에서는 데이터 액세스 기술에 독립적으로 만들기 위해 DAO를 인터페이스를 통해 DI 받는다.
- 서비스는 단위 작업을 보장해주는 트랜잭션이 필요하다.
- 트랜잭션의 시작과 종료를 지정하는 일을 트랜잭션 경계설정이라고 한다.
- 스프링이 제공하는 트랜잭션 동기화 기법을 사용한다.
- 트랜잭션 API의 종류와 방법은 다양한데 서비스 로직에 영향을 주지 않기 위해 이 또한 추상화가 필요하다.
- 테스트하기 어려운 API(ex) 메일 전송 등)도 추상화하여 작성하여 테스트를 편리하게 작성한다.

---

## 🍎 코드 개선하기

**\*\* 작성된 코드 검토하기 - 질문 4가지**

- 코드에 중복된 부분은 없는가?
- 코드가 하는 일을 이해하기 어렵진 않은가?
- 코드가 자신이 있어야 할 자리에 잘 있는가?
- 앞으로 어떤 변경이 일어날 수 있고, 그 변화에 쉽게 대응할 수 있게 작성되어 있는가?

```java
@Service
@RequiredArgsContructor
public UserServiceImpl {
    private final UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users) {
            Boolean changed = null;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if(user.getLevel() == Level.SILVER && user.getLogin() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if(user.getLevel() == Level.GOLD) {
                changed = false;
            } else {
                changed = false;
            }

            if(changed) userDao.update(user);
        }
    }
}
```

- 이 코드는 레벨이 증가함에 따라 else if 문을 계속 넣어줘야한다. 그리고 레벨 체크와 레벨업 조건을 같이 검사하고 있다. 즉 성격이 다른 검증을 동시에 하고 있다.
- UserService가 일일이 User의 레벨을 업그레이드 하지 말고, User에게 자신의 레벨을 업그레이드하라고 요청하자.

```java
@Service
@RequiredArgsContructor
public UserServiceImpl {
    private final UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users) {
            if(canUpgradeLevel(user)) {
                user.upgradeLevel();
            }
        }
        if(canUpgradeLevel(user))
    }
    private boolean canUpgradeLevel(User user) {
        switch (user.getLevel()) {
            case BASIC: return user.getLogin() >= 50;
            case SILVER: return user.getLogin() >= 30;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : "+ user.getLevel());
        }
    }
}

```

```java
public class User {
    private Level level;

    public void upgradeLevel() {
        Level nextLevel = this.level.getNextLevel();
        if(nextLevel != null) {
            this.level = nextLevel;
        } else {
            throw new IllegalStateException(this.level + "은 업그레이드 불가능");
        }
    }
}
```

```java
package com.spring.tobi.ch5;

public enum Level {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level nextLevel;

    Level(int value, Level nextLevel) {
        this.value = value;
        this.nextLevel = nextLevel;
    }

    public int intValue() {
        return value;
    }

    public Level getNextLevel() {
        return this.nextLevel;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1:
                return BASIC;
            case 2:
                return SILVER;
            case 3:
                return GOLD;
            default:
                throw new AssertionError("UnKnown value: " + value);
        }
    }
}

```

- 객체지향적인 코드는 다른 객체의 데이터를 set을 통해 변경하지 말고, 그 객체에게 데이터를 변경해달라는 작업을 요청해야 한다.

## 🍎 트랜잭션

### 트랜잭션의 경계설정

- 트랜잭션을 commit(), rollback()하는 작업을 트랜잭션 경계설정이라고 한다.
- 트랜잭션은 하나의 Connection에서 이뤄진다. 즉, 작업을 `하나의 트랜잭션`으로 묶으려면 `같은 Connection을 사용`해야 한다는 의미다.
- 기존에 JdbcTemplate으로 작성한 DAO의 메소드는 호출할 때마다 커넥션을 생성하여 실행한다. 그럼 DAO의 여러 메서드를 하나의 트랜잭션으로 묶을 수 없다.

### 트랜잭션 동기화 방식

- 스프링에서 제공하는 `트랜잭션 동기화`는 여러 작업을 하나의 트랜잭션으로 묶기 위해 `여러 작업들이 하나의 Connection만을 사용하게 해준다.`
- 작동 방식
  - Service에서 Connection을 생성하면, 그 Connection을 트랜잭션 동기화 저장소에 저장한다.
  - DAO의 메소드가 호출되면 트랜잭션 동기화 저장소에서 현재 시작된 트랜잭션을 가진 Connection을 찾는다.
  - 그 Connection을 이용해 PreparedStatement를 만들어 SQL을 실행한다.
  - 트랜잭션 내의 모든 작업이 정상적으로 끝났으면 Connection의 commit()을 호출한다.
  - 트랜젝션 동기화 저장소에서 Connection을 제거한다.
- `DataSourceUtils`의 `getConnection()` 메소드가 `트랜잭션 동기화`를 제공한다.

### 트랜잭션 서비스 추상화

- 하나의 트랜잭션에서 여러 DB에 데이터를 넣는 작업을 하려면 어떻게 해야할까?
  - 트랜잭션 매니저를 통해 여러 개의 DB와 작업을 하는 것을 하나의 트랜잭션으로 묶을 수 있다!
- 자바는 JTA(Java Transaction API)로 트랜잭션 매니저를 지원한다.
  - 트랜잭션 매니저는 DB와 메시징 서버를 관리하는 각각의 `리소스 매니저`와 XA `프로토콜`을 통해 연결된다. 이를 통해 트랜잭션 매니저가 실제 DB와 메시징 서버의 트랜잭션을 종합적으로 제어할 수 있다.
- 데이터 액세스 기술마다 지원하는 트랜잭션 매니저가 다르다. 따라서 스프링에선 `PlatformTransactionManager`로 트랜잭션 추상화 계층을 만들었다. 이것도 트랜잭션 동기화를 사용한다.

```java
public void addUser() {
    PlatformTransactionManager tm = new DataSourceTransactionManager(datasource);

    TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());

    try {
        // dao 메소드 실행
        tm.commit(status);
    } catch( Exception e) [
        tm.rollback(status);
    ]
}
```

- 위처럼 트랜잭션 매니저의 구현체를 메서드 내에서 작성하지 말고, 빈으로 등록 후 DI 받아야한다.

## 🍎 Service 추상화

- 구현체가 변경되는 오브젝트이면 인터페이스를 통해 DI 받는 것이 중요하다고 했다. (DAO, 트랜잭션, DataSource 같이.)
- 테스트하기 어려운 API를 사용할 때도 추상화를 할 필요가 있다.(테스트를 위한 서비스 추상화)
  - 예를 들어 메일 전송 기능을 만들 때, 테스트 코드를 돌릴 때마다 메일 전송을 할 필요가 있을까? 이는 서버에 부담이 많이 가므로 바림직하지 않다.
  - 이럴 땐, 메일 전송 기능의 주요 기능 (ex) sendMail)을 뽑아 인터페이스로 만들고, 실제 구현체는 DI 받는다.
  - 실제로 메일 전송 시에는 `메일 서버에게 요청하는 코드를 작성한 구현체`를 DI하고, 테스트 시에는 `아무 작업을 하지 않는 구현체`를 DI한다.

## 🍎 Mock Object

- 테스트 시 Mock Object를 이용해서 구현체를 원하는 대로 DI할 수 있다.
- 메일 전송 기능을 테스트할 땐, 실제로 메일을 전송할 필요는 없다. 따라서 아무일도 하지 않는 Mock Object를 생성하여 주입한다.

```java
public class UserServiceTest {
    @Test
    @DirtiesContext
    public void sendMail_when_upgrade_level() {
        // 테스트를 위한 Mock Object 생성
        MockMailSender mockMailSender = new MockMailSender();
        UserService userSerivce = new UserServiceImpl(userDao, mockMailSender);//Mock Object를 DI한다.
        ...
    }
}
```
