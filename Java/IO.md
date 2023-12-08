## 🍎 스트림
- 자바에서 데이터(바이트)는 스트림(Stream)을 통해 입출력된다.
- 스트림은 `단방향 통신`이다.
- 입력하는 스트림과 출력하는 스트림이 둘 다 필요하다.
  - 입력 스트림 예시: 키보드, 파일, 프로그램 등
  - 출력 스트림 예시: 모니터, 파일, 프로그램 등
- 스트림은 바이트 스트림, 문자 스트림으로 나눌 수 있다.


## 🍎 바이트 스트림 - InputStream, OutputStream
- 이미지, 문자 등 모든 종류의 데이터를 주고 받을 수 있다. 
- 문자를 바이트 스트림으로 입출력할 경우, 문자로 변환하는 작업이 따로 필요하다.

### OutputStream
- 바이트 출력 스트림의 최상위 클래스

|리턴 타입|메소드|설명|
|--------|------|----|
|void|write(int b)|1byte를 출력, 4byte 중 마지막 1byte만 출력, 따라서 정수-128~127만 올바르게 전송가능|
|void|write(byte[] b)|배열 b의 모든 바이트를 출력|
|void|write(byte[] b, int off, int len)|배열 b[off]부터 len개의 바이트를 출력|
|void|flush()|출력 버퍼에 잔류하는 모든 바이트를 출력|
|void|close()|출력 스트림을 닫고 사용 메모리 해제|

![Alt text](/image/OutputStream.png)  

![Alt text](/image/OutputStream2.png) 


- OutputStream은 내부에 작은 버퍼(Buffer)를 가지고 있다. 
- write는 버퍼에 바이트를 저장하며 데이터를 출력할 준비만 한다. 버퍼가 다 차면 입력된 순서대로 출력한다. 
- flush는 버퍼에 남은 잔류를 모두 내보낸다. 모든 버퍼를 출력한다.

```java
public void method() {

  OutputStream os = null
  
  try {
    // 해당 파일이 없다면 파일을 생성 후 열고, 있다면 기존 파일에 덮어쓰기한다.
    // 단 폴더는 있어야 한다. 폴더가 없으면 FileNotFoundException 발생한다.
    // FileNotFoundException 필수 처리
    os =  new FileOutputStream("C:/Temp/test.txt");

    byte a = 10; //1바이트
    byte b = 20;
    byte c = 30;

    os.write(a); // 00000000 00000000 00000000 00001010 (4byte 중 끝 1바이트만 출력)
    os.write(b);
    os.write(c);

    os.flush();
    os.close();

  } catch(FileNotFoundException e) {
    e.printStackTrace();

  } catch(IOException e) {
    e.printStackTrace();
  } finally {
    try {
      os.close(); //IOException 필수 처리
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
```
[try-catch-resources 사용하기]

```java
public void method() {
  try(OutputStream os = new FileOutputStream("C:/Temp/test.txt")) {
    
    byte a = 10;
    byte b = 20;
    byte c = 30;
    
    os.write(a);
    os.write(b);
    os.write(c);

    os.flush();
  } catch (IOException e) {
    e.printStackTrace(e);
  }
}
```


### InputStream
- 바이트 입력 스트림의 최상위 클래스

|리턴 타입|메소드|설명|
|--------|------|----|
|int|read()|1byte를 읽고 4byte(int)로 반환, 따라서 4byte의 끝 1byte에만 데이터가 들어있다. 더 이상 읽을 게 없으면 -1 리턴|
|int|read(byte[] b)|b의 크기만큼의 데이터를 읽고 b에 데이터를 저장 후 읽은 데이터의 수를 반환, 더 이상 읽을게 없으면 -1 리턴|
|int|read(byte[] b, int off, int len)|배열 b[off]부터 len개의 바이트를 읽음|
|void|close()|입력 스트림을 닫고 사용 메모리 해제|

[read() 예제]
```java
public void method() {

  InputStream is = null;
  try {
    is = new FileInputStream("C:/Temp/test.txt");
    int data;
    while((data = is.read()) > -1) {
      System.out.println(data);
    }

  } catch(IOException e) {
    e.printStackTrace();
  } finally {
    try {
      is.close();
    } catch(IOException e) {
      e.printStackTrace();
    }

  }
}

```
[try-catch-resources 사용하기]
```java
public void method() {
  try(InputStream is = new FileInputStream("C:/Temp/test.txt")) {
    int data;
    while((data = is.read()) > -1) {
      System.out.println(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

```

