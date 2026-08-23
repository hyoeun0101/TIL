## SQL 중심 개발 VS ORM

- **객체지향과 관계형 DB의 불일치 문제**를 해결하기 위해 ORM을 사용한다.
- MyBatis는 SQL 매핑 기술.
- **ORM (object-relation mapping)** = 객체 관계 매핑

> java Application --> JPA --> JDBC API --> DB

- JPA는 애플리케이션과 JDBC API 사이에서 객체지향 매핑을 지원함.
- jpa가 Entity를 분석해서 SQL문을 자동 생성해준다.

## JPA의 핵심
- 객체와 관계형 DB의 매핑
- 영속성 컨텍스트

## 영속성 컨텍스트
### EntityManager
- EntityManager를 통해 영속성 컨텍스트에 접근 가능하다.

- **스프링 환경**에서 EntityManager
  - 한 개의 EntityManagerFactory가 존재하고, EntityManagerFactory에서 EntityManager를 생성한다.
  - 하나의 트랜잭션을 EntityManager가 관리함.
  - 여러 EntityManager가 하나의 PersistenceContext를 공유하여 사용한다.

### 영속성 컨텍스트의 이점
- **1차 캐싱**
  - 엔티티 조회 시 영속성 컨텍스트의 1차 캐시에서 먼저 조회한다. 1차 캐시에 값이 없으면 DB에서 SELECT 후 1차 캐시에 저장한다.
- **동일성 보장**
  - 1차 캐시로 Repeatable read를 제공한다.
- **쓰기 지연**
  - 코드 실행 시점에 쿼리를 실행하지 않고, 쿼리를 쌓아놓았다가 커밋 시점에 한번에 쿼리 실행
- **엔티티 변경 감지(Dirty Checking)**
  - 엔티티 변경되면 영속성 컨텍스트가 이를 감지하여 자동으로 update 쿼리 실행
- **지연 로딩(lazy loading)**

```java
/**** 1차 캐싱, 동일성 보장  ***/
Member a = em.find(Member.class, 1L);   // SELECT 발생 → 1차 캐시에 저장
Member b = em.find(Member.class, 1L);   // 1차 캐시에서 조회.

System.out.println(a == b);   // true — 1차 캐시를 통해 동일성 보장
```

```java
/**** 쓰기 지연  ***/
Member m = new Member(1L, "name");
em.persist(m); // 아직 쿼리 실행X. 1차 캐시에 저장

tx.commit; // 커밋 시점에 쿼리 실행
```

```java
/**** 변경 감지  ***/
Member m = em.find(Member.class, 1L);
m.setName("One");

tx.commit(); // 커밋 시점에 update문 실행
```
### flush
- 영속성 컨텍스트의 변경내용에 대해 쿼리를 실행함. 영속성 컨텍스트를 비우진 않음.
- 직접 호출: `entityManager.flush();`
- 자동 호출: 트랜잭션 커밋 시점에 자동으로 flush 됨. JPQL 실행 시점에 자동으로 flush됨.

### 영속성 컨텍스트 라이프사이클
- 비영속(transient): 영속성 컨텍스트와 무관한 새 객체
- 영속(managed): 영속성 컨텍스트가 관리 중
- 준영속(detached): 관리되다가 분리된 상태. 준영속 상태에서 엔티티를 수정하거나, 삭제해도 쿼리 실행되지 않음.
- 삭제(removed): 삭제하기로 예약된 상태.

### 준영속 상태로 만드는 법
- `entityManager.detach(entity);`: 특정 엔티티만 준영속 상태로 전환
  - ex) `entityManager.detach(member);`: 영속성 컨텍스트가 관리하던 member 객체를 준영속 상태로 전환한다. member의 필드를 수정하더라도 update문이 실행되지 않는다.
- `entityManager.clear()`: 영속성 컨텍스트를 완전히 초기화
- `entityManager.close()`: 영속성 컨텍스트를 종료

```java
Member member = em.find(Member.class, 1L);
em.detach(member); // 준영속

m.setName("newName");

em.flush(); // 아무 SQL도 안 나감.
```

