## 🍎 로딩하자마자 실행

```javascript
$(document).ready(function () {
  //
});
```

```javascript
$(function () {
  //
});
```

### ready와 load의 차이점

- 우선 브라우저 프로세스를 이해해야한다.
- HTML Parser를 통해 DOM Tree를 생성 -> CSS Parser를 통해 Style Rules를 생성 -> DOM과 Style Rules를 결합한 Render Tree 생성

```javascript
$(document).ready(function () {
  $("#doc").text("문서가 전부 로드됐어요!");
});
$(window).load(function () {
  $("#win").append("창이 모두 로드됐어요!");
});
```

- `ready`는 DOM Tree 생성 후 실행된다.(먼저 실행)
- `load`는 Render Tree 생성 후, 스타일까지 로드된 후 실행된다. (나중에 실행)

### js onload는 다음과 같이 작성한다.

```javascript
window.onload = function () {};
```

## .attr()

요소의 속성 가져오기 또는 추가

```
// div의 class 속성을 가져온다
$('div').attr('class')
//div에 title = 'hello' 속성을 추가한다
$('div').attr('title','hello')

//testInput 값을 disabled로.
$('#testInput).attr("disabled",true)

//testInput값이 disabled이면 disabled 반환, 아니면 undefined 반환
$("#testInput").attr("disabled")
```

## @disabled 속성 설정하기

- true면 비활성화
- false면 활성화

```
$("#editBox").attr("disabled", true); //비활성화
$("#editBox").attr("disabled", false); //활성화
```
