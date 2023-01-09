하위 컴포넌트에서의 이벤트 발생을 상위 컴포넌트에서 어떻게 처리하는지 알아보고, 동작 원리를 이해해보자.

## $emit

하위에서 상위로 이벤트 전달하기 위해서는 $emit을 사용해야 한다.

자식 컴포넌트

```html
<!-- MyButton -->
<button @click="$emit('increaseBy',1)">click</button>
```

부모 컴포넌트

```javascript
<MyButton @increase-by="(n)=> count+=n"/>
```

또는

```javascript
<MyButton @increase-by="increaseCount"/>
...
methods:{
    increaseCount(n){
        this.count+=n
    }
}
```

$emit의 첫 번째 인자는 v-on과 연결되며 두 번째 인자는 이벤트가 발생했을 때 실행되는 함수의 인자로 들어간다.

<<예제>>

```html
<template>
  <div>
    <MyBtn @hello="log" @change-msg="logMsg"> Banana </MyBtn>
  </div>
</template>

<script>
  import MyBtn from "./components/MyBtn.vue";
  export default {
    components: {
      MyBtn,
    },
    methods: {
      log(e) {
        alert("click!");
        console.log(e);
      },
      logMsg(msg) {
        console.log(msg);
      },
    },
  };
</script>
```

[MyBnt.vue]

```html
<template>
  <div class="btn" @click="$emit('hello',$event)"></div>
  <input type="text" v-model="msg" />
</template>
<script>
  export default {
    emits: ["hello", "changeMsg"],
    data() {
      return {
        msg: "",
      };
    },
    watch: {
      msg() {
        this.$emit("changeMsg", this.msg);
      },
    },
  };
</script>

<style scoped>
  .btn {
    display: inline-block;
    margin: 4px;
    padding: 6px 12px;
    border-radius: 4px;
    color: white;
    background-color: gray;
    cursor: pointer;
  }
  .btn.large {
    font-size: 20px;
    padding: 10px 20px;
  }
</style>
```
