1. 기능별 uri 정의하기
|작업|URI|HTTP 메서드|설명|
|읽기|/board/read?bno=번호|GET|지정된 번호의 게시물 보여주기|
|삭제|/board/remove|POST|게시물 삭제|


uri와 url 차이    
url:전체 경로    
uri: 일부 경로    

[boardMapper.xml]
```
<select id="select" parameterType="int" resultType="BoardDto">
    SELECT bno,title,content, writer, view_cnt, comment_cnt, reg_date
    FROM board
    WHERE bno=#{bno}
</select>
```


[repository/BoardDaoImpl.java]
```
@Repository
public class BoardDaoImpl implements BoardDao{
    @Autowired
    SqlSession session;

    String namespace = "com.fastcampus.ch4.dao.BoardMapper.";

    @Override
    public BoardDto select(Integer bno) throws Exception{
        return session.selectOne(namespace+"select", 3);
    }
}
```

[BoardDaoImplTest.java]
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class BoardDaoImplTest {
    @Autowired
    private BoardDao boardDao;

    @Test
    public void selectTest() throws Exception {
        boardDao.deleteAll();
        assertTrue(boardDao.count()==0);

        BoardDto boardDto = new BoardDto("no title", "no content", "asdf");
        assertTrue(boardDao.insert(boardDto)==1);

        Integer bno = boardDao.selectAll().get(0).getBno();
        boardDto.setBno(bno);
        BoardDto boardDto2 = boardDao.select(bno);
        assertTrue(boardDto.equals(boardDto2));
    }


}
```
