# Async Socket Server similar to Spring Project
- Java의 NIO(AsynchronousServerSocketChannel)를 이용한 멀티 쓰레드 비동기 소켓 서버 라이브러리 및 프레임워크 구축
- Spring 프레임워크의 주요 개념을 참고하여 IoC/DI(Bean주입), Spring MVC 동작구조, Session, DB ConnectionPool, RequestMapping, Controller등의 어노테이션을 직접 구현

# 프로젝트 개요
프로젝트 기간 
 - 서버 라이브러리 & 프레임워크 구축 (23.10.09 ~ 23.10.15)
 - 해당 프레임워크를 이용한 첫 프로젝트(도서관리 프로그램) 개발 기간 (23.10.18 ~ 23.10.22)

사용 기술
 - Java(8 람다,스트림,함수형 인터페이스), git, FTP(배포, FileZilla), Oracle & JDBC

# 프로젝트 기능 구현
 - IoC/DI 구현
https://github.com/dev-min2/JavaFirstProject/blob/96a2c2dfa6cd1f3251fade9c8f49cf2d83189f45/bookManagementServer/src/main/resources/bean.xml#L1-L23
   *(실제 프로젝트에 사용한 bean 등록 xml)*

   bean.xml에 등록된 bean들을 Bean컨테이너를 통해 생성 및 관리


   구현 클래스
   > javaSvr/src
   >> CoreActive
   >>> ApplicationBeanLoader.java
   >>> 
   >>> BeanContainer.java

   **현재는 생성자를 통한 의존성 주입만 구현이 되어있음.**
 - Spring MVC 동작구조 구현

   
   ![이미지](img/프레임워크MVC동작구조.png)
   
   구현 클래스
   > javaSvr/src
   >> CoreActive
   >>> DispatcherBot.java
   >>> 
   >>> HandlerMapping.java
   >>> 
   >>> HandlerAdapter.java
   >>> 
   >>> RequestMapping.java
   >>> 
   >>> Controller.java
   
- 패킷 클래스(객체 직렬화)
  









































[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fgjbae1212%2Fhit-counter)](https://hits.seeyoufarm.com)
