## Docker란?
### 실행환경, 실행코드, 라이브러리 등을 한 곳에 모아 정의한다. => 그게 바로 도커 이미지.
___

1. __docker run [options] {image_name} [command]__
 : 이미지에서 컨테이너 생성
```
docker run -d -p 80:80 docker/getting-strated
```
- -d : 컨테이너를 백그라운드에서 실행
- -p 80:80 : 호스트포트와 컨테이너 포트 연결
- https://hub.docker.com/r/docker/getting-started  => 이 registry에 있는 이미지.
```
docker run -it python:3.8
```
2. __docker ps__
 : 컨테이너 상태 확인하기
 __docker ps -a__ : 꺼진 컨테이너도 확인하기
 
3. __docker stop {container_id}__
  : 컨테이너 멈추기. SIGTERM 신호 보냄.
 - docker kill {container_id}도 있음. SIGKILL 신호 보냄.

4. __docker restart {container_id}__
 : 컨테이너 되살리기

5. __docker rm {container_id}__
 : 컨테이너 삭제하기

6.__docker exec {container_id} ls__
 : 컨테이너 내부에 있는 파일 

7. __docker build . -t {image_name}:{tag}__
 : 이미지 생성하기
8. __docker images__
 : 이미지 확인하기
9. __docker history {image_name}:{tag}__
 : 이미지의 레이어 보기
 이미지를 빌드하면 이미지에서 각각의 명령어가 실행될 때 레이어를 생성한다.
10. __docker rmi {image_name}:{tag}__
 : 이미지 삭제
