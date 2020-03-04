# 객체지향개발방법론 1팀 용 설명서

> 작성자: 김상재(s5kywa1k3r, hestarium@hotmail.com)

## 개요

본 문서는 소프트웨어 V&V 팀이 객체지향개발방법론 팀의 개발 코드를 관리 및 검사하기 위하여 객체지향개발방법론 팀에 필요한 작업들을 정리해놓은 문서로서, 각 객체지향개발방법론 팀은 본 문서를 숙지하여 양 팀간의 마찰없이 무사히 프로젝트를 진행 및 마무리 함을 목적으로 합니다.

이하 문서에서 객체지향개발방법론 팀은 "객체팀", 소프트웨어 V&V 팀은 "검증팀"으로 언급합니다.

## 1. 객체팀 <-> 검증팀과의 소통 방법

Slack이라는 메신저를 사용하며 본 Slack에 전 팀원을 가입시켜야 합니다. 따라서 각 객체팀 팀장은 아래의 Link로 Slack에 가입한 후 검증팀 Slack에 접속하여 주시기 바랍니다.

### Link: <http://konkuk-svv.slack.com/>

### 1-1. Slack Channel 설명

본 Slack은 여러(팀 수 미정) 객체 팀과 본 검증팀이 있습니다. 각 Channel 설명을 작성하오니 이 점 참고하여 본 Slack에서 소통할 수 있길 바랍니다.

#### 1) 전체공지 (Mute를 금지합니다.)

- 이 Channel은 모든 팀(검증팀 포함)에게 공통적으로 전해져야할 사항을 알려주는 Channel입니다.

#### 2) 객체?팀_공지

- 이 Channel은 각 객체 팀이 사용하실 공지방입니다.

#### 3) 객체?팀_대화

- 이 Channel은 각 객체 팀이 사용하실 대화방입니다.

#### 4) 객체?팀_개발공지

- 이 Channel은 각 객체 팀이 작성은 불가하나 개발 관련한 알림들이 발생하는 Channel 입니다.
- 본 Channel은 다음과 같은 정보를 알려줍니다.

    1. git의 특정 branch에 push 시 push 알림 발생
    2. jenkins에서 build 시작 및 결과 알림 발생
    3. git에서 완료되지 않은 issue에 대한 알림 발생

- 이후에 Slack에 추가적인 알림이 발생 시 본문에 추가예정.

#### 5) 전체대화

- 이 Channel은 각 객체 팀원들 및 검증 팀원들 간의 대화 방입니다.
- 각 팀간의 소통은 본 채널에서만 이루어지길 권장합니다. (질문에 대한 기록이 중구난방이 될 경우가 있음.)

## 2. 객체팀 개발 방법

### 개발 시 필요한 요소

- IDE: IntelliJ Ultimate
- JDK: Oracle JDK 11.0.6
- Auto Build: Gradle 6.2
- Unit Test: JUnit 5
- 형상 관리: Git

### 2-1. Git Project 사용 방법

#### 개요

본 검증 팀은 Organization을 만들고 각 팀의 Repository를 생성함에 따라 각 팀은 팀 Repository에 팀에서 작성한 코드를 업로드 해야합니다.

#### 규칙

1. 본 검증 팀의 시스템을 이용하기 위해선 반드시 Konkuk-SVV Organization의 각 팀 Repository에 있는 master branch에 코드를 업로드해야합니다.

    - Konkuk-SVV/SMA_T? 의 master branch
    - 만일 팀의 Repository의 master branch에 업로드 하지 않을 경우 본 검증팀의 시스템을 사용할 수 없습니다.
    - ex) s5kywa1k3r/SMA_T? 의 master branch 에 업로드 할 경우 아무것도 실행되지 않습니다.

    - Git 관리를 하는 방법은 크게 두가지 입니다.

        1. Konkuk-SVV/SMA_T? Repository를 fork하여 팀 repository에서 개발 한 후 개발 결과물을 Konkuk-SVV/SMA_T?에 Pull Request 하는 것.
        2. Konkuk-SVV/SMA_T?에서 branch를 만들어서 개발한 후 master branch에 push 하는 것.

    - Git 설명은 본 문서 및 본 프로젝트에서 일절 언급하지 않겠습니다.
    - Git Repository의 권한 및 다른 문제는 팀장에게 질문해주시면 됩니다.

2. Git 사용 미숙으로 인한 코드 이상 및 삭제 발생 시 본 검증 팀은 책임이 없습니다. 따라서 Git에서의 코드 관리를 신중하게 해주시길 바랍니다.
