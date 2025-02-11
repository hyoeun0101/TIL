### 2025-02-07-금
recaptcha 적용

- 로그인 제한 우선순위에 추가하여 적용.

- 리캡챠 on/off가 의미없다고 생각하는게 로그인 2factor처럼 on/off를 한다면, 로그인 성공 후에 exception을 던지는거기 때문에 이 상황에 대해서 봇을 체크하는 건 의미없음

    - 그럼 DB에다가 on/off 기능을 넣겠지.

- 원하는 건 과금때문에 on/off를 하고 싶은 거다.

- 과금이 script를 띄우는게 아니라 api 쏘는 횟수로 과금을 매긴다면 on/off는 굳이 안해도 되지 않을까.



- 과금
    - 새 키 생성 : 기본 기능 세트, 매월 만회의 무료 평가 제공 Essentials 등급
    - 프로젝트에서 결제 사용 설정하면 고급 기능과 매월 만회 무료 + Standard 등급 업그레이드

    - Standard 등급에서 매월 만회 평과 하면 최대 십만회의 평가에 대하 $8 요금 청구

    - 매월 10만회 평가 통과하면 Enterprise 등급으로 자동전환. 10만 이상부터 평가당 $0.001 청구.

    
### Memo


https://cloud.google.com/recaptcha/docs?hl=ko

https://console.cloud.google.com/security/recaptcha?hl=ko&inv=1&invt=Abo53w&project=polar-strata-405810

### 할당량 및 한도

https://cloud.google.com/recaptcha/quotas?hl=ko


- 할당량 초과했을 시 예외 처리 필요.


#### 해야할 것.
- 과금 매기는 방식.

- on/off 가능여부


---
## 2025-02-10 - 신규알림
1. add_info에 신규알림 컬럼 추가

2. device 관리 테이블 알림발송여부 컬럼 추가

3. 신규알림 초기화에 대해선 아직 논의 중. (신규기기처럼 off하면 초기화하는지, insert 할 때만 알림전송할 건지)

4. mber_login_hist에 os명(window safari) 분기문 확인하기. 이메일에는 chrome 이렇게 들어가야 함.

5. 발송로그(알림전송 후 로그) -> type : login, device, alarm

6. email 전송은 확정. 어떤 액션 추가하면 알림톡도 전송. 이건 아직 기획안 논의 중.

- v3는 3월 오픈, 2월 개발 끝
- 신규알람은 4월 오픈, 3월 개발 끝


## 2025-02-11
### reCAPTCHA
- 리캡챠 : 2factor 발송

- 리캡챠 이력은 webapp에.

- mber_s_2factor_send에 캡챠 컬럼 4개(전송건수, 전송 제한 일시, 인증요청 제한일시, 성공일시) 추가

- mber_s_2factor_send_log에 인증유형 캡챠 추가 (최초프)

- 리캡챠 2factor는 모바일, 관계사 둘 다 적용.

### 신규알림
- 이메일 : 일시, ip, os

### 미정 내용

- 알림 전송 이메일, 알림톡

- 신규기기 알람 사용여부 초기화

- 알림톡 여부 (공수 고려해서)
    - 알림톡 O : 리캡챠 모바일은 2factor 로직, 관계사는 본인인증.

    - 알림톡 X : 리캡챠 모바일, 관계사 둘다 2factor 로직.




### 해야할 것

- ⬜ 리캡챠 로직 추가

- ⬜ 2factor 로그인, 신규기기 로직 수정 : 발송제한 로직.


