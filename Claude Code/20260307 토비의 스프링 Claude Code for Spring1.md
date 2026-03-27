### 리마인더 앱 개발하기

- Backend: Spring Boot 4.0.x
- Frontend: Next.js
- Mobile: React native


### 1. 프로젝트 생성하기

```text
Spring Initializer를 이용해서 Srping Boot 4 최신버전, JPA/H2/Lombok을 사용하는 프로젝트 생성해줘
- Gradle Kotlin DSL
- package: eunoo.ai.eunooreminder
- project name: eunooreminder
```

- Spring Boot 버전 명시. Spring Intializer 사용 명시.

### SKILL
- 반복할 때 skill로 만듦.


---

### 2. 바이브 코딩

- `prompt -> 개발 `: 자연어로 요구사항 입력하면 바로 코드 생성
- `prompt -> plan -> 개발` : 개발 계획을 먼저 만들고 코드 생성
- `prompt -> prd.md -> 개발` : prd(제품 요구사항 문서)를 만들고 코드 생성
- `prompt -> spec(기술명세) -> plan(개발 계획) -> task(구현 작업 분해) -> 개발(SDD)`

- 핵심은 이번 개발은 어느 범위인가를 파악해야 한다는 것이다.
- 우리는 plan mode로 prd.md를 먼저 만들어보자.

```text
Apple Reminder App의 Web 버전을 개발하고 싶어.
- backend: 지금 생성된 Spring Boot, JPA/H2, API 서비스로 개발
- frontend: NextJs lastest로 개발
우선 이 개발에 대한 PRD를 prd.md에 정리해줘. 내가 리뷰할게.
```


```text
UI/UX는 apple reminder app과 최대한 유사하게
```


```text
prd.md를 spec.md로 변경해줘.
spec.md의 내용을 단순한 것부터 점점 기능을 추가하는 방식으로 개발하도록 phase를 구분해서 plan.md에 개발 계획을 넣어줘. 
기술에 대한 내용도 같이 넣어줘.
```


```text
plan.md를 구현하는 세부 작업을 정리해서 tasks.md 리스트를 만들고, 체크 가능하도록 해줘.
```


```text
ReminderList 도메인 엔티티를 생성해줘. 패키지명은 domain으로 만들어줘.
```

```text
ReminderList에 대한 Test 만들고, 생성자와 update, date 정보 자동 등록에 대해서 테스트해줘.
```

```text
@PrePersist를 이용하지 않고 생성 로직에서 createdAt 등을 넣도록 해줘.
domain 엔티티에 대한 테스트에서는 JPA 사용하지마. unittest여야 해.
```

```text
앞으로 기능을 넣거나 수정할 때 이를 검증할 테스트도 같이 만들어줘.
CLAUDE.md에 지켜야 할 코딩 관례를 남겨줘.
```
- CLAUDE.md 만들기
  - AI가 원래 알아서 잘해주는 건 작성하지 않기.


```text
앞으로 ServiceTest는 @SpringBootTest를 이용한 통합 테스트로 만들어줘. Mock test 사용하지마.
```

```text
ReminderListService는 인터페이스를 분리해서 이를 구현하는 방식으로 만들어줘.
Service 계층의 인터페이스는 아래 ports/inp 패키지 안에 저장해.
구현 클래스의 기본 네이밍 룰은 앞에 Default를 붙여줘.
앞으로 개발하면서 내가 요구했던 내용은 코딩 가이드로 CLAUDE.md에 넣어줘.

```

```text
ReminderList API를 Controller에서 구현해줘.
OpenAPI Spec을 따라서 구현하고, 그 내용은 openapi.yml 파일에 표준 형식으로 저장해줘.
```

```text
이제 @tasks.md의 phase1 기능은 전체를 개발하고, task 하나가 완료될 때마다 tasks.md 파일에 check를 해줘.
```

- 리모트 컨트롤 연결해두면 폰에서 보기 가능.


```text
/statusline 유지 홈부터 상대 폴더, 모델, Git 브랜치, Context 사용량, 시계
상대 경로가 길면 폴더 이름 중간은 한글자씩만
```

- clause usage 확인할 수 있음.

```text
phase 3까지 frontend 화면 개발해줘.
```

```text
다양한 영역에서 코드 리뷰해줘.
```

```text
코드 리뷰한 결과 issue를 task 형태로 정리해서 fix.md 에 체크리스트 형태로 넣어줘.
```

```text
@fix.md의 이슈를 TDD 방식으로 수정해줘. 항상 이슈를 검증하는 실패 테스트를 만들고, 그리고 그걸 수정하는 방식으로 진행해.
task 해결되면 체크해주고, 각 번호 내의 작업을 완료하면 commit 해줘.
```
---

## 테스트 애노테이션
### @ExtendWith(MockitoExtension.class)
- 순수 단위 테스트
- Spring 컨테이너 미사용
- Service 등 클래스 단독 테스트
- 속도는 가장 빠름
- 즉 Spring 없이 로직만 검증

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository; // Mock 객체 주입

    @InjectMocks
    UserService userService; //테스트 대상

    @Test
    void 사용자_이름_조회() {
        given(userRepository.findNameById(1L)).willReturn("kim");

        String name = userService.findName(1L);

        assertThat(name).isEqualTo("kim");
    }
}
```

### @WebMvcTest
- 웹 계층 슬라이스 테스트
- Spring 컨테이너 사용
- Controller의 REST API 테스트
- 속도는 빠름.
- 요청/응답, validation, JSON, 상태 코드 등 검증

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class) //테스트 대상
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean //Mock 객체 주입
    UserService userService;

    @Test
    void 사용자_조회_API() throws Exception {
        given(userService.findName(1L)).willReturn("kim");

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("kim"));
    }
}
```
### @SpringBootTest + @AutoConfigureMockMvc
- 전체 통합 테스트
- Spring 컨테이너 사용
- 실제 어플리케이션 흐름 테스트
- 속도는 가장 느림.
- Controller-Service-Repository 설정까지 함께 검증

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 사용자_조회_통합테스트() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }
}
```