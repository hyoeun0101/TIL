### 주의할점❗
- 커밋이 하나도 없으면 브랜치 생성이 안된다.

### 브랜치 생성 동시에 이동

```
git switch -c 브랜치명
```

### 브랜치 삭제

```
git branch -d 브랜치명
```

### 브랜치 이름 변경

```
git branch -m "기존 브랜치명" "새 브랜치명"
```

### 기본 브랜치 명을 main으로

```
git branch -M main
```

### 터미널에서 보기

```
git log --all -decorate --oneline --graph
```

### 원격브랜치를 로컬로 가져오기

1. git fetch : 원격 내용 가져오기
2. git branch -a : 원격브랜치를 로컬에 가져온 걸 확인
3. git switch -t origin/브랜치명
   - -t : 원격브랜치명과 같은 브랜치 생성 후 이동, 로컬 브랜치 생성과 동시에 원격 브랜치와 로컬 브랜치를 연결시킴.

### 원격 브랜치 삭제

```
git push origin -d 원격브랜치명
```