[ read(byte[] b) 예제- 사진을 읽어서 새로운 파일 생성 ]
```java
public void method() {

		try (InputStream is = new FileInputStream("D:/Me/inPicture.jpg");
			 OutputStream os = new FileOutputStream("D:/Me/outPicture.jpg")) {

			//5,363 byte 크기의 파일 읽기
			// 1024byte = 1Kbyte
			byte[] data = new byte[1024];
			int num;
			int cnt = 0;
			while((num = is.read(data)) > -1) {
				os.write(data, 0, num);
				cnt++;
			}
			System.out.println("cnt::"+cnt); //6 출력
			os.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

``` 


## 🍎 문자 스트림 - Reader, Writer

### Writer
- 문자 출력 스트림의 최상위 클래스

|리턴 타입|메소드|설명|
|--------|------|----|
|void|write(int c)|주어진 한 문자 출력|
|void|write(char[] c)|배열 c의 모든 문자를 출력|
|void|write(char[] c, int off, int len)|배열 c[off]부터 len개의 문자를 출력|
|void|write(String str)|str 문자열을 출력|
|void|write(String str, int off, int len)|문자열 str의 off부터 len개의 문자를 출력|
|void|flush()|출력 버퍼에 잔류하는 모든 문자를 출력|
|void|close()|출력 스트림을 닫고 사용 메모리 해제|




```java
public void method() {
  try (Writer writer = new FileWriter("C:/Temp/text.txt");){

    char a = 'A';
    writer.write(a);

    char b = 'B';
    writer.write(b);

    char[] arr = {'C', 'D', 'E'};
    writer.write(arr);

    writer.flush();
  } catch (IOException e) {
    e.printStackTrace
  }
}
```








ㄴ


### Reader

-  문자 입력 스트림의 최상위 크래스이다.


|리턴 타입|메소드|설명|
|--------|------|----|
|int|read()|2byte(1개의 문자)를 읽고 4byte(int)로 반환, 따라서 4byte의 끝 2byte에만 데이터가 들어있다. 더 이상 읽을 게 없으면 -1 리턴|
|int|read(char[] cbuf)|읽은 문자를 cbuf 배열에 저장하고, 읽은 문자 수를 리턴|
|void|close()|입력 스트림을 닫고, 사용 메모리 해제|


```java
public void method() {
  Reader reader = null;
  try {
  
    reader = new FileReader("C:/Temp/test.txt");
    while(true) {
      // 문자 한 개씩 읽기
      int data = reader.read();
      if(data == -1) break;
      System.out.println((char)data);
    }
    reader.close();
    System.out.println();


    reader = new FileReader("C:/Temp/test.txt");
    char[] data = new char[100];

    while(true) {
      // 문자 배열로 읽기. 100개씩 읽음.
      int num = reader.read(data);
      if(num == -1) break;
      for(int i=0; i<num; i++) {
        System.out.println(data[i]);
      }
    }

    reader.close();

  } catch (FileNotFoundException e) {
    e.printStackTrace();
  } catch (IOException e) {
    e.printStackTrace();
  }
}
```

## 🍎 보조 스트림

- 스트림은 바이트를 다루는데 실제 우리가 어플리케이션에선 int, 문자열 등을 다루지 바이트를 다루진 않는다.
- 그래서 보통 데이터를 내가 원하는 타입으로 바로 변환하거나, 성능을 향상하는 보조 스트림을 사용한다.
- 보조 스트림은 혼자서 입출력을 할 순 없다.

- 입력 스트림 -> 보조 스트림 -> 프로그램 -> 보조 스트림 -> 출력 스트림
```
보조스트림 변수 = new 보조스트림(입출력스트림);
```
- 스트림 체인 : 보조스트림은 또 다른 보조 스트림과 연결되어 체인으로 구성할 수 있다.
```
보조스트림2 변수 = new 보조스트림2(보조스트림1);
```

### 보조 스트림 종류
|보조 스트림|기능|
|---------|----|
|InputStreamReader, OutputStreamWriter|바이트 스트림->문자 스트림으로 |
|BufferedInputStream, BufferedOutputStream, BufferedReader, BufferedWriter|입출력 성능향상|
|DataInputStream, DataOutputStream|기본 타입 데이터 입출력|
|PrintStream, PrintWriter|줄바꿈 처리 및 형식화된 문자열 출력|
|ObjectInputStream, ObjectOutputStream|객체 입출력|





