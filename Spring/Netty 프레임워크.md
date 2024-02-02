- 필요한 배경 지식 : 소켓, NIO
- netty 기본 개념 정리.

---

## 🍎 Netty?

- `비동기 이벤트 기반 네트워크 프레임워크`이다. 따라서 더 많은 커넥션을 처리할 수 있는 장점이 았다.
- 네티는 추상화 모델을 제공하여 개발자가 간단하게 코드 작성을 할 수 있게 해주며, 안정적이고 빠른 네트워크 어플리케이션을 개발할 수 있게 해준다.

## 🍎 Netty의 핵심 컴포넌트

### Channel

- 입출력 작업을 수행할 수 있는 Open된 Connection을 의미한다. 쉽게 생각하면 입출력 데이터를 운반하는 운송수단으로 볼 수 있다.

### CallBack

- ChannelHandler 인터페이스 구현을 통해 이벤트를 처리할 때, 처리하는 과정에서 콜백을 사용한다. ex) channelRead(), channelActive() 등

### Future

- Future를 사용하여 이벤트 작업이 완료되었을 때 어플리케이션에게 알리는 작업을 할 수 있다.
- JDK에서 java.util.concurrent.Future 인터페이스를 제공하는데, Future 인터페이스의 구현은 `작업 완료 여부`와 `완료전까지 블로킹`하는 기능만 제공한다.
- 네티에서는 Future를 개선한 `ChannelFuture` 인터페이스를 제공한다. ChannelFuture에 ChannelFutureListener를 등록하여 작업 처리에 대한 설정을 할 수 있다. ex) `ChannelFutureListener.CLOSE` : 작업 완료 시 채널을 닫음.
- 네티의 모든 입출력은 ChannelFuture를 반환하며 진행에 있어 블로킹 작업은 없다. 모든 것은 비동기 이벤트 기반이다.

## 🍎 네티 동작 방식

1. BootStrap : Netty를 구동하기 위한 클래스
2. EventLoopGroup : 여러 EventLoop를 묶은 그룹. 같은 그룹에 속한 EventLoop들은 스레드 등 몇몇 리소스를 공유한다. 즉 같은 그룹의 EventLoop는 스레드 같은 리소스들을 공유한다.
3. EventLoop : channel에서 발생하는 이벤트를 체크하고, 이벤트가 발생하면 핸들러에게 전달하는 역할
4. SocketChannel
5. ChannelPipeline : ChannelPipeline에 ChannelHandler를 순서대로 연결한다. ChannelPipeline을 통해 채널을 ChannelHandler에게 전달한다.
6. ChannelHandler : 채널에서 발생한 이벤트를 수신하고 처리한다. 들어오는 Inbound는 ChannelInboundHandler가, 나가는 Outbound는 ChannelOutboundHandler가 처리한다.

## 🍎 부트스트랩

- 네티를 구동하기 위한 클래스, 네티로 작성한 프로그램이 시작하면 제일 먼저 수행된다.
- 어플리케이션이 수행할 동작과 설정을 지정하는 클래스이다.
- `Bootstrap`, `ServerBootstrap`이 있다. `Bootstrap`은 원격 호스트/포트와 연결하며, EventLoopGroup은 하나를 가지고 있다. `ServerBootstrap`은 로컬 포트로 바인딩하는 기능을 하며, EventLoopGroup을 2개 가지고 있다.

### ServerBootstrap API

- `group`

  - 서버는 이벤트 루프 그룹 두 가지가 필요하다. 연결 수락을 위한 그룹, 데이테 송수신 처리를 위한 그룹
  - group()을 사용하여 이벤트 루프를 지정한다.

- `channel`

  - channel()에 등록된 소켓 채널 클래스가 소켓 채널을 생성한다.
  - ex) LocalServerChannel.class, OioServerSocketChannel.class, NioServerSocketChannel.class 등

- `option`

  - 서버 소켓 채널의 옵션(동작방식) 지정.
  - 커널에서 사용하는 소켓 관련 설정값을 변경.
  - socket.send가 호출되면 커널의 시스템 함수가 호출되고, 송신용 커널 버퍼에 어플리케이션에서 전송한 데이터가 저장된다.

- `childOption`

  - 서버에 접속한 클라이언트 소켓 채널에 대한 옵션 지정

- `handler`

  - 서버 소켓 채널의 이벤트를 처리할 핸들러 설정

- `childHandler`
  - 클라이언트 소켓 채널의 이벤트를 처리할 핸들러 설정

