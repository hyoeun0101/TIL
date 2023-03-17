## 🍎 Netty?

- `비동기 이벤트 기반 네트워크 프레임워크`이다.
- 네티는 추상화 모델을 제공하여 개발자가 간단하게 코드 작성을 할 수 있게 해주며, 안정적이고 빠른 네트워크 어플리케이션을 개발할 수 있게 해준다.

### 기존 소켓 프로그래밍 - 블로킹 소켓

- 예) Socket, ServerSocket
- 클라이언트의 요청마다 쓰레드가 생성된다. -> 자원 낭비, 잦은 문맥 교환으로 deadlock 발생
- 클라이언트가 요청을 하면 서버에서 연결 수락과 처리 완료까지 Blocking이 발생한다.

### 논블로킹 소켓

- ex) SocketChannel, ServerSocketChannel
- 클라이언트 당 스레드를 생성하지 않고, `Selector`가 이벤트 통지 API를 사용하여 `Channel`에서 이벤트가 발생하면 스레드에게 통지를 한다.
- `Channel` : Open된 Connection을 말한다.
- 네트워크 프로그램에서 이벤트 발생 주체는 `소켓`이며, `소켓 연결`, `데이터 송수신` 등의 이벤트가 발생한다.
- 이벤트가 공유하는 데이터 객체를 생성하고 그 객체를 통해 채널로 데이터를 전송한다.

## 🍎 네티 동작 방식

1. BootStrap : Netty를 구동하기 위한 클래스
2. EventLoopGroup : EventLoop의 그룹
3. EventLoop : channel에서 발생하는 이벤트를 체크하고, 이벤트가 발생하면 핸들러에게 전달하는 역할
4. SocketChannel :
5. ChannelPipeline
6. ChannelHandler

## 🍎 부트스트랩

- 네티를 구동하기 위한 클래스, 네티로 작성한 프로그램이 시작하면 제일 먼저 수행된다.
- 어플리케이션이 수행할 동작과 설정을 지정하는 클래스이다.

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
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // NoiServerSocketChannel로 소켓 채널 생성.
                    .option(ChannelOption.SO_BACKLOG, nettyProperties.getSo_backLog())
                    .option(ChannelOption.SO_RCVBUF, nettyProperties.getSo_recvBuf())
                    .childOption(ChannelOption.SO_KEEPALIVE, nettyProperties.isSo_keepAlive())
                    .childOption(ChannelOption.TCP_NODELAY, nettyProperties.isSo_tcpNoDelay())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 클라이언트 소켓 채널이 생성될 때 실행.
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

            cf.channel().closeFuture().sync(); //채널의 CloseFuture를 얻고 완료될 때까지 현재 스레드를 블로킹한다.
        } finally {
          workerGroup.shutdownGracefully();
          bossGroup.shutdownGracefully();
        }
    }
}
```

- ChannelFuture는 작업이 완료되면 그 결과에 접근할 수 있게 해주는 자리 표시자 역할을 하는 인터페이스다.

### option 종류

- SO_BACKLOG : 동시에 수용 가능한 클라이언트의 연결 요청 수
- SO_RCVBUF, SO_SNDBUF : 커널의 수신 버퍼 크기, 송신 버퍼 크기 조정. 보통 UDP에서 사용, 보통 TCP에서는 크기가 지정되서 오고감.
- SO_KEEPALIVE : 커널에서 지정된 시간에 한번씩 keepAlive 패킷을 상대방에게 전송, 상대는 정상이면 ACK 패킷 전송. 상대가 답이 없거나 다른 답을 하면 소켓 자동 종료
- TCP_NODELAY : 데이터 송수신에 Nagle 알고리즘 비활성화 여부 지정

## 🍎 ChannelPipeline

- 채널에서 발생한 이벤트가 이동하는 통로 역할을 한다. 즉 채널과 ChannelHandler를 연결해주는 통로이다.
- EventLoop가 채널에서 데이터를 읽으면 데이터는 첫번째 ChannelHandler에게 넘겨진다. 그럼 ChannelHandler에서 데이터 처리를 한다.

### 🍎 ChannelHandler

- 채널에서 발생한 이벤트를 ChannelPipeline으로부터 받아 수신하고 처리한다.
- 네티는 소켓 채널에서 발생하느 이벤트를 인바운드 이벤트와 아웃바운드 이벤트로 추상화한다.
- ChannelInboundHandler, ChannelOutboundHandler 인터페이스를 구현한다.

### 인바운드 이벤트 순서

1. channelRegistered : 채널이 이벤트 루프에 등록되었을 때 발생, 새로운 채널이 생성되면 발생.
2. channelActive : channelRegistered 이후에 발생. 입출력을 수행할 상태가 되었음. 연결 직후
3. channelRead : 데이터가 수신될 때 실행. 수신된 데이터는 ByteBuf 객체에 있음.
4. channelReadComplete : 데이터 수신이 완료되었을 때 실행. 채널의 데이터를 다 읽고 더이상 읽을 게 없을 때 발생.
5. channelInactive : 채널 비활성시 발생. 이 이후에는 채널에 대한 입출력 작업을 할 수 없음.
6. channelUnregistered : 채널이 이벤트 루프에서 제거되었을 때 발생.
