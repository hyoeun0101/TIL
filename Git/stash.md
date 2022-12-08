## stash

하고있던 작업 다른 곳으로 치워두기.

```
git stash
또는
git stash save
```

: 내가 작업하고 있던 커밋하지 않은 내용들을 다른 공간에 치워두고, 상태는 마지막 커밋 상태로 돌아간다.  
stash한 건 어디서든 다시 가져올 수 있다. 다른 브랜치에서 stash해둔 걸 메인 브랜치로 가져올 수도 있다.

```
git stash pop
```

: stash 가져오기

```
git stash -p
```

: `-p`옵션은 변경사항 하나하나 보면서 stash할 것인지 체크하는 옵션이다. 이 옵션을 사용하면 한 파일 내에서도 변경사항을 선택적으로 stash할 수 있다.

```
git stash -m "스테시이름"
```

: 스테시에도 메세지를 붙여줄 수 있다.

```
git stash list
```

: 스태시 리스트 보기

```
git stash apply stash@{1}[stash이름]
```

: 스태시 선택하여 적용시키기

```
git stash drop stash@{1}[stash이름]
```

: 스태시 삭제하기

- `git stash pop`은 마지막 항목 적용 및 삭제(apply+drop)를 하는 것이다.

```
git stash branch 브랜치명
```

: `git stash`후에 사용. 스태시한 것을 새 브랜치 만들어서 그 브랜치에서 pop을 한다. stash한 내용과 충돌이 날 수 있기 때문에 새브랜치를 만들어서 거기서 충돌해결 후 merge하면 된다.

```
git stash clear
```

: 스태시 모두 비우기
