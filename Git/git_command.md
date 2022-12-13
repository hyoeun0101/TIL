## config

### git 최초 설정

```
git config --global user.name "이름"
git config --global user.email "이메일"
```

### 기본 브랜치명 변경

```
git config --global init.defaultBranch main
```

![ss](https://user-images.githubusercontent.com/96059261/204958793-f2aff42f-45c2-4c93-859f-20bbecccc7e7.png)

- working directory
  - untracked: 새로 생성한 파일, gitignore된 파일
  - tracked: 내역 있는 파일

## HEAD

: 브랜치의 마지막 커밋을 의미한다.
`HEAD^^ 또는 HEAD~~`는 마지막 커밋의 전전 커밋을 의미한다. 커밋 해시를 몰라도 HEAD를 이용해서 보다 편하게 커밋간 이동이 가능하다.

```
git checkout HEAD^
```

: 최신 커밋의 전 커밋으로 이동

```
git checkout -
```

: HEAD를 통해 커밋을 여러 번 이동을 했다면 git checkout HEAD^^와는 정반대로 전에 있었던 커밋으로 이동한다.

switch은 해당 브랜치의 최신 커밋(HEAD)으로 이동하는 것이다.

- HEAD로 이동을 하게 되면 익명의 브랜치가 생성된다. HEAD로 이동 후 `git switch -c 새브랜치명`을 치면 그 이동한 커밋으로부터 새로운 브랜치를 생성하게 된다.

## 커밋 관련 command

```
git add -p
```

`-p`옵션 : 수정 사항 하나하나 확인하며 스테이징할지 안할지 정함.

### 커밋 메세지 수정

```
git commit --amend
git commit --amend -m "commit message"
```

### add 취소하기- unstaging

```jsx
git restore --staged 파일명
```

### 커밋 수정하기 - 최근 커밋에 방금 수정한 파일까지 포함시키고 싶을 때

1. A파일 수정
2. git add 로 스테이징
3. git commit --amend 하기
   -> A파일의 수정까지 커밋에 들어감.

### 과거의 커밋을 수정,삭제,병합하기

1. 우선 수정할 커밋의 전 커밋으로 rebase

- `git rebase -i 전커밋해시`

2. pick을 내가 원하는 것으로 수정
   |명령어|설명|
   |-----|----|
   |p,pick|커밋 그대로 두기|
   |r, reword|커밋 메세지 변경|
   |e,edit|수정을 위해 정지|
   |d, drop|커밋 삭제|
   |s, squash|이전 커밋에 합치기|
