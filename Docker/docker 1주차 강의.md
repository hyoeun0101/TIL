## Docker란?
실행환경, 실행코드, 라이브러리 등을 한 곳에 모아 정의한다. => 그게 바로 도커 이미지.
___
## 1. 이미지에서 컨테이너 생성
```
docker run [options] {image_name} [command]
> docker run -d -p 5000:5000 docker-memo:version1
```

```
docker run -d -p 80:80 docker/getting-strated
```
- -d : 컨테이너를 백그라운드에서 실행   
- -p 80:80 : 호스트포트와 컨테이너 포트 연결.   
```
docker run -it python:3.8
```

## 2.  컨테이너 상태 확인하기
```
docker ps
docker ps -a  # 꺼진 컨테이너 확인하기
```

## 3. 컨테이너 멈추기
```
docker stop {container_id} # SIGTERM 신호 보냄
docker kill {container_id} # SIGKILL 신호 보냄
```

## 4. 컨테이너 되살리기
```
docker restart {container_id}
```

## 5. 컨테이너 삭제하기
```
docker rm {container_id}
```

## 6. 컨테이너 내부에 있는 파일 보기
```
docker exec {container_id} ls
```

## 7. 이미지 생성하기
```
docker build . -t {image_name} : tag
docker build . -t hyoeun/docker-memo:version1
```

## 8. 이미지 확인하기
```
docker images
```

## 9. 이미지의 레이어 보기
```
docker history hyoeun/docker-memo:version1
```
 이미지를 빌드하면 이미지에서 각각의 명령어가 실행될 때 레이어를 생성한다.
 
## 10. 이미지 삭제하기
```
docker rmi hyoeun/docker-memo:version1
```


![image](https://user-images.githubusercontent.com/96059261/157365754-f26a0477-103d-4a7c-8e70-f19585b58b4b.png)
- DOCKER_HOST : 도커 객체(이미지나 컨테이너)를 관리하는 공간. 레지스트리에서 이미지를 다운받거나 컨테이너 실행 등의 일을 한다.
- Registry : 이미지를 저장하는 공간. registry에서 이미지를 다운받는다.
- docker build : 도커 이미지 생성
- docker pull : Registry에서 이미지 다운
- docker run : 이미 있는 이미지에서 컨테이너 생성

# Virtual Machine
 