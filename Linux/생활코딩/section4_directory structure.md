## 🔴 파티션?
- 하드디스크를 논리적으로 나눈 구역. 파티션 별로 파일 시스템을 만듦.
-  윈도우에선 각 파티션마다 드라이브로 지정. ex) C, D 드라이브마다 루트 디렉터리가 있음.
- 리눅스에서는 오직 1개의 루트 디렉터리 존재.
  - 리눅스는 하드디스크나 주변 장치를 파일로 취급함.
  - 저장 장치를 사용하기 위해선 해당 저장 장치 이름을 특정 디렉터리에 마운트시켜야 함.


## 🔴 directory structure

![img_1.png](img_1.png)

| 위치    | 설명                                           |
|-------|----------------------------------------------|
| /     | 모든 파일과 디렉터리의 최상위 디렉터리                        |
| /bin  | 기본적인 명령어가 저장됨.                               |
| /sbin | 부팅 과정이나 시스템 관리에 필요한 명령어가 저장됨.                |
| /etc  | 시스템의 환경 설정 파일을 가짐.                           |
| /dev  | 장치를 접근하는데 사용되는 파일을 가짐.                       |
| /proc | 커널이 사용하는 가상의 파일을 가짐.                         |
| /var  | 가변 자료 저장하는 디렉터리                              |
| /tmp  |                                              |
| /usr  |                                              |
| /home | 사용자 계정 홈 디렉터리가 만들어짐. /home/khe   /home/gusdn |
| /boot |                                              |



## 🔴 파일 찾는 법

### 🟡 locate, find
```shell

# 데이터베이스에서 파일을 찾음.
$ locate *.log

### 이 명령어로 DB를 하루에 한 번씩 정돈해야 함.
$ sudo updatedb

### 루트 디렉터리에서 이름이 .log인 파일 찾기
$ find / -name *.log


### 홈 디렉터리에서 이름이 .log인 파일 찾기
$ find ~ -name *.log

```

- locate는 데이터베이스(mlocate)에서 파일을 찾는다. 
- find 사용법은 다양하니 서칭 후 실습해보기.


### 🟡 whereis, $PATH

```shell
### 명령어 위치 알려줌.
$ whereis ls
# ls: /bin/ls


$ echo $PATH
# /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:
# /usr/local/games:/snap/bin:/usr/lib/jvm/java-1.11.0-openjdk-amd64/bin:/home/tomcat/bin
```


- 리눅스에서 사용하는 환경 변수 : $PATH
  - ls를 명령어를 입력 => 리눅스는 $PATH 경로에 있는 디렉터리를 통해 ls 명령어를 찾고 실행시킨다.
  - ls 입력만으로 실행 가능한 건 $PATH에 /bin이 있기 때문. 없으면 `/bin/ls` 이렇게 입력해야 함.
  - 즉 명령 이름만으로 실행하려면 경로가 $PATH에 있어야 한다.
  - 현재 디렉터리(.)은 보통 $PATH에 포함되어 있지 않음. 따라서 현재 디렉터리에 있는 파일을 실행하려면 `./` 이렇게 작성해야 함.





