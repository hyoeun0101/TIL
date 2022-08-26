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
포커스되면 동작. 포커스 가능한 요소들 :  input, select, textarea      
포커스 안되는 요소에  `tabindex = "-1"` 속성 추가하면 포커스 가능해짐.   
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