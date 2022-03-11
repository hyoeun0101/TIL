### 두 개의 값 입력받기
```
a ,b =input().split()
```
문자열 -> 리스트
split('_') : 문자열에서 _ 이걸 기준으로 나눈다.

```
a, b, c = [1,2,3]
```
### 10진수-> 16진수
```
a= 45
print("%x" %a)
print("%X" %a) # 대문자로 표현
```
### 10진수-> 8진수
```
print('%o' %a)
```
### 영어(문자)-> 10진수 : 아스키코드
```
print(ord('A'))
>>65
```
### 10진수-> 영어(문자)
```
print(chr(65))
>>A
```
### 소숫점 반올림
```
a= 3.45675
print(f'{a:.2f}') #2자릿수까지 출력
print('{:.2f}'.format(a))
print(format(a,".2f"))
```
### 2배 곱하기
```
a=3
print(a<<1)
```
### 🍎 비트연산자 & | ^ ~ >> <<
```
print(3&5)
3 = 0000 0011
5 = 0000 0101
=>  0000 0001
>> 1
```
![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/219d0b71-ace3-4fad-86ce-25c6a4b4c6c7/Untitled.png)

### 🍎 bool 자료형
=> 결과값  True, False: 0
### 🍎 논리연산자 and or not
_tip! 파이썬에서는 && || ! 안씀_   

암기   
or  0111   
and 0001   
xor 0110 =>서로 다르면 True   

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0937bfcb-7423-43f9-824a-eabff4dc1f64/Untitled.png)
(A and not B) or (not A and B)   

