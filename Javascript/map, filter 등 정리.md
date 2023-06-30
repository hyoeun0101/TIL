### join :

### split

### reverse

### slice

### find

- 조건이 true인 첫 데이터를 리턴한다.

```javascript
const students = [
  new Student("A", 29, true, 45),
  new Student("B", 28, true, 80),
  new Student("C", 30, true, 90),
  new Student("D", 40, true, 66),
  new Student("E", 10, true, 88),
];

students.find((student) => student.score > 50);
// 학생 B 리턴
```

### filter

- 조건이 true인 데이터만 리스트로 반환한다.

```javascript
const students = [
  new Student("A", 29, true, 45),
  new Student("B", 28, true, 80),
  new Student("C", 30, true, 90),
  new Student("D", 40, true, 66),
  new Student("E", 10, true, 88),
];

students.filter((student) => student.score > 50);
// []
```

### map

### some

### reduce : 누적 계산할 때 사용

```javascript
const students = [
  new Student("A", 29, true, 45),
  new Student("B", 28, true, 80),
  new Student("C", 30, true, 90),
  new Student("D", 40, true, 66),
  new Student("E", 10, true, 88),
];

const result = student.reduce((prev, curr) => {
  console.log("------------");
  console.log(prev);
  console.log(curr);
  //리턴값은 다음 실행할 prev에 들어간다.
  return curr;
});

const result1 = student.reduce((prev, curr) => {
  return curr;
}, 0);
//인자 0을 넣어주면 맨 처음의 prev는 0부터 시작한다.

//reduce 활용하기
const result2 = student.reduce((prev, curr) => {
  console.log("------------");
  console.log(prev);
  console.log(curr);
  //모든 학생 점수 더하기
  return prev + curr.score;
}, 0);
```

- reduceRight()는 거꾸로 호출된다.

### sort

```javascript
//오름차순으로 정렬
const result = students.map((student) => student.score).sort((a, b) => a - b);

//내림차순으로 정렬
const result = students.map((student) => student.score).sort((a, b) => b - a);
```
