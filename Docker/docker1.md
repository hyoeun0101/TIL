### 서버를 실행하기 위해 필요한 작업들
- os설치, 실행환경 세팅, 어플리케이션 코드 다운로드, 라이브러리 다운로드 등

실제 서비스를 제공하기 위해선 많은 서버를 관리하게 된다.
서버가 늘어나면서 서버 운영도 복잡해지고, 원하는 프로그램을 실행하는 것도 어려워진다.   


### 해결방안 
- 서버 관리를 인프라 관리, 어플리케이션 작성으로 분리하자.
실행환경, 코드, 라이브러리, 설정 파일을 한 곳에 정의해두자. 바로 이것이 도커 이미지.
서버에서는 간단하게 이미지를 가지고 실행시키기.

즉 도커 이미지를 생성하고, 공유하고, 실행함으로써 프로그램을 쉽게 관리할 수 있다.

## 🍎도커란?   
원하는 프로그램을 쉽게 실행하기 위한 플랫폼.   

### 도커의 구성요소

![image](https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fe1162a9e-8a77-493d-bdeb-1f2d30dab103%2FUntitled.png?table=block&id=1bed1972-c7fc-4735-8ee5-5d1b7f7899d7&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1540&userId=95c89e1b-2e61-4f6d-af1d-1927035a9891&cache=v2)

- image : 프로그램을 실행하기 위해 필요한 모든 것이 생성되어 있는 파일.
- container : 이미지를 실행한 것. 프로세스의 독립성을 위해 네트워크, 저장소 분리 되어있음.
- docker registry : 이미지를 저장하는 공간.
- docker daemon : 이미지를 레지스트리로부터 다운받거나 올리기, 이미지로부터 컨테이너 실행, 이미지를 새로 생성 등 docker object(image, container 등)를 관리
- Client에서 명령어를 통해 생성, 다운, 실행하기. 


### VM 
하비퍼바이저를 사용해서 여러 개의 OS를 하나의 호스트에서 생성해 사용하는 방식

![image](https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F09e4ac55-ae8f-4e36-aaba-916fd24a66f9%2FUntitled.png?table=block&id=05af8119-7475-40eb-8e8e-a2b669889831&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1530&userId=95c89e1b-2e61-4f6d-af1d-1927035a9891&cache=v2)

- 완벽한 OS를 생성할 수 있지만, 하이퍼바이저를 거치기 때문에 성능 손실 발생.
- OS까지 포함해야되서 이미지의 크기가 커짐.

- 도커
    - OS를 가지고 있지 않음.
    - host OS의 kernel을 공유해서 사용하므로 성능 손실 x.


# 어플리케이션 실행하기
```
docker run -d -p 80:80 docker/getting-started
```
-> localhost 들어가보기
- -d : 컨테이너를 백그라운드에서 실행
- -p host_port:container_port  : 호스트 포트와 컨테이너 내부 포트 연결


### 1. image를 사용해서 container 실행하기
```
docker run [options] {image_name} [command]
```

### 2. container 상태 확인하기
```
docker ps
docker ps -a   #꺼진 컨테이너도 확인하기
```

### 3. container 멈추기
```
docker stop {container_id|container_name}
docker kill {container_id|container_name}
```
- stop : SIGTERM 신호 보냄. 프로세스 종료.
- kill : SIGKILL 신호 보냄. 바로 삭제.

### 4. container 되살리기
```
docker restart {container_id|container_name}
```
### 5. 실행 중인 container에서 명령어 실행하기
```
```jsx
docker exec [options] {container_id|container_name} [command]
```
- 컨테이너 내부에 있는 파일 보기
```
docker exec {container_id} ls
```
- 쉘 스크립트 실행하기
```
docker exec -ti {container_id} sh
>>ls
>>exit
```
### 6. container 삭제하기
```
docker rm {container_id}
```

### Dockerfile 생성하기
- 이미지를 만들기 위해서는 Dockerfile이 필요하다. Dockerfile이란 이미지를 어떻게 생성할 것인지 정의한 파일이다.

```
# 이미지 생성할 때 기본으로 사용할 base image 작성
FROM openjdk:11
COPY .
ADD requirements.txt .

RUN pip install -r requirements.txt

ADD templates templates

ADD app.py .

CMD ["python", "app.py"]
```