## 🔴 mybatis-config.xml
- resources 바로 아래 위치.

### 카멜케이스 변환
- DB의 컬럼(snake_case)과 java의 필드(calmelCase)와 매핑

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<settings>
		<setting name="mapUnderscoreToCamelCase" value="true"/>
	</settings>
</configuration>
```

- java 파일에서 camelCase, mapper에서 snake_case를 작성한다.

## 🍎 MyBatis가 getter/setter 찾는 법

- DTO나 VO를 이용해서 DB의 데이터를 가져올 때 Reflection을 이용한다. Reflection을 이용해서 객체를 생성할 때 기본 생성자를 이용하기 때문에 기본 생성자를 작성해 주어야 한다.
- 우선 대상 클래스에서 getter, setter를 각각 getMethods, setMethods라는 Map에 저장한다. Map의 key는 해당 메서드를 통해 추출한 필드명이다. ex) name : getName()
  - 실제 변수는 없고, getter나 setter만 있더라도 필드명을 추출하여 Map에 저장한다.
- 그 다음 필드에 대해 처리한다. 위에서 말한 Map(getMethods, setMethods)에 해당 필드가 없으면, getter 또는 setter를 만들어 Map에 추가한다.
  - 그래서 클래스에서 명시적으로 getter, setter를 작성하지 않아도 동작하는 것이다.
