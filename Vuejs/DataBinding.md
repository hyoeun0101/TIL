- 앵귤러- 양방향 데이터 바인딩 지원, but 무거움
- 리액트- 가상 DOM 지원, but 단방향
- 뷰 - 양방향 데이터 바인딩, 가상 DOM 지원

## 1. {{}} , String - 단방향

```javascript
<template>
  <div>
    <h1>Hello {{ userName }}</h1>
    <p>{{ message }}</p>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      userName: 'khe',
      message: 'welcome',
      sampleData: ''
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {}
}
</script>

```

## 2. v-html, HTML - 단방향

```javascript
<template>
  <div>
    <h1>Hello {{ userName }}</h1>
    <div v-html="htmlTest"></div>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      htmlTest: '<p style="color:red;">빨강</p>'
      sampleData: ''
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {}
}
</script>

```

- InnerHtml과 같음

## 3. v-model, 양방향 databinding

```javascript
<template>
  <div>


    <input type="text" v-model="userId" />
    <button @click="myFunc">클릭</button>
    <button @click="changeId">변경</button>


    <select v-model="selectVal">
      <option value="1">가</option>
      <option value="2">나</option>
      <option value="3">다</option>
    </select>


    <input type="checkbox" value="valC" id="css" v-model="checkboxVal" /><label for="css">CSS</label>
    <input type="checkbox" value="valH" id="html" v-model="checkboxVal" /><label for="html">HTML</label>
    <input type="checkbox" value="valJ" id="js" v-model="checkboxVal" /><label for="js">JS</label>

  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      userId: 'eun',
      selectVal: '1',//가 선택됨
      checkboxVal: [] //체크박스는 배열!
    }
  },
  setup() {},
  created() {},
  mounted() {},
  unmounted() {},
  methods: {
    myFunc() {
      console.log(this.userId)
    },
    changeId() {
      this.userId = 'gggg'
    }
  }
}
</script>

```

- v-model="" 으로 값 주고받기
- v-model.number="" 해야 숫자가 된다. 기본 input값은 String이다!
- input
  - 사용자가 값을 입력할 때 `v-model`을 사용한다. 사용자가 변경한 값이 바로 userId에 들어오는 것이다.
- select
  - 사용자가 선택한 option의 value값이 select의 v-model로 들어온다.
- checkbox
  - 선택한 value가 v-model에 들어온다. 여러 개 선택이므로 배열로 선언, v-model이 같은 것 끼리 묶음.
- radio
  - 하나만 선택이므로 문자열

## v-bind, 속성 - 단방향

- `v-bind:value`을 사용하여 단방향으로 값을 가져온다.
- `v-bind:`는 `:` 이렇게 쓸 수 있다.

```javascript
<input type="text" :value="userId" readonly/>
...
{
    userId:"eunoo"
}
```

- 속성에 조건줄 수 있다.

```javascript
<input type="search" v-model="txt1" />
<button :disabled="txt1===''">조회</button>
...
{
    txt1: ''
}
```

- txt1이 ''이면 disabeld가 ture이다.

## v-for, 리스트 뽑기

```javascript
<option :key="city.id" v-for="city in 리스트"></option>
```

key는 반드시 지정해줘야함.

```javascript
<option :key="i" v-for="(city, i) in 리스트"></option>
```

i는 리스트의 인덱스 번호

## class 바인딩

```javascript
<template>
  <div>
    <div :class="{ 'text-red': hasError, active: isActive }">클래스</div>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      hasError: true,
      isActive: true
    }
  }
}
</script>
<style scoped>
.active {
  background-color: aqua;
}
.text-red {
  color: red;
}
</style>
```

## style 바인딩

```javascript
<template>
  <div>
    <div :style="style1">ddd</div>
    <button @click="style1.color = 'red'">btn</button>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      style1: {
        color: 'blue',
        fontSize: '30px'
      }
    }
  }
}
</script>

```