```java
Member member = em.find(Member.class, 1L);
member.setUsername("변경1");

em.clear();   // 준영속 → 더 이상 추적 안 함

em.flush();   // UPDATE 안 나감. 변경사항 증발
```

```java
Member member = em.find(Member.class, 1L);

em.close();   // 영속석 컨텍스트를 닫음

member.getUsername();          // OK — 그냥 자바 객체라 필드 접근은 됨
em.find(Member.class, 2L);     // IllegalStateException!
```
### DDL Auto
- create : 기존 테이블 삭제 후 생성 
- create-drop: 테이블 생성 후 애플리케이션 종료 시점에 테이블 삭제 
- update: 변경한 것만 반영(단 삭제는 안됨)
- validate: 엔티티와 테이블 매핑이 됐는지 체크만 함. 
- none: 사용하지 않음
- 개발초기 : create, update
- 테스트 서버 : update, validate
- 스테이징/운영서버: validate, none

> 운영장비에 절대 create,create-drop,update 사용하면 안됨!!!
---

## 객체와 테이블 매핑하기
### 엔티티 설정
- @Entity : public/protected 기본 생성자 있어야 함.
- @Table

### 필드와 컬럼 매핑
- @Column
- @Id, @GeneratedValue
- @Enumerated : EnumType.STRING으로 작성하기
- @Lob
- @Transient

### 기본 키 매핑
**@GeneratedValue**: 기본키 어떻게 생성할건지 설정

- `GenerationType.IDENTITY`: auto_increment (MyBatis의 auto_increment)
  - 인조키를 미리 알 수 없기 때문에 `em.persist()`시점에 바로 insert문 실행하고 id 셋팅함.
- `GenerationType.SEQUENCE`: 자동으로 시퀀스를 생성해줌. (Oracle의 sequence)
  - `em.persist()` 시점에 DB의 시퀀스만 실행해서 id를 셋팅하고 1차 캐시에 저장함. 그 다음 commit 시점에 insert문 실행함.
  - 기존에 있던 시퀀스와 매핑하려면 클래스 위에 @SequenceGenerator 작성 필요.
- `GenerationType.TABLE`: 키 생성용 테이블을 생성해줌.
  - 기존 키 생성용 테이블과 매핑하려면 클래스 위에 @TalbeGerator 작성 필요.
- `GenerationType.AUTO` : 디폴트. 방언에 따라 자동으로 지정함.

```java
@Entity 
@Table(name = "MBER") // 테이블명은 MBER
public class Member {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    @Column(name = "name", nullable = false) // 컬럼 매핑
    private String username; 

    private Integer age; 

    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private RoleType roleType; 

    @Temporal(TemporalType.TIMESTAMP) // Date 타입 매핑
    private Date createdDate; 

    // LocalDateTime쓰면 @Temporal 생략
    private LocalDateTime lastModifiedDate; 

    @Lob //BLOB, CLOB 매핑
    private String description; 

    @Transient // 해당 필드 컬럼에 매핑X
    private String memberId;
}
```

```java
/**
 * id 시퀀스 사용
 */
@Entity 
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR", //필수
        sequenceName = "MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, // DDL 생성할 때 시퀀스의 시작값 설정
        allocationSize = 50
)
public class Member { 
    @Id 
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE, 
            generator = "MEMBER_SEQ_GENERATOR"
    )
    private Long id;
}
```

```java
/**
 * id 테이블 사용
 */
@Entity
@TableGenerator(
    name = "MEMBER_SEQ_GENERATOR",
    table = "MY_SEQUENCES",
    pkColumnValue = "MEMBER_SEQ",
    allocationSize = 1
)
public class Member {
    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "MEMBER_SEQ_GENERATOR"
    )
    private Long id;

}
```

## ⭐연관관계 매핑⭐
- 객체와 테이블의 연관관계 차이점
  - **테이블은 외래키**로 조인해서 연관 테이블을 조회한다.
  - **객체는 참조**를 통해 연관 객체를 가진다.
- 테이블에선 Member의 외래키 team_id 하나로 두 개의 테이블 모두 접근이 가능하다.
- 객체에선 Member가 Team의 정보를 가질 수도, Team이 Member의 정보를 가질 수도 있다. 즉 양방향이 가능하다.
  - => 여기서 오는 딜레마 : 외래키를 뭐로 관리해야 하지? Member의 team? Team의 members?
  - => **연관관계 주인이 필요하다! 외래키가 있는 곳을 주인으로 결정하자.**


