## 학습 목표
- 실제 어플리케이션을 실행하는 데 있어서 필요한 기능
  - Docker에서 data 유지하는 법
  - Docker로 실행한 여러 개의 application을 연결하는 법
  - 리소스를 제한하는 법

- Docker를 잘 활용하기 위한 팁
  - docker-compose 사용하기
  - docker image에 관련된 꿀팁
  - 사용하지 않은 이미지, 컨테이너 정리하기
  

## 1. Docker Container 데이터 유지하기
- Docker 컨테이너는 불변 + 일회성을 전제로 한다.
- docker rm, docker run을 하게 되면 컨테이너 내부 파일 시스템도 함께 삭제가 된다.
- 따라서 DB, 로그, 업로드 파일 같은 데이터는 컨테이너 밖에 저장해야 한다.
- 컨테이너 데이터 유지하는 방법에는 named volume, bind mount 두 가지가 있다.

### 1-1. Named Volume(권장)
- Docker가 관리하는 데이터 저장 공간. Docker가 지정한 경로에 데이터를 저장한다.
- 언제 쓰나?
  - DB
  - 운영 환경 
  - CI/CD, 서버 환경
  - Docker Compse/Swarm/Kubernetes 

```shell
# volume 생성
docker volume create my-volume

# volume 정보 확인
docker volume inspect my-volume

# volume 연결하여 실행
docker run -v my-volume:/var/lib/mysql mysql
```
- 실제 저장 위치 : `/var/lib/docker/volumes/my-volume/_data`


### 1-2. Bind Mount
- 호스트의 특정 디렉토리를 직접 연결. 즉 내 로컬 디렉토리를 컨테이너에 그대로 연결해서 저장한다.
- Docker가 아닌 호스트 파일 시스템에 의존한다.
- 언제 쓰나?
  - 로컬 개발
  - 로그 바로 확인
  - 테스트 용도
```shell
docker run -v {host_path}:{container_path} <docker_image>

docker run -v /home/user/data:/app/data my-app
```

## 2. Docker에서 여러 개의 container를 연결하기
### Docker network
- 컨테이너 간 통신을 어떻게 연결할지 정의한다.
- 컨테이너는 기본적으로 격리된 네트워크, file system을 가진다. 
- Docker가 네트워크 드라이버를 통해 컨테이너 간 통신, DNS 기반 서비스 디스커버리, 포트 공개 등을 구성해준다.

```shell
# docker network 생성
docker network create app-net

docker network ls

# network 연결해서 container 실행하기
docker run -d --name api --network app-net nginx:apline

docker run -d -p 24017:24017 --network test --network-alias mongo mongo

# 네트워크 상태 확인
docker network inspect {network_name}
docker network inspect app-net


docker run -it --network test nicolaka/netshoot
```








