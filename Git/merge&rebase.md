## 🍎 브랜치 병합 2가지 - merge, rebase

![merge](https://user-images.githubusercontent.com/96059261/204185173-d2f8ee19-52bd-4d83-b3f4-4b7185db8e1f.PNG)

- 노란 커밋이 merge했을떄, 분홍 커밋이 rebase했을 때이다.  
### merge와 rebase의 차이점
- merge는 브랜치의 잔가지들이 남고 하나의 커밋이 생성되어 합쳐진다.  
- 반면 rebase는 브랜치의 잔가지를 없애고 브랜치의 커밋 모두를 메인브랜치에 가져와 합치는 방식이다.  
- 브랜치의 가지를 없애고 메인 브랜치 하나로 깔끔하게 관리하고 싶을 때 rebase를 사용하지만 협업 할때는 rebase를 사용하지 않는 것이 좋다.

## 🍎 merge
- 메인 브랜치와 다른 브랜치 합치기. 
- 메인 브랜치에서 다른 브랜치를 merge하면 메인 브랜치에 하나의 커밋이 생성된다. 히스토리에 브랜치들이 남아있다.
```
//메인 브랜치에서 입력
git merge Branch-Name
```

### merge 시 충돌 해결

- 메인 브랜치와 머지하려는 다른 브랜치의 내용이 서로 다를 때 충돌이 발생한다.
- merge하는 하나의 커밋만 생성되기 때문에 한번만 충돌 해결을 하면 된다.
- 충돌 해결 후 커밋.

### merge가 이루어지는 방식

- **Fastforward**     

![img](https://user-images.githubusercontent.com/96059261/210202726-58fc16b8-f6f2-49e2-bb50-4c571f4e075a.png)     


- 메인 브랜치의 마지막 커밋에서 A브랜치가 뻗어나갔을 때, 메인 브랜치에서 A브랜치를 병합하면 메인 브랜치의 HEAD는 Fast forward 한다.  
- 병합된 새로운 커밋이 생성되는 것이 아니라 메인 브랜치가 병합할 브랜치의 마지막 커밋 상태로 간다는 것이다.  
- 하지만 이렇게 되면 어떤 브랜치를 생성하여 작업했는지 알 수가 없다.
- 기존의 merge 방식처럼 메인 브랜치에서 새로운 커밋을 생성하려면 다음 명령어를 사용한다.
```
git merge --no --ff 병합할 브랜치명
```

- **3-way-merge**     

   ![r](https://user-images.githubusercontent.com/96059261/210203371-890d2e71-4d5e-4aa3-a456-42a40c7b7b2b.png)  


- 우리가 아는 merge 방식  
- 새로운 브랜치로 뻗어간 메인브랜치의 커밋, 그 이후의 커밋, 다른 브랜치와 병합한 커밋 이렇게 세 개의 상태를 비교하며 merge해서 3-way-merge라고 한다.

## 🍎 rebase

- 메인 브랜치에 다른 브랜치의 커밋을 모두 가져오며 브랜치의 흔적이 사라진다.
- 다른 브랜치에서 메인 브랜치로 rebase한다. 
- 다른 브랜치에서 rebase 한 후, 메인 브랜치로 이동하면 다른 브랜치의 커밋이 생성된 걸 볼 수 있다. 그 다음 메인 브랜치에서 다른 브랜치를 merge 해줘야한다.
```
//A브랜치에서 진행
git rebase main
git switch main
get merge A-branch
```

### rebase 시 충돌 해결

- 여러 커밋이 메인 브랜치로 옮겨지기 때문에 각각의 커밋마다 충돌 해결을 해줘야한다.  

```
//한 개 커밋 충돌 해결하고
git add .
git rebase --continue //남은 커밋 rebase 계속 하기

//rebase 완료 후, 메인 브랜치로 이동 후 merge하기.
```



## 🍎 merge/rebase 중단

```
git merge --abort
git rebase --abort
```
