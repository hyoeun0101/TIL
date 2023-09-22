## 🍎 computed

- 데이터를 가공하는 등의 복잡한 연산은 뷰 인스턴스 안에서 하고, HTML에는 데이터만 뿌려야 한다.
- computed 속성은 이러한 데이터 연산들을 정의하는 영역이다.

```javascript
<div id="app">
  <p>{{ reversedMessage }}</p>
</div>

...

<script>
  new Vue({
    el: '#app',
    data: {
      message : 'Hello Vue~',
    },
    computed: {
      reversedMessage: function() {
        return this.message.split('').reverse().join('');
      }
    }
  });
</script>
```

- HTML에 바로 `{{ message.split('').reverse().join('') }}` 이렇게 정의할 수 있지만 computed를 활용해야 한다.

### computed 속성의 장점

- computed내에서 참조하고 있는 data값이 바뀌면 자동으로 computed가 실행되어 값을 변환한다.
- 캐싱 기능 : `{{ reversedMessage }}`를 여러 곳에서 호출하면 처음 한번만 실행하고, 그 다음 호출 시에는 연산을 하지 않고, 캐싱된 값을 사용한다.  
  ➡︎ 정리 : computed내에서 참조하고 있는 data값이 변경되면 computed가 실행되고, 변경되지 않으면 캐싱된 값을 사용한다.

### computed와 methods의 차이

- methods
  - 호출을 해야만 실행하며, 호출할 때마다 실행하기 때문에 별도의 캐싱 기능이 필요없다.
- computed
  - 호출하지 않아도 종속된 data의 변경을 감지하여 자동으로 실행한다.
  - 데이터가 변경되기 전에 이전 값을 가지고 있다가(캐싱하고 있다가) 캐싱값을 반환한다.
- 따라서 복잡한 연산을 반복 수행해서 화면에 나타내야 한다면 methods 대신 computed를 사용하는 것이 성능 면에서 더 효율적이다.

```javascript
export default {
  data() {
    return {
      author: {
        name: "John Doe",
        books: [
          "Vue 2 - Advanced Guide",
          "Vue 3 - Basic Guide",
          "Vue 4 - The Mystery",
        ],
      },
    };
  },
  computed: {
    // 계산된 값을 반환하는 속성
    publishedBooksMessage() {
      return this.author.books.length > 0 ? "Yes" : "No";
    },
  },
};
```

```html
<p>책을 가지고 있다:</p>
<span>{{ publishedBooksMessage }}</span>
<span>{{ publishedBooksMessage }}</span>
<span>{{ publishedBooksMessage }}</span>
```

- 위의 예제의 computed는 author에 의존하고 있다. author의 값이 바뀌면 computed가 실행되고, 값이 변하지 않으면 캐싱된 값을 불러온다. 따라서 publishedBooksMessage를 세 번 호출하지만 실제론 한 번만 호출된다.

## getter, setter

- 계산된 속성은 기본적으로 getter 전용이다. 계산된 속성에 새로운 값을 할당하려면 setter도 작성해주어야 한다.

```javascript
<template>
  <div>
    <input type="text" v-model="firstName" />
    <input type="text" v-model="lastName" />
    <input type="text" v-model="fullName" />

    {{ firstName }}||{{ lastName }}||{{ fullName }}
  </div>
</template>
<script>
export default {
  data() {
    return {
      firstName: 'Foo',
      lastName: 'Bar',
      fullName: 'Foo Bar'
    }
  },
  computed: {
    fullName: {
      get() {
        console.log('get호출')
        return this.firstName + ' ' + this.lastName
      },
      set(newValue) {
        console.log('set 호출', newValue)//newValue는 fullName의 새로운 값이다.
        let names = newValue.split(' ')
        this.firstName = names[0]
        this.lastName = names[names.length - 1]
        //[this.firstName, this.lastName] = newValue.split(' ')
      }
    }
  }
}
</script>

```

## 🍎 watch

- watch, computed 모두 종속된 값이 변경되면 실행된다는 공통점을 가지고 있다.
- computed는 간단한 연산 정도로 적합하고, watch는 비동기 서버 통신과 같이 비용이 많이 드는 작업을 실행할 때 적합하다.
