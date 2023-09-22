- 비동기 작업을 만들고 결과 얻기
- 넌블록 동작으로 생산성 높이기
- 비동기 API 설계와 구현
- 동기 API를 비동기적으로 소비하기
- 두 개 이상의 비동기 연산을 파이프라인으로 만들고 합치기
- 비동기 작업 완료에 대응하기

## 🍎 Future의 단순 활용

[자바8 이전의 코드 - Future로 작업을 비동기로 처리하기]

```java
ExecutorService executor = Executors.newCachedThreadPool(); //스레드풀에 태스크 제출하기 위해 ExecutorService 생성
Future<Double> future = exector.submit(new Callable<Double>() { //Callable 태스크 제출
    public Double call() {
        return doSomeLongComputation(); //시간이 걸리는 작업은 다른 스레드(excutor스레드)에서 비동기적으로 실행
    }
});

doSomethingElse(); //비동기 작업 수행하는 동안 다른 작업 실행

try {
    Double result = future.get(1, TimeUnit.SECONDS); //비동기 작업의 결과 가져오기. 결과가 준비되어 있지 않으면 1초동안 블록.
} catch(ExecutionException ee) {
    // 계산 중 예외 발생
} catch(InterruptedException ie) {
    //현재 스레드에서 대기 중 인터럽트 발생
} catch(TimeoutException te) {
    //Future가 완료되기 전에 타임아웃 발생
}
```

- Future의 여러 결과들의 의존성을 표현하기에 어렵다. ex) A 계산이 끝나면 그 결과를 B에게 전달. B의 결과를 다른 질의의 결과와 조합.

### CompletableFuture로 비동기 애플리케이션 만들기

- CompletableFuture를 통해 다음과 같은 기술을 구현할 수 있다.
  - 고객에게 비동기 API 제공하기
  - 동기 API를 논블록으로 만들기.
  - 두 개의 비동기 동작을 파이프라인으로 만들기
  - 두 개의 동작 결과를 하나의 비동기 계산으로 합치기
  - 비동기 동작의 완료에 대응하기. 새로운 정보로 즉시 갱신하기

## 🍎 비동기 API 구현

[예제] 온라인상점 제품 중 가장 저렴한 가격 찾기

```java
public class Shop {
    public double getPrice(String product) { //1초 블락킹
        return calculatePrice(product);
    }

    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculatePrice(String product) { // 가장 저렴한 가격 찾기
        delay(); //임의로 지연시키기
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
```

### 동기 메서드인 getPrice를 비동기 메서드로 변환하기

```java
public class Shop {
    public Future<Double> getPriceAsync(String product) {
        // 계산 결과를 담을 CompletableFuture 생성
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product); // 다른 스레드에서 비동기적으로 계산 수행
                futurePrice.complete(price); //계산이 완료되면 Future에 값을 설정.

            } catch ( Exception ex) {
                futurePrice.completeExceptionally(ex); // 문제 발생하면 Future를 종료.
            }

        }).start();
        return futurePrice; //계산 결과가 완료되길 기다리지 않고 바로 Future 반환
    }
...
}
```

- Future : 비동기 계산의 결과를 저장하는 인터페이스. get으로 결과값을 반환한다.

```java
//[클라이언트]

Shop shop = new Shop("BestShop");
Future<Double> futurePrice = shop.getPriceAsync("iPhone 15"); //제품의 가격 요청. 비동기이므로 즉시 Future 반환.넌블락킹

//제품 가격 계산하는 동안 다른 작업 수행
doSomethingElse();
try {

    double price = futurePrice.get(); // 결과 완료되었으면 결과 반환. 아직 완료 안됐으면 블락킹.
    System.out.printf("제일 저렴한 가격: %.2f",price);

} catch (Exception e) {
    throw new RuntimeException(e);
}

```

### 팩토리 메서드로 CompletableFuture 생성하기

```java
public Future<Double> getPriceAsync(String product) {
    return CompletabelFuture.supplyAsync(() -> calculatePrice(product));
}
```

- supplyAsync는 Supplier를 인수로 받아 CompletableFuture를 반환한다.

