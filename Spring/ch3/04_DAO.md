# DAO

: Data Access Object  
DB에 저장된 데이터를 CRUD 수행.  
DB테이블당 하나의 DAO

![img](/image/DAO.PNG)

- 계층 분리
- 영속 계층(Persistence Layer)인 UserDao와 Presentation Layer를 나눔.
- persistence layer: 데이터에 접근
- presentation layer: 데이터 보여주는 역할
- 변경에 유리. 중복 제거.
- MySQLUserDao, OracleUserDao 이렇게 사용 가능.
- 중간에 Business layer 있음.

`UserDao`  
deleteUser 실패하면
데이터베이스와 관련된 작업은 컨트롤러에서 예외를 처리할 수 없음. Dao에 있는 메서드들이 예외 처리.
예외 발생하면 예외 출력하고 0을 반환.

conn과 PreparedStatement 객체는 다 쓰고 난 후 무조건 close 해줘야함.  
안그럼 메모리 부족할 수 있음.  
close(pstmt,conn);

connection 만들고 PreparedStatement 만들고 resultSet 만듦. 닫을 땐 순서 반대로 닫아줘야함. close(rs,pstmt,conn)

try - with - resources

```java
public int updateUser(User user) {
        int rowCnt = FAIL; //  insert, delete, update

//        Connection conn = null;
//        PreparedStatement pstmt = null;

        String sql = "update user_info " +
                     "set pwd = ?, name=?, email=?, birth =?, sns=?, reg_date=? " +
                     "where id = ? ";

        // try-with-resources - since jdk7
        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        ){
            pstmt.setString(1, user.getPwd());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setDate(4, new java.sql.Date(user.getBirth().getTime()));
            pstmt.setString(5, user.getSns());
            pstmt.setTimestamp(6, new java.sql.Timestamp(user.getReg_date().getTime()));
            pstmt.setString(7, user.getId());

            rowCnt = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        }

        return rowCnt;
    }
```

try 부분 예외 발생하건 안하건 자동으로 close됨.  
이렇게 해라~  
AutoClosable를 구현한 객체만 자동으로 close되는 거임.

UserDao를 Interface로~~~  
public 메서드만 추출.

```
@Repository
public class UserDaoImpl implements UserDao{}
```

지금 이해는 안되지만...ㅠ

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/\*\*/root-context.xml"})  
이거 쓸 때 설정 파일이 root-context.xml로 되어있는데 여기서 component-scan 해줘야함.

```java
   @Test
    public void updateUser() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        User user = new User("xxxx","1234","zzzz","asf@asdf.com",new Date(),"fd",new Date());
        int rowCnt = userDao.insertUser(user);
        assertEquals(rowCnt,1);

        user.setPwd("4321");
        rowCnt = userDao.updateUser(user);
        assertEquals(1,rowCnt);

        User user2 = userDao.selectUser(user.getId());
        System.out.println(user);
        System.out.println(user2);
        assertEquals(user,user2);
    }

```

에러뜨는 이유. birth 필드의 타입에서 시간 변경되서.  
시간은 날려버리자.

```java
Calendar cal = Calendar.getInstance();
cal.clear();
cal.set(2000,1,1);
User user = new User("ccc","1234","zzzz","asf@asdf.com",new Date(cal.getTimeInMillis()),"fd",new Date());
```
