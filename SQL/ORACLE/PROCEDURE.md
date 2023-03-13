## 🍎 프로시저

- PL/SQL(Procedural Language extention to SQL)
  https://goddaehee.tistory.com/99

## 🍎 프로시저 생성

## 🍎 프로시저 변수

### 레퍼런스 변수

###

### %ROWTYPE, %TYPE

- `변수명  테이블명%ROWTYPE`
  - 테이블명을 변수로 선언한 것이다.
  - 변수를 사용할 때때 `변수.해당 테이블의 컬럼명` 이렇게 사용하면 된다.
- `변수명  테이블명.컬럼명%TYPE`

  - 테이블 컬럼을 변수로 선언한 것이다.
  - 해당 변수는 지정한 테이블 컬럼의 데이터 타입, 크기와 동일하다.

  ## PL/SQL cursor

  - cursor ? 쿼리 결과를 가르키는 포인터이다.

  ### implicit cursor
