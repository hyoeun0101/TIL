- User -> My Application -> Their Application
- 나의 서비스에서 구글 캘런더와 같은 다른 서비스를 사용할 때 어떻게 해야할까?
- OAuth를 통해서 액세스 토큰을 얻어 나의 서비스에서 다른 서비스의 API를 호출 할 수 있다!

## 🍎 OAuth에 등장하는 세 가지 주체
1. Resource Owner = User
2. Client = My Application
3. Resource Server = Their Application (+ Authorization Server)

### 1. Resource Server에 client 등록

- client가 resource server를 사용하기 위해선 사전에 승인을 받아야 함. 원하는 서비스(ex)google, facebook 등)에 가서 client를 등록해야 함.
- 서비스마다 등록하는 방법이 다르지만 등록할 떄 공통적으로 생성하는 정보는 다음과 같다.
    - ClientID : 나의 어플리케이션의 식별자
    - ClientSecret : 그것에 대한 비밀번호. 절대 노출하면 안됨.
    - Authorized redirect URIs : 리소스 서버가 권한을 부여한 uri. client는 해당 uri로만 요청할 수 있음.

### 2. Resource owner의 승인

1. user가 `Login with Goole` 버튼 클릭. -> resource server에게 요청.
    - `Login with Goole` 버튼의 url은 다음과 같다.
    - https://resource.server?cliend_id=1&scope=B,C&redirect_uri=https://client/callback

2.  resource server는 user가 로그인 안되었으면 구글 로그인 페이지 반환
3. user가 로그인 성공.
4. resource server는 user가 요청한 url을 토대로 clientId 값이 있는지 확인, 자신이 가지고 있는 clientId의 redirect_uri와 user가 보낸 redirect_uri가 일치하는 확인. 일치하지 않으면 끝.
5. 일치하면 user에게 client에서 해당 scope 기능을 사용할 것을 허용하냐는 페이지 보여줌. 
6. user가 허용하면, resource server는 user_id와 user_id가 허용한 scope를 저장.


### 3. Resource Server의 승인

1. resource server는 임시 비밀번호인 authorization code를 client에게 전송. how? 헤더에 `Location: https://client/callback?code=코드값`을 넣어서 반환
2. user는 자동으로 저 uri로 리다이렉션. 그럼 client는 authorization code를 받음.
3. client는 resource server에게 요청.
    - https://resource.server/token?grant_type=authorization_code&code=3&redirect_uri=https://client/callback&client_id=1&client_secret=2
    - 즉 client는 secret인 client_secret과 authorization_token을 가지고 resource server에게 요청하는 것. 
4. resource server는 해당 요청과 자신이 가지고 있는 client_id, client_secret, redirect_Url,authorization_code가 일치하는지 확인

### 4. 액세스 토큰 발급

1. resource server는 authorization_code 삭제, authorization_code에 대한 user_id를 찾아 그 사용자에 대한 액세스 토큰 생성 후 client에게 반환
    - 그럼 액세스 토큰은 user_id=1인 사용자의 scope 기능에 대해 권한을 가짐을 의미.

### 5. API 호출

- client는 이제 액세스 토큰을 가지고 resource server의 API를 호출할 수 있음. 방법은 서비스마다 다르니 찾아볼 것. 
- get요청의 파람 또는 요청 헤더의 `Authorization: Bearer`에 액세스 토큰을 넣어서 요청할 수 있는데 후자 방법을 더 추천.

### 6. refresh token
- 액세스 토큰은 수명이 있음. 액세스 토큰을 재발급할 때 리프레시 토큰을 사용함.
- oauth 2.0 rfc 6749 볼 것
- 서비스마다 리프레시 토큰 방법은 다르니 메뉴얼은 찾아볼 것.

1. client가 authorization server에게 권한 요청
2. authorization server은 액세스 토큰, 리프레시 토큰 발급 후 반환 
3. client는 resource server에게 액세스 토큰을 가지고 요청, resource server는 (권한이 필요한) 자원 반환
4. 액세스 토큰으로 요청했는데, 유효하지 않은 토큰이면 client는 리프레시 토큰으로 Authorization server에게 요청, 그럼 액세스 토큰(optoional 리프레시 토큰) 반환