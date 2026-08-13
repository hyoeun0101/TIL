## SQL 중심 개발 VS ORM

- 객체지향과 관계형 DB의 불일치 문제를 해결하기 위해 ORM을 사용한다.
- MyBatis는 SQL 매핑 기술.
- ORM: object-relation mapping = 객체 관계 매핑
- JPA: java 애플리케이션 - jpa - jdbc api - DB
- jpa가 Entity를 분석해서 insert SQL 자동 생성해주고, 객체를 반환해줌.

## JPA
### JPA의 핵심
- 객체와 관계형 DB의 매핑
- 영속성 컨텍스트

### 영속성 컨텍스트
#### EntityManager

- EntityManager를 통해 접근 가능하다.
- 

#### 스프링 환경
- EntityManagerFactory 하나 존재.
- EntityManagerFactory에서 EntityManager를 생성
- 하나의 트랜잭션을 EntityManager가 관리함.
- 여러 EntityManager는 하나의 PersistenceContext에서 관리됨.

#### 영속성 컨텍스트의 이점
- 1차 캐싱: 엔티티 조회 시 영속성 컨텍스트의 1차 캐시에서 먼저 조회함.
- 동일성 보장
- 쓰기 지연: 코드 실행 시점에 쿼리를 실행하지 않고, 쿼리를 쌓아놓았다가 커밋 시점에 한번에 쿼리 실행
- 엔티티 변경 감지(Dirty Checking): 엔티티 변경되면 영속성 컨텍스트가 이를 감지하여 update 쿼리 실행
- 지연 로딩(lazy loading)

#### flush
- 영속성 컨텍스트의 변경내용을 DB에 반영하는 것. 영속성 컨텍스트를 비우진 않음.
- 직접 호출: `entityManager.flush()`
- 자동 호출: 트랜잭션 커밋 시점에 자동으로 flush 됨, JPQL 실행 시점에 자동으로 flush됨.


#### 준영속 상태로 만드는 법
- 영속성 컨텍스트가 관리하는 건 영속 상태이다. 반대로 영속성 컨텍스트의 관리를 제거한 것은 준영속 상태라 한다.
- `em.detach(member)`: 특정 엔티티만 준영속 상태로 전환 (영속성 컨텍스트가 관리하던 member 객체를 준영속 상태로 전환. member의 필드를 수정하더라도 update문이 실행되지 않음)
- `em.clear()`: 영속성 컨텍스트를 완전히 초기화
- `em.close()`: 영속성 컨텍스트를 종료
- 그 외 영속성 컨텍스트에서 객체 삭제: `em.remove(member)`

#### DDL Auto
- create : 기존 테이블 삭제 후 생성 
- create-drop: 테이블 생성 후 애플리케이션 종료 시점에 테이블 삭제 
- update: 변경분만 반영(단 삭제는 안됨)
- validate: 엔티티와 테이블 매핑이 됐는지 체크만 함. 
- none: 사용하지 않음
- 개발초기에: create/update
- 테스트 서버: update/validate
- 스테이징/운영서버: validate/none

> 운영장비에 절대 create,create-drop,update 사용하면 안됨!!!
---

## 객체와 테이블 매핑하기

- @Entity : public/protected 기본 생성자 있어야 함.
- @Table

#### 필드와 컬럼 매핑
- @Column
- @Id, @GeneratedValue
- @Enumerated : EnumType.STRING으로 작성하기
- @Lob
- @Transient

#### 기본 키 매핑
@GeneratedValue: 기본키 어떻게 생성할건지 설정
- GenerationType.IDENTITY: auto_increment
  - 인조키를 미리 알 수 없기 때문에 `em.persist()`시점에 바로 insert문 실행함.
- GenerationType.SEQUENCE: 시퀀스. 자동으로 시퀀스 생성해줌.
  - 기존에 있던 시퀀스와 매핑하려면 클래스 위에 @SequenceGenerator 작성.
  - `em.persist()` 시점에 시퀀스만 실행하고, commit 시점에 insert문 실행함.
- GenerationType.TABLE: 키 생성용 테이블을 만듦.
  - 기존 키 생성용 테이블과 매핑하려면 클래스 위에 @TalbeGerator 작성.
- GenerationType.AUTO : 디폴트

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
@Entity 
@SequenceGenerator( 
    name = "MEMBER_SEQ_GENERATOR", //필수
    sequenceName = "MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
    initialValue = 1, // DDL 생성 시에만 사용. DDL 생성할 때 시퀀스의 시작값 설정
    allocationSize = 50 // 시퀀스
    )
public class Member { 

 @Id 
 @GeneratedValue(strategy = GenerationType.SEQUENCE, 
 generator = "MEMBER_SEQ_GENERATOR") 
 private Long id;
}
```

```java
@Entity
@TableGenerator(
    name = "MEMBER_SEQ_GENERATOR",
    table = "MY_SEQUENCES",
    pkColumnValue = "MEMBER_SEQ",
    allocationSize = 1
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

}
```

- 방향
- 다중성
- 연관관계의 주인
## ⭐⭐연관관계 매핑
- 객체와 테이블의 연관관계 차이점
  - 객체는 참조를 통해 연관관계를 맺고, 테이블은 외래키를 통해 연관관계를 맺는다.


### 예제 시나리오

팀 1 - 회원 N

```java
class Member {
    private Long id;
    
