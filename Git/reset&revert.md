## 1. reset

![reset](https://user-images.githubusercontent.com/96059261/203552136-09f6c31e-1dbe-46d2-8a08-77ef5fdc143f.PNG)

: 원하는 커밋 상태로 되돌리기. 원하는 커밋 이후의 커밋들은 아예 삭제됨.

```
git reset --hard 돌아갈커밋해시
```

:돌아갈 커밋해시 상태로 감

```
git reset --hard
```

: 마지막 커밋 상태로 감. 즉 커밋하지 않은 수정 사항들이 다 삭제됨. 하지만 생성된 파일은 그대로 남아있음.

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
