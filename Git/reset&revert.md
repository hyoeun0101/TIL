## 1. reset

![reset](https://user-images.githubusercontent.com/96059261/203552136-09f6c31e-1dbe-46d2-8a08-77ef5fdc143f.PNG)

: 원하는 커밋 상태로 되돌리기. 원하는 커밋 이후의 커밋들은 아예 삭제됨.

```
git reset --hard 커밋해시
```

: 원하는 커밋 상태로 감. 즉 커밋하지 않은 수정 사항들이 다 삭제됨. 주의할 점은 생성된 파일은 그대로 남아있음.

```
git reset --mixed
```

: hard는 원하는 커밋 상태로 아예 돌아가서 그 이후 커밋 내용들은 완전히 삭제하는 것이라면, mixed는 커밋은 지우되 커밋의 내용들은 남아 있음. 커밋의 파일 묶음을 수정하고 싶다면 mixed로 돌아간 후 커밋을 다시 작성하면됨.  
mixed는 working directory에 있는 상태이고, soft는 스테이징까지 올라가있는 것임.

## 2. revert

![revert](https://user-images.githubusercontent.com/96059261/203552527-ca0fdaca-b745-4c2f-81ca-412911d86b08.PNG)  
: 원하는 커밋 상태로 되돌리기. A를 revert 한다면 A에서 실행한 모든 걸 취소해버림. 지금 상태는 `Add team Cheetas` 상태임. 협업 시 공유된 파일을 함부로 reset 해버리면 문제가 발생함.

```
git revert 되돌릴커밋해시
```

: 커밋해시A 상태를 반대로 동작함. 추가했다면 삭제, 삭제했다면 추가

- 하지만 A에서 건드린 파일을 A 이후의 커밋에서 건드렸다면 충돌 발생. 직접 충돌 해결 후

```
git revert --continue
```

하고 커밋해주기

- 커밋없이 revert 하기

```
git revert  --no-commit  되돌릴커밋해시A
```
