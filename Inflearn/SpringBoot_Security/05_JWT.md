## 🔴 RFC
- 벨 연구소에서 내부망을 구축하여 사용하다가 다른 내부망과도 연결해야 하는 문제가 생겼다.

- 그래서 어떤 방식으로 통신할 것인지 규약을 만들어 RFC 1번을 만들었다.
- 이 것이 퍼지고 퍼져 World Wild Web(www)을 만들었다.
- 망들이 커지며 RFC 규칙도 점점 많아졌고, 이것을 HTTP라는 프로토콜이 만들어졌다.
- 그 중 RFC 7519번에 명시된  JWT에 대해 알아보자!


## 🔴 JWT(Json Web Token)
- 말그대로 JSON 형태로 된 토큰이다. JWT에는 인증에 필요한 정보들이 암호화되어 있으며 JWT의 정보로 인증이 가능하다.

- 큰 로직은 다음과 같다.
    1. 클라이언트가 서버에 요청하면 서버에서 해당 클라이언트를 인증하고 JWT를 만들어 응답한다.

    2. 그럼 클라이언트는 다음 요청 때 JWT를 함께 담아 요청한다.

    3. 서버는 JWT 토큰을 통해 클라이언트를 검증하고, JWT에 담겨있는 클라이언트의 정보를 활용할 수 있다.

- **JWT로 어떻게 클라이언트를 인증할 수 있을까?**

    - JWT에서는 **공개키/개인키의 쌍**을 활용하여 검증을 진행한다.

    - 이를 이해하기 위해서는 먼저 JWT의 구조를 이해해야 한다.
    

## 🔴 JWT의 구조 3가지
- JWT는 `header`, `payload`, `signature` 3가지로 구성되어 있다.

- 이 3가지는 도트(.)를 기준으로 나눠져 있다.

```json
// JWT의 형태
"header.payload.signature"
```


### 🟡 header

- header는 정보를 암호화하는 데에 사용된 알고리즘, 토큰 유형을 나타낸다. 
- 그리고 이 정보들을 Base64Url로 인코딩한다.


```json

header = base64UrlEncode({
    "alg" : "HS256", //암호알고리즘
    "typ" : "JWT" //토큰 유형
})

```

### 🟡 payload

- 사용자의 정보를 가지고 있는 부분이다.

- payload의 구성을 3가지 클레임으로 나눌 수 있다.

    - `registered claims` : 필수X. 유용한 정보를 담음. ex) `iss`(발행자), `exp`(만료시간), `sub`(제목), `aud`(대상) 등
    
    - `public claims`: 사용자가 자유롭게 정의하여 사용.

    - `private claims` : 클라이언트의 정보 들어있음.

- 그리고 클레임을 Base64Url로 인코딩한다.

```json
payload = base64UrlEncode({
    "sub" : "1234567890", //registered claims
    "name" : "John Doe", //자유롭게 정의...
    "admin" : true
})

```

- 추가로 클레임의 이름은 3자로 제한된다.

- JWT는 누구나 읽을 수 있기 때문에 암호화되지 않은 비밀 정보는 넣으면 안된다는 것에 주의하자.

- payload에 담겨 있는 사용자의 정보를 활용할 수 있다. 
    - 예를 들어 payload에 사용자의 pk를 넣어둬서 DB에서 필요한 값들을 조회할 수도 있다. 


### 🟡 signature

- signature는 JWT의 무결성을 확인하는데 사용된다. 
    - 전송하는데 변조가 일어나지 않았는지 signature로 확인할 수 있다.

- 별도의 시크릿 키가 필요하다. 

```json
// header의 알고리즘이 HMACSHA256이라면

signature= HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)

```
- 추가로 HMAC은 시크릿을 포함한 암호화 방식, SHA256은 해쉬를 이용한 방식이다.

- 서버에서 JWT를 받으면 JWT의 header + payload + 서버가 가지고 있는 secret을 조합하여 sigature를 만든다.
    
    - 그리고 JWT의 signature와 자신이 만든 signature를 비교하여 검증을 한다.


### RSA 사용한 JWT
- Header : RSA
- Payload : username
- Signature :( header + payload )를 서버의 개인키로 잠굼.

- JWT를 가지고 클라이언트가 요청하면 서버는 Signature를 자신의 공개키로 복호화하면됨.
