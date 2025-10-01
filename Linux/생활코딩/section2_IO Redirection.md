### 🔴 Unix의 IO 종류

- Mean of input
    - Program arguments
    - Environment variables
    - Standard input
- Mean of output
    - Return status code
    - Standard out
    - Standard error


### 🔴 Output

```shell

### 출력할 땐 > 기호를 사용(standard out)
### ls -al 결과를 my_file.txt 파일로 출력
$ ls -al > my_file.txt
$ ls -al 1> my_file.txt

### 오류 결과를 error.txt로 출력(standard error)
rm my_file.txt 2> error.txt


```
- `명령 1> 파일명` : 1은 생략함. 기본 출력을 의미함. 명령의 결과를 파일로 출력.
- `명령 2> 파일명` : 명령의 오류 결과를 파일로 출력.



### 🔴 Input
```shell

### 입력값을 받아 그대로 출력
$ cat hello~
# hello 출력.

### hello.txt의 내용을 입력값으로 받음.
$ cat < hello.txt

### 해당 파일을 명령의 입력으로.
$ sort < /etc/passwd

```

### 🔴응용

```shell

### linux.txt 파일을 한 줄만 출력.
$ head -n1 linux.txt
$ head -n1 < linux.txt

### 한 줄만 출력한 결과를 one.txt로 출력.
$ head -n1 < linux.txt > one.txt
```

### 🔴 append

```shell

### ls -al 결과를 result.txt 생성 및 출력
$ ls -al > result.txt

### ls -al 결과를 result.txt 기존 내용에 붙이기.
$ ls -al >> result.txt
```

### 🔴 기타

```shell

### /dev/null 쓰레기통임.
$ ls -al > /dev/null
```
