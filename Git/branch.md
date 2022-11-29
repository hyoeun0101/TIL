❗ 커밋이 하나도 없으면 브랜치 생성이 안된다 ❗

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

### 원격 브랜치를 로컬로 가져오기

```
git checkout -t origin/브랜치명
```

```
git fetch
git switch -t origin/브랜치명
```

- -t : 원격 브랜치와 같은 이름의 로컬 브랜치 생성하고 switch 한다.

### 원격 브랜치 삭제

```
git push origin -d 브랜치명
```
