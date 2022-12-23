# 이벤트 핸들링

v-on 의 단축 문법 : @  
예시)

```
v-on:click="handler"
또는
@click="handler"
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

- 다수의 메서드를 호출하려면 메서드 뒤에 괄호를 붙여줘야한다. `@click="method1() method2()"` 이렇게.

## 다양한 이벤트명

| 이벤트명  | 설명                                             |
| --------- | ------------------------------------------------ |
| click     | 마우스를 클릭했을 때 실행함                      |
| dblclick  | 마우스를 더블 클릭했을 때 실행함                 |
| mouseover | 마우스의 포인트가 요소 위로 올라왔을 때 실행함   |
| mouseout  | 마우스의 포인트가 요소 밖으로 벗어났을 때 실행함 |
| mousemove | 마우스의 포인트가 이동했을 때 실행함             |
| mousedown | 마우스의 버튼을 눌렀을 때 실행함                 |
| mouseup   | 마우스의 버튼을 놓았을 때 실행함                 |
| keydown   | 키보드의 키를 눌렀을 때 실행함                   |
| keyup     | 키보드의 키를 놓았을 때 실행함                   |
| keypress  | 키보드의 키를 눌렀다가 놓았을 때 실행함          |
| change    | 요소가 변경될 때 실행함                          |
| submit    | `<Form>`이 제출될 때 실행함                      |
| reset     | `<Form>`이 재설정될 때 실행함                    |
| select    | `<select>`의 값이 선택되었을 때 실행함           |
| focus     | 태그에 포커스가 있을 때 실행함                   |
| blur      | 태그에 포커스를 잃었을 때 실행함                 |

## 이벤트 수식어

- `.stop` : 이벤트 전파 중지. stopPropagation()과 동일한 기능
- `.prevent` : 기본 이벤트의 자동 실행을 중단. preventDefault()와 동일한 기능
- `.self` : 자신일 경우에만 실행. 원래 자식을 클릭하면 버블링으로 인해 부모도 이벤트 발생하는데 부모에 `.self`를 붙이면 자식을 클릭했을 때 부모는 발생 안된다.
- `.capture` : 이걸 쓴 이벤트가 먼저 실행된다.
- `.once` : 이벤트를 한번만 실행
- `.passive` : 기본 이벤트 취소할 수 없음. 핸들러 내에 `event.preventDefault()`가 있어도 기본 이벤트가 동작함.

```javascript
<!-- 이벤트에 핸들러 없이 수식어만 사용할 수 있습니다. -->
<form @submit.prevent></form>

<!-- 이벤트 리스너를 추가할 때 캡처 모드 사용 -->
<!-- 내부 엘리먼트에서 클릭 이벤트 핸들러가 실행되기 전에, 여기에서 먼저 핸들러가 실행됩니다. -->
<div @click.capture="doThis">...</div>
```

예시)

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

- @click.prevent : 함수는 실행 되고 그 이후의 동작을 막음. 콘솔 찍히고 네이버 안들어가짐.
- @click.once : 함수가 한번만 실행됨.
- @click.prvent.once : 처음 클릭하면 콘솔찍히고, 네이버 안들어가짐. 두 번째 클릭은 콘솔 안찍히고 들어가짐.

---

- @wheel.passive : 휠과 함수와 분리되어 동작
- @keydown.key="함수"

- key는 키보드키 의미

- v-model.lazy="msg" : 변경내용 작성 후 다른 곳 클릭해야 msg 값이 변경됨.

---

## 입력키 수식어

- `.enter`
- `.tab`
- `.delete` ("Delete" 및 "Backspace" 키 모두 캡처)
- `.esc`
- `.space`
- `.up`
- `.down`
- `.left`
- `.right`
- `.ctrl`
- `.alt`
- `.shift`
- `.meta`

```javascript
<!-- Alt + Enter -->
<input @keyup.alt.enter="clear" />

<!-- Ctrl + Click -->
<div @click.ctrl="doSomething">시작하기</div>
```

```javascript
<!-- Ctrl과 함께 Alt 또는 Shift를 누른 상태에서도 클릭하면 실행됩니다. -->
<button @click.ctrl="onClick">A</button>

<!-- 오직 Ctrl만 누른 상태에서 클릭해야 실행됩니다. -->
<button @click.ctrl.exact="onCtrlClick">A</button>

<!-- 시스템 입력키를 누르지 않고 클릭해야지만 실행됩니다. -->
<button @click.exact="onClick">A</button>
```

## 마우스 버튼 수식어

- `.left`
- `.right`
- `.middle`
  특정 마우스 버튼에 의해 이벤트가 트리거 되도록 제한하고 싶을 때 사용합니다.
