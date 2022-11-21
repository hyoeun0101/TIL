# position
: 요소의 위치 지정 기준 결정, 이 후 top, botton, left, right, z-index를 이용하여 값을 지정해줘야함.   
- top, right, bottom, left 속성이 요소를 배치할 최종 위치 결정.    

- `static`(default) : 기준 없음
- `relative` : 원래 자기자신의 위치가 기준.
  - 거의 사용X

<img src="https://us er-images.githubusercontent.com/96059261/202970567-96d17436-54fa-4e7f-99b5-c6c27dcb9934.PNG"  width="70" height="70"/>     
```html
.container .item:nth-child(2){
  width: 140px;
  height:170px;
  position:relative;
  bottom:100px;
  left:200px;
} 
```
자기 자신의 위치를 기준으로 아래에서 100px 떨어지고, 왼쪽 200px 떨어짐.

- `absolute` : 위치 상 부모 요소를 기준
  - 단순히 absolute하면 공중부양, 즉 다른 요소와 겹치게 됨.
  - 부모에 `postion: relative;` 작성 후, 자식에 `position:absolute;` 해야 부모를 기준삼는 것.
  - 부모 요소에 기준에 없다면 뷰포트를 기준삼음

<img src="https://user-images.githubusercontent.com/96059261/202971043-51036a6a-4fa4-4382-a37a-3b74fe1bb8f9.PNG"  width="70" height="70"/>
```html
.container{
  width:300px;
  background-color:red;
  position: relative;
}
.container .item:nth-child(2){
  width: 140px;
  height:170px;
  position:absolute;
  bottom:100px;
  left:200px;
} 
```
부모에게 relative주고, 자식에게 absolute주니 부모를 기준으로 위치함.    
- `fixed` : 뷰포트를 기준
  - 완전히 뷰포트를 기준으로 고정해놓음.
  - ex) 네비바

   
# 요소 쌓임
쌓임 우선순위
-  position 부여
-  z-index 값이 큰 것이 위에 쌓인다.(position을 부여해야 의미있음.)
- html에서 나중에 작성된 것이 위에 쌓인다.

### 요소의 display가 변경됨
position 속성 값으로 absolute, fixed가 지정된 요소는 display 속성이 block이 된다!!
