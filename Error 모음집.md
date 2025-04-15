### mvn package 할 때 발생
[에러 메세지]
```
There are test failures.

Please refer to /Users/u/Desktop/intellijWorkspace/demo-rest-api/target/surefire-reports for the individual test results.
Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
```
[원인]
- package는 모든 테스트가 통과되어야 할 수 있음. 나의 경우, Spring Security 의존성을 추가만 한 상태였기 때문에 기존 update 테스트가 다 실패했음.