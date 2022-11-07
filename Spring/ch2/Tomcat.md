1. 원격 프로그램 실행
- @Controller로 프로그램 등록
- @ReqeustMapping으로 url 연결
여기서 인스턴스 메서드를 작성하면 톰캣이 객체를 만들어줌. 접근 제어자는 상관이 없는데 그 이유는 스프링 프레임워크가 Resflection API를 사용해서 호출하기 때문임.    


![톰캣 내부 구조](https://user-images.githubusercontent.com/96059261/200199871-5272a408-9a8b-48bd-934c-af91df354c49.png)
      
디버깅을 해본 후 특정 url에 요청을 보내보자.   
그러면 톰캣 실행 과정이 나오는데, 쓰레드 여러 개 중 하나가 처리하는 걸 볼 수 있다.       
![톰캣내부](https://user-images.githubusercontent.com/96059261/200200276-8a5c8dce-54ea-4d75-bea6-beefd636b705.png)      

### 설정파일
- 톰캣/conf/server.xml : Tomcat 서버 설정 파일
    - Server=Tomcat, Service=Catalina, Engine, Host, Context 순서로 서버에 대한 설정이 들어있음
- 톰캣/conf/web.xml : Tomcat의 모든 web app 공통 설정
    - servelt 등록, url 맵핑하는 코드가 들어있음. 요즘은 xml에서 애노테이션으로 대체됨.
- 웹앱/WEB-INF/web.xml : web app 개별 설정