- AI Agentic Coding Tool
    - 이걸 이해하는게 중요함.
    - 얘가 어떻게 동작하는지 알아야 어떻게 대답하는지 알 수 있고, 어떻게 말해야 하는지 알 수 있음.

### Claude Code 작동방식

에이전트 루프는 두 가지 구성 요소로 구동된다.
추론 모델과 도구.


### 해야할 것
- Claude Code의 기본 도구(Tool), 에이전트(Agent)가 뭐가 있고, 어느 경우 어떤 것을 사용해야할지 파악.
- Official Plugin: 검증된 플러그인만 사용할 것. 그 외엔 필요한 게 있으면 직접 플러그인 만들어서 사용.


### Claude가 가지고 있는 도구
- Bash 도구
- Read 도구
- Explore 도구 (에이전트)
- 등...

ex) Explore없이 Grep, Grob 등으로 동작한다면? 멀티로 띄워놓고 싶다면?
- Explore 에이전트를 10개씩 띄워서 작업해줘.


```text

/plugin
```
### 추천 플러그인
- ralph-loop
- skill-creator
- explanatory-output-style
- plugin-dev

### SKILL
- 프롬프트 재사용하게 만들어줌.

```text
/skill-creator 스프링 부트 프로젝트를 생성하는 스킬을 만들어줘.
SpringInitializ를 이용해서 진행해줘.
SpringBoot 버전 사용가능한 것을 보여주고 내가 선택할 수 있게 해줘. AskUserQuestion 도구를 사용해.
자바 버전도 나에게 물어봐줘.
Gradle Kotiln DSL을 사용해.
Artifact는 현재 폴더 이름을 디폴트로 해줘. 수정할지 물어봐줘.
Package는 toby.ai.[artifact]을 디폴트로 하고, 수정할지 물어봐줘.
Dependency는 webmvc, jpa/h2, lombok은 필수로 설정해줘.
SpringInitializr에서 dependency 목록을 가져오고, group 별로 어떤 것을 선택할지 물어봐줘. 다중선택이 가능해야 해.


사용자 레벨 스킬로 설치해줘.
```

- 시스템 프롬프트 (클로드 팀이 개발한 것)
- 대화 히스토리
- 도구 목록
- 최신 프롬프트 


`.claude` : 유저 레벨. 매번 지켜야 하는 것. 모든 프로젝트에 해당. 팀원 공유 가능.

```text

/reload-plugins
```

에이전트 만들기? 에이전트도 스킬이랑 비슷하긴 함.
별도의 세션에서 동작함.
코드 품질을 평가하는 애.
스킬 vs 에이전트로 만들기.

### 에이전트 만들기.

```text
/agents
```

- 화면을 분할해서 동시에 에이전트 돌림.
```text
tmux new -s spring
```

```text

CreateTeam을 이용해서 TDD를 수행하는 agent team을 구성해줘.
- Red: 실패하는 테스트 작성. 테스트를 실행해서 실패하는지 확인해줘.
- Green: 실패하는 테스트를 성공시키기 위한 코드만 작성해줘.
- Refactor : 작성된 코드를 리팩터링해줘.
Team lead는 주어진 개발 요구사항을 받아서 task를 분할해서 작성하고 팀과 함께 TDD를 진행시켜줘.
```
