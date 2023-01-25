원격 프로그램 실행하기 위해선 다음 두 가지 작업이 필요하다.
1. 서블릿 등록
2. url 연결
옛날에는 이를 xml파일로 설정해줬지만 요즘은 애노테이션을 통해 한다.
- @Controller로 프로그램 등록하기
- @ReqeustMapping으로 url 연결하기

### 🍎 톰캣 내부 구조
![톰캣 내부 구조](https://user-images.githubusercontent.com/96059261/200199871-5272a408-9a8b-48bd-934c-af91df354c49.png)
      
디버깅을 해본 후 특정 url에 요청을 보내보자.   
그러면 톰캣 실행 과정이 나오는데, 쓰레드 여러 개 중 하나가 처리하는 걸 볼 수 있다.       
![톰캣내부](https://user-images.githubusercontent.com/96059261/200200276-8a5c8dce-54ea-4d75-bea6-beefd636b705.png)      

### 🍎 톰캣 설정 파일
- `톰캣설치경로/conf/server.xml` : Tomcat 서버 설정 파일
   - Server > Service > Engine > Host > Context 순서로 서버에 대한 설정이 있다.
- `톰캣설치경로/conf/web.xml` : Tomcat의 모든 web app의 공통 설정
   - 서블릿 등록
- `웹앱이름/WEB-INF/web.xml` : web app의 개별 설정
   - DispatcherServlet 등록
