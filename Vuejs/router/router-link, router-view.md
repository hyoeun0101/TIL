# <router-link>

<a> 태그로 랜더링된다.

## to

목표 위치는 `to` prop으로 지정한다.

```html
<!-- 리터럴 string -->
<router-link to="home">Home</router-link>
<!-- 이렇게 렌더링된다. -->
<a href="home">Home</a>

<!-- `v-bind`를 이용한 표현식 -->
<router-link v-bind:to="'home'">Home</router-link>

<!-- 위와 같음. -->
<router-link :to="{ path: 'home' }">Home</router-link>

<!-- 이름을 가지는 라우트 -->
<router-link :to="{ name: 'user', params: { userId: 123 }}">User</router-link>

<!-- 쿼리가 있으면, `/register?plan=private` -->
<router-link :to="{ path: 'register', query: { plan: 'private' }}"
  >Register</router-link
>
```

- to는 내부적으로 router.push()에 전달된다.

## replace

```html
<router-link :to="{ path: '/abc'}" replace></router-link>
```

- 기본값: false
- 클릭하면 router.replace() 호출.

## append

```html
<router-link :to="{ path: '/abc'}" append></router-link>
```

https://v3.router.vuejs.org/kr/api/#router-addroutes
