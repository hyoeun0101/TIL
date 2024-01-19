### 시퀀스란?
- 유니크한 값을 생성해주는 오라클 객체.
- 주로 PK 값을 생성할 때 사용한다.

```sql
CREATE SEQUENCE TEST_SEQ
    START WITH 1000 -- 시퀀스 시작값
    INCREMENT BY 1 -- 증가값
    MAXVALUE 999 -- 최대값
    CYCLE -- 최대값 도달 시 순환 여부
    NOCACHE -- 메모리에 시퀀스 값을 미리 할당할지 여부
    NOORDER; -- 시퀀스 값을 순차적으로 모두 채울지, 건너뛸 수 있을지 여부
```
