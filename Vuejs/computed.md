# computed()

: 메서드와 비슷한 기능을 제공하지만 computed에는 `캐싱`기능이 있다.

## computed와 methods의 차이

- computed에 종속된 속성이 변경되면 computed가 실행이 되고, 변경이 되지 않으면 캐싱된 값을 사용한다.
- 반면에 methods는 랜더링할 때마다 함수가 호출된다.
  <예제>

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

위의 예제의 computed는 author에 의존하고 있다. author의 값이 바뀌면 computed가 실행되고, 값이 변하지 않으면 캐싱된 값을 불러온다. 따라서 publishedBooksMessage를 세 번 호출하지만 실제론 한 번만 호출된다.  
또한 반응형인 값을 의존해야 computed가 실행되는 것이다.

<예제>

```javascript
<template>
  <div>{{ firstName }}</div>
  <div>{{ lastName }}</div>
  <div>{{ fullName }}</div>
  <input type="text" v-model="firstName" />
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
    fullName: function () {
      console.log('fullName update')
      return this.firstName + ' ' + this.lastName
    }
  }
}
</script>
```

- computed에 종속된 속성 값(firstName, lastName)이 변경되면 computed가 실행된다.

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

## watch

: watch, computed 모두 종속된 값이 변경되면 실행된다는 공통점을 가지고 있다.  
대부분의 경우 computed를 사용하지만, 데이터에 대한 응답으로 비동기를 사용하거나, 비용이 많이 드는 작업을 실행할 때는 watch를 사용한다.
