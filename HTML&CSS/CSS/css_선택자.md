# 선택자

## 가상 클래스 선택자

### 1. :hover

마우스 올리면 동작

```
.box{
    width: 100px;
    height: 100px;
    transition: 1s;
}
.box:hover{
    width: 300px;
    background-color: red;
}
```

### 2. :active

마우스 클릭하고 있는 동안 동작

### 3. :focus

포커스되면 동작. 포커스 가능한 요소들 : input, select, textarea  
포커스 안되는 요소에 `tabindex = "-1"` 속성 추가하면 포커스 가능해짐.  
포커스는 한 페이지에서 하나만 될 수 있음.

## 가상 클래스 선택자2

```html
<div class="fruits">
  <span>딸기</span>
  <span>수박</span>
  <div>오렌지</div>
  <p>망고</p>
  <h3>사과</h3>
</div>
```

### 1. :first-child

형제 요소 중 첫째라면 선택  
`fruits span:first-child` : 딸기  
`fruits div:first-child` : ?

### 2. :last-child

형제 요소 중 막내라면 선택  
`fruits h3:last-child` : 사과

### 3. :nth-child(n)

형제 요소 중 n째라면 선택  
`fruits *:nth-child(2)` : 수박  
`fruits *:nth-child(2n)` : n은 0부터 시작. 수박, 망고  
`fruits *:nth-child(2n+1)` : 딸기, 오렌지, 사과  
`fruits *:nth-child(n+2` : 수박, 오렌지, 망고,사과

### 4. ABC:not(XYZ)

XYZ아닌 ABC요소 선택  
`fruits *:not(span)` : 오렌지, 망고,사과

## 가상 요소 선택자

### 1. ABC::before

ABC요소의 내부 앞에 내용(인라인)을 삽입. 가상 요소는 인라인 특성을 가짐.  
인라인 요소를 블럭 요소로 바꾸기 `display:block;`

```html
<div class="box">Content!</div>
```

```
.box::before{
    content:"앞!";
}
```

`앞!Content!` 출력

### 2. ABC::after

```
.box::before{
    content:"뒤!";
}
```

`Content!뒤!` 출력  
content와 가상 요소는 세트. 내용 비워도 `content:"";` 써줘야함

## 속성 선택자

### [ABC]

```html
<input type="text" value="HELLO" />
<input type="password" value="1234" />
<input type="text" value="ABCD" disabled />
```

```css
[disabled] {
  color: red;
}
```

### [ABC="XYZ"]

```css
[type="password"] {
  color: red;
}
```

# 상속

글자와 문자 관련 속성들은 상속된다.  
font-style(글자 기울기), font-weight(글자 두께), font-size(글자 크기), line-height(줄 높이), font-family(폰트), color(글자 색사), text-align(정렬) 등

### 강제 상속

inherit : 부모 상속.

```css
.parent {
  width: 400px;
  height: inherit;
}
.child {
  width: 200px;
  height: inherit;
}
```

### 선택자 우선순위

- 우선순위란? 같은 요소가 여러 번 선언되었을 시, 어떤 선언의 css속성을 우선 적용할지 결정.
- 점수가 높은 선언이 우선.
- 점수가 같으면, 마지막에 해석된 선언이 우선.
