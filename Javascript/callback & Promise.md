**Javascript is synchronous**

```
console.log('1')
setTimeout(()=> console.log('2'), 1000)
console.log('3')

>>>1
>>>3
>>>2
```

- 콜백함수 : 지금 당장 불러오는게 아니라 1초 뒤 다시 부름.

1. synchronous callback

```
function printImmediately(print){
  print(); # 함수 호출
}
printImmediately(()=> console.log('hello'))
```

2. asynchronous callback

```
function printWithDelay(print,timeout){
  setTimeout(print,timeout);
}
printWithDelay(()=> console.log('hello'), 2000)
```

# 콜백

자바스크립트에서는
