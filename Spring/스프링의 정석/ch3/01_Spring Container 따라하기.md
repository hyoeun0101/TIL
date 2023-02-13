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

## 🍎@Resource
: by Name
- 표준 애너테이션 - Project Structure > Libraries > apache-tomcat 추가 > 빌드 클릭
- @Resource(name="이름"). name 생략 시 참조변수 이름이 기본값이다.

```java
private void doResource(){
    try{
        for(Object bean: map.values()){
            for(Field field : bean.getClass().getDeclaredFields()){
                //필드에 @Resource가 붙어있으면 map에서 객체 찾아 주입하기
                if(field.getAnnotation(Resource.class)!= null){
                    field.set(bean, getBean(field.getName()));//by Name
                }
            }
        }
    }catch (Exception e){
        e.printStackTrace();
    }
}
```