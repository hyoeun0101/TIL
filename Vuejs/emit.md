## emits

자식에서 부모로 이벤트 전달하기

자식 컴포넌트

```html
<button @click="$emit('increaseBy',1)">click</button>
```

부모 컴포넌트

```javascript
<MyButton @increase-by="(n)=> count+=n"/>
또는
<MyButton @increase-by="increaseCount"/>
...
methods:{
    increaseCount(n){
        this.count+=n
    }
}
```

자식의 $emit의 인자가 해당 메서드의 첫 번째 파라미터로 전달된다.

```javascript
<template>
  <div>
    <MyBtn @hello="log" @change-msg="logMsg">
      Banana
    </MyBtn>
  </div>
</template>

<script>
import MyBtn from './components/MyBtn.vue'
export default {
  components: {
    MyBtn
  },
  methods: {
    log(e){
      alert("click!")
      console.log(e)
    },
    logMsg(msg){
      console.log(msg)
    }
  }
}
</script>
```

[MyBnt.vue]

```javascript
<template>
    <div class="btn" @click="$emit('hello',$event)">
    </div>
    <input type="text" v-model="msg"/>
</template>
<script>
export default {
  emits:[
    'hello',
    'changeMsg'
  ],
  data(){
    return {
      msg: ''
    }
  },
  watch:{
    msg(){;
      this.$emit('changeMsg',this.msg)
    }
  }
}
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
.btn.large{
  font-size:20px;
  padding: 10px 20px;
}
</style>
```
