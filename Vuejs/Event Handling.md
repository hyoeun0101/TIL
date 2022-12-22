# 이벤트 핸들링

v-on 의 단축 문법 : @  
예시)

```
v-on:click="handler"
또는
@click="handler"
```

**\*\* handler의 값 두가지**

- 인라인 핸들러 : 이벤트가 트리거될 때 실행. javascript의 onclick 속성과 유사
- 매소드 핸들러 : 컴포넌트에 정의된 메서드 이름, 메서드를 가르키는 경로

## 인라인 핸들러

```javascript
<button @click="count++">1추가</button>
<p>{{ count }}</p>
...
data(){
    return{
        count:0
    }
}
```

```javascript
<button @click="warn('아직 양식을 제출할 수 없습니다.', $event)">
  제출하기
</button>
...
methods: {
  warn(message, event) {
    // 이제 네이티브 이벤트 객체에 접근할 수 있습니다.
    if (event) {
      event.preventDefault()
    }
    alert(message)
  }
}

```

: $event 를 사용하여 DOM 이벤트 객체에 접근할 수 있다.

## 메서드 핸들러

```javascript
<button @click="greet">클릭</button>
...
data() {
  return {
    name: 'Vue.js'
  }
},
methods: {
  greet(event) {
    alert(`안녕 ${this.name}!`)
    // 'event'는 네이티브 DOM 이벤트 객체
    if (event) {
      alert(event.target.tagName)
    }
  }
}
```

event를 매개변수로 받아 `event.target`으로 요소에 접근할 수 있다.

## 이벤트 수식어

- `.stop` : 이벤트 전파 중지. stopPropagation()과 동일한 기능
- `.prevent` : 기본 이벤트의 자동 실행을 중단. preventDefault()와 동일한 기능
- `.self`: 자신일 경우에만 실행. 자식에서 이벤트 발생했다면 실행 안됨. 즉 부모에서 자식을 뺀 부분을 클릭했을 때만 실행됨.
- `.capture`
- `.once`: 이벤트를 한번만 실행
- `.passive`: 기본 이벤트 취소할 수 없음. `.preventDefault()`의 실행을 막음.

```javascript
<!-- 수식어를 연결할 수 있습니다. -->
<a @click.stop.prevent="doThat"></a>

<!-- 이벤트에 핸들러 없이 수식어만 사용할 수 있습니다. -->
<form @submit.prevent></form>

<!-- event.target이 엘리먼트 자신일 경우에만 핸들러가 실행됩니다. -->
<!-- 예를 들어 자식 엘리먼트에서 클릭 액션이 있으면 핸들러가 실행되지 않습니다. -->
<div @click.self="doThat">...</div>

<!-- 이벤트 리스너를 추가할 때 캡처 모드 사용 -->
<!-- 내부 엘리먼트에서 클릭 이벤트 핸들러가 실행되기 전에, 여기에서 먼저 핸들러가 실행됩니다. -->
<div @click.capture="doThis">...</div>


<!-- 핸들러 내 `event.preventDefault()`가 포함되었더라도 -->
<!-- 스크롤 이벤트의 기본 동작(스크롤)이 발생합니다.        -->
<div @scroll.passive="onScroll">...</div>


```

- `@click.prevent.self`

## 이벤트 핸들링

아래에서 e는 이벤트 객체이다.

```javascript
<template>
  <div>
    <button @click="handler">click!</button>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {}
  },
  methods: {
    handler(e) {
      console.log(e)
    }
  }
}
</script>

```

@click에서 handler('value', $event)를 넘겨주면 문자와 이벤트 객체가 출력된다. 다수의 메서드를 호출하려면 메서드 뒤에 괄호를 붙여줘야한다.`@click="method1() method2()"` 이렇게.

## 이벤트 핸들링 - 이벤트 수식어

- @click.prevent : event.prventDefult()와 같음.함수 실행 되고 그 이후의 동작을 막음. 콘솔 찍히고 네이버 안들어가짐.
- @click.once : 한번만 동작
- @click.prvent.once : 처음 클릭하면 콘솔찍히고, 네이버 안들어가짐. 두 번째 클릭은 콘솔 안찍히고 들어가짐.

```javascript
<template>
    <div>
        <a href="https://naver.com"
        target="_blank"
        @click="handler">
        Naver
        </a>
    </div>
</template>
<script>
export default {
    methods: {
        handler(){
            console.log("cal handler")
        }
    }
}</script>

  </template
```

## 이벤트 버블링

child를 클릭하면 parent도 클릭하는 것이기 때문에 parent의 click도 동작한다. child 클릭 실행 다음 parent 클릭 실행. 이벤트가 부모로 타고 올라가 동작함. `B출력 후 A 출력`

```javascript
<template>
    <div>
        <div class="parent" @click="handlerA">
            <div class="child" @click="handlerB"></div>
        </div>
    </div>
</template>
<script>
export default {
    methods: {
        handlerA(){
            console.log("A")
        },
        handlerB(){
            console.log("B")
        }

    }
}</script>

```

이를 방지하기 위해선 child에서 다음과 같이 작성하여 이벤트 전파를 막을 수 있다.

```javascript
handlerB(e){
    e.stopPopagation()
}
또는
@click.stop="handlerB"
```

## 이벤트 캡쳐링

이벤트 버블링의 반대. 부모 요소에서 자식 요소로 내려오는 것. 자식을 클릭했는데 A출력 후 B출력하려면 다음과 같이 작성한다.

```javascript
@click.capture="handlerA"
```

- click.self 는 부모-자식 상관없이 자신의 영역 클릭했을 때 동작.

- @wheel.passive : 휠과 함수와 분리되어 동작
- @keydown.key="함수"

  - key는 키보드키 의미

- v-model.lazy="msg" : 변경내용 작성 후 다른 곳 클릭해야 msg 값이 변경됨.
