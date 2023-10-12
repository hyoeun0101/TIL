## 🍎 @MapperScan
- java config를 사용할 때, 매퍼 인터페이스를 등록하기 위해 사용한다.
- `MapperScannerRegistrar`를 통해 `MapperScannerConfigurer`와 같은 동작을 수행한다.
- basePackageClasses(), basePackages()를 지정하면 해당 패키지에 존재하는 모든 인터페이스를 찾아 매퍼로 등록한다. 2.0.4 버전 이후로 특정 패키지를 지정하지 않으면 @MapperScan을 선언한 클래스의 패키지를 디폴트로 설정한다.
## 🍎 @Mapper
- @Mapper가 붙은 인터페이스를 매퍼로 등록한다.

