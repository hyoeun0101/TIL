## 라이프 사이클 훅

```
처음 `createApp(app).mount('#app')` 마운트를 시작
```

1. `beforCreat()` : 여기서 변수를 가져오면 읽을 수 없음. 아직 읽기 전

```
event & lifecycle 초기화
```

2. `created()`: 여기서 읽기 가능

```
템플릿 컴파일
```

3. `beforeMount()`: 여기서 아직 태그 못읽음

```
DOM 노드 생성 및 삽입(html 생성)
```

4. `mounted()` : 태그 읽기 가능,초기 렌더링과 DOM노드 생성 완료 후 코드를 실행하는 데 사용.
