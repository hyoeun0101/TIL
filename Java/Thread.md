## 🍎 프로세스 vs 쓰레드

### 프로세스란?

- 메모리를 할당받아 실행 중인 프로그램, 쓰레드 + 자원(메모리, 데이터 등)

### 쓰레드란?

- 프로세스의 자원을 이용해서 실제로 작업을 수행하는 것
- 모든 프로세스에는 최소한 하나 이상의 쓰레드가 존재한다.
- 두 개 이상의 쓰레드를 실행하는 것을 멀티 쓰레드라고 한다.

## 🍎 싱글 쓰레드, 멀티 쓰레드

- 싱글 쓰레드는 쓰레드가 하나만 존재할 수 있으며, 한 번에 한 가지 작업만 할 수 있다.
- 멀티 쓰레드는 동시에 여러 작업을 할 수 있다. 우리가 채팅을 하는 동시에 파일을 다운로드를 받을 수 있는 것은 멀티 쓰레드로 작성되어 있기 때문이다.

### 멀티 쓰레드의 장단점

- 멀티 쓰레드는 동시에 여러 작업을 하기 때문에 CPU의 사용률을 향상시키고, 자원을 효율적으로 사용한다. 또한 사용자에 대한 응답성이 향상된다.

- 하지만 항상 멀티쓰레드가 빠른 것은 아니다. 멀티쓰레드는 작업 전환 시(context switching) 현재 진행 중이던 작업 상태를 저장하고 읽어오는 시간이 소요된다. 때문에 간단한 계산같은 경우 싱글쓰레드가 더 빠르다.

- 문제점 : `동기화`와 `데드락`
  - `교착상태`란 서로 상대 쓰레드가 점유한 자원을 사용하려고 대기하다가 실행을 멈추는 것을 말한다.

### 싱글 쓰레드의 문제점

- 싱글쓰레드는 현재 작업이 끝나기 전까지 뒤의 작업을 진행하지 못하는 `I/O블락킹`이 발생할 수 있다.
  - 파일 다운로드 작업과 메세지 전송 작업을 동시에 실행한다고 해보자.
    - 싱글 쓰레드는 사용자가 메세지 입력을 마칠 때까지 파일을 다운로드 받지 못하고 계속 대기하게 된다. 이러한 현상을 `I/O 블락킹`이라고 한다.
    - 멀티 쓰레드라면 두 작업을 동시에 할 수 있기 때문에 사용자가 메세지를 입력하는 동안 파일을 다운받을 수 있다.
- 따라서 서로 다른 자원을 사용하는 작업은 멀티 쓰레드가 더 효율적이다.

### I/O blocking

## 🍎 쓰레드의 구현, 실행

### 쓰레드 구현하는 방법 2가지

1. Thread 클래스 상속하기

```java
class ThreadExam extends Thread {
    @Override
    public void run() {...}
}

public class Exam {
    public static void main(String args[]) {
        ThreadExam t1 = new ThreadExam();
        t1.start(); // 쓰레드 실행
    }
}
```

2. Runnable 인터페이스 구현하기

- 자바는 다중 상속을 지원하지 않기 때문에 Runnable 인터페이스를 구현하는 것이 일반적이다.
- 이 방법이 재사용성이 높고 코드의 일관성을 유지하기 때문에 더 객체지향적인 방법이다.

```java
class ThreadExam2 implements Runnable{
    @Override
    public void run(){...}
}

public class Exam2 {
    public static void main(String args[]) {
        Runnable r = new ThreadExam2();
        Thread t2 = new Thread(r);
        t2.start(); // 쓰레드 실행
    }
}
```

### 쓰레드 실행하기