### 양방향 관계와 연관관계의 주인(Owner)
- 예시) 팀 1 - 회원 N

```java
@Entity
class Member { 
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    
    private String username;
    
    @ManyToOne // => Member가 N, Team이 1. 연관관계 주인
    @JoinColumn(name = "team_id")
    private Team team;

  /**
   * 연관관계 편의 메서드
   */
  public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
```

```java
@Entity
class Team {
  @Id
  @GeneratedValue
  @Column(name = "team_id")
  private Long id;

  private String name;

  // 주인이 아닌 쪽에서 mappedBy를 통해 주인을 지정해줘야 함.
  // 읽기 전용
  @OneToMany(mappedBy = "team")
  private List<Member> members = new ArrayList<>();
}
```

- 되도록이면 양방향 관계는 지양하자. 단방향으로 설계하자.
- 만약 양방향 관계를 사용해야 한다면 owner가 아닌 쪽에서 mappedBy로 주인을 지정해줘야 한다.
- owner가 아닌 쪽은 읽기만 가능하다.
- owner에서 연관관계 편의 메서드를 정의해서 사용하자.
- 양방향 관계를 가진 엔티티를 
```java
Team team = new Team();
team.setName("TeamA");
em.persist(team);

Member member = new Member();
member.setName("member1");
member.setTeam(team); // 단방향 연관관계 설정
em.persist(member);


```

### 다대다 M:N @ManyToMany
- 실무에서 사용하면 안된다.
- 관계형 데이터베이스에서 다대다는 가운데에 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함.

### 다대일 N:1 @ManyToOne
- 보통 "다"에 외래키가 있어야 한다. 그래서 다대일일 땐? "다"가 연관관계 주인이다.
- 양방향할 때 mppedBy 꼭 넣기. + 읽기만 가능.

### 일대다 1:N @OneToMany
- 실무에서 이 구조는 거의 가져가지 않는다. 원래 '다'에 외래키가 있는게 정상.
- '일'이 연관관계 주인이 되는 건 객체에서 Team이 Member를 알고싶은 것을 의미한다.
  - => 문제: Team을 수정하면? Member도 같이 N개 update 된다. 성능 저하가 발생한다. 진짜 심각한건 team만 수정한 것 같은데, Member 테이블의 update를 인지하기 어렵다는 것이다.
- Member에서 Team을 갈 일이 없더라도, 객체지향을 약간 포기하고, DB에 맞춰서 유지보수하기 쉽게 설계하자.


@JoinColumn(name="team_id", insertable = false, updatable = false) : 읽기 전용. insert, update를 안함.

차라리 다대일 양방향을 사용하자.

### 일대일 1:1
- 일대일 관계에서는 외래키가 어디에 있든 상관없다.
- 일대일 관계에서 트레이드 오프 : 외래키를 어디에 둘까?

- ex_ Member와 Locker. 일대다 관계
  - 추후에 하나의 회원이 여러 개의 Locker를 가질 수 있다면? 추후까지 생각하면 변경이 더 쉬운 Locker에 member_id를 가지고 있는 설계가 더 좋다.
  - 반대로 요구사항이 하나의 Locker를 여러 Member가 가질 수 있다면? Member에 locker_id를 가지고 있는 설계가 더 좋다.
- 개발자 입장에서 객체지향 관점으로 본다면...?
  - Member가 locker_id 가지고 있는게 유리하다. 성능 상으로도 좋다. Member join 없이 하나만 조회하면 되니까 성능 상 장점도 있음.
- 결국 트레이드 오프이다.
- 너무 먼 미래는 생각하지 않는다. 명확한 1:1 관계일 경우에 Member에 locker_id를 넣음.


### 연관관계 매핑 정리
- 주 테이블에 외래 키
- 대상 테이블에 외래 키
  - 단점: 양방향이 필요함. 프록시 기능의 한계로 항상 **즉시 로딩**됨.


## 상속관계 매핑
상속관계 테이블 설계 3가지 방법

