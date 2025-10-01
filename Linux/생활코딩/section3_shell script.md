shell vs kernel

## 🔴 Shell
- 명령어 해석기. 사용자와 커널 사이에서 명령어를 해석해서 처리함.
- 반복적으로 수행되는 작업을 shell script로 작성하여 처리함.
- 텍스트 모드로 로그인하면 기본 shell이 주어짐.

### shell의 종류
- bash : 많은 리눅스 배포판에서 기본 shell로 사용.
- zsh : bash보다 좀 더 편리한 기능 제공.

- bash와 zsh 사용법에 차이점이 존재.


```shell
### 현재 사용 중인 shell 확인하기.
$ echo $0
# -bash
```
 

## 🔴 Shell Script
- `#!`로 시작.


- 예제) 현재 디렉터리에 bak 만들고, .log로 끝나는 파일을 bak로 복사.
```shell

### backup이라는 파일 만들기.
$ nano backup

##### backup 파일의 내용 #########################################################

#!/bin/bash
if ! [ -d bak ]; then ### bak 디렉토리가 없다면 bak를 만들어라.
  mkdir bak
fi 
cp *.log bak ### .log로 끝나는 파일을 bak로 복사



##################################################################################

### backup 파일에 대한 권한 설정
$ chmod +x backup

### backup 파일 실행
$ ./backup

```