![ㅎ](https://user-images.githubusercontent.com/96059261/222309262-0d73c9cf-3169-4fd6-b9bc-f8cadb9c596a.jpg)

1. 메인 쓰레드에서 start()로 쓰레드를 실행시킨다.
2. start()는 새로운 쓰레드를 생성하고, 쓰레드가 작업할 새로운 호출스택을 생성한다.
3. 새로운 호출스택에서 run()이 호출되고, 독립적인 쓰레드의 작업을 진행한다.
4. 독립적인 두 개의 호출스택은 스케쥴러가 정한 순서대로 번갈아가며 작업을 수행한다.

- 쓰레드가 start()에 의해 실행되었다고 바로 실행되는 것은 아니다. 실행 대기 상태에 있다가 자신의 차례가 되어야 실행된다.
- 하나의 쓰레드는 한번의 start()를 호출할 수 있다. 두 번 이상 호출하면 `IllegalThreadStateException` 발생한다.
- 프로그램은 실행되는 쓰레드가 하나도 없을 때 종료된다. main 메서드가 수행을 마쳐도 다른 쓰레드가 작업을 진행 중이면 프로그램은 종료되지 않는다.

## 🍎 쓰레드의 우선순위

- 쓰레드에 우선순위를 부여하여 중요한 작업을 먼저 실행하도록 한다.
- 쓰레드의 우선순위는 쓰레드를 생성한 쓰레드로부터 상속받는다.
- 우선순위 범위는 1 ~ 10 이며, 숫자가 높을수록 우선순위가 높다.
- 쓰레드를 실행하기 전에만 우선순위를 부여할 수 있다.

```java
void setPriority(int newPriority)
int getPriority()
```

-

## 🍎 쓰레드 그룹

- 서로 관련된 쓰레드를 관리하기 위해 쓰레드 그룹으로 묶는다.
- 기본적으로 자신을 생성한 쓰레드의 쓰레드 그룹에 속한다.
- JVM은 main, system 쓰레드 그룹을 만든다. GC를 실행하는 Finalizer 쓰레드는 system쓰레드 그룹에 속한다.

```java
// Thread 생성자로 쓰레드 그룹에 포함시키기
Thread(ThreadGroup group, String name)
Thread(ThreadGroup group, Runnable target, String name)
```

## 🍎 데몬 쓰레드

- 일반 쓰레드의 작업을 돕는 보조 쓰레드
- 일반 쓰레드가 종료되면 같이 종료된다.
- 쓰레드를 실행하기 전 `setDaemon(boolean on)`으로 데몬 쓰레드임을 정해야한다.
- 데몬 쓰레드가 생성한 쓰레드는 데몬 쓰레드이다.
- 데몬 쓰레드의 예로는 가비지 컬렉터, 워드프로세서의 자동저장, 화면자동갱신 등이 있다.

## 🍎 쓰레드의 동기화

- 멀티쓰레드 프로세스의 경우 여러 쓰레드가 같은 프로세스의 자원을 공유하기 때문에 서로의 작업에 영향을 주게 된다.
- 동기화를 통해 쓰레드가 진행중인 작업을 다른 쓰레드가 건들지 못하게 막아야한다.
- `공유 데이터를 사용하는 코드`는 `임계영역`으로 지정하자.
  - 임계 영역 : 다른 쓰레드가 간섭하지 못하는 영역
- 모든 객체는 lock을 가지고 있으며, 해당 객체의 lock을 획득한 단 하나의 쓰레드만 임계영역의 코드를 수행할 수 있다. 쓰레드가 모두 실행된 후 lock을 반납한다.

### synchronized

1. 메서드 전체를 임계 영역으로 지정하기

```java
public synchronized void methodName(){}
```

- 쓰레드는 synchronized 메서드가 호출되면 해당 메서드가 포함된 객체의 lock을 얻어 작업을 수행하고, 메서드가 종류되면 lock을 반환한다.

2. 특정한 영역을 임계 영역으로 지정하기

```java
synchronized(객체의 참조변수){}
```

- 참조변수는 lock을 걸고자하는 객체를 참조한다.
- synchronized 블럭 안으로 들어가면 쓰레드는 지정된 객체의 lock을 얻게 되고, 블럭을 벗어나면 lock을 반납한다.

```java
public class Exam {
    public static void main(String[] args) {
        Runnable r = new MyThread();
        new Thread(r).start();
        new Thread(r).start();
    }
}

class Account {
    private int balance = 100;

    public int getBalance() {
        return balance;
    }

    public synchronized void withdraw(int money) {
        if(balance >= money) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){}
            balance-=money;
        }
    }
}

class MyThread implements Runnable{
    Account ac = new Account();

    @Override
    public void run() {
        while(ac.getBalance() > 0) {
            int money = (int)(Math.random() * 3 + 1) * 100;
            ac.withdraw(money);
            System.out.println("balance : "+ ac.getBalance());
        }
    }
}
```

- withdraw메서드에 synchroniezed를 붙이지 않으면 balance는 공유되기 때문에 음수가 될 수 있다.
- synchronized를 붙여 데이터를 공유하지 못하게 했다.
- 주의할 점은 balance가 private이라는 것이다. private으로 외부로부터의 접근을 막아야한다.

## 🍎 쓰레드 제어

### 쓰레드의 상태

![thread-status](https://user-images.githubusercontent.com/96059261/204418592-6f28605b-200d-4960-9cb1-c01847a80dcf.png)

1. 쓰레드를 생성, start()호출하면 실행 대기 상태
2. 실행에서 yield()만나면 다시 실행 대기 상태로 감.
3. 실행하다가 sleep(), wait(), join(), I/O BlOCK 만나면 일시정지(BLOCKED, WAITING) 상태가 됨
4. 일시정지에서 interrupt(), notify(), time-out 으로 다시 실행대기 상태가 됨
5. 실행을 마치거나 stop()이 호출되면 쓰레드는 소멸.

### 1. sleep()

- 실행하다가 일시정지 상태가 된다.
- 지정된 시간이 다 되거나, interrupt()가 호출되어 InterruptException이 발생하면 다시 실행 대기 상태가 된다.
- 때문에 sleep()은 반드시 try-catch로 InterruptedException 예외 처리를 해줘야한다.

### 2. join()

- 쓰레드 자신이 하던 작업을 잠시 멈추고 다른 쓰레드가 지정된 시간동안 작업을 수행하도록 한다.
- sleep()과 비슷하다. 차이점은 join()은 특정 쓰레드에 대해 동작하는 것이고, sleep()은 현재 실행 중인 쓰레드에 대해 동작하는 것이다.
- sleep()과 마찬가지로 interrupt()로 일시정지를 빠져나올 수 있다.
- 시간을 지정하지 않으면, 특정 쓰레드의 작업이 끝날 때까지 일시 정지 상태이다.

```java
public class Examp{
    static long startTime = 0;
    public static void main(String[] args) {
       MyThread th1 = new MyThread();
       MyThread2 th2 = new MyThread2();
       th1.start();
       th2.start();
       startTime = System.currentTimeMillis();

       try{
        th1.join();//main쓰레드가 th1의 작업이 끝날때까지 기다린다.
        th2.join();//main쓰레드가 th2의 작업이 끝날때까지 기다린다.

       }catch(InterruptedException e){}

       System.out.println("time="+(System.currentTimeMillis()-Examp.startTime));
    }

}
class MyThread extends Thread{
    public void run(){
        for(int i=0;i<300;i++){
            System.out.print(new String("-"));
        }
    }
}

class MyThread2 extends Thread{
    public void run(){
        for(int i=0;i<300;i++){
            System.out.print(new String("|"));
        }
    }
}
```

- 메인 쓰레드가 다른 쓰레드의 작업이 끝날 때까지 기다린 후 실행한다.

### 3. yield()

- 쓰레드는 자신에게 주어진 실행시간을 다음 차례의 쓰레드에게 양보한다.
- 예를 들어 스케쥴러에 의해 1초의 실행시간을 할당받은 쓰레드가 0.5초동안 작업하고, yield()를 호출하면 다시 실행대기 상태로 간다.

### 4. interrupt()

```java
void interrupt()
boolean isInterrupted()
static boolean interrupted()
```

- isInterrupted()와 interrupted()는 interrupt()가 발생하면 true가 된다.
- 다만 interrupted()는 상태를 반환 후, false로 변환한다. 즉 interrupt()가 호출되면 interrupted()의 값은 true가 반환되고, false로 변환한다.

```java
import javax.swing.JOptionPane;

public class Exam {
    static boolean autoSave=false;
    public static void main(String[] args) {
        MyThread th = new MyThread();
        th.start();

        String input = JOptionPane.showInputDialog("아무값이나 입력");
        System.out.println("값은 "+input);
        th.interrupt();
        System.out.println("isinterrupted():"+th.isInterrupted());//true
        System.out.println("interrupted():"+th.interrupted());//false
    }
}

class MyThread extends Thread {
    public void run(){
        int i=10;

        while(i!=0 && !isInterrupted()) {//0이거나, interrupt 호출되면 false
            System.out.println(i--);
            for(long x=0;x<1000000000L;x++);
        }
        System.out.println("end");

    }
}
```

### 5. 동기화 관련 쓰레드 제어 - wait(), notify()

- 동기화의 효율을 높이기 위해 wait(), notify()를 사용한다.
- Object 클래스에 정의되어 있으며, 동기화 블록 내에서만 사용할 수 있다.
- wait()

  - 객체의 lock을 풀고 쓰레드를 해당 객체의 waiting pool에 넣는다.
  - 매개 변수가 있는 wait()는 지정된 시간동안만 대기 후 다시 실행한다.

- notify()

  - waiting pool에서 대기 중이던 임의의 쓰레드에게 다시 실행할 것을 통지한다.

- notifyAll()
  - waiting pool에서 대기 중이던 모든 쓰레드에게 통지한다.
