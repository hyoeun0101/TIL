## 🍎config

```
git config --global user.name "이름"
git config --global user.email "이메일"
```

: git 최초 설정 시 필요

```
git config --global init.defaultBranch main
```

: 기본 브랜치명 변경(master-> main)

```
git config (--global) --list
```

: global은 컴퓨터 전체를 의미한다.

```
git config (global) -e
```

: config 설정 보기& 변경하기

```
git config --global core.editor "code --wait"
```

: 기본 에디터 vim을 vscode로 변경하기, 취소하려면 파일의 editor=code --wait 부분을 삭제하면 됨.

```
git config --global core.autocrlf true
```

: 협업 시 맥과 윈도우에서 줄바꿈에 대한 차이가 있어서 문제가 발생하는데 그때 줄바꿈 호환 문제를 해결하는 명령어이다.  
맥은 true 대신 input

```
git config pull.rebase false
git config pull.rebase true
```

: pull할 때 기본전략 merge인지 rebase인지 설정. false면 merge, true면 rebase

```
git config --global push.default current
```

: 푸시할 때 로컬과 동일한 브랜치명으로 푸시

```
git config --global alias.(단축키) "명령어"
```

: 단축키 만들기.  
ex) git config --global alias.cam "commit -am"
