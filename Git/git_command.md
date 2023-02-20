![ss](https://user-images.githubusercontent.com/96059261/204958793-f2aff42f-45c2-4c93-859f-20bbecccc7e7.png)

- working directory
  - untracked: 새로 생성한 파일, gitignore된 파일
  - tracked: 내역 있는 파일

---

## 🍎HEAD

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

- switch은 해당 브랜치의 최신 커밋(HEAD)으로 이동하는 것이다.
- HEAD로 이동을 하게 되면 익명의 브랜치가 생성된다. HEAD로 이동 후 `git switch -c 새브랜치명`을 치면 그 이동한 커밋으로부터 새로운 브랜치를 생성하게 된다.

---

## 🍎 커밋 관련 command

```
git add -p
```

`-p`옵션 : 수정 사항 하나하나 확인하며 스테이징할지 안할지 정함.

### \*\*커밋 메세지 수정

```
git commit --amend
git commit --amend -m "commit message"
```

### \*\* 커밋 수정하기 - 최근 커밋에 방금 수정한 파일까지 포함시키고 싶을 때

1. A파일 수정을 했다.
2. git add 로 스테이징하기.
3. git commit --amend 하기
   -> A파일의 수정까지 커밋에 들어감.

### \*\* 과거의 커밋을 수정,삭제,병합하기

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
1. 과거의 커밋 메세지 변경하기

- `git rebase -i 전커밋해시`
- 커밋 메세지 변경할 커밋 `r`로 변경
- 커밋 메세지 변경

2. 과거의 커밋 두개 합치기

- `git rebase -i 전커밋해시`
- 커밋1과 커밋2를 합칠 때 커밋2를 `s`로 변경하여 앞의 커밋과 합치기
- 커밋 메세지는 하나만 작성한 후 저장

3. 과거의 커밋 두 개 커밋으로 나누기

- `git rebase -i 전커밋해시`
- 나눌 커밋 `e`로 변경
- `git reset HEAD^` 한 단계 뒤로 가서 커밋 reset해주기, `git status`로 확인.
- 이제 각각의 커밋 만들어주기
- `git rebase --continue`하면 끝.

---

## 🍎restore

```
git restore 파일명
```

: 깃이 관리하는 파일 중에서 add 하지않은 내용 없애기. 즉 commit 상태로 내용 돌림. 커밋 후 수정사항을 삭제하는 것.

```jsx
git restore --staged 파일명
```

: add 취소하기, 언스테이징하기

```

git restore --source=(해드 또는 커밋해시) 파일명
```

: 파일을 특정 커밋의 상태로 되돌리기

---

## 🍎clean - git에서 관리하지 않는 파일 삭제하기

| 옵션 | 설명                            |
| ---- | ------------------------------- |
| -n   | 삭제될 파일 보여주기            |
| -i   | 인터렉티브 모드 시작            |
| -d   | 폴더 포함                       |
| -f   | 강제로 바로 지워버리기          |
| -x   | .gitignore에 등록된 파일도 삭제 |

```
git clean -df
```

: 깃이 관리하지 않는 파일 삭제하기

---

## 🍎cherry-pick

원하는 커밋만 복사해서 가져오기.

```
git cherry-pick (체리의 해시)
```

어느 브랜치에 있던 상관없이 원하는 커밋을 복사하여 생성한다.

---

![branches](https://user-images.githubusercontent.com/96059261/210207464-2871bb23-3dca-484d-a8bc-8956c8533e3e.png)

## 다른 브랜치에서 파생된 브랜치만 가져오기

```
git rebase --onto (도착 브랜치) (출발 브랜치) (이동할 브랜치)
```

fruit에서 파생된 citrus 브랜치를 메인브랜치로 옮겨붙이기

1. `git rebase --onto main fruit citrus`
   - 메인에서 입력
   - 입력 후 citrus 브랜치로 이동하고 메인에 citrus 브랜치가 rebase 됨.
2. `git switch main`
   - 1번하면 citrus 브랜치로 이동되기 때문에 다시 main으로 이동하기
3. `git merge citrus`

---

## 🍎log 옵션

- (-갯수) : 최근 n개 커밋보기
- -stat : 통계와 함께 보기 (--shortstat : 더 간략히 보기)
- --oneline : 한 줄로 보기
- -S (검색어) : 변경 사항 내단어 검색
- --grep (검색어) : 커밋 메세지로 검색

---

## 🍎diff

```
git diff
```

: 워킹 디렉토리의 변경 사항 확인

- `git diff --name-only` : 변경된 파일명만 확인
- `git diff --staged` : 스테이징의 변경 사항 보기
- `git diff 커밋1 커밋2` : 커밋끼리 변경 사항 비교하기
- `git diff 브랜치1 브랜치2` : 브랜치끼리 변경 사항 비교하기
- `git diff --name-only HEAD HEAD^^` : 전전 커밋부터 변경된 파일 보기

## 🍎blame

: 작성자 확인하기

```
git blame 파일명
```

```
git blame -L (시작줄,끝줄) (파일명)
```

## 🍎bisect

: 이진 탐색 알고리즘으로 문제의 발생 시점 찾아내기

1. 에러가 발생한 시점에서 탐색 시작하기

```
git bisect start
```

2. 현재 커밋이 오류발생 지점임을 표시하기

```
git bisect bad
```

3. 오류발생 의심 지점으로 이동하기

```
git checkout (커밋해시)
```

4. 오류 발생하지 않을 시 양호함을 표시하기

```
git bisect good
```

- 원인을 찾을 때까지 반복

5. 이진 탐색 종료

```
git bisect reset
```
