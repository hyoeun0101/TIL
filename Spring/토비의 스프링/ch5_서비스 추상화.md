- 작성된 코드 검토하기 - 질문 4가지

* 코드에 중복된 부분은 없는가?
* 코드가 하는 일을 이해하기 어렵진 않은가?
* 코드가 자신이 있어야 할 자리에 잘 있는가?
* 앞으로 어떤 변경이 일어날 수 있고, 그 변화에 쉽게 대응할 수 있게 작성되어 있는가?

##

```java
@Service
@RequiredArgsContructor
public UserServiceImpl {
    private final UserDao userDao;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user : users) {
            Boolean changed = null;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if(user.getLevel() == Level.SILVER && user.getLogin() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if(user.getLevel() == Level.GOLD) {
                changed = false;
            } else {
                changed = false;
            }

            if(changed) userDao.update(user);
        }
    }
}
```
