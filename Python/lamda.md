### lamba

def 함수이름(parameter): return 결과

lamda parameter: return

```
def my_key(string):
  return len(string.strip())

target = ['  cat ', ' tiger ', '    dog', 'snake   ']

print(sorted(target, key = my_key))
# key를 기준으로 정렬

print(sorted(target, key=lamba x: len(x.strip())))

```
