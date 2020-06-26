# money-scatter-server
## 과제 개요
- 같은 대화방에 있는 다수의 사용자에게 머니 뿌리기 기능을 제공하기 위한 간소화 된 REST API 구현
### 요구사항 분석
#### 요구사항
- [x] 뿌리기, 받기, 조회 기능 구현
- [x] X-USER-ID, X-ROOM-ID 값을 header로 활용
- [x] 다수의 인스턴스에서도 동작
- [x] 단위 테스트 작성
### 핵심 문제해결 전략
#### 핵심 문제 파악
#### 해결 전략

## 개발 환경
### 활용 library
- gradle
- spring-boot
- h2database
### 활용 tool
- IntelliJ IDEA 2020.1 (Community Edition)
- Postman
### 개발언어
- Java 8

## API 명세
### 뿌리기 
- 머니 뿌리기 생성
- Request
    - method: PUT
    - URI: /scatter
    - header: 
        - X-USER-ID: 사용자 ID (숫자)
        - X-ROOM-ID: 대화방 ID (문자열)
    - body
        - cost: 뿌릴 금액
        - number_of_person: 뿌릴 사람의 수
        - example
        ```json
            {"cost":10000, "number_of_person": 5}
        ```
- Response
    - Status: 200 OK
    - body
        - 뿌리기 token: 3자리 문자열
### 받기
- 생성된 머니 뿌리기를 받기
- Request
    - method: POST
    - URI: /pick
    - header: 
        - X-USER-ID: 사용자 ID (숫자)
        - X-ROOM-ID: 대화방 ID (문자열)
- Response
    - Status: 200 OK
    - body
        - 받은 금액: 숫자
    - Error 상황: 400 Bad Request 를 응답
        - 해당 뿌리기가 존재하지 않을 때
        - 스스로 생성한 뿌리기 였을 경우
        - 이미 한차례 해당 뿌리기에서 받았던 경우
        - 다른 대화방에서 받기 시도할 경우
        - 뿌리기 생성 이후 10분이 지났을 경우
### 조회
- 생성된 머니 뿌리기를 조회
- Request
    - method: GET
    - URI: /scatter
    - header: 
        - X-USER-ID: 사용자 ID (숫자)
        - X-ROOM-ID: 대화방 ID (문자열)
- Response
    - Status: 200 OK
    - body
        - scattered_time: 뿌린 시간
        - total_cost: 뿌린 금액
        - picked_cost: 받기 완료된 금액
        - pick: 받기 완료된 정보
            - user_id: 받은 사용자 id
            - cost: 받은 금액
        - example
        ```json
            {
                "scattered_time": 1593198716928,
                "total_cost": 30,
                "picked_cost": 30,
                "pick": [
                    {
                        "user_id": 1,
                        "cost": 15
                    },
                    {
                        "user_id": 2,
                        "cost": 5
                    },
                    {
                        "user_id": 1,
                        "cost": 10
                    }
                ]
            }
        ```
    - Error 상황: 400 Bad Request 를 응답
        - 해당 뿌리기가 존재하지 않을 때
        - 사용자 스스로 생성한 뿌리기가 아닐 때
        - 뿌리기가 유효하지 않을 때
        - 뿌리기 생성 이후 7일이 지났을 경우