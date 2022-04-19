# lambda

def 함수이름(parameter): return 결과

=>

lamda parameter: return

```
def my_key(string):
  return len(string.strip())

target = ['  cat ', ' tiger ', '    dog', 'snake   ']

print(sorted(target, key = my_key))
# key를 기준으로 정렬

print(sorted(target, key=lambda x: len(x.strip())))

```

# map
map(함수, 리스트)

리스트에서 원소를 하나씩 꺼내서 함수에 적용시킨다.
```
m = map(lambda x : x**2, range(5))
>> map 객체
list(m)
>>>[0,1,4,9,16]
```

# reduce
reduce(함수, 시퀀스)
시퀀스 : 문자열, 리스트, 튜플
```
from functools import reduce

reduce(lambda x,y : x+y, [0,1,2,3,4])
>>> 10
```
0에 1을 더함=> 1
1에 2를 더함 => 3
3에 3을 더함 => 6
6에 4를 더함 => 10

# filter
filter(함수, 리스트)
```
f = filter(lambda x: x < 5, range(10))
>>>filter 객체
list(f)
>>>[0,1,2,3,4]
```

홀수 출력
```
list(filter(lambda x : x % 2, range(10)))
>>>[1,3,5,7,9]
```
x%2가 참이면 ==> x%2 가 1이면 참
통과
