# push 할 것이 있을 때 pull 하는 두가지 방법

-일단 내 로컬에 있는 코드 모두 commit한 상태

- merge 방식 : origin/main 과 로컬 main이 merge된다. 즉 두 가지가 합쳐져서 하나의 커밋이 생긴다.

```jsx
git pull --no-rebase
```

- rebase 방식 : orgin/main에 로컬 main의 커밋이 합쳐지는 방식. origin/main 의 줄에 로컬 main의 커밋이 한줄로 붙음. (협업시 사용ok)

```jsx
git pull --rebase
```
