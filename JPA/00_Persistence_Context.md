### JPA에서 중요한 두 가지
1. 객체와 RDB의 매핑
2. 영속성 컨텍스트

## 🍎 영속성 컨텍스트?
- "엔티티를 영구적으로 저장하는 환경"이라는 뜻

### EntityManagerFactory , EntityManager
- EntityManagerFactory가 EntityManager를 생성하면 이 엔티티 매니저를 통해 영속성 컨텍스트에 간접적으로 접근할 수 있다.
- 영속성 컨텍스트는 눈에 보이진 않는다.
- J2SE 환경에서는 엔티티 매니저 : 영속성 컨텍스트가 1대 1이다.
- J2EE, 스프링 프레임워크같은 컨테이너 한경에서는 엔티티 매니저: 영속성 컨텍스트가 N:1이다.

```java
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();
tx.begin();

Member member = new Member();
member.setId("member1");

em.persist(member);

tx.commit();
```

## 🍎 엔티티의 생명주기
### 비영속(transient)
- 영속성 컨텍스트와 상관없는 상태
### 영속(managed)
- 영속성 컨텍스트가 관리하는 상태
```java
Member member1 = new Member();
member.setId("memeber1");

EntityManager em = emf.createEntityManager();
// 영속
em.persist(member1);
```
### 준영속(detached)
- 영속성 컨텍스트가 관리했다가 분리된 상태(영속-> 준영속)
- 영속 상태의 엔티티가 영속성 컨텍스트에서 분리됨.
- 따라서 영속성 컨텍스트가 제공하는 기능을 사용 못함.
```java
// 준영속상태로 만드는 법
em.detach(member1); //특정 엔티티만 준영속 상태로 전환
em.clear(); // 영속성 컨텍스트를 완전히 초기화
em.close(); //영속성 컨텍스트를 종료
```
### 삭제(removed)
- 삭제된 상태
```java
em.remove(member1);
```

## 🍎 영속성 컨텍스트의 이점
### 1. 1차 캐시
- 엔티티 조회 시, 영속성 컨텍스트의 1차 캐시에서 먼저 조회한 후, 값이 있으면 리턴, 없으면 DB에서 조회 후 1차 캐시에 저장.
- 따라서 엔티티를 2번 조회하면 첫 번째 조회에서 쿼리 발생하지만 두 번째 조회에선 1차 캐시에서 가져오기 때문에 쿼리 발생하지 않음.
```java
em.persist(member1);//1차 캐시에 저장
em.find(Member.class, "member1"); // 1차 캐시에서 조회

em.find(Member.class, "member2"); // 1차 캐시에서 조회, 없어서 DB에서 조회 후 1차 캐시에 저장
```
### 2. 동일성(identity) 보장
- 1차 캐시로 반복 가능한 읽기(Repeatable Read) 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 수준에서 제공한다.
```java
Member a = em.find(Member.class, "member1");
Member b = em.find(Member.class, "member1");

System.out.println(a==b); //동일성 비교 true
```
### 3. 쓰기 지연
- em.persist()한다고 쿼리가 바로 발생하는 것이 아님.
```java
em.persist(memberA);
em.persist(memberB); // 아직 쿼리 발생 X

tx.commit(); //커밋하는 순간 자동으로 쿼리 발생
```
1. `em.persist(memberA);` : memberA를 1차 캐시에 저장하는 동시에 쓰기 지연 SQL 저장소라는 곳에 INSERT SQL을 저장한다.
2. `em.persist(memberB);` : memberB를 1차 캐시에 저장하는 동시에 쓰기 지연 SQL 저장소라는 곳에 INSERT SQL을 저장한다.
3. `tx.commit();` : 쓰기 지연 SQL 저장소에 있는 쿼리문들을 DB에 보낸다.(flush) 그리고 commit한다.
### 4. 변경 감지(Dirty Checking)
- 영속성 컨텍스트에서 관리하는 엔티티가 변경 되면 영속성 컨텍스트가 자동으로 변경을 감지하여 update문을 생성한다.
```java
Member member1 = em.find(Member.class, "member1");
member1.setUserName("One");

tx.commit();
```
1. tx.commit()할 때 자동으로 flush 발생.
2. flush()는 우선 1차 캐시에 있는 엔티티와 스냅샵을 비교한다. 
3. 엔티티와 스냅샵의 값이 같지 않으면 UPDATE SQL을 생성하고 이를 쓰기 지연 SQL 저장소에 저장한다.
4. 그리고 쓰기 지연 SQL 저장소에 있는 쿼리문들을 DB에 보낸다. 
5. 그리고 commit 한다.

### 5. 지연 로딩(lazy loading)

## 🍎 플러시
- 영속성 컨텍스트의 변경 내용을 DB에 반영
- 영속성 컨텍스트를 비우는게 아님. 영속성 컨텍스트의 변경 내용을 DB에 동기화하기 위한 매커니즘.
- 결국 트랜잭션이라는 작업 단위가 존재해서 가능함. 커밋 전에만 동기화하면 됨.
### 영속성 컨텍스트를 플러시하는 방법
- em.flush() : 직접 호출
- tx.commit() : 자동으로 플러시 호출
- JPQL 쿼리 실행 : 자동으로 플러시 호출
```java
em.persist(memberA);
em.persist(memberB);
em.persist(memberC);

query = em.createQuery("select * from Member m", Member.class);
List<Member> members = query.getResultList();

```

### 플러시 모드 옵션
```java
em.setFlushMode(FlushModeType.AUTO);
```
- FlushModeTyoe.AUTO : 기본값, 커밋이나 쿼리 실행할 때 플러시됨. 보통 기본값 바꿀 일 없음.
- FlushModeTyoe.COMMIT : 커밋할 때만 플러시됨.