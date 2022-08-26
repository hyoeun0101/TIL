
### 로딩하자마자 실행
```
$(document).ready(function(){
    listing();
})
function listing(){
  console.log('hello')
}
```

### .attr()
요소의 속성 가져오기 또는 추가
```
// div의 class 속성을 가져온다
$('div').attr('class')
//div에 title = 'hello' 속성을 추가한다
&('div').attr('title','hello')
```

### @ disabled 속성 설정하기
- true면 비활성화   
- false면 활성화   
```
$("#editBox").attr("disabled", true); //비활성화
$("#editBox").attr("disabled", false); //활성화
```