## 🍎 스프링 컨테이너 따라하기

### ApplicationContext와 유사한 AppContext 클래스를 정의해보자!

- 내부에 스프링 빈과 유사한 `Map<String, Object>`을 만든다.
- `getBean(String key)`: 이름, 타입으로 객체를 찾는다.
- `doComponentScan()` : @Component가 붙어있는 클래스를 map에 이름-객체 형태로 저장한다.
- `doAutowired()` : 필드에 @Autowired가 붙어있으면 map에서 타입으로 객체를 찾아 필드에 주입한다.
- `doResource()` : 필드에 @Resource가 붙어있으면 map에서 이름으로 객체를 찾아 필드에 주입한다.

```java
package hello.core.prac;
import com.google.common.reflect.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppContext {
    Map<String, Object> map;

    AppContext(){
        map = new HashMap<>();
        doComponentScan();
        doAutowired();
    }

    private void doComponentScan(){
        try{
            //hello.core.prac 패키지 내의 모든 클래스 가져오기
            ClassLoader classLoader = AppContext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);
            //Set에 모든 클래스 저장하기
            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("hello.core.prac");

            for(ClassPath.ClassInfo classInfo : set){
                Class<?> clazz = classInfo.load();
                Component component = (Component)clazz.getAnnotation(Component.class);
                //클래스에 @Component가 붙어있다면
                if(component!=null){
                    //map에 저장
                    String id= StringUtils.uncapitalize(classInfo.getSimpleName());
                    map.put(id,clazz.newInstance());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //by Name
    public Object getBean(String key){
        return map.get(key);
    }
    //by Type
    public Object getBean(Class clazz){
        for(Object obj: map.values()){
            if(clazz.isInstance(obj))
                return obj;
        }
        return null;
    }

    private void doAutowired(){
        try{
            for(Object bean: map.values()){
                for(Field field : bean.getClass().getDeclaredFields()){
                    //필드에 @Autowired가 붙어있으면 map에서 객체 찾아 주입하기
                    if(field.getAnnotation(Autowired.class)!= null){
                        field.set(bean, getBean(field.getType()));//by Type
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        AppContext ac = new AppContext();

        Car car = (Car) ac.getBean(Car.class);
        Engine engine = (Engine) ac.getBean(Engine.class);
        Engine engine1 = car.engine;

        System.out.println("engine = " + engine);
        System.out.println("engine1 = " + engine1);

    }
}

@Component
class Car {
    @Autowired
    Engine engine;

    @Autowired
    Door door;
}

@Component
class Engine{}

@Component
class Door{}
```

[결과값]

```
engine = hello.core.prac.Engine@cb0ed20
engine1 = hello.core.prac.Engine@cb0ed20
```

- Car의 engine 필드에 AppContext의 map이 관리하는 Engine 객체가 들어간 것을 확인할 수 있다.
- Guava:Google Core Libraries For Java 라이브러리 추가 필요.

### @Resource

: by Name
Project Structure > Libraries > apache-tomcat 추가> 빌드 클릭

```java
private void doAutowired(){
    try{
        for(Object bean: map.values()){
            for(Field field : bean.getClass().getDeclaredFields()){
                //필드에 @Resource가 붙어있으면 map에서 객체 찾아 주입하기
                if(field.getAnnotation(Autowired.class)!= null){
                    field.set(bean, getBean(field.getName()));//by Name
                }
            }
        }
    }catch (Exception e){
        e.printStackTrace();
    }
}
```

---

## 🍎 변경에 유리한 코드 작성하기

### 1. 기존 코드

```java
package com.hyocoding.ch2.diCopy1;

class Car{}
class SportsCar extends Car{}
class Truck extends Car{}

public class Main{
    public static void main(String[] args){
        //SportsCar 객체 생성
        Car car = new SportsCar();
        System.out.println("car="+car);
    }
}
```

- SportsCar가 아닌 Truck 객체를 생성하려면?
- Car car = new Truck(); 이렇게 직접 코드를 건드려서 수정해야함.

### 2. 변경에 유리한 코드- getCar() 작성하기

```java
package com.hyocoding.ch2.diCopy1;

class Car{}
class SportsCar extends Car{}
class Truck extends Car{}

public class Main{
    public static void main(String[] args) throws Exception{
        Car car = getCar();
        System.out.println("car="+car);
    }
    static Car getCar()  throws Exception{
        Properties p = new Properties();
        p.load(new FileReader("config.txt"));

        Class clazz = Class.forName(p.getProperty("car"));

        return (Car)clazz.newInstance();
    }
}
```

[config.txt]는 최상위 프로젝트에 추가하기

```
car=com.hyocoding.ch2.diCopy1.SportsCar
```

- 코드를 변경할 필요 없이 config.txt 외부 파일을 변경함으로써 프로그램이 다르게 동작한다.

### 3. getCar()를 getObject()로 변경하기

```java
package com.hyocoding.ch2.diCopy1;

class Car{}
class SportsCar extends Car{}
class Truck extends Car{}
class Engine{}

public class Main{
    public static void main(String[] args) throws Exception{
        Car car = (Car)getObject("car");
        Engine engine = (Engine) getObject("engine");
        System.out.println("car="+car);
        System.out.println("engine="+engine);
    }

    static Object getObject(String key)  throws Exception{
        Properties p = new Properties();
        p.load(new FileReader("config.txt"));

        Class clazz = Class.forName(p.getProperty(key));

        return clazz.newInstance();
    }
}
```

[config.txt]

```
car=com.hyocoding.ch2.diCopy1.SportsCar
engine=com.hyocoding.ch2.diCopy1.Engine
```
