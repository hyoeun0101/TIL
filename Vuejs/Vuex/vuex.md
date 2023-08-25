## 🍎 Vuex란?

- 상위와 하위 컴포넌트는 props, $emit을 사용해서 통신을 할 수 있다.
- 형제 컴포넌트끼리 통신을 하기 위해선 상위 컴포넌트를 거쳐 통신을 해야한다. 이러면 너무 많은 컴포넌트를 거칠 수 있기 때문에 중앙 집중 상태 관리를 하는 라이브러리 Vuex를 사용한다.
- `중앙 집중 상태 관리`란 필요한 데이터를 한 곳에 모아 처리하는 것이다. 이를 통해 형제 컴포넌트끼리 상위 컴포넌트를 거치지 않고 필요한 데이터를 바로 가져다 사용할 수 있다.

```javascript
export default {
  //모듈로 사용할 수 있음을 나타냄.
  namespaced: true,
  // data와 유사
  state: "",
  //computed와 유사
  getters: "",
  //methods와 유사
  //state에서의 데이터는 mutations에서만 변경할 수 있다.
  mutations: "",
  //기본적으로 비동기로 처리된다.
  actions: "",
};
```

### 예제

[store/movie.js]

```javascript
// store/movie.js
export default {
  namespaced: true,

  state: () => ({
    movies: [],
  }),
  //state의 값을 가져와 계산함.
  getters: {
    movieIds(state) {
      return state.movies.map((m) => m.mId);
    },
  },

  mutations: {
    //영화 초기화
    resetMovies(state) {
      state.movies = [];
    },
  },
  // 비동기로 처리.
  actions: {
    async searchMovies(context, payload) {
      //context를 통해 state에 접근할 수 있다.
      //이외에도 getters, commit도 있음.
      context.state;
      contesxt.getters;
    },
  },
};
```

- 해당 스토어들은 store/index.js의 store의 modules에 정의해줘야한다.

```javascript
import movie from "./movie";

export default createStore({
  modules: {
    movie: movie,
  },
});
```

## 🍎 actions

- context : state, getters, mutations를 활용할 수 있는 값들이 들어있음.
- payload : 메서드의 인자값

```javascript
const actions = {
  async searchMovies(context, payload) {
    const res = await axios.get("url", params);
    const result = res.data;
    // mutations의 assignMovies 실행.
    // state의 값은 mutations만이 변경할 수 있다고 했다!
    context.commit("assignMovies", result);
  },
};

const mutations = {
  assignMovies(state, result) {
    state.movies = result;
  },
};

const state = {
  movies: [],
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
```

- mutations를 실행할 때는 commit() 메소드를, actions를 실행할 때는 dispatch() 메소드를 사용한다.

## 🍎 store안에 있는 데이터 활용하기

```javascript
//MovieList.vue 파일

export default {
  computed: {
    movies() {
      return this.$store.state.movie.movies;
    },
  },
};
```

- computed에서 사용하는 이유
  - 처음 state는 빈 리스트이고, 비동기 처리 후 값이 세팅이 되므로 computed(계산된 속성)으로 작성해야 한다.
