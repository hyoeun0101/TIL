## 🍎 java.lang.ProcessBuilder?

- 자바에서 ProcessBuilder로 특정 프로그램을 강제 실행할 수 있다.
- 생성자 두 개

```java
public ProcessBuilder(List<String> command) {
    if (command == null)
        throw new NullPointerException();
    this.command = command;
}

public ProcessBuilder(String... command) {
    this.command = new ArrayList<>(command.length);
    for (String arg : command)
        this.command.add(arg);
}
```

```java
try {
    ArrayList<String> list = new ArraysList<>();
    list.add("경로.exe");
    list.add("사이트")

    ProcessBuilder pb = new ProcessBuilder(list);
    pb.start();
} catch(Exception e) {
    e.printStackTrace();
}
```

- ProcessBuilder는 명령의

```java
//cmd에서 dir 명령어 실행 결과를 가져온다.
ProcessBuilder pb = null;

```

Runtime 클래스는 자바 런타임 환경을 캡슐화한다.