1. 부모 테이블과 자식 테이블로 쪼개기
   - ITEM : item_id, name, price, dtype
   - MOVIE: item_id, actor, director
   - BOOK: item_id, author
   - ALBUM: item_id, artist

2. 단일 테이블 사용
   - ITEM: item_id, name, price, dtype, actor, director, author, artist

3. 부모 테이블 없이 자식 테이블만 존재
  - MOVIE: item_id, name, price, dtype, actor, director
  - BOOK: item_id, name, price, dtype, author
  - ALBUM: item_id, name, price, dtype, artist


```java
@Entity
public class Item { /*...*/ }

@Entity
public class Movie extends Item { /*...*/ }


@Entity
public class Book extends Item { /*...*/ }

@Entity
public class Album extends Item { /*...*/ }
```
- 기본적으로 2번으로 테이블이 생성됨.

- 부모 클래스에 `@Inheritance(strategy = InheritanceType.JOINED)` 붙이면 1번으로 테이블이 생성됨.

- 부모 클래스에 `@DiscriminatorColumn` 넣으면 자동으로 dtype 컬럼이 생성됨.

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Item { /*...*/ }

@Entity
public class Movie extends Item { /*...*/ }


@Entity
public class Book extends Item { /*...*/ }

@Entity
public class Album extends Item { /*...*/ }
```





## 프록시

- em.find() : DB에서 실제 select문 실행함.
- em.getReference() : DB 조회를 미루는 가짜 조회.
  - 진짜 데이터를 조회하는게 아니라 가짜 프록시 객체를 반환한다.
  - 프록시는 원본 엔티티를 상속한다. (Proxy extends Entity)
  - 프록시 객체는 처음 사용될 때 한 번만 초기화된다.

```java
Member member = em.getReference(Member.class, "id1"); // 프록시 객체 반환
member.getId(); // 프록시 초기화: DB 조회 후 영속성 컨텍스트에 저장하고, 실제 Entity를 생성함.
```
- 동등성을 ==로 비교 불가하고, `instanceof`로 확인 해야 한다.
- 영속성 컨텍스트에 엔티티가 존재하면 em.getReference()로 조회하더라도 실제 엔티티를 반환한다.

```java
Member member = em.find(Member.class, "id1");

Member reference = em.getReference(Member.class, "id1");

System.out.println("m1 == reference :: " + (m1 == reference)); // true


```
- 준영속 상태일 때, 프록시를 초기화하면? 예외 발생 (LazyInitializationException: could not initialize proxy - no Session)

```java
Member member = em.getReference(Member.class, "id1");

em.close();

member.getId(); // LazyInitializationException 발생
```

```java
// 프록스 인스턴스의 초기화 여부 확인
PersistenceUnitUtil.isLoaded(Object entity);

// 프록시 클래스 확인
entity.getClass().getName(); 

// 프록시 강제 초기화
Hibernate.initialize(Object entity);
```

## 즉시 로딩, 지연 로딩
Member를 조회할 때 Team도 같이 join해서 조회해야 할까?

- Member에서 Team 조회를 많이 하지 않는 경우: 지연 로딩 LAZY를 사용해서 프록시로 조회하자.

```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}
```
```java

Member member = em.find(Member.class, 1L);

System.out.println("team of member: " + member.getTeam().getClass()); // 프록시 객체

meber.getTeam().getName(); // team을 사용하는 시점에 초기화(DB 조회 )
```

- Member에서 Team 조회를 자주 사용하는 경우: 즉시 로딩 EAGER를 사용해서 join해서 조회하자.

```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;
}
```
```java

Member member = em.find(Member.class, 1L); // team을 join해서 조회

