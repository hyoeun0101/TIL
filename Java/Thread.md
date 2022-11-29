# 프로세스 vs 쓰레드

`프로세스`란? 메모리를 할당받아 실행 중인 프로그램, 쓰레드 + 자원(메모리, 데이터 등)  
`쓰레드`란? 프로세스의 자원을 이용해서 실제로 작업하는 것, 두 개 이상의 쓰레드를 실행하는 것을 멀티 쓰레드라고 한다.

## 쓰레드의 구현

1. Thread 클래스 상속하기
2. Runnable 인터페이스 구현하기  
   다중 상속을 지원하지 않기 때문에 Runnable 인터페이스를 구현하여 쓰레드를 생성하자. 이 방법이 재사용성이 높고 코드의 일관성을 유지하기 때문에 더 객체지향적인 방법이다.

```java
class MyThread extends Thread{
    @Override
    public void run(){...}
}

class MyThread2 implements Runnable{
    @Override
    public void run(){...}
}

class Exam{
    public static void main(String[] args){
        MyThread th1 = new MyThread();

        Runnable r= new MyThread2();
        Thread th2 = new Thread(r);
    }
}
```

## 쓰레드 실행

- `start()`: 쓰레드를 `실행 대기` 상태로 보낸다.
- `run()` : run()내부의 코드를 실행시킨다.
- 하나의 쓰레드는 한번의 start()를 호출할 수 있다. 즉, 한번 종료된 쓰레드는 다시 실행할 수 없다. 두 번 이상 호출한다면 `IllegalThreadStateException`이 발생한다.
- 실행되는 쓰레드가 하나도 없을 때 프로그램이 종료된다.

1. start()를 호출하면 새로운 쓰레드가 생성되고, 그 쓰레드가 작업할 호출스택이 생성된다.
2. 새로 생성된 호출스택에서 run()이 호출되어 쓰레드가 독립적인 공간에서 작업을 진행한다.
3. 독립적인 두 개의 호출스택은 스케쥴러가 정한 순서대로 번갈아가며 작업을 수행한다.

## 싱글쓰레드, 멀티쓰레드

### 멀티쓰레드의 장점과 문제점

- CPU의 사용률을 높인다.
- 자원을 보다 효율적으로 사용할 수 있다.
- 사용자에게 보다 빠른 응답을 할 수 있다.
  - 하지만 항상 멀티쓰레드가 빠른 것은 아니다. 멀티쓰레드는 작업 전환 시(context switching) 현재 진행 중이던 작업 상태를 저장하고 읽어오는 시간이 소요된다. 때문에 간단한 계산은 싱글쓰레드가 더 빠르다.
- 문제점 : `동기화`와 `데드락`
  - 데드락이란 서로 상대 쓰레드가 점유한 자원을 사용하려고 대기하다가 실행을 멈추는 것을 말한다.
- 서로 다른 자원을 사용하는 작업은 멀티 쓰레드가 더 효율적이다. 싱글쓰레드는 전 작업이 끝나기 전까지 뒤의 작업을 진행하지 못하는 `I/O블락킹`이 발생할 수 있다.
  - 파일 전송하는 작업과 메세지를 보내는 작업을 멀티쓰레드로 동시에 동작하게 할 수 있다. 이 두 작업을 싱글 쓰레드에서 진행한다면 사용자가 메세지를 입력하기 전까지 뒤의 작업을 진행하지 못하지만 멀티 쓰레드라면 사용자가 메세지를 입력하는 동안 파일을 다운받을 수 있다.
- 메세지 전송이 더 중요한 작업이므로 더 중요한 작업의 `우선순위`를 더 높게 지정하여 먼저 처리하도록 한다.
  - `기본적으로 우선순위는 쓰레드를 생성한 쓰레드로부터 상속`받는다. 메인 쓰레드의 우선순위의 디폴트값은 `5`이다.
  - 쓰레드를 실행하기 전에만 우선순위를 부여할 수 있다.
- 싱글 코어일 때는 작업이 겹칠 일이 없지만, 멀티 코어일 때는 작업이 겹쳐 실행될 수도 있다.

## 쓰레드 그룹

- 서로 관련된 쓰레드를 관리하기 위함. 기본적으로 자신을 생성한 쓰레드의 쓰레드 그룹에 속하며 `모든 쓰레드는 쓰레드 그룹에 속해있다`.
- JVM은 main, system 쓰레드 그룹을 만든다. GC를 실행하는 Finalizer 쓰레드는 system쓰레드 그룹에 속한다.
- 자신의 쓰레드 그룹, 하위 쓰레드 그룹만 변경할 수 있다.

