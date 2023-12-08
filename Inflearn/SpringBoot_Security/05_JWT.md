## 🍎 RFC
- 벨 연구소에서 내부망을 구축하여 사용하다가 다른 내부망과도 연결해야 하는 문제가 생겼다.
- 그래서 어떤 방식으로 통신할 것인지 규약을 만들어 RFC 1번을 만들었다.
- 이 것이 퍼지고 퍼져 World Wild Web(www)을 만들었다.
- 망들이 커지며 RFC 규칙도 점점 많아졌고, 이것을 HTTP라는 프로토콜이 만들어졌다.
- 그 중 RFC 7519번에 명시된  JWT에 대해 알아보자!

## 🍎 JWT(Json Web Token)
- 정보를 JSON으로 안전하게 전송하기 위한 방법을 고안.
- 서명된 토큰(signed token)에 중점을 둔다. 보안의 문제점 두 가지를 공개키/개인키 쌍을 사용하여 해결.

### JWT 구조
```
xxxxx.yyyyy.zzzzz
```
1. Header : 사용되는 알고리즘( HMAC SHA256 또는 RSA)과 토큰 유형을 구성. 그 다음 Base64Url로 인코딩함.
```json
{
    "alg" : "HS256",
    "typ" : "JWT"
}
```
2. Payload : 클레임을 포함하고 있음. 마찬가지로 Base64Url로 인코딩
    - registered claims : iss(발행자), exp(만료시간), sub(주제), aud(청중) 등 작성
    - public claims
    - private claims : 여기에 user의 정보가 들어있음!

```json
{
    "sub" : "1234567890", //등록된 클레임
    "name" : "John Doe", //공개 클레임
    "admin" : true // 개인 클레임.
}
```
3. Signature
- (Header + Payload + 시크릿값)을 Header에 있는 알고리즘으로 암호화한다. 그 다음 역시 Base64로 인코딩.

```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```

- 세 부분을 Base64로 디코딩하면 데이터를 볼 수 있다. 하지만 JWT는 암호화에 중점을 둔 게 아니라 서명에 중점을 둔 토큰이다. 이 데이터가 유효한지, 무결성에 대한 토큰이다.

### JWT 사용 흐름 (HMAC 사용)
- 클라이언트가 서버에게 요청.
- 서버는 JWT 생성해서 응답 
    - JWT = header를 Base64로 암호화 + payload를 Base64로 암호화 + 시그니처를 Base64로 암호화
    - 시그니처 : HMACSHA256( header + payload + 서버의 시크릿 키) 암호화
    - HMAC : 시크릿을 포함한 암호화 방식
    - SHA256 : 해쉬
- 클라이언트는 JWT를 웹 스토리지에 저장.
- 다음 요청 때 JWT를 담아서 서버에 요청
- 서버에서는 header+payload+시크릿 키를 HMACSHA256으로 암호화한 값과 클라이언트의 JWT의 시그니처를 비교해서 JWT의 유효성을 체크한다.
- 서버는 JWT의 payload에 담겨있는 user의 정보로 DB에 select 할 수도 있음.


- 이 방법은 서버에선 secret 값만 알고 있으면 된다.

### RSA 사용한 JWT
- Header : RSA
- Payload : username
- Signature :( header + payload )를 서버의 개인키로 잠굼.

- JWT를 가지고 클라이언트가 요청하면 서버는 Signature를 자신의 공개키로 복호화하면됨.
