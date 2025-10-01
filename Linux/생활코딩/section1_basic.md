### 🔴 directory & file
```shell

### 현재 위치 조회
$ pwd
# /home/hello


### 파일 생성
$ touch empty_file.txt


### 디렉터리 생성
$ mkdir my_dir

### 부모 디렉터리까지 생성
$ mkdir -p dir1/dir2/dir3



### 파일, 디렉터리 출력
$ ls 
# empty_file.txt   my_dir


### 모든 파일, 디렉터리(숨김 파일까지) 출력
$ ls -a


### 구체적으로 출력
$ ls -l
# -rw-rw-r-- 1 ubuntu    0 Nov 25 15:12 empty_file.txt
# drwxrwxr-x 2 ubuntu 4096 Nov 25 15:10 my_dir
# -> d로 시작하면 디렉토리임.


### 구체적 + 모두 출력
$ ls -al

### 파일 삭제
$ rm empty_file.txt

### 디렉터리 삭제 
$ rm -r my_dir
# -r은 디렉터리 안의 파일,디렉터리 모두 삭제.

```
- 파일이 .으로 시작하는 건 숨김 파일을 의미함.
- 상대 경로?
  - 내 위치를 기준으로 다른 디렉터리 위치를 표현.
  - `cd ..` : 부모 디렉터리로 이동.
- 절대 경로?
  - `/home/egoing` 이런 식의 구체적인 경로.

### 🔴 --help와 man
```shell

### 명령어 사용법 출력
$ rm --help
$ pwd --help
$ ps --help

### 메뉴얼 페이지 보기.
$ man mkdir

```

- 출력 내용에서 검색하기
  - /검색어
  - n : 이동
  - q : 나가기


### 🔴 sudo
- sudo = super user do
- 관리자 권한으로 실행함.
```shell

### 관리자 권한으로 ls 실행
$ sudo ls
```


### 🔴 nano
- unix 계열에서 파일 편집할 수 있는건 vi, nano이다.
- vi보단 nano가 더 쉬움.


```shell

$ nano
```