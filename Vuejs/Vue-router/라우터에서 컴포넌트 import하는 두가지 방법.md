## router

라우팅 : sqa는 미리 페이지 한번에 받아놓고 라우팅을 통해서 컴포넌트를 이동하는 것이다.  
router/index.js

```javascript
//첫번째 방법
import HomeView from './뷰이름.vue'
{
    path: '/about',
    name: 'about',
    component: HomeView
}
//두번째 방법
{
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../뷰이름.vue')
}
```

vue에서 script부분을 app.js 파일에 모두 넣는다.

- 첫번째 방법 : 임포트로 컴포넌트 불러오기
  - app.js 파일에 들어감
- 두번째 방법 : 컴포넌트에 직접 임포트하기
  - 해당 라우터로 이동했을 때, `webpackChunkName으로 지정한 이름.js` 파일이 따로 생성된다.
  - 라우터를 거칠 때 불러오기 때문에 느린 로딩.

**webpackPrefetch:true**

```javascript
component: () =>
  import(/* webpackChunkName: "about2" webpackPrefetch:true */ "../뷰이름.vue");
```

처음에 로딩될 때 about2.js가 캐시로 저장되고, 해당 경로로 가면 js파일을 캐시로부터 불러오는 것이다. 처음에만 로딩하고, 메뉴를 클릭하면 캐시로부터 불러오기 때문에 빠르다.  
처음 화면에서 캐시로 about2.js를 불러온다. 웹소스를 보면 `<link rel="prefetch" as="script" href="/js/about.js">` 가 생긴다.  
하지만 사용자가 누를 가능성 없는 메뉴까지 prefetch로 불러온다면?? 모든 메뉴를 prefetch로 하기엔 처음 로딩할 때 시간이 오래 걸린다. 그래서 라우터 설계가 필요한 것! 사용자가 많이 들어가는 메뉴는 prefetch로 해서 캐시로부터 불러와서 빨리 접속이 가능하게 하자.

### 정리

1. 처음 화면은 임포트 네임으로 불러와서 app.js에서 접속하도록 한다. 하지만 모든 라우터의 소스를 app.js에 넣게되면 느리다! 파일을 나누자.
2. 처음 화면에서 사용자의 접속 적은 메뉴, 사이즈가 작아서 금방 불러오는 애들은 직접 임포트한다. 직접 임포트하면 따로 js파일로 불러오는 것이다.(prefetch X)
3. 처음 화면에서 사용자가 자주 사용하는 메뉴, 사이즈가 커서 사용자가 눌렀을 때 느릴거 같은 애들은 직접 임포트 하되 prefetch:true로 한다. 처음 로딩할 때 캐시로 저장하고, 캐시로부터 불러오기 때문에 빠르다.