### 문자 변환 스트림
- InputStreamReader: InputStream을 Reader로 변환한다.
```java
InputStream is = new FileInputStream("C:/Temp/text.txt");
Redaer reader = IntputStreamReader(is);
```
- OutputStreamWriter : OutputStream을 Writer로 변환한다.
```java
OutputStream os = new FileOutputStream("C://Temp/text.txt");
Writer writer = new OutputStreamWriter(os, "UTF-8");

```

- InputStream -> Reader -> 프로그램 -> Writer -> OutputStream


### 성능 향상 스트림
- BufferedInputStream, BufferedReader
- BufferedOutputStream, BufferedWriter
- 프로그램이 하드디스크의 입출력과 작업하지 않고, 메모리 버퍼와 작업함으로써 실행 성능을 향상시킨다.
- 데이터를 하드디스크에 바로 보내지 않고, 메모리 버퍼에 데이터를 쌓아두었다가 꽉 차면 한꺼번에 하드 디스크로 전송한다.


### 기본 타입 입출력 보조 스트림 - DataInputStream, DataOutputStream

- 바이트 스트림은 바이트 단위로 입출력하기 때문에 기본 데이터 타입으로 입출력할 수 없는데 이를 가능하게 해준다.
- readInt(), readBoolean(), readUTF() 등

### 프린터 보조 스트림- PrintStream, PrintWriter

- print(),println() 메소드를 가지고 있는 보조 스트림.

### 객체 입출력 보조 스트림

## 🍎 IO와 NIO의 차이점

| 구분                 | IO                 | NIO                            |
| -------------------- | ------------------ | ------------------------------ |
| 입출력 방식          | 스트림 방식        | 채널 방식                      |
| 버퍼 방식            | 넌버퍼(non-buffer) | 버퍼(buffer)                   |
| 비동기 방식          | 지원 X             | 지원                           |
| 블로킹/넌블로킹 방식 | 블로킹 방식만 지원 | 블로킹/넌블로킹 방식 모두 지원 |

### IO

- `스트림(Stream)` 기반이다. 따라서 입력 스트림, 출력 스트림 둘 다 필요하다.
- `넌버퍼`
  - 버퍼를 사용하지 않고, 출력 스트림이 1바이트를 쓰면 입력 스트림이 1바이트를 읽는다.
  - 따라서 버퍼를 제공해주는 보조 스트림 BufferdInputStream, BufferedOutputStream을 사용한다.
- `블로킹`을 빠져나올 수 있는 방법은 스트림을 닫는 것 뿐이다.

### NIO

- `채널(channel)` 기반이다. 스트림과 달리 하나의 채널로 입출력이 모두 가능하다.
- `버퍼`
  - 버퍼(메모리 저장소)를 사용하여 여러 개의 바이트를 한번에 입력받고 출력한다.
  - 채널은 데이터를 버퍼에 저장하고, 버퍼에 저장된 데이터를 출력한다.
  - 버퍼 내에서 데이터의 위치를 이동하며 필요한 부분만 읽고 쓸 수 있다.
- 넌블로킹 : Selector를 통해 입출력 작업이 준비완료된 채널만 선택해서 처리하기 때문에 블로킹이 발생하지 않는다.
- 블로킹 : NIO의 블로킹은 스 레드를 interrupt해서 블로킹에서 빠져나올 수 있다.

### 정리

- NIO는 스레드를 계속 생성하지 않고, 재사용한다. 또 버퍼를 사용하여 입출력하기 때문에 입출력 성능이 향상된다.
- 따라서 NIO는 클라이언트 수가 많고, 하나의 입출력 처리가 오래 걸리지 않을 때 사용한다.

- 클라이언트 수가 적고, 전송 데이터가 대용량이면서 순차적으로 처리될 필요성이 있을 때 IO로 구현하는 것이 좋다.

## 🍎 블로킹, 넌블로킹

### 블로킹

- 서버 소켓에서 accept() 하기 전까지 블로킹(대기 상태)이 발생.
- 입력 스트림의 read()를 호출하면 데이터가 입력 되기 전까지 블로킹이 발생.
- 출력 스트림의 write()를 호출하면 데이터가 출력 되기 전까지 블로킹이 발생.

### 넌블로킹

- 입출력 작업 시 스레드가 블로킹되지 않는다.