## 🍎 넌블록 코드 만들기

- 동기 방식의 블록 메서드를 비동기적으로 동작하도록 하기

```java
//동기적인 코드
List<Shop> shops = Arrays.asList(new Shop("A"), new Shop("B"));

// 제품명 입력하면 상점이름과 제품가격 반환
public List<String> findPrices(String product) {
    return shops.stream()
                .map(shop ->
                    String.format("상점: %s, %s제품의 가격: %.2f", shop.getName(), product, shop.getPrice(product)))
                .collect(toList());
                // 두 개의 상점의 제품 가격을 조회하므로 2초정도 걸린다.
}

```

### 개선 1 - 병렬 스트림 사용

- 병렬로 처리해서 성능이 개선된다.

```java
List<Shop> shops = Arrays.asList(new Shop("A"), new Shop("B"));

// 제품명 입력하면 상점이름과 제품가격 반환
public List<String> findPrices(String product) {
    return shops.parallelStream()
                .map(shop ->
                    String.format("상점: %s, %s제품의 가격: %.2f", shop.getName(), product, shop.getPrice(product)))
                .collect(toList());
}

```

- 하지만 병렬 스트림에선 스레드 풀의 크기가 고정되어 있다.
- 예를 들어 스레드의 수는 4개, 조회하려는 상점은 5개이다. 4개의 상점을 조회할 때는 병렬로 빠르게 처리하지만 나머지 하나의 상점은 4개의 스레드의 연산이 끝날 때까지 기다려야 한다.

### 개선 2 - CompletableFuture로 비동기 호출 구현하기

```java
List<Shop> shops = Arrays.asList(new Shop("A"), new Shop("B"));


public List<String> findPrices(String product) {
    List<CompletableFuture<String>> priceFutures
            = shops.stream() // 비동기로 호출
                    .map(shop -> CompletableFuture.supplyAsync(() ->
                                String.format("상점: %s, %s제품의 가격: %.2f", shop.getName(), product, shop.getPrice(product))))
                    .collect(toList());

    return priceFutures.stream()
                       .map(CompletableFuture::join)//모든 비동기 동작이 끝나길 기다림
                       .collect(toList());

}
```

- ComletableFuture의 join은 Future의 get과 같다. join은 모든 동작이 끝나기를 기다린다. 단 join은 예외를 발생시키지 않는다.
- 두 map 연산을 하나의 스트림 파이프라인으로 처리하지 않고, 두 개의 스트림 파이프라인으로 처리했다.
  - 하나의 파이프라인으로 연산을 처리하면 동작이 동기적, 순차적으로 이루어진다. ex) A상점에서 상품가격 조회 후, 바로 join. B상점에서 상품가격 조회 후, 바로 join
  - 두 개의 파이프라인으로 처리해야 상품 가격 조회하고, 결과를 join한다.
- 하지만 이 방법으로 성능이 생각보다 빨라지진 않는다. 코드의 복잡성을 생각하면 병렬 스트림 사용이 더 낫다.

### 개선 3 - 더 확장성이 좋은 해결 방법 - 커스텀 Executor

- 작업량을 고려해서 풀에서 관리하는 스레드 수를 결정할 수 있다.

```java
private final Executor executor = //상점 수만큼의 스레드를 갖는 풀을 생성.
        Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true); //프로그램 종료를 방해하지 않는 데몬 스레드를 사용.
                    return t;
                }
});


// 위에서 만든 풀은 데몬 스레드를 포함한다.
//일반 스레드는 실행 중이면 자바 프로그램은 종료되지 않는다. 데몬 스레드는 자바 프로그램이 종료되면 강제로 종료된다.

//Executor를 두 번째 인수로 전달하여 CompletableFuture 생성
CompletableFuture.supplyAsync(() -> shop.getName + "price is " + shop.getPrice(product), executor);
```

### 스레드 풀의 적절한 크기 조절

