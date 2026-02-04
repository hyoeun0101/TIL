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
2. interrupt()가 발생하면 InterruptedException으로 예외 발생.
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

## join()

## interrupt()


## name()/currentThread()/isAlive()

## Priority

