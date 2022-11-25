# SOLID 객체 지향 설계 5원칙
높은 응집력, 낮은 결합력

## 1. SRP
single responsibility principle (단일 책임의 원칙) : `클래스는 단 한 개의 책임을 가져야한다.`, `클래스를 변경하는 이유는 하나여야함.`   

## 2. OCP
open-closed principle(개방 폐쇄 원칙) : `확장에는 열려있고, 변경에는 닫혀있어야한다.`   
기존의 코드를 변경하지 않고, 수정할 수 있다.

## 3. LSP
Liskov Substitution (리스코프 치환 원칙) : `하위 타입 객체는 상위 타입 객체로 치환할 수 있어야한다.`

## 4. ISP
Interface Segregation (인터페이스 분리 원칙) : `클라이언트는 자신이 사용하는 메서드에만 의존해야 한다.`

## 5. DIP
Dependency Inversion Principle (의존 역전의 원칙) : `구체적인 것이 추상적인 것에 의존해야한다.`, 즉 자주 변하는 클래스에 의존하지 마라.