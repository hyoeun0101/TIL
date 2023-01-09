# router-link
`<router-link>`는 `<a href="..."></a>`로 랜더링 된다. 목표 위치는 to로 지정한다.    
a태그보다 router-link를 사용해야하는 이유는?
- `router-link`는 클릭 이벤트를 차단하여 페이지의 리로딩을 막는다. a태그는 클릭 할때마다 페이지가 리로딩된다.
- html5 히스토리 모드와 해시 모드에서 모두 동일한 방식으로 작동.
```javascript
<!-- 리터럴 string -->
<router-link to="home">Home</router-link>
<!-- 이렇게 렌더링 됩니다. -->
<a href="home">Home</a>

<!-- `v-bind`를 이용한 표현식 -->
<router-link v-bind:to="'home'">Home</router-link>

<!-- `v-bind`를 생략하면 다른 prop를 바인딩 하는 것과 같습니다. -->
<router-link :to="'home'">Home</router-link>

<!-- 위와 같습니다. -->
<router-link :to="{ path: 'home' }">Home</router-link>

<!-- 이름을 가지는 라우트 -->
<router-link :to="{ name: 'user', params: { userId: 123 }}">User</router-link>

<!-- 쿼리가 있으면, `/register?plan=private` 이 됩니다. -->
<router-link :to="{ path: 'register', query: { plan: 'private' }}">Register</router-link>
```


```javascript
<router-lnik :to="{path: '/abc'}" replace>ABC</router-link>
```
: router.push() 대신 router.replace()를 호출한다. push와 replace의 차이는 replace는 히스토리 스택을 쌓지 않는다는 것이다.


# router-view
`<router-view>` 컴포넌트는 주어진 라우트에 일치하는 컴포넌트를 랜더링한다. 



vue-router의 기본 모드는 해시모드이다. 