## 🍎비동기 처리

- Javascript is synchronous. 자스는 동기적이다. 호이스팅된 이후 순서대로 동작한다.
  - 호이스팅? var, function 선언이 제일 위로 올라가는 것.

```javascript
console.log("1");
setTimeout(() => console.log("2"), 1000); //1초 후 실행
console.log("3");
```

```
[결과]
>>> 1
>>> 3
>>> 2
```

## 🍎 Callback

- 콜백도 동기, 비동기로 실행할 수 있다.

### 동기 콜백 예제

```javascript
console.log("1");
setTimeout(() => console.log("2"), 1000); //1초 후 실행
console.log("3");

function printImmediately(print) {
  print();
}

printImmediately(() => console.log("hello~"));
```

```
[결과]
>>> 1
>>> 3
>>> hello~
>>> 2
```

- 함수 선언은 호이스팅되어 제일 위에서 실행한다. 1 출력, 3출력, 함수 호출, 1초 후 2 출력

### 비동기 콜백 예제

```javascript
console.log("1");
setTimeout(() => console.log("2"), 1000); //1초 후 실행
console.log("3");

function printWithDelay(print, timeout) {
  setTimeout(print, timeout);
}

printWithDelay(() => console.log("async callback~"), 2000);
```

```
[결과]
>>> 1
>>> 3
>>> 2
>>> async callback~
```

## 🍎 콜백 지옥

- 콜백 안에 또 콜백을 부르는 구조.
- 가독성이 너무 떨어진다. 비즈니스 로직을 한 눈에 알아보기 어렵다. 디버깅도 너무 어렵고, 유지보수도 어렵다.

```javascript
class UserStorage {
  loginUser(id, pwd, onSuccess, onError) {
    //로그인
    setTimeout(() => {
      // 2초 후에 실행 , 원래는 서버 API 호출

      if (
        (id === "ellie" && pwd === "dream") ||
        (id === "coder" && pwd === "academy")
      ) {
        onSuccess(id);
      } else {
        onError(new Error("not Found"));
      }
    }, 2000);
  }

  getRoles(user, onSuccess, onError) {
    setTimeout(() => {
      if (user === "ellie") {
        onSuccess({ name: "ellie", role: "admin" });
      } else {
        onError(new Error("no access"));
      }
    }, 1000);
  }
}

const userStorage = new UserStorage();
const id = "ellie";
const pwd = "dream";

userStorage.loginUser(
  id,
  pwd,
  (user) => {
    userStorage.getRoles(
      user,
      (userWithRole) =>
        alert(
          `Hello ${userWithRole.name}, you have a ${userWithRole.role} role~`
        ),
      (error) => console.log(error)
    );
  },
  (error) => console.log(error)
);
```

## 🍎 Promise

- 비동기로 동작하는 자바스크립트 객체
- 네트워크 통신, 파일 읽기 같이 시간이 걸리거나 무거운 동작들은 비동기로 처리하여 빠르게 실행하도록 한다.
- state : 동작 수행 중일 땐 pending 상태, 동작 성공하면 fulfilled 상태, 에러 발생하면 rejected

```javascript
...
// Promise에서 resolve, reject 아무것도 호출하지 않으면 Promise의 state는 pending 이다.
 return new Promise((resolve, reject) => {
    return 'ellie';
  })
```

### Producer

```javascript
const promise = new Promise((resolve, reject) => {
  console.log("doing something..."); // 바로 출력
});
```

- Promise 객체를 만드는 순간, 전달한 executor 함수가 자동으로 실행한다.

### Consumers

- then, catch, finally

### 예제

```javascript
//Producer
const promise = new Promise((resolve, reject) => {
  // network, read files 같이 무거운 동작 실행, 예제는 단순 console.log로 작성
  console.log("doing something...");

  setTimeout(() => {
    resolve("ellie"); // 2초 후 resolve 실행, resolve는 'ellie'값 전달
    // reject(new Error('no network'));
  }, 2000);
});

//Consumer
promise
  .then((value) => {
    console.log(value); // ellie 출력
  })
  .catch((error) => console.log(error))
  .finally(() => console.log("finally~"));
```

### Promise chaining

```javascript
const fetchNumber = new Promise((resolve, reject) => {
  setTimeout(() => resolve(1), 1000); // 1초 후 resolve 실행
});

fetchNumber
  .then((num) => num * 2) //2
  .then((num) => num * 3) //6
  .then((num) => {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve(num - 1), 1000); //1초 후 resolve 실행
    });
  })
  .then((num) => console.log(num)); //5출력, 2초 소요
```

### Error Handling

[예제] 닭 => 달걀 => 달걀후라이 요리 완성하기

```javascript
const getHen = () =>
  new Promise((resolve, reject) => {
    setTimeout(() => resolve("🐔"), 1000);
  });

const getEgg = (hen) =>
  new Promise((resolve, reject) => {
    setTimeout(() => resolve(`${hen} => 🥚`), 1000);
  });

const cook = (egg) =>
  new Promise((resolve, reject) => {
    setTimeout(() => resolve(`${egg} => 🍳`), 1000);
  });

getHen()
  .then((hen) => getEgg(hen))
  .then((egg) => cook(egg))
  .then((meal) => console.log(meal)); //🐔 => 🥚 => 🍳 출력

// 다음처럼 작성할 수 있음.
getHen().then(getEgg).then(cook).then(console.log);
```

```javascript
const getHen = () =>
  new Promise((resolve, reject) => {
    setTimeout(() => resolve("🐔"), 1000);
  });

const getEgg = (hen) =>
  new Promise((resolve, reject) => {
    setTimeout(() => reject(new Error(`error! ${hen} => 🥚`)), 1000); //에러발생
  });

const cook = (egg) =>
  new Promise((resolve, reject) => {
    setTimeout(() => resolve(`${egg} => 🍳`), 1000);
  });

// 에러 헨들링 1 - 달걀에서 에러 발생해서 에러 메세지 출력
getHen().then(getEgg).then(cook).then(console.log).catch(console.log);

// 에러 헨들링 2 - 달걀에서 에러 발생해서 빵으로 대체
getHen()
  .then(getEgg)
  .catch((error) => {
    return "🥖";
  })
  .then(cook)
  .then(console.log)
  .catch(console.log); //🥖 => 🍳 출력
```

## 🍎 콜백 지옥을 Promise로 리팩토링하기

- 위에 콜백 지옥 예제 리팩토링

```javascript
class UserStorage {
  loginUser(id, pwd) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        // 2초 후에 실행 , 원래는 서버 API 호출
        if (
          (id === "ellie" && pwd === "dream") ||
          (id === "coder" && pwd === "academy")
        ) {
          resolve(id);
        } else {
          reject(new Error("not Found"));
        }
      }, 2000);
    });
  }

  getRoles(user) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (user === "ellie") {
          resolve({ name: "ellie", role: "admin" });
        } else {
          reject(new Error("no access"));
        }
      }, 1000);
    });
  }
}

const userStorage = new UserStorage();
const id = "ellie";
const pwd = "dream";

userStorage
  .loginUser(id, pwd)
  .then((user) => userStorage.getRoles(user)) //then(userStorage.getRoles) 와 동일
  .then((user) => alert(`Hello ${user.name}, you have a ${user.role} role~`))
  .catch((error) => console.log(error));
```

## 🍎 async - await

- Promise를 깔끔하게 사용하기

### async

```javascript
function fetchUser() {
  // 서버 API 호출
  return new Promise((resolve, reject) => {
    resolve("ellie");
  });
}
const user = fetchUser();
user.then(console.log);
console.log(user);
```

- async를 함수 앞에 작성하면 결과값을 Promise로 반환한다.

```javascript
async function fetchUser() {
  // 서버 API 호출
  return "ellie";
}
const user = fetchUser();
user.then(console.log);
console.log(user);
```

### await

- async가 붙은 함수 안에서 사용할 수 있다. 실행이 끝나길 기다림.

```javascript
function delay(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function getApple() {
  await delay(1000);
  return "🍎";
}
async function getBanana() {
  await delay(1000);
  return "🍌";
}
// Promise 사용해서 작성
function getBanana() {
  return delay(1000).then(() => "🍌");
}

// Promise 사용해서 작성
function pickFruits() {
  return getApple().then((apple) => {
    return getBanana().then((banana) => `${apple} + ${banana}`);
  });
}
pickFruits().then(console.log);

// 문제점: 사과 1초, 바나나 1초 소요된다.
async function pickFruits() {
  const apple = await getApple();
  const banana = await getBanana(); //2초 소요
  return `${apple} + ${banana}`;
}

//수정 : 병렬로 처리
async function pickFruits() {
  const applePromise = getApple();
  const bananaPromise = getBanana();
  const apple = await applePromise;
  const banana = await bananaPromise;
  return `${apple} + ${banana}`;
}
```

### Promise API 사용

```javascript
function pickAllFruits() {
  return Promise.all([getApple(), getBanana()]).then((fruits) =>
    fruits.join(" + ")
  );
}
pickAllFruits().then(console.log);

function pickOnlyOne() {
  return Promise.race([getApple(), getBanana()]);
}
pickOnlyOne().then(console.log); //먼저 전달되는 값 출력
```
