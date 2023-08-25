## push

- 해당 url로 이동하기.
- 히스토리 스택에 추가되므로 뒤로가기 버튼 동작 시 이전 URL로 이동한다.

```javascript
this.$router.push("home");
this.$router.push({ name: "product", params: { id: "P001" } });
```

## replace

- 현재 URL을 대체하기 때문에 스택에 쌓이지 않는다.

```javascript
this.$router.push("home");
this.$router.replace("about"); // home의 url에서 about의 url로 대체
```

## go

- 숫자만큼 뒤로가기 또는 앞으로 가기

```javascript
this.$router.go(-1); // 이전 페이지
this.$router.go(2); // 두 단계 앞으로
```
