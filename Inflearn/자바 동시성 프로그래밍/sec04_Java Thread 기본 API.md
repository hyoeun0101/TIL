## 1. sleep()
- 지정된 시간동안 현재 스레드를 대기상태로 전환하고, 시간이 지나면 다시 실행상태로 전환한다.
- native 메서드로 연결된다. 

### 1-1. sleep() 작동방식 2가지
1. 지정된 시간이 지남.
```java
public static void main(String[] args) {
    Thread t1 = new Thread(() -> {
        try {
            System.out.println("t1 실행");
            Thread.sleep(3000);
            System.out.println("3초 sleep됨.");
        } catch (InterruptedException e) {
        }
    });
    
    t1.start();
}

```
2. 대기 상태인 와중에 interrupt()가 발생하면 InterruptedException으로 예외 발생. 대기에서 해제되고 실행상태로 전환된다.
```java
public static void main(String[] args) {
    Thread t1 = new Thread(() -> {
        try {
            System.out.println("t1 시작");
            Thread.sleep(20000);
            System.out.println("t1 끝");
        } catch (InterruptedException e) {
            System.out.println("t1 interrupt 발생!");
        }
    });

    Thread t2 = new Thread(() -> {
        try {
            System.out.println("t2 시작");
            Thread.sleep(5000);
            System.out.println("t2 끝");

            t1.interrupt();
        } catch (InterruptedException e) {
            System.out.println("t2 interrupt 발생!");
        }
    });

    t1.start();
    t2.start();
}
```

## 2. join()
- 다른 스레드가 종료될 때까지 대기상태였다가 다른 스레드가 종료되면 실행대기 상태로 전환된다.
- join을 통해 스레드의 순차적인 흐름을 제어할 수 있다.
- join()는 내부적으로 wait() & notify() 흐름을 가진다.


## 3. interrupt()

### 3-1. interrupted() vs isInterrupted()
- 기본값은 false다.
- thread1.interrupted()를 호출하면 false로 초기화한다.
- thread1.isInterrupted()는 단순히 interrupted()의 상태만 반환한다.


## 4. name()/currentThread()/isAlive()

## 5. Priority

- 자바 런타임은 고정 우선순위 선점형 스케줄링이란 알고리즘을 지원한다.
- 이 알고리즘은 실행 대기 상태의 스레드 중 우선순위가 높은 스레드에게 먼저 CPU를 할당한다.

### 5-1. 우선순위
- 1~10 정수이며 값이 높을 수록 우선순위가 높다.
- 기본값은 5이다.
- 스케줄러는 우선순위가 높은 스레드를 먼저 실행한다. 두 스레드의 우선순위가 같다면 라운드 로빈 스케줄링 방식을 사용한다.
- 스레줄러가 반드시 우선순위가 높은 스레드를 먼저 실행한다고 보장할 순 없다. 
- 운영체제마다 다른 정책들이 있을 수 있으며 기아상태를 방지하기 위해 우선순위가 낮은 스레드를 먼저 실행할 수 있다.