    private String username;
    
    private Team team;
}
```

```java
class Team {
    private Long id;
    
    private String name;
}
```

```java
Team team = new Team();
team.setName("TeamA");

em.persist(team);

Member member = new Member();
member.setName("member1");
member.setTeam(team); // 단방향 연관관계 설정

em.persist(member);



```

객체가 외래키값을 가지고 있다면?? 객체지향이 안됨.

객체를 테이블에 맞추어 데이터 중심으로 설계하면?
-> **테이블은 외래키**로 조인해서 연관 테이블을 조회함.
**객체는 참조**를 통해서 연관 객체를 가짐
테이블과 객체 사이엔 패터다임이 불일치가 존재한다.


테이블에선 Member의 외래키 team_id 하나로 두 개의 테이블 모두 접근이 가능하다.
객체에선?? Member도 team의 정보를 가지고 있어야 하고, Team도 Member의 정보를 가지고 있어야 한다.


객체의 양방향 관계:
단방향 연관관계가 2개가 있는 것.
@OneToMany의 mappedBy??

테이블의 양방향 관계:
외래키 하나로 연관관계를 가짐.

=> 여기서 오는 딜레마 : 외래키를 뭐로 관리해야 하지? Member의 team? Team의 members?
=> 그래서 연관관계의 주인(owner)이 필요하다.

#### 양방향 관계 - 연관관계의 주인(Owner)
- 객체의 두 관계 중 하나를 주인으로 지정한다.
- owner가 테이블의 외래키를 관리한다.
- owner가 아닌 쪽은 읽기만 가능하다.
- owner가 아닌 쪽에서 mappedBy 사용해서 주인을 지정해줘야 함.

- 누구를 주인으로 정할까?
  - 외래키가 있는 곳을 주인으로 정하라. 

주인 매핑할 때 주의할 점: 값 변경할 때 owner를 기준으로 수정해야 한다.
set하는 건 한 쪽에서만 할 것. 편의 메소드 하나 만들어서 수정.


컨트롤러에서 Entity 반환하지 말고, DTO 반환하기.


#### 다대일 N:1

다대다는 실무에서 사용하면 안된다.



"다"에 외래키가 있어야 함.
그래서 다대일일 땐? "다"가 연관관계 주인이다.

양방향할 때 mppedBy 꼭 넣기. + 읽기만 가능.

#### 일대다 1:N
- 실무에서 이 구조는 거의 가져가지 않음.
- 원래 '다'에 외래키가 있는게 정상임.
- '일'이 연관관계 주인이 되는 것. 객체에서 Team이 Member를 알고싶은 것.
- => 문제: Team을 수정하면? Member도 같이 update 된다.성능 저하. 진짜 심각한건 team만 수정한거 같은데, Member 테이블에 update가 된다는 걸 인지하기 어려움.
- Member에서 Team을 갈 일이 없더라도, 객체지향을 약간 포기하고, DB에 맞춰서 유지보수하기 쉽게 설계.


@JoinColumn(name="team_id", insertable = false, updatable = false) : 읽기 전용. insert, update를 안함.

차라리 다대일 양방향을 사용하자.

#### 일대일 1:1
외래키가 어디에 있든 상관없다.
양방향할거면 연관관계 주인이 아닌 곳에 mappedBy는 반드시 적어줘야 한다.

일대일 관계에서 트레이드 오프 : 외래키를 어디에 둘까?
Member-Locker
- 추후에 하나의 회원이 여러 개의 Locker를 가질 수 있다면? 추후까지 생각하면 변경이 더 쉬운 Locker에 member_id를 가지고 있는 설계가 더 좋다.
- 반대로 요구사항이 하나의 Locker를 여러 Member가 가질 수 있다면? Member에 locker_id를 가지고 있는 설계가 더 좋다.
- 개발자 입장에서 객체지향 관점으로 본다면...?
  - Member가 locker_id 가지고 있는게 유리하다. 성능 상도 좋음. Member join 없이 하나만 조회하면 되니까 성능 상 장점도 있음.
  - 결국 트레이드 오프이다.
  - 너무 먼 미래는 생각하지 않는다. 명확한 1:1 관계일 경우에 Member에 locker_id를 넣음.

**정리**

- 주 테이블에 외래 키
- 대상 테이블에 외래 키
  - 단점: 양방향이 필요함. 프록시 기능의 한계로 항상 **즉시 로딩**됨.
#### 다대다 N:M
- 실무에서 사용하면 안된다.
- 관계형 데이터베이스에서 다대다는 가운데에 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함.



### 상속관계 매핑
- 테이블 설계 3가지 방법

1. ITEM : item_id, name, price, dtype
  - MOVIE: item_id, actor, director
  - BOOK: item_id, author
  - ALBUM: item_id, artist

2. ITEM: item_id, name, price, dtype, actor, director, author, artist




기본적으로 2번처럼 테이블 생성됨.
Item에 @Inheritance(strategy = InheritanceType.JOINED) 붙이면? 1번처럼 생성됨.

@DiscriminatorColumn 넣으면 자동으로 dtype이 생김.
