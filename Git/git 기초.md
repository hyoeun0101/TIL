### 기본 브랜치명 변경
```
git config --global init.defaultBranch main
```

# .gitignore 사용법
```
# 모든 file.c
file.c

# 최상위 폴더의 file.c
/file.c

# 모든 .c 확장자 파일
.c

# .c 확장자이지만 무시하지 않을 파일
!not_ignore_this.c

#파일 또는 폴더 내용
파일명

# 폴더와 그 내용
logs/

# logs 폴더 안, 그안의 다른 폴더 안의 debug.log
logs/**/debug.log
```
### 브랜치 생성
```
git bracn test
```
### 브랜치 생성 동시에 이동
```
git switch -c test
```
### 브랜치 삭제
```
git branch -d test
```
### 브랜치 이름 변경
```
git branch test change_test
```
### 여러 브랜치 내역 한꺼번에 보기
```
git log --all -decorate --oneline --graph
```

