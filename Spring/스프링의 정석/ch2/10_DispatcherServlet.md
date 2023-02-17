- 입력, 처리, 출력 중 `입력`을 `DispatcherServlet`이 처리를 한다.

## 🍎 DispatcherServlet 처리과정

![z](https://user-images.githubusercontent.com/96059261/214463553-91b1a5da-f816-42bb-8612-299cc5c4ddcd.jpg)

- `HandlerMapping`: URL-메서드 식으로 맵핑되어 있는데, URL과 맵핑되는 메서드를 찾아 DispatcherServlet에게 반환한다.
- `HandlerAdapter` : DispatcherServlet에게 메서드를 받아 Controller와 연결해준다. DispatcherServlet와 Controller의 느슨한 연결을 위해 존재한다. 이를 통해 DispatcherServlet는 변경없이 컨트롤러 뿐만 아니라 서블릿 등 다양한 대상을 호출할 수 있다.
- `ViewResolver` : 실제 뷰이름을 반환한다. 기본적으로 InternalResourceViewResolver 사용한다.(servlet-context.xml 에서 빈 등록함)
- `JstlView` : 모델을 받아 jsp에 전달한다.

## 🍎 DispatcherServlet 소스 분석하기

__spring-webmvc-5.0.7RELEASE.jar >__  
__소스파일 위치>__ org/springframework/web/servlet/DispatcherServlet.class  
__기본 전략>__ org/springframework/web/servlet/DispatcherServlet.properties

### DispatcherServlet.properties

- DispatcherServlet의 기본 전략을 설정한다.
- HandlerMapping, HandlerAdapter로 무엇을 사용할지 정한다. 두 개 이상의 클래스가 설정되어 있는데, 이는 작성된 클래스를 순서대로 처리한다.

### 주요 메서드

| 메서드                                                                                                      | 역할                                                      |
| ----------------------------------------------------------------------------------------------------------- | --------------------------------------------------------- |
| void initStrategies(ApplicationContext context)                                                             | 기본 전략 초기화                                          |
| void doService(HttpServletRequest request, HttpServletResponse response)                                    | doDispatch() 호출                                         |
| void doDispatch(HttpServletRequest request, HttpServletResponse response)                                   | 실제 요청 처리                                            |
| void processDispatchResult(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain) | 예외가 발생했는지 확인, 발생하지 않았으면 render()를 호출 |
| void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)                      | 응답 결과를 생성해서 전송                                 |

