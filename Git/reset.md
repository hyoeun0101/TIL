## 1. reset
```
git reset --hard commitHead
```
: 원하는 커밋 상태로 되돌리기. 원하는 커밋 이후의 커밋들은 아예 삭제됨.

## 2. revert
```
git revert commitHash
```
: 원하는 커밋 상태로 되돌리기. A를 revert 한다면 A에서 실행한 모든 걸 취소해버림.   
  만약 A에서 건드린 파일을 A 이후의 커밋에서 건드렸다면 충돌 발생.
  
충돌 해결 후
```
git revert --continue
```
### 커밋없이 revert 하기
```
git revert  --no-commit  08a4b562
```

