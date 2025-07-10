## 🔴 OAuth2
- 나의 서비스에서 구글 캘런더와 같은 다른 서비스를 사용하고 싶을 때 어떻게 해야할까?

- OAuth를 통해서 액세스 토큰을 얻어 나의 서비스에서 다른 서비스의 API를 호출할 수 있다.


- 사용자 -> 나의 서비스에 들어와 -> 구글과 같은 다른 서비스 이용 가능.


## 🔴 OAuth에 등장하는 세 가지 주체

- 사용자 = User = Resource Owner 

- 나의 서비스 = My Application =  Client

- 다른 서비스 = Their Application = Resource Server (+Authorization Server)

## 🔴 동작방식

### 🟡 1. 다른 서비스에 나의 서비스 등록
- 나의 서비스에서 다른 서비스를 이용하기 위해서 승인이 필요하다.

- ex) kakao developer에 가서 나의 서비스를 등록해야함.

- 등록하면 생성되는 정보
    - clientId : 나의 서비스의 식별자

    - clientSecret : 그것에 대한 비밀번호. 노출 절대 금지.

    - Authorized redirect URIs : 권한을 부여한 uri. 콜백 url

    - Authorized redirect URIs : 리소스 서버가 권한을 부여한 uri. client는 해당 uri로만 요청할 수 있음.

### 🟡 2. 리소스 서버의 승인

1. 사용자가 '카카오 로그인' 버튼을 클릭하면 리소스 서버에게 요청이 간다.
    - ex) https://resource.server?cliend_id=1&scope=B,C&redirect_uri=https://client/callback 

2. 리소스 서버의 동작 -> 사용자의 계정으로 카카오 로그인 성공

    - 리소스 서버는 사용자가 요청한 url을 토대로 clientId 값을 확인한다.
    - clientId와 redirect_uri가 일치하는지 검증.

    - (+) 사용자에게 나의 서비스에서 해당 scope 기능 사용을 허용하는지 체크함.

3. 리소스 서버는 user_id와 scope를 저장한다.


### 🟡 3. Resource Server의 승인

1. resource server는 임시 비밀번호인 authorization code를 user에게 전송. how? 헤더에 `Location: https://client/callback?code=코드값`을 넣어서 반환
2. user는 자동으로 저 uri로 리다이렉션. 그럼 client는 authorization code를 받음.
3. client는 resource server에게 요청.
    - https://resource.server/token?grant_type=authorization_code&code=3&redirect_uri=https://client/callback&client_id=1&client_secret=2
    - 즉 client는 secret인 client_secret과 authorization_code를 가지고 resource server에게 요청하는 것. 
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