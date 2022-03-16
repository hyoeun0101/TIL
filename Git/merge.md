## 1. merge
: 메인 브랜치와 다른 브랜치 합치기   
main 브랜치에서
```
git merge branchname
```

## 2. rebase
: 메인 브랜치에 다른 브랜치의 커밋을 가져와 한 줄로 붙이기      
팀원들간에 공유된 커밋에 대해서는 rebase하면 안된다.      

test 브랜치에서  
```
git rebase main
git switch main
git merge test
```

## 3. 충돌 해결
### merge/rebase 중단
```
git merge --abort
git rebase --abort
```

### 충돌 해결 후
```
git add .
git commit -m ""
### rebase 경우   
만약 합칠 커밋이 여러개이면 continue를 해서 일일히 합쳐줘야함.
```
git add .
git rebase --continue
```
