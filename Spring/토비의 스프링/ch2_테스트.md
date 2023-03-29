## 🍎 테스트 클래스 동작 방식

1. 테스트 클래스에서 @Test 붙은 public void이며, 파라미터가 없는 메서드를 모두 찾는다.
2. 테스트 메서드 실행할 때 테스트 클래스의 오브젝트를 생성한다.
3. @Before 메서드 실행
4. @Test 메서드 실행 후 결과 저장
5. @After 메서드 실행
6. 2~5 반복.
7. 테스트 결과 출력

- 테스트 메서드를 실행할 때마다 테스트 클래스의 인스턴스를 생성한다. 테스트 메서드를 3번 실행하면, 테스트 클래스의 인스턴스를 3번 생성하는 것이다.
  - why? 각 테스트가 서로 영향을 주지 않고, 독립적으로 실행됨을 보장하기 위해
- @Before, @After 메서드
  - 클래스 전체에서 공통으로 처리하는 로직을 넣는다.
  - 일부 메서드에서만 공통으로 사용하는 로직은 메서드 분리하여 테스트 메서드에서 직접 호출하거나, 또 다른 테스트 클래스로 빼주는 것이 좋다.

[예제1]

```java
public class UserDaoTest {
  private UserDao useDao; //픽스쳐 : 테스트를 수행하는데 필요한 객체

  @Before
  public void setUp() {
    ApplicationContext ac = new GenericXmlApplicationContext("applicationContext.xml");
    this.userDao = ac.getBean(UserDao.class);
  }

  @Test
  public void UserDaoGetTest() throws SQLException{
    ...
  }
}
```

### @RunWith, @ContextConfiguration

- 테스트 컨텍스트는 테스트 메서드를 실행할 때마다 ApplicationContext를 생성한다. 테스트 메서드 3개를 실행하면 ApplicationContext가 3번 생성되는 것이다.
- 애노테이션 설정만으로 테스트에서 필요로 하는 ApplicationContext를 한번만 생성하고 공유해서 사용할 수 있다! 테스트 클래스에 @RunWith, @ContextConfiguration 을 붙여주자.

[예제2]

```java
@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 Junit 확장 기능 지정.
@ContextConfiguration(locations="/applicationContext.xml") //테스트 컨텍스트가 생성할 AppcliationContext의 설정파일 위치 지정.
public class UserDaoTest {
  @Autowired
  private ApplicationContext ac; // 테스트 컨텍스트가 생성한 ApplicationContext가 자동 주입된다.

  private UserDao useDao;

  @Before
  public void setUp() {
    this.userDao = this.ac.getBean(UserDao.class);
  }

  @Test
  public void UserDaoGetTest() throws SQLException{
    ...
  }
}
```

- 이제 테스트 메서드를 실행할 때 처음 딱 한 번만 ApplicationContext를 생성하고, 그 이후에는 ApplicationContext를 주입하여 사용한다. 그래서 처음 실행하는 테스트 메서드가 제일 느린 것을 확인할 수 있다.
- 여러 테스트 클래스에서 같은 설정 파일을 가진 ApplicationContext를 사용한다면, 이 또한 ApplicationContext를 한 번만 생성하고, 공유하여 사용한다.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml") // 동일한 설정 파일
public class UserDaoTest1 {
  @Autowired
  private UserDao userDao;

  ...
}

```

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml") // 동일한 설정 파일
public class UserDaoTest2 {
  @Autowired
  private UserDao userDao;
  ...
}
```

- UserDaoTest1과 UserDaoTest2의 테스트 메서드는 하나의 ApplicationContext를 공유하여 사용한다.

## 테스트에서 DI하는 방법 3가지

### 1. 테스트용 설정 파일 만들기

- application-test.xml와 같이 테스트 전용 설정 파일을 따로 만든다.
- 테스트 시 @ContextConfiguration에 설정 파일 위치만 변경해주면 내가 원하는 객체를 DI할 수 있다.
- 로컬 DB를 사용하고 싶을 때는 `application-local.xml`을 설정파일로 사용하여 테스트할 수 있고, 테스트 DB를 사용하고 싶을 때에는 `application-test.xml`을 설정 파일로 사용하여 테스트할 수 있다는 말이다.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-test.xml") //지정한 위치의 설정 파일을 사용하여 스프링 컨테이너를 생성한다.
public class UserDaoTest {
  @Autowired
  private UserDao userDao;

  ...
}
```

### 2. 스프링 컨테이너 사용하지 않고 DI하기.

- @Before 메서드에서 직접 객체를 생성하고, 직접 의존관계를 주입한다.
- 직접 의존관계를 관리한다는 번거로움은 있지만 AppicationContext를 사용하지 않아 더 단순하고, 테스트 시간을 단축할 수도 있다.

```java
public class UserDaoTest2 {
 private UserDao userDao;

 @Before
 public void setUp() {
   userDao = new UserDao(); // 오브젝트 생성
   DataSource dataSource = new SigleConnectionDataSource("jdbc:mysql://localhost/testdb","userId","pwd",true);
   userDao.setDataSource(dataSource);// 의존관계 주입
 }

 ...
}
```

### 3. 수동으로 DI하기

- @Before 메서드에서 수동으로 DI 하여, UserDao의 의존관계 빈을 수동으로 변경한다.
- 의존관계가 변경된 스프링 컨테이너는 나머지 테스트 메서드에서도 계속 공유된다. 그것을 방지하기 위해 수동으로 강제로 의존관계를 변경한 테스트에는 @DirtiesContext를 붙여줘야 한다.
- @DirtiesContext는 ApplicationContext를 공유하지 않고, 매번 새로운 ApplicationContext를 생성하여 사용한다.

```java
@DirtiesContext
public class UserDaoTest {
  @Autowired
  UserDao useDao;

  @Before
  public void setUp() {
    // 직접 객체를 생성하고
    DataSource dataSource = new SigleConnectionDataSource("jdbc:mysql://localhost/testdb","userId","pwd",true);
    // 수동으로 DI한다.
    userDao.setDataSource(dataSource);
  }

}
```

### ** 정리 **

- 우선적으로 스프링 컨테이너를 사용하지 않을 것(2번)을 고려해라. 오브젝트의 생성과 초기화가 단순하다면 2번 방법이 제일 빠르고, 간결하다.
- 복잡한 의존관계를 가진 오브젝트를 테스트할 경우는 스프링의 설정을 이용한 DI(1번)를 고려해라. 테스트할 설정 파일을 따로 만들어 사용하는 것이 좋다.
- 예외적인 의존관계를 강제로 구성해야하는 경우가 있다. 이는 수동 DI(3번)를 고려해라. @DirtiesContext붙이는 것도 잊지 말아야한다.
