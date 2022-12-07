# 매타 애너테이션

- @Target : 적용대상 지정
  - `@Target({TYPE, FIELD, METHOD,LOCAL_VARIABLE})`
- @Documented: 애너테이션 유지기간 지정
  - SOURCE : 소스 파일에만 존재
    - EX) 오버라이드 `@Retention(RetentionPolicy.SOURCE)`
  - RUNTIME: 클래스 파일에도 존재, 실행 시 사용
    - EX) FunctionalInterface `@Retention(RetentionPolicy.RUNTIME`
  - CLASS : 클래스 파일에도 존재, 실행 시 사용불가
- @Inherited
- @Retention
- @Repeatable