```java
public class NettyRelayServer {
    public static main(String[] args) {
        //클라이언트의 연결을 수락하는 부모 쓰레드
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //클라이언트 소켓과 연결된 소켓의 데이터 입출력 및 이벤트 처리를 담당하는 자식 쓰레드
        EventLoopGroup workerGroup = new NioEventLoopGroup(300);
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // NoiServerSocketChannel로 소켓 채널 생성.
                    .option(ChannelOption.SO_BACKLOG, nettyProperties.getSo_backLog())
                    .option(ChannelOption.SO_RCVBUF, nettyProperties.getSo_recvBuf())
                    .childOption(ChannelOption.SO_KEEPALIVE, nettyProperties.isSo_keepAlive())
                    .childOption(ChannelOption.TCP_NODELAY, nettyProperties.isSo_tcpNoDelay())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 클라이언트 소켓 채널이 생성될 때 실행한다. 즉 소켓 생성 이벤트가 발생하면 실행됨.
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 채널 파이프라인 설정.
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            channelPipeline.addLast("Decoder", new NettyEncoderDecoder.NettyDecoder());
                            channelPipeline.addLast("Encoder", new NettyEncoderDecoder.NettyEncoder());
                            channelPipeline.addLast("RelayServerHandler", new NettyRelayServerHandler(new PacketNavigator(roCommonMapper)));
                        }
                    });

            ChannelFuture cf = bootstrap.bind().sync(); // sync()는 바인딩이 완료되길 대기한다.

            cf.channel().closeFuture().sync(); //채널의 CloseFuture를 얻고 완료될 때까지 현재 스레드를 블로킹
        } finally {
          workerGroup.shutdownGracefully();
          bossGroup.shutdownGracefully();
        }
    }
}
```
### option 종류
- SO_BACKLOG : (int) 동시에 수용 가능한 클라이언트 연결 요청 수
- SO_KEEPALIVE : (boolean) 지정된 시간마다 keep-alive 패킷을 상대방에게 전송하는지 여부 설정. 상대는 정상이면 ACK 패킷을 전송함. 상대가 답이 없거나 다른 답을 하면 소켓 자동 종료
- TCP_NODELAY : Nagle 알고리즘 비활성화 여부 설정, 반응속도를 높이기 위해선 비활성화.
- SO_SNDBUF : 커널 송신 크기 설정
- SO_RCVBUF : 커널 수신 크기 설정. 보통 UDP에서 사용(?) 보통 TCP에서는 크기가 지정되어 오고감.
- SO_REUSEADDR : (boolean) TIME_WAIT 상태의 포트를 재사용할 수 있도록 설정
- SO_LINGER : (int) 소켓을 close할 때 송신 버퍼에 남은 데이터 전송 대기 시간 지정. 소켓을 닫을 때 신뢰성 있는 종료를 위해 4way-handshake가 발생하고, TIME_WAIT로 리소스가 낭비된다. 이를 방지하기 위해 0으로 설정한다.
- CONNECT_TIMEOUT_MILLIS : 연결하는데 소요되는 최대 시간. 즉 3way-handshake가 발생하는 시간. 아웃되면 어떻게 되는데????

## 🍎 ChannelPipeline

- 채널에서 발생한 이벤트가 이동하는 통로 역할을 한다. 즉 채널과 ChannelHandler를 연결해주는 통로이다.
- EventLoop가 채널에서 데이터를 읽으면 데이터는 첫번째 ChannelHandler에게 넘겨진다. 그럼 ChannelHandler에서 데이터 처리를 한다.
- ChannelInitializer.initChannel()를 호출하면 ChannelHandler를 ChannelPipeline에 등록한다.
- ChannelHandler를 하나 추가할 때 ChannelHandler와 ChannelPipeline을 바인딩하는 `ChannelHandlerContext`가 하나 생성된다.

```java
ServerBootstrap bs = new ServerBootstrap();
bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
    @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
      // 채널 파이프라인 설정.
      ChannelPipeline channelPipeline = socketChannel.pipeline();
      channelPipeline.addLast("Decoder", new NettyDecoder());
      channelPipeline.addLast("Encoder", new NettyEncoder());
      channelPipeline.addLast("RelayServerHandler", new NettyRelayServerHandler());
  }
})
```

## 🍎 EventLoopGroup, EventLoop

- 위의 설명한 바와 같이 ServerBootstrap은 두 개의 EventLoopGroup을 필요로 한다.
- 첫 번째 EventLoopGroup은 연결 요청이 들어오면 그 요청에 대한 Channel을 생성하는 EventLoop를 하나 지정한 후, 연결을 수락한다.(bossGroup)
- 연결이 수락되면 두 번째 EventLoopGroup은 해당 Channel에 EventLoop를 할당한다. Channel에 할당된 이 EventLoop가 이벤트를 처리하는 일을 한다. (workerGroup)

```java
//클라이언트의 연결을 수락하는 부모 쓰레드
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//클라이언트 소켓과 연결된 소켓의 데이터 입출력 및 이벤트 처리를 담당하는 자식 쓰레드
EventLoopGroup workerGroup = new NioEventLoopGroup(300);
ServerBootstrap bootstrap = new ServerBootstrap();
bootstrap.group(bossGroup, workerGroup);
```