## 데몬 쓰레드

: 일반 쓰레드의 작업을 돕는 보조 쓰레드, 일반 쓰레드가 종료되면 같이 종료된다.

- 데몬 쓰레드의 예로는 가비지 컬렉터, 워드프로세서의 자동저장, 화면자동갱신 등이 있다.
- 쓰레드를 실행하기 전 `setDaemon(boolean on)`으로 지정해줘야한다.
- 데몬 쓰레드가 생성한 쓰레드는 데몬 쓰레드이다.

## 쓰레드 상태

![thread-status](https://user-images.githubusercontent.com/96059261/204418592-6f28605b-200d-4960-9cb1-c01847a80dcf.png)

1. 쓰레드를 생성, start()호출하면 실행 대기 상태
2. 실행에서 yield()만나면 다시 실행 대기 상태로 감.
3. 실행하다가 sleep(), wait(), join(), I/O BlOCK 만나면 일시정지(BLOCKED, WAITING) 상태가 됨
4. 일시정지에서 interrupt(), notify(), time-out 으로 다시 실행대기 상태가 됨
5. 실행을 마치거나 stop()이 호출되면 쓰레드는 소멸됨.

### 1.sleep()

```java
 static void sleep(long millis)
 static void sleep(long mills, int nanos)
```

- 실행하다가 일시정지 상태가 됨.
- time-out, interrupt() 발생하면 다시 실행 대기 상태가 됨.
- InterruptedException 예외 처리 해줘야함.
- 현재 실행 중인 쓰레드에 대해 작동하는 것임.

### 2. interrupt()

```java
void interrupt()
boolean isInterrupted()
static boolean interrupted()
```

isInterrupted()와 interrupted()는 interrupt()가 발생하 면 true가 된다. 다만 interrupted()는 상태를 반환하고, false로 변환한다. 즉 interrupt()가 호출되어 interrupted()의 값은 true가 되고, 이 후 interrupted()를 또 호출하면 false가 된 것을 볼 수 있다.

```java
import javax.swing.JOptionPane;

public class Examp{
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

class MyThread extends Thread{
    public void run(){
        int i=10;

        while(i!=0 && !isInterrupted()){//0이거나, interrupt 호출되면 false
            System.out.println(i--);
            for(long x=0;x<1000000000L;x++);
        }
        System.out.println("end");

    }
}
```

### 3. join()

- 작업 중 다른 쓰레드가 먼저 작업해야 할 때 사용한다.
- sleep()과 비슷하다. 차이점은 join()은 특정 쓰레드에 대해 동작하는 것이고, sleep()은 static 메서드로 현재 쓰레드에 대해 동작하는 것이다.
- sleep()과 마찬가지로 interrupt()로 일시정지를 빠져나올 수 있다.

```java
void join()
void join(long millis)
void join(long millis, int nanos)
```

시간을 지정하지 않으면, 특정 쓰레드의 작업이 끝날 때까지 기다린다.

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

메인 쓰레드가 다른 쓰레드의 작업이 끝날 때까지 기다린 것을 볼 수 있다.

## 쓰레드의 메서드 정리

| 메서드                                       | 설명                                                    |
| -------------------------------------------- | ------------------------------------------------------- |
| static Thread currentThread()                | 현재 실행 중인 쓰레드 반환                              |
| String getName()                             | 쓰레드의 이름 반환, 디폴트는 Thread-n, n은 0부터 시작   |
| void setPriority(in newPriority)             | 우선순위 지정                                           |
| int getPriority()                            | 우선순위 반환                                           |
| ThreadGroup getThreadGroup()                 | 쓰레드 그룹 반환                                        |
| void uncaughtException(Thread t, Throwable e | 예외로 인해 쓰레드가 중단되었을 때, JVM이 자동으로 호출 |

## 쓰레드 동기화

: 한 쓰레드가 진행중인 작업을 다른 쓰레드가 건들지 못하게 막는 것.

- 임계 영역 : 다른 쓰레드가 간섭하지 못하는 영역

1. 메서드 전체를 임계 영역으로 지정

```java
public synchronized void methodName(){}
```

synchronized 메서드를 호출하면 2. 특정한 영역을 임계 영역으로 지정

```java
synchronized(객체의 참조변수){}
```
