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
- 예측이 불가능한 3자리 문자열로 구성된 token 생성
- 다수의 서버에 다수의 인스턴스로 동작하더라도 기능
#### 해결 전략
##### 예측이 불가능한 3자리 문자열로 구성된 token 생성
###### 문제 해결 idea
- token에 사용된는 character 종류를 숫자 + 소문자 알파벳 + 대문자 알파벳 으로 설정
- 한자리에 올수 있는 문자열의 경우의 수는 숫자 10개 + 소문자 알파벳 26 + 대문자 알파벳 26, 총 62개의 문자
- 3자리의 문자열이므로 62 * 62 * 62 = 238,328
- 한자리에 올수 있는 경우의 수가 62이므로, 3자리의 62진수 숫자를 생성하는 idea
###### 문제 해결 방법
- 뿌리기 생성시 입력받는 값의 hashCode를 Random class의  seed로 사용
- 0 ~ 238,328 - 1 사이의 숫자 random generation
- 해당 숫자를 62진수로 변경
##### 다수의 서버에 다수의 인스턴스로 동작하더라도 기능
###### 문제 해결 idea
- 다수의 서버를 Load Balancer를 이용하여 Concurrent Access 하여 동작하기 위해서 서버는 stateless 해야함
- 다수의 서버나 인스턴스가 접근할 수 있는 DB가 필요
- Transaction 필요
###### 문제 해결 방법
- JPA를 활용하여 DB에 접근
- 개별 API에 Transaction 설정 (@Transactional annotation)
- 필요 data는 DB에 저장하며, 서버는 stateless 상태 유지
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