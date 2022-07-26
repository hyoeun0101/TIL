### 분리
- 변하는 것 vs 변하지 않는 것
- 관심사의 분리
- 중복 코드

자신의 코드 수정하기. 실습 부족. 이론 보단 어떻게 변경에 유리한 코드를 작성할 수 있을 까 고민하기.
AOP가 중복 코드 제거 역할.   

# 1. 변경에 유리한 코드 작성하기

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

SportsCar가 아닌 Truck 객체를 생성하려면?   
Car car = new Truck(); 이렇게 직접 코드를 건드려서 수정해야함.   

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
config.txt는 최상위 프로젝트에 추가하기
```
car=com.hyocoding.ch2.diCopy1.SportsCar
```
   
-> 코드를 변경할 필요 없이 config.txt 외부 파일을 변경함으로써 프로그램이 다르게 동작한다.   

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
config.txt
```
car=com.hyocoding.ch2.diCopy1.SportsCar
engine=com.hyocoding.ch2.diCopy1.Engine
```


# 2. AppContext

### 1. 하드코딩
```java
class AppContext{
    Map map;

    AppContext(){
        map  = new HashMap();
        map.put("car", new SportsCar());
        map.put("engine", new Engine());
    }

    Object getBean(String key){
        return map.get(key);
    }
}

public Class Main2{
    public static void main(String[] args){
        AppContext ac = new AppContext();
        Car car =(Car) ac.getBean("car");
        Engine engine = (Engine)ac.getBean("engine");
    }
}
```
-> map에 <String, Object>로 저장한 후, getBean(key)을 통해 객체를 반환한다.
   
### 2. 변경에 유리한 코드로 변경하기
- Properties <String, String>을 <String, Object>로 변경하여 map에 저장.
     
```java
class AppContext{
    Map map;

    AppContext(){
        try{
            Properties p = new Properties();
            p.load(new FileReader("config.txt"));

            map  = new HashMap(p);

            for(Object key : map.keySet()){
                Class clazz = Class.forName((String)map.get(key));
                map.put(key,clazz.newInstance());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    Object getBean(String key){
        return map.get(key);
    }
}
```
   
### 3. 자동 객체 등록하기 - Component Scanning
① 패키지 내의 모든 클래스를 읽어서 Set에 저장      
② 패키지 내에 @Component 붙은 클래스 찾기    
③ 객체 생성해서 map에 저장    

```java
class AppContext{
    Map map;

    AppContext(){
        map = new HashMap();
        doComponentScan();
    }

    private void doComponentScan(){
        try{
            //1. 패키지 내의 모든 클래스 가져오기
            ClassLoader classLoader = AppContext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);

            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("com.hyocoding.ch2.diCopy3");

        
            for(ClassPath.ClassInfo classInfo : set){
                Class clazz = classInfo.load();
                Component component = (Component)clazz.getAnnotation(Component.class);
                //2. @Component 붙었는지 확인
                if(component!=null){
                    String id= StringUtils.uncapitalize(classInfo.getSimpleName());
                    map.put(id,clazz.newInstance());
                }
            }
        //3. map에 객체 생성하여 저장하기<String, Object>

        }
        

    }
}
```
maven repository-Guava:Google Core Libraries For Java 라이브러리 사용하기

# 3. 
### 1.객체 찾기-by Name, by Type
by Name
```java
Object getBean(String key){
    return map.get(key);
}
```
by Type
```java
Object getBean(Class clazz){
    for(Obejct obj: map.values){
        if(clazz.isInstance(obj))
            return obj;
    }
    return null;
}

...
main(){
    Car car2 = ac.getBean(Car.class);
}
```

### 2. @Autowired
- @Autowired : by Type
```java
class Car{
    Engine engine;
    Door door;
}
```
```java
AppContext ac = new AppContext();
Car car = (Car)ac.getBean("car");
Engine engine = (Engine)ac.getBean("engine");
Door door = (Door)ac.getBean("door");

car.engine = engine;
car.door = door;
```
-> 수동 연결. 
```java
class Car{
    @Autowired
    Engine engine;

    @Autowired
    Door door;
}
```
-> 자동 연결

- @Resource : by Name
```java
class Car{
    //@Resource(name="engine")
    //name 생략하면 첫글자 소문자가 디폴트
    @Resource
    Engine engine;
    @Resource
    Door door;
}
```
### 3. @Autowired 실습하기
```java
private void doAutowired(){
    //map에 저장된 객체의 iv중에 @Autowired 붙어있으면
    //map에서 iv의 타입에 맞는 객체를 찾아(by Type) iv와 연결.
    try{
        for(Object bean : map.values()){
        for(Field field : bean.getObject().getDeclareFields()){
            fi(field.getAnnotation(Autowired.class)!=null){//Autowired 붙었으면
                field.set(bean.getBean(field.getType()));
            }
        }
    }

    }catch(IllegalAccessException e){
        e.printStackTrace();
    }
}
```

### 4. @Resource 실습하기
Project Structure > Libraries > apache-tomcat 추가> 빌드 클릭   

```java
private void doResource(){
    //map에 저장된 객체의 iv중에 @Resource 붙어있으면
    //map에서 iv의 타입에 맞는 객체를 찾아(by Name) iv와 연결.
    try{
        for(Object bean : map.values()){
        for(Field field : bean.getObject().getDeclareFields()){
            fi(field.getAnnotation(Resource.class)!=null){//Autowired 붙었으면
                field.set(bean.getBean(field.getName()));
            }
        }
    }

    }catch(IllegalAccessException e){
        e.printStackTrace();
    }

}
```