### NioEventLoopGroup

- NIO Seletor 기반 채널이 사용되는 멀티 쓰레드 이벤트루프 그룹.

## 🍎 ChannelHandler

- 채널에서 발생한 이벤트를 ChannelPipeline으로부터 받아 수신하고 처리한다.
- 네티는 소켓 채널에서 발생하는 이벤트를 인바운드 이벤트와 아웃바운드 이벤트로 추상화하여 실제로는 ChannelInboundHandler, ChannelOutboundHandler 인터페이스를 구현한다.
- 네티는 비지니스 로직을 쉽게 개발할 수 있도록 어댑터 클래스의 형태로 여러 기본 핸들러를 제공한다.
- ex) `ChannelHandlerAdapter`, `ChannelInboundHandlerAdapter`, `ChannelOutboundHandlerAdapter`, `ChannelDuplexHandlerAdapter`

### 인바운드 이벤트 핸들러의 메서드 콜백

1. `channelRegistered()` : 채널이 이벤트 루프에 등록되었을 때 발생, 새로운 채널이 생성되면 발생.
2. `channelActive()` : channelRegistered 이후에 발생. 입출력을 수행할 상태가 되었음. 연결 직후 발생.
3. `channelRead()` : 데이터가 수신될 때 실행. 수신된 데이터는 ByteBuf 객체에 있음.
4. `channelReadComplete()` : 데이터 수신이 완료되었을 때 실행. 채널의 데이터를 다 읽고 더이상 읽을 게 없을 때 발생.
5. `channelInactive()` : 채널 비활성시 발생. 이 이후에는 채널에 대한 입출력 작업을 할 수 없음.
6. `channelUnregistered()` : 채널이 이벤트 루프에서 제거되었을 때 발생.

- `exceptionCaught()` : 오류 발생하면 실행. 실행한 다음 channelReadComplete 동작하고 그 이후의 동작은 위와 동일하다.

## 🍎 ChannelHandlerContext

- 채널 핸들러가 채널 파이프라인 및 다른 핸들러와 상호작용할 수 있게 해준다.
- 핸들러를 파이프라인에 추가할 때 그 둘을 바인딩하기 위해 ChannelHandlerContext를 생성한다.
- close() : 작업이 성공해서 완료했거나 오류 발생했을 때, 채널을 닫고, ChannelFuture에게 알린다.

---

- 코드 작성하면서 모르는 개념 정리

---

## 🍎 디코딩, 인코딩하는 핸들러 만들기

```java
package com.mycloudmembership.prelaysocket2.netty;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mycloudmembership.prelaysocket2.utils.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyEncoderDecoder {
    // 아웃바운드 핸들러 등록
    @Component
    public static class NettyEncoder extends MessageToByteEncoder<byte[]> {
        @Override
        protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        	log.debug("ByteBuf : {}", out);
        	log.debug("msg : {}",msg);
            out.writeBytes(msg);
        }
    }

    /*
     * 인바운드 핸들러. 들어온 데이터를 사용하려면 디코딩을 한 후 사용 가능
     * 1. channelActive 실행 => decode 실행 => channelRead 실행
     * ByteBuf를 byte[]로 변환한 후 List에 저장한다. 디코더를 통해 변환해야 channelRead()의 msg를 사용할 수 있다.
     * ByteBuf의 구현체는 PooledUnsafeDirectByteBuf 이다.
     */
    @Component
    public static class NettyDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            log.debug("ByteBuf : {}", in);
            String msg = in.toString(Charset.forName("EUC-KR"));
            byte[] inData = StringUtils.getStringToByteEUC(msg);
            in.readerIndex(in.readerIndex() + in.readableBytes());
            log.debug("readerIndex : {}", in.readerIndex());
            log.debug("readableBytes : {}", in.readableBytes());
            out.add(inData);
        }
    }
}

```

## 🍎 @ChannelHandler.Sharable

```java
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
  ...
}
```

- 이 애너테이션을 붙이면, 핸들러의 동일한 인스턴스를 여러 채널 파이프라인에 등록할 수 있다.
- 붙이지 않으면 파이프라인에 등록할 때, 계속 새로운 인스턴스를 생성해줘야한다.

## 🍎 NioEventLoopGroup

- NIO Seletor 기반 채널이 사용되는 멀티 쓰레드 이벤트루프 그룹.

```java
//클라이언트의 연결을 수락하는 부모 쓰레드
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//클라이언트 소켓과 연결된 소켓의 데이터 입출력 및 이벤트 처리를 담당하는 자식 쓰레드
EventLoopGroup workerGroup = new NioEventLoopGroup(300);
ServerBootstrap bootstrap = new ServerBootstrap();
bootstrap.group(bossGroup, workerGroup)
```

## 🍎 ByteToMessageDecoder

- @Sharalble을 붙일 수 없음.
