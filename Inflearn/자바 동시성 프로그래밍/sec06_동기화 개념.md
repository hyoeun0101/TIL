## 1. 싱글 스레드 VS 멀티 스레드
### 1-1. 싱글 스레드
- 장점
  - 문맥교환이 없음.
  - 동기화 문제 없음.
  - 자원 비용이 적음.
  - 프로그래밍 난이도가 낮음.
- 단점
  - CPU 멀티코어 활용 못함.
  - 순차적 실행으로 처리 속도가 느림.
  - I/O 처리 시 CPU가 낭비됨.
  - 스레드에 오류가 발생하면 프로그램이 종료됨.

### 1-2. 멀티 스레드
- 장점
  - 동시성을 통해 처리속도 향상
  - CPU 멀티코어의 병렬성, 동시성을 통해 성능 향상
  - CPU 낭비없이 자원을 효율적으로 사용
  - 하나의 스레드에서 오류가 발생하더라도 다른 스레드에 영향 없음.
  
- 단점
  - 빈번한 문맥교환으로 성능 저하
  - 스레드 간 동기화 이슈가 발생
  - 스레드 생성 비용이 작지 않음.
  - 프로그래밍 난이도 높음.

### 1-3. 동시성 문제
- 동시성 문제는 멀티 스레드에서만 발생한다.
- 여러 스레드가 같은 메모리 영역을 읽고 쓸 때 동시성 문제가 발생할 수 있다.
- 동시성 문제
  - race condition
  - deadlock
  - livelock
  - Starvation
  - Visibility 문제
  - Reordering 문제
  - Atomicity 문제

## 2. 동기화와 CPU 관계
### 2-1. 동기화(Synchronization)?
- 여러 프로세스가 공유 영역을 동시에 접근하여 동시성 문제가 발생하는 것을 막는 메카니즘.


### 2-2. CPU 연산 처리 이해
 - 하나의 기계어는 원자성을 보장하고, 동시성 문제가 발생하지 않음.
   - ex) `LOAD R1,data;`
 - 그러나 두 개 이상의 기계어 명령어는 원자성을 보장하지 않고, 동시성 문제가 발생하여 동기화가 필요하다.
 

## 3. Critical Section (임계영역)

### 3-1. 임계영역의 구성
- entry section : 입장영역. critical section에 진입하기 위한 진입허가를 요청하는 영역
- **critical section** : 임계영역. 하나의 스레드만 접근할 수 있는 영역
- exit section : 퇴장영역. critical section에서 빠져나올 때 신호를 알리는 영역
- remainder section : 위의 3가지를 제외한 나머지 영역

```java
import java.util.concurrent.locks.ReentrantLock;

public class SharedObject {
  ReentrantLock lock = new ReentrantLock;
  
  int counter = 0;
  
  public void perform() {
      lock.lock(); // entry section
      try {
          counter++; // critical section
      } finally {
          lock.unlock(); // exit section
          log.info("finished"); //remainder section
      }
  }
}

```
### 3-2. critical section problem
- 한 스레드가 임계영역을 실행하고 있는 중에 다른 스레드가 임계영역을 사용하려고 할 때 발생하는 문제
- 이 문제의 해결하기 위해선 3가지 충족 조건이 요구된다.
  - Mutual Exclusion (상호 배제) : 어떤 스레드가 critical section을 실행 중이면 다른 스레드는 해당 critical section을 실행할 수 없다. 
  - Progress (진행) : critical section에서 실행 중인 스레드가 없으면 진입하려는 다음 스레드를 진행시켜야 한다.
  - Bounded Waiting (한정 대기) : 기아 상태가 발생하지 않도록 스레드가 임계 영역에서 실행할 수 있는 횟수에 제한이 있어야 한다.

- 동기화 도구들
  - 뮤텍스, 세마포아, 모니터, CAS 등과 같은 도구를 통해 critical section problem이 발생하지 않도록 할 수 있다.
  - 자바에선 synchronized 키워드를 포함한 여러 동기화 도구들을 제공하고 있다.
  
### 3-3. Race Condition (경쟁상태)
- 동시성 문제 중 하나로 여러 스레드가 공유 자원에 동시에 접근하면서 실행 순서에 따라 결과가 달라지는 문제를 말한다.
- Critical Section Problem이 해결되지 않은 상태에서 여러 스레드가 동시에 임계영역에 접근하여 공유 데이터를 변경하면서 발생한다.

## 4. 안전한 스레드 구성
- 여러 스레드에서 같은 객체에 동시에 접근해서 실행하더라도 지속적인 정확성이 보장되는 코드를 'thread-safe하다' 라고 한다.
### 스레드에 안전한 구조

- 임계영역을 동기화하기
  - 동시에 여러 스레드가 임계영역에 접근하지 못하도록 lock 메카니즘을 사용한다.
- 동기화 도구 사용하기
  - 세마포어, CAS, Atomic 변수 등의 도구를 사용한다.
- ThreadLocal 사용하기
  - 스레드마다 가지고 있는 독립적인 저장소 ThreadLocal을 사용해서 상태를 관리한다.
- 불변 객체 사용하기
