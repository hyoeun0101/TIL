# display : flex;
수평 정렬로 바뀜.    
![e](https://user-images.githubusercontent.com/96059261/202985867-5e5298be-551c-4ea9-bdd9-1c4a9df8a984.jpg)
- 부모-flex container가 블록처럼 쌓임. 기본 수직정렬
- 자식-flex items가 수평으로 정렬

# display: inline-flex;
인라인 요소 특징     
- 사이즈 요소 크기에 맞춤.

# flex container에 적용하는 요소들

## flex-direction
row ? 행 ------     
column ? 열 |       
            |        
            
 - row(default)
 - row-reverse    
![qq](https://user-images.githubusercontent.com/96059261/202988168-86722eb0-3345-4d1c-b5a3-29fd4c02af09.jpg)

- column
- column-reverse         
![aa](https://user-images.githubusercontent.com/96059261/202988486-a0da01da-8bde-49d2-bc64-ce70cd51dd7c.jpg)

## flex-wrap
flex Items 줄바꿈 여부      
안쓰면 찌그러짐.    
- wrap : 아이템이 넘치면 줄바꿈 처리    
- nowrap(default)      
![ss](https://user-images.githubusercontent.com/96059261/202989710-11f07d5d-792b-4c5c-a99d-b268985f1eb8.jpg)

## justify-content
주 축의 정렬 방법- 수평!!!       
- flex-start(default)
- flex-end: 끝으로 정렬
- center : 가운데로 정렬

## align-content
`사용 별로 안함`        
교차 축의 <span style="color:red;">여러 줄</span>을 모두 정렬 방법- 수직!!       
flex Item이 여러 줄일 때 적용이 가능하다. 즉 flex-wrap:wrap 과 같이 사용      
- stretch (default)
- flex-start
- flex-end
- center
- space-between
- space-around      

![ssss](https://user-images.githubusercontent.com/96059261/202992196-7bd03acd-481c-4e4c-8156-5c07e439a851.jpg)         
![qqqa](https://user-images.githubusercontent.com/96059261/202992236-d2c1099b-ac0b-4188-8500-44adf986f656.jpg)    

## align-items
교차 축의 <span style="color:red;">한 줄</span> 따로 정렬    
align-content와 다르게 align-items는 한 줄에 대한 정렬이다!!            
![align-items](https://user-images.githubusercontent.com/96059261/202997822-5addccf6-894f-4e21-aa2f-16c34a0d0d1b.jpg)   

- stretch(default)
- flex-start
- flex-end
- center
- baseline


# flex item에 적용하는 속성들
## order
item의 순서     
- 0(default) : 순서 없음
- 숫자 : 작을 수록 앞에 놓임
## flex-grow
Flex Item의 증가 너비 비율   
- 0 (default) : 없음
- 숫자 : 증가 비율     
![flex-grow](https://user-images.githubusercontent.com/96059261/203004728-4bf9507e-d35c-4b48-8a5a-6a70e80d2c85.jpg)
2. A가 나머지 공간 다 채우고, B,C를 놓는다. 
3. B : C = 2:1 비율로 채운다.   
- flex-basis:0; 으로 해야 우리가 원하는 이쁜 비율이 나오는 것이다!!    
## flex-shrink
flex item의 감소 너비 비율    
- 1 (default) : Flex Container 너비에 따라 감소 비율 적용
- 숫자 : 감소 비율    
1은 부모의 크기와 같이 1대1비율로 아이템도 감소한다.   
0으로 지정하면 감소하지 않는다.
## flex-basis
기본 너비    
- auto(default) : 요소의 내용의 너비
- 단위
- 0으로 해야 기본 너비이 없어짐.
- 
