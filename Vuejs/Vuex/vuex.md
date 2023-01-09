# Vuex란?

상위와 하위 컴포넌트는 props, $emit을 사용해서 서로 통신을 했다.  
형제 컴포넌트끼리 통신을 하기 위해선 상위 컴포넌트를 거쳐 통신을 해야한다. 이러면 너무 많은 컴포넌트를 거칠 수 있기 때문에 중앙 집중 상태 관리를 하는 라이브러리 Vuex를 사용한다!
중앙 집중 상태 관리란 필요한 데이터를 한 곳에 모아 처리하는 것이다. 이를 통해 형제 컴포넌트끼리 상위 컴포넌트를 거치지 않고 필요한 데이터를 바로 가져다 사용할 수 있다.

store파일에서 사용한다!  
store의 멤버

```javascript
export default {
  //module
  namespaced: true,
  //data
  state: "",
  //Vue.js의 computed와 유사.
  getters: "",
  //Vue.js의 methods와 유사.
  //state에서의 데이터는 mutations에서만 변경할 수 있다.
  mutations: "",
  //비동기 처리
  actions: "",
};
```

예제> 영화 스토어를 만들어보자.  
[store/movie.js]

```javascript
export default {
  //module
  namespaced: true,
  //data
  state: () => ({
    movies: [],
  }),
  //state의 값을 가져와 계산함.
  getters: {
    movieIds(state) {
      return state.movies.map((m) => m.mId);
    },
  },
  //이곳에서만 state의 값을 직접 수정할 수 있다.
  mutations: {
    //영화 초기화
    resetMovies(state) {
      state.movies = [];
    },
  },
  // 비동기로 처리.
  actions: {
    async searchMovies(context, payload) {
      //context를 통해 state에 접근할 수 있다. 직접 state에 접근할 수 없다.
      context.state;
      contesxt.getters;
    },
    //context 객체에서 각 속성들을 꺼내 사용할 수도 있다.
    /*searchMovies({ state, getters, commit}){

        }*/
  },
};
```

해당 스토어들은 store/index.js의 store의 modules에 정의해줘야한다.

## actions

매개변수 : context, payload  
payload를 통해 원하는 객체의 데이터에 접근할 수 있다.

```javascript
const { title, type, number, year } = payload;
```

$store의 `mutations` 실행할 때는 `.commit()` 메소드를 사용하고, `actions` 실행할 때는 `.dispatch()`를 사용한다.  
예시

```javascript
this.$store.dispatch("movie/searchMovies", {
  //payload를 통해 넘겨줄 값
  title: this.title,
  type: this.type,
});
```
