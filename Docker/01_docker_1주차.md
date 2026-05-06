## 학습 목표
- 1주차 : Docker가 무엇인지, Docker와 친해지는 과정
- 2주차 : Docker를 기반으로 Application을 띄우는 과정
- 3주차 : Docker를 통해서 실제 서비스를 띄우는 과정



## 1. Docker가 없었던 세상
- 실제 서비스를 운영할 땐 **서비스를 제공하기 위한 서버**와 **서버 운영 작업**이 필요하다.
- 서버에서 코드를 실행하기 위해 필요한 작업 : OS 설치, 실행환경 세팅, 어플리케이션 코드 다운로드(또는 빌드), 라이브러리 다운로드 등
- 서버 운영할 때 하는 작업들 : 새로운 코드 또는 라이브러리 추가, 스케일아웃 등. 
  - 운영을 쉽게 하기 위해 스크립트를 작성하여 사용한다.

- 결론: Docker 없이 서버 운영 환경을 세팅하기 위해서 많은 작업이 필요하다.

## 2. 가상화 방식 3가지

### 2-1. 호스트 가상화

<table style="text-align: center">
  <tr>
    <td>가상환경</td>
    <td>가상환경</td>
  </tr>
  <tr>
    <td>애플리케이션</td>
    <td>애플리케이션</td>
  </tr>
  <tr>
    <td>미들웨어</td>
    <td>미들웨어</td>
  </tr>
  <tr>
    <td>게스트 OS</td>
    <td>게스트 OS</td>
  </tr>
  <tr>
    <td colspan="2">가상화 소프트웨어</td>
  </tr>  
  <tr>
    <td colspan="2">호스트 OS</td>
  </tr>
  <tr>
    <td colspan="2">하드웨어</td>
  </tr>
</table>

- ex) 개인 PC에서 VMware같은 가상화 프로그램을 설치하고, VMware를 통해 가상환경을 만든다.
- 호스트 OS 위에 게스트 OS가 있는 구조여서 오버헤드가 클 수 있다.


### 2-2. 하이퍼바이저 가상화
<table style="text-align: center">
  <tr>
    <td>가상환경</td>
    <td>가상환경</td>
  </tr>
  <tr>
    <td>애플리케이션</td>
    <td>애플리케이션</td>
  </tr>
  <tr>
    <td>미들웨어</td>
    <td>미들웨어</td>
  </tr>
  <tr>
    <td>게스트 OS</td>
    <td>게스트 OS</td>
  </tr>
  <tr>
    <td colspan="2">하이퍼바이저</td>
  </tr>
  <tr>
    <td colspan="2">하드웨어</td>
  </tr>
</table>

- 하드웨어 위에 OS 없이 하이퍼바이저가 설치된다. 하이퍼바이저가 직접 하드웨어를 제어하고 그 위에 게스트 OS를 동작시킨다.
- 호스트 OS가 없어서 호스트 가상화보단 오버헤드가 적다.
- 하지만 여전히 어플리케이션 환경마다 게스트 OS가 각각 존재한다.

### 2-3. 컨테이너 가상화
<table style="text-align: center">
  <tr>
    <td>가상환경</td>
    <td>가상환경</td>
  </tr>
  <tr>
    <td>애플리케이션</td>
    <td>애플리케이션</td>
  </tr>
  <tr>
    <td>미들웨어</td>
    <td>미들웨어</td>
  </tr>
  <tr>
    <td colspan="2">컨테이너 관리 소프트웨어 (도커)</td>
  </tr>
  <tr>
    <td colspan="2">호스트 OS</td>
  </tr>
  <tr>
    <td colspan="2">하드웨어</td>
  </tr>
</table>

- 호스트 OS 위에 바로 컨테이너 엔진이 있다. 게스트 OS 필요없이 컨테이너 엔진이 애플리케이션과 그 환경을 컨테이너 단위로 독립적으로 분리한다.
- ex) 개인 PC에서 도커를 설치하면 손쉽게 바로 서버 운영 환경을 세팅할 수 있다.

## 3. Docker의 구성요소
### 3-1. Docker client
- 클라이언트가 `docker run`과 같은 명령어 입력.
- 그럼 `docker run` 명령어가 dockered에게 보내짐.

### 3-2. Docker daemon(dockered)
- Docker daemon은 Docker API 요청을 듣고 이미지, 컨테이너, 네트워크 같은 Docker object를 관리한다.
- 이 데몬은 다른 데몬과도 통신하여 Docker 서비스를 관리할 수 있다.

### 3-3. Docker registries
- 도커 이미지를 저장하는 공간. 
- 도커 허브는 누구나 사용할 수 있는 공용 레지스트리이며, 기본적으로 도커 허브에서 이미지를 찾는다.

### 3-4. Docker objects
- image : 어플리케이션을 실행에 필요한 모든 것이 생성되어 있는 파일
- container : image를 사용하여 실행한 것. 각 컨테이너는 독립성이 보장되며 이를 위해 네트워크, 저장소가 분리되어 있다. 



## 4. Docker 컨테이너 실행 명령어
```shell
# 컨테이너 실행
docker run [options] {immage_name} [command]

# 호스트 8080으로 들어오면 컨테이너 80 포트로 포워딩
docker run -d -p 8080:80 docker/getting-started


# 컨테이너 상태 확인
docker ps
docker ps -a

# 컨테이너 멈추기
docker stop {container_id|container_name}
docker kill {container_id|container_name}

# 컨테이너 되살리기
docker restart {container_id|container_name}

# 실행 중인 컨테이너에서 명령어 실행하기
docker exec [options] {container_id|container_name} [command]

docker exec {container_id} ls

docker exec -ti {container_id} sh


## 컨테이너 삭제
docker rm {container_id}

```
- `-d` : 컨테이너를 백그라운드에서 실행
- `-p host_port:container_port` : 호스트 포트와 컨테이너 내부 포트 연결

- stop : 도커 데몬에 SIGTERM 신호 보냄. 프로세스 종료
- kill : SIGKILL 신호 보냄. 바로 삭제


## 5. 내 어플리케이션을 Docker로 실행하기

### 5-1.Dockerfile 만들기
- 도커 이미지를 만들기 위해선 이미지를 어떻게 생성할 것인지를 정의한 Dockerfile을 작성해야 한다.

- FROM : 이미지를 생성할 때 기본으로 사용할 base image 작성
- ADD src dst : 호스트 머신에 있는 파일이나 폴더(src)를 dst 위치에 저장.
- RUN script : script 실행.
- CMD : 생성된 도커 이미지를 실행할 때 자동으로 실행되는 커맨드
```shell
FROM python:3.8

ADD requirements.txt .

RUN pip install -r requirements.txt

ADD templates templates

ADD app.py .

CMD ["python", "app.py"]

```

### 5-2. Docker image build 하기
```shell
docker build [options] PATH

docker build -t docker-memo:version1 .
```
- `-t` : image에 원하는 이름 붙이기. {image_name}:{tag} 형식. tag의 기본값은 latest
- `.` : build를 어느 위치에서 실행할 것인지 정의.

```shell
# image 목록 확인
docker images

# image 실행
docker run docker-memo:version1
```


## 6. 이미지 공유하기
### 6-1. Docker hub에 image 올리기
- Docker registry는 이미지를 저장하는 장소. 대표적인게 docker hub이다.
- 먼제 Docker humb 가입하고 repository를 생성해야 함. **도커 이미지 이름을 registory와 동일하게 만들어줘야 한다.**

```shell
# image 빌드
docker build -t {user_id}/docker-memo:version2 .

# docker hub 로그인
docker login
# 후에 username, password 입력

# docker image 올리기
docker push wellshs/docker-memo:version2


# docker image 히스토리 보기
docker history 

# docker image 목록보기
docker images

# docker image 삭제
docker rmi wellshs/docker-memo:version2

# docker image 가져오기
docker pull wellshs/docker-memo:version2

# docker image 실행
docker run -p 80:5000 wellshs/docker-memo:version2

```







