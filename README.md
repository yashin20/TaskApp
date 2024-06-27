# SpringBoot-Project TaskApp (일정 관리 서비스)


## 목차
 - [1. 프로젝트 소개](#1-프로젝트-소개)
   - [1-1. 프로젝트 소개](#1-1-프로젝트-소개)
   - [1-2. 프로젝트 기능](#1-2-프로젝트-기능)
   - [1-3. 개발 환경](#1-3-개발-환경)
   - [1-4. 실행 화면](#1-4-실행-환경)
  
 - [2. 프로젝트 구조](#2-프로젝트-구조)
   - [2-1. 패키지 구조](#2-1-패키지-구조)
   - [2-2. DB 설계](#2-2-DB-설계)
   - [2-3. API 설계](#2-3-API-설계)
  
 - [개발 내용](#개발-내용)

 - [마치며](#마치며)
   - [1. 프로젝트 보완사항](#1-프로젝트-보완사항)
   - [2. 프로젝트 과정에서 발생한 문제](#2-프로젝트-과정에서-발생한-문제)
   - [3. 후기](#3-후기)
  
     


## 1. 프로젝트 소개

### 1-1. 프로젝트 소개

실생활에서 할일 정리에 대한 필요성을 많이 느낀 것이 아이템 선정 이유의 가장 큰 부분입니다.
TaskApp 프로젝트에서 가장 신경쓴 부분은 '소셜 로그인' 구현 입니다.
많은 웹 서비스에서 활용하고 있는 소셜 로그인 기능을 직접 구현해 보고 동작 방식을 학습하는 것에 많은 의미를 두었습니다.
이 프로젝트를 통해, Spring Security 를 통한 로그인 기능 , OAuth2 소셜 로그인 방식 등을 학습할 수 있었습니다.


### 1-2. 프로젝트 기능

TaskApp의 주요 기능은 다음과 같습니다.

회원
- Security 회원가입 및 로그인 기능
- 소셜 로그인 기능
- 회원 정보 수정
- 회원 탈퇴
- 유효성 및 중복 검사

TASK
- CRUD 기능
- TASK 페이징
- TASK 검색 기능
- TASK 완료 / 비완료 체크 기능
- TASK 완료 퍼센트 시각화 기능


### 1-3. 개발 환경

#### Back-end
 - Java 21
 - SpringBoot 3.2.6
 - JPA(Spring Data JPA)
 - Spring Security
 - OAuth2.0

##### Build Tool
 - Gradle 8.7

##### DataBase
 - MySQL 8.0.36

#### Front-end
 - html/css
 - JavaScript
 - Thymeleaf
 - Bootstrap 5.3.2


### 1-4. 실행 화면
 
  <details>
    <summary>회원</summary>
  
   **1. 회원가입 화면** 
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/671ba3b5-c449-4942-8a85-6d80d9c7ed6c)  

   ![image](https://github.com/yashin20/TaskApp/assets/92693776/e7a42011-46d6-44eb-90d6-0f4e0f29750d)  
   ※ 회원가입 폼의 유효성 검사 에러 메시지를 확인 할 수 있다.  
 

   **2. 로그인 화면**
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/032b0208-f1f8-46fd-926e-dba07d0cb31a)  


   **3. 회원정보 화면**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/3bd743d9-d183-4eaf-a8bd-0d96426f909d)
   우측 상단, 현재 로그인된 회원명을 클릭하여 회원 정보 페이지로 이동  

   ![image](https://github.com/yashin20/TaskApp/assets/92693776/4d8f0f3b-d797-4cdf-933f-4e32aa2dbafa)  
   현재 회원의 "회원 정보" , "완료 과업", "비완료 과업" 을 확인 할 수 있다.


   **4. 회원정보 수정 화면 (Email, Phone)**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/455b3d36-6b2e-4398-9f9f-f6e6c97b0d41)  
   "회원 정보 수정" 버튼을 클릭하여, 회원 정보 수정 페이지로 이동한다.

   ![image](https://github.com/yashin20/TaskApp/assets/92693776/b7871b20-6c0a-4977-9fc4-9f0cd4e0a45f)  
   Email, Phone 을 수정하고 'SAVE' 버튼을 누른다.  

   ![image](https://github.com/yashin20/TaskApp/assets/92693776/750572e5-813f-4881-885f-962a15108479)  
   수정된 회원 정보(Email, Phone) 을 확인 할 수 있다.  


   **5. 회원정보 수정 화면 (Password)**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/31f39f48-5523-4d29-985a-9e2598b8cf42)  


   **6. 회원 탈퇴 화면**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/9c7e74d4-2043-4eef-a235-24af96afc183)  

    
  </details>


  <details>
    <summary>TASK</summary>

   **1. Task 목록**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/2e39df83-6bb5-4f36-838b-d9a5e4531c8d)  
   화면 왼편에 페이징된 Task 테이블을 확인할 수 있다.  


   **2. Task 상세정보**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/38918305-a9dc-43af-998a-d43d9d12a503)  


   **3. Task 생성**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/94d580db-4ee2-466c-841b-3bfdecdeae2e)  
   화면 오른편에 Task 작성 폼을 확인할 수 있다.  


   **4. Task 수정**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/19a733e3-c902-49e8-b6bb-ca47d3c72f7a)  
   파란색 "Task 수정" 버튼을 클릭하여, Task 수정 페이지로 이동한다.  

   ![image](https://github.com/yashin20/TaskApp/assets/92693776/212d2dac-56ae-4283-8b33-c4dc7098ef58)  
   Task Title, Task Content 를 수정하고 "SAVE TASK" 를 클릭하여, Task 수정을 완료한다.  


   **5. Task 삭제**  
   ![image](https://github.com/yashin20/TaskApp/assets/92693776/743ab935-f531-47c7-9af6-d5ba2953fca6)  
   "Task 삭제"를 클릭하여, Task를 삭제한다.  


   **6. Task 완료 체크 기능 화면**  
   ![GIF](https://github.com/yashin20/TaskApp/assets/92693776/6ce34493-2878-45a3-96bd-f78b268a5a87)  
   Task를 체크 표시, 체크 표시 해제를 할 수 있고, 체크된 Task 비율이 시각적으로 표시되는 것을 확인할 수 있다.  

    
  </details>




## 2. 프로젝트 구조

### 2-1. 패키지 구조

<details>

<summary>패키지 구조 보기</summary>

```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂project
 ┃ ┃ ┃ ┗ 📂task_app
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜WebSecurityConfig.java
 ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📜HomeController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InitMember.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskController.java
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜TaskRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskResponseDto.java
 ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseEntity.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Member.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜Task.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserRole.java
 ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DataAlreadyExistsException.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DataNotFoundException.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜PasswordCheckFailedException.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UnauthorizedAccessException.java
 ┃ ┃ ┃ ┃ ┣ 📂oauth
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomOAuth2UserService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜OAuthAttributes.java
 ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskRepository.java
 ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskService.java
 ┃ ┃ ┃ ┃ ┗ 📜TaskAppApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┃ ┣ 📂css
 ┃ ┃ ┃ ┃ ┣ 📜footer-styles.css
 ┃ ┃ ┃ ┃ ┣ 📜header-styles.css
 ┃ ┃ ┃ ┃ ┣ 📜info-styles.css
 ┃ ┃ ┃ ┃ ┣ 📜info-update-styles.css
 ┃ ┃ ┃ ┃ ┣ 📜join-styles.css
 ┃ ┃ ┃ ┃ ┣ 📜main-styles.css
 ┃ ┃ ┃ ┃ ┣ 📜styles.css
 ┃ ┃ ┃ ┃ ┣ 📜task-info-styles.css
 ┃ ┃ ┃ ┃ ┗ 📜task-styles.css
 ┃ ┃ ┃ ┗ 📂img
 ┃ ┃ ┃ ┃ ┣ 📜check-icon.png
 ┃ ┃ ┃ ┃ ┣ 📜empty-circle.png
 ┃ ┃ ┃ ┃ ┣ 📜github-icon.png
 ┃ ┃ ┃ ┃ ┣ 📜google-logo.png
 ┃ ┃ ┃ ┃ ┣ 📜kakao-logo.png
 ┃ ┃ ┃ ┃ ┣ 📜naver-logo.png
 ┃ ┃ ┃ ┃ ┗ 📜task-app.png
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┣ 📂fragments
 ┃ ┃ ┃ ┃ ┣ 📜footer.html
 ┃ ┃ ┃ ┃ ┗ 📜header.html
 ┃ ┃ ┃ ┣ 📂members
 ┃ ┃ ┃ ┃ ┣ 📜info-update.html
 ┃ ┃ ┃ ┃ ┣ 📜info.html
 ┃ ┃ ┃ ┃ ┣ 📜join.html
 ┃ ┃ ┃ ┃ ┣ 📜login.html
 ┃ ┃ ┃ ┃ ┗ 📜password-update.html
 ┃ ┃ ┃ ┣ 📂tasks
 ┃ ┃ ┃ ┃ ┣ 📜task-info.html
 ┃ ┃ ┃ ┃ ┣ 📜task-list.html
 ┃ ┃ ┃ ┃ ┗ 📜task-update.html
 ┃ ┃ ┃ ┗ 📜index.html
 ┃ ┃ ┣ 📜application-oauth.yml
 ┃ ┃ ┗ 📜application.yml
 ┗ 📂test
 ┃ ┗ 📂java
 ┃ ┃ ┗ 📂project
 ┃ ┃ ┃ ┗ 📂task_app
 ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜TaskServiceTest.java
 ┃ ┃ ┃ ┃ ┗ 📜TaskAppApplicationTests.java
```


</details>



### 2-2. DB 설계
![image](https://github.com/yashin20/TaskApp/assets/92693776/bc650a40-e364-482b-ba13-a9b09eb70f13)  
  

### 2-3. API 설계
**회원(Member) 관련 API**  
![image](https://github.com/yashin20/TaskApp/assets/92693776/3c72adb1-12d1-4f02-864c-edadb96ed4e4)  

**Task 관련 API**  
![image](https://github.com/yashin20/TaskApp/assets/92693776/83207f28-78c2-49d3-bb76-7162d3b8b2e7)  

  


## 개발 내용

- <a href="https://notorious.tistory.com/343" target="_blank">[TaskApp] 프로젝트 환경 설정</a>
- <a href="https://notorious.tistory.com/344" target="_blank">[TaskApp] Spring Security 로그인 구현 하기</a>
- <a href="https://notorious.tistory.com/345" target="_blank">[TaskApp] 회원정보 수정하기 (E-mail, Phone)</a>
- <a href="https://notorious.tistory.com/346" target="_blank">[TaskApp] 회원정보 수정하기 - 2 (Password)</a>
- <a href="https://notorious.tistory.com/347" target="_blank">[TaskApp] Google 로그인 구현하기</a>
- <a href="https://notorious.tistory.com/348" target="_blank">[TaskApp] NAVER 로그인 구현 하기</a>
- <a href="https://notorious.tistory.com/349" target="_blank">[TaskApp] KAKAO 로그인 구현 하기</a>
- <a href="https://notorious.tistory.com/350" target="_blank">[TaskApp] 한 페이지에 2개 테이블 페이징하기 (AJAX 아님)</a>
- <a href="https://notorious.tistory.com/351" target="_blank">[TaskApp] Task 체크 기능 구현 (완료 / 비완료 과업 변경하기)</a>



## 마무리

### 1. 프로젝트 보완사항


### 2. 프로젝트 과정에서 발생한 문제


### 3. 후기

