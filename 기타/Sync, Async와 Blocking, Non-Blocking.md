설명하는 A와 B는 서로 다른 주체를 말한다. 예를 들면 멀티 쓰레드? A 쓰레드, B 쓰레드

## 🍎 Blocking VS Non-Blocking

### Blocking

- A가 작업하다가 B가 작업하면 A는 B의 작업이 끝날 때까지 기다렸다가(Blocking) B의 작업이 끝나면 이어서 작업을 수행한다.
- Ex) Java에서 JDBC를 이용하여 DB에 질의 날리고 결과를 받아오는 작업.
- 사용자가 입력값을 입력할 때까지 프로그램은 멈춰있는다.

### Non-Blocking

- B의 작업과 상관없이 A는 계속 자신의 작업을 진행한다.

## 🍎 Synchronous VS Asynchronous

### synchronous

- A와 B가 서로 동시에 작업을 수행하거나, 동시에 끝난다.
- A의 작업이 끝나는 동시에 B의 작업을 시작한다.
- syn : together
- chrono : time  
  -> 함께 시간을 맞춘다.
- ex) java의 synchronized 블록
- 메서드 호출 시 리턴하는 시간과 전달받는 시간이 같다??

### Asynchronous

- A와 B가 서로의 시작, 종료 시간과 상관 없이 동작한다.