- 스레드 풀이 너무 크면? CPU와 메모리 자원을 차지하기 위해 경쟁하느라 시간 낭비.
- 스레드 풀이 너무 작으면? CPU의 일부 코어는 활용X.
- Nthreads = Ncpu _ Ucpu _ (1 + W / C)
  - Ncpu는 Runtime.getRuntime().availableProcessors() 가 반환하는 코어 수
  - Ucpu는 0과 1 사이의 값을 갖는 CPU의 활용 비율
  - W/C는 대기시간과 계산시간의 비율
- CPU 활용률이 100퍼센트라면 400 스레드를 갖는 풀을 만들어야 한다.?

### 컬렉션 계산을 병렬화하는 방법

1. 병렬 스트림으로 변환해서 컬렉션 처리
2. 컬렉션을 반복하면서 CompletableFuture 내부의 연산으로 만드는 것.

- I/O를 사용하지 않는 계산 중심의 동작일 때는 스트림 인터페이스 구현이 가장 간단하다.
- I/O를 기다리는 작업을 병렬로 실행할때는 CompletableFuture가 많은 유연성을 제공하므로 (스레드 수 설정 가능 등) CompletableFuture 사용이 더 좋다. 스트림으로 처리하면 I/O를 실제로 언제 처리할 지 예측하기 어렵다.

## 🍎 비동기 작업 파이프라인 만들기

[예제] - 계약을 맺은 모든 상점이 하나의 할인 서비스를 사용한다. 할인 서비스에서는 서로 다른 할인율을 제공하는 5개의 코드를 제공한다.  
[할인 서비스 구현]

```java
public class Discount {
    /**
     * 할인 코드 ENUM 정의
     */
    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }
     // Quote 클래스는 Shop의 getPrice의 String 반환값을 파싱한 클래스이다.
    public static String applyDiscount(Quote q) {
        return q.getShopName() + Discount.apply(q.getPrice(), q.getDiscountCode());
    }

    /**
     * 기존 가격에 할인 코드를 적용한다.
     */
    private static double apply(double price, Code code) {
        delay();
        return format(price * (100 - code.perentage) / 100);
    }
}
```

```java
public class Shop {
    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return String.format("상점 이름 : %s, 제품가격: %.2f,  할인율: %s", name, price, code)
    }

    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculatePrice(String product) { // 가장 저렴한 가격 찾기
        delay(); //임의로 지연시키기
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
```

[할인 서비스 사용]

```java
public List<String> findPrices(String product) {
    return shops.stream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount) //할인 적용
                .collect(toList());
}
```

- 순차적으로 실행하고 있다. 5개의 상점이 있다면 상점의 제품 가격 요청하느라 5초 소요, 할인 코드 적용에 5초 소요되었다.

### 동기 작업과 비동기 작업 조합하기

- findPrices 메서드를 비동기적으로 재구현

```java
public List<String> findPrices(String product) {
    List<CompletableFuture<String>> priceFutures =
            shops.stream()
                 .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), exeutor)) // 비동기적으로 조회. Stream<Completable<String>>
                 .map(future -> future.thenApply(Quote::parse))
                 .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() ->
                                Discount.applyDiscount(quote), executor)))
                 .collect(toList());
    return priceFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(toList());
}
```

1. 첫 번째 map : Stream<CompletableFuture<String>> 을 반환, 비동기적으로 가격을 조회하여 CompletableFuture에 가격 정보 문자열을 담는다. 그리고 커스텀 Excutor로 CompletableFuture을 설정한다.

- 두 번째 map : 가격 정보 문자열을 Quote 객체로 파싱한다. thenApply 메서드는 CompletableFuture 동작이 끝날 때까지 블록하지 않는다. (?) CompletableFuture가 동작을 완전히 완료한 다음에 thenApply 메서드에 전달된 람다식을 실행한다.(?) 이는 CompletableFuture의 결과물을 지정하는 것이다.

- thenApply == 스트림의 map
- thenCompose == 스트림의 flatMap

## 🍎 CompletableFuture의 종료에 대응하는 법

16.3.4 Executor로 ㅇ플리케이션 성능 개선??



- 비동기 에러 처리
예외 던지거나, 저번 주 에러 담은 거

- 결국은 스레드 실제로 돌려봐야함.
