# Vuex란?

상위와 하위 컴포넌트는 props, $emit을 사용해서 서로 통신을 했다.  
형제 컴포넌트끼리 통신을 하기 위해선 상위 컴포넌트를 거쳐 통신을 해야한다. 이러면 너무 많은 컴포넌트를 거칠 수 있기 때문에 중앙 집중 상태 관리로 Vuex를 사용한다.!  
중앙 집중 상태 관리란 필요한 데이터를 한 곳에 모아 처리하는 것이다. 이를 통해 형제 컴포넌트끼리 상위 컴포넌트를 거치지 않고 필요한 데이터를 바로 가져다 사용할 수 있다.

store파일에서 사용한다!

```javascript
export default {
  //module
  namespaced: true,
  //data
  state: "",
  //computed
  getters: "",
  //methods
  //변이,
  mutations: "",
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
  //computed
  getters: {
    movieIds(state) {
      return state.movies.map((m) => m.mId);
    },
  },
  //methods
  //state에서의 데이터는 mutations에서만 변경할 수 있다.
  mutations: {
    //영화 초기화
    resetMovies(state) {
      state.movies = [];
    },
  },
  //비동기 처리
  actions: {
    searchMovies(context) {
      //context를 통해 state에 접근할 수 있다.
      context.state;
      contesxt.getters;
    },
    /*searchMovies({ state, getters, commit}){

        }*/
  },
};
```