System.out.println("team of member: " + member.getTeam().getClass()); // 실제 Entity
```

실무에서 가급적 지연 로딩만 사용하자.
즉시 로딩을 적용하면 예상하지 못한 SQL이 발생한다.
즉시로딩은 JPQL에서 N+1 문제를 발생시킨다.
@ManyToOne, @OneToOne은 기본값이 즉시 로딩, @ManyToMany, @OneToMany는 기본값이 지연 로딩.
기본적으로 LAZY로 걸어두고, 필요한 것만 fetch join을 사용하자.



## 영속성 전이
- 영속성 전이는 연관관계 매핑과 아무 관계가 없다.

```java
public class Order {
  @OneToMany(mappedBy = "order", casecade = CasecadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();
  
}
```
- Order를 저장할 때, OrderItem도 같이 저장해줌.

```java
// 원래 코드
em.persist(orderItem1);
em.persist(orderItem2);
em.persist(order);

// casecade ALL로 설정하면 orderItem도 같이 persist됨.
em.persist(order);
```
- 영속성 전이 사용하는 경우: 라이프사이클이 유사할 때, 소유자가 하나일 때 사용한다.


고아 객체 제거: 부모 엔티티와 연관관계 끊어진 자식 엔티티를 자동으로 삭제

@OneToMany(orphanRemoval = true)
- 참조하는 곳이 하나일 때 사용해야 함.

영속성 전이 + 고아객체 => 두 옵션 활성화하면 부모 엔티티가 자식의 생명주기를 관리함.
em.persist(parent); 만 해도 child 저장됨.
em.remove(parent); 만 해도 child가 삭제됨.

도메인 주도 설계의 Aggregate Root 개념을 구현할 때 유용하다. 



## 값 타입

- JPA의 데이터 타입 분류
  - 엔티티 타입: @Entity로 정의하는 객체. 데이터가 변해도 식별자를 통해 지속적으로 추적이 가능하다.
  - 값 타입: int, Integer, String 등 같은 단순한 값으로 사용하는 객체. 식별자가 없으므로 변경 시 추적이 불가능하다.


### 기본값 타입
- 자바 기본 타입(int, double)
- 래퍼 클래스(Integer, Long)
- String

### 임베디드 타입
- embedded type: 새로운 값 타입을 직접 정의할 수 있다. 더 객체지향으로 작성 가능해짐.
  - ex) Address 클래스로 컬럼을 묶음. (city, street, zipcode 등)


@Embedded, @Embeddable

@AttributeOverrides(value = Attribute)


#### 불변 객체
- 임베디드 타입 객체를 여러 엔티티에서 공유하면 안된다. 공유한 객체의 값을 변경하면, side effect가 발생한다.
- 대신 값(인스턴스)를 복사해서 사용해야 한다. 그러나 인스턴스 공유를 원천적으로 차단하는 건 아니다.
- 불변객체로 만들어서 사용하자.

#### 동등성 비교
- 동일성 : 참조값 비교 (==)
- 동등성 : 값을 비교 (equals)
- 임베디드 타입을 == 으로 비교할 순 없다.
- equals 재정의해서 equals를 사용해서 동등성 비교하기.
- 반드시 equals, hascode를 작성해줘야 함. equals 작성할 때 직접 필드에 접근하지 말고, getter로 접근하자. (프록시 객체를 고려)

### 컬렉션 값 타입
- collection value type. 값 타입을 하나 이상 저장할 때 사용.
- 컬렉션을 저장하기 위한 별도의 테이블이 필요함.
- 엔티티의 생명주기를 따라감. 영속성 전이 + 고아객체 제거 기능을 필수로 가짐.
- 기본적으로 지연 로딩이다.

```java
// @OrderColumn(name = "address_history_order") // 순서값 컬럼을 생성하고, 값을 넣어줌
@ElmentCollection(fetch = FetchType.LAZY) // LAZY 기본값
@CollectionTable(name = "ADDRESS", 
        joinColumns = @JoinColumn(name = "member_id")
) // address 별도의 테이블 존재
List<Address> addressHistory = new ArrayList<>(); 
```
- 값 타입 컬렉션을 변경하면, 주인 엔티티와 관련된 모든 데이터를 삭제하고, 다시 저장한다. 

- 실무에서는 값 타입 컬렉션 대신에 일대다 관계를 고려하자. (영속성 전이 + 고아객체 제거 설정 필요)

- 값 타입 컬렉션은 단순할 때만 사용하자. ex) List<String> favoriteFoods


## 객체지향 쿼리 언어 JPQL
- 엔티티 객체를 대상으로 쿼리한다.

JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, 마이바티스를 같이 사용.
