폼 데이터 요청 할 때 요청 데이터 거르기

```html
<form action="/url" method="post" onsubmit="testFunc();">

</form>

<script>
    function testFunc(){

        if("조건이뭐뭐면"){
            alert("다시 선택해주세요.")
            event.preventDefault();
        }
    }
</script>
```