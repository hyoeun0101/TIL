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

## ⭐⭐연관관계 매핑
- 객체와 테이블 연관관계의 차이
- 객체의 참조 VS 테이블의 외래키

- 방향
- 다중성
- 연관관계의 주인

"객체지향 설계"를 알면 orm의 필요성이 느껴진다?

### 예제 시나리오
- 회원 - 팀

객체가 외래키값을 가지고 있다면??

객체를 테이블에 맞추어 데이터 중심으로 설계하면?
-> **테이블은 외래키**로 조인해서 연관 테이블을 조회함.
**객체는 참조**를 통해서 연관 객체를 가짐
테이블과 객체 사이엔 패터다임이 불일치가 존재한다.


객체지향 모델링??
Member
