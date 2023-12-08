## 🍎 SQL 중심적인 개발의 문제점
- 무한 반복, 지루한 코드 : 자바 객체를 SQL로, SQL을 자바 객체로 매핑해주는 일을 개발자가 직접 한다. 필드가 추가되면, SQL문에도 추가해줘야하는 번거로운 작업...
- SQL에 의존적인 개발
- 객체와 RDB의 패러다임 불일치 : 객체에서는 상속을 사용하고 RDB에서는 슈퍼타입 서브타입 관계를 사용한다. 객체에서는 참조를 사용하지만 RDB에서는 외래키를 사용한다.
    - 이런 RDB의 패러다임은 객체지향 패러다임과 전혀 맞지 않는다.?
- 진정한 계층 분할이 안된다. 물리적으로 자바와 SQL을 분리했더라도 논리적으론 전혀 분리되지 않았다.
- 객체를 자바 컬렉션에 저장하듯이 DB에 저장할 수 없을까? -> ORM!!

## 🍎 ORM
- object-relational mapping (객체 관계 매핑)
- 객체는 객체대로 설계하고 RDB는 RDB대로 설계하여 ORM이 그 사이에서 매핑해준다.
- JAVA 애플리케이션과 JDBC API 사이에서 JPA가 동작한다.

- insert 동작 방식
    - Entity 분석
    - insert sql 생성
    - jdbc api 사용
    - 패러다임 불일치 해결
- select 동작 방식 
    - select sql 생성
    - jdbc api 사용
    - ResultSet 매핑
    - 패러다임 불일치 해결
## 🍎 JPA는 왜 사용해야 하는가?
- SQL 중심적인 개발에서 객체 중심으로 개발할 수 있게 해준다.
- 이는 생산성을 높이고, 유지보수를 쉽게 해준다.
- 패러다임의 불일치를 해결한다.
    

## 🍎JPQL
- JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리를 제공한다.
- SQL 문법과 유사하지만 필드가 아닌 객체를 사용한다.

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

EntityManager em = emf.createEntityManager();

//JPQL
List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();

```
- jpql은 엔티티 객체를 대상으로 쿼리문을 생성한다.
- 데이터의 모든 변경은 tx안에서 실행되어야 한다.