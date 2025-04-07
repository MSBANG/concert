## 프로젝트
<details>

<summary>Getting Started</summary>

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```
</details>

<details>
<summary>콘서트 예약 서비스</summary>

## Description

- **`콘서트 예약 서비스`**를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

## Requirements

- 아래 5가지 API 를 구현합니다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
- 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.
- 대기열 개념을 고려해 구현합니다.

## API Specs

1️⃣ **`주요` 유저 대기열 토큰 기능**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.
*** 대기열 토큰 발급 API
* 대기번호 조회 API**
>

**2️⃣ `기본` 예약 가능 날짜 / 좌석 API**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
>

3️⃣ **`주요` 좌석 예약 요청 API**

- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 **5분**간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 한다.
- 누군가에게 점유된 동안에는 해당 좌석은 다른 사용자가 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

<aside>
💡 **KEY POINT**

</aside>

- 유저간 대기열을 요청 순서대로 정확하게 제공할 방법을 고민해 봅니다.
- 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 합니다.
</details>

---

### Step03 ~ Step04
<details>
<summary>시퀀스 다이어그램</summary>

<details>
<summary><b>📌콘서트 조회 시퀀스 다이어그램</b></summary>
	
```mermaid
sequenceDiagram
  participant ConcertController
  ConcertController ->>+ ConcertService: 콘서트 목록 조회
  ConcertService ->>+ ConcertRepository: DB 조회 책임 위임
  ConcertRepository -->>- ConcertService: 콘서트 목록
  ConcertService -->>- ConcertController: 콘서트 목록 응답
  ConcertController ->>+ ConcertService: 콘서트 상세 조회
  ConcertService ->>+ ConcertRepository: DB 조회 책임 위임
  ConcertRepository -->>- ConcertService: 콘서트 상세 정보
  ConcertService -->>- ConcertController: 콘서트 상세 정보 응답
```
</details>

<details>
<summary><b>📌대기열 토큰 요청 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    Client ->> Token: 대기열 토큰 요청
    Token ->> Concert: 예매 가능 여부 조회
    opt if 콘서트 전체 매진됨
        Concert -->> Client: ALL_RESERVATED
    end
    Concert -->> Token: 콘서트 예매 가능
    Token ->> Queue: 대기열 Queue Add
    activate Queue
        Queue ->> Queue: 대기열 Queue Add
        Queue ->> Token: 
    deactivate Queue
    activate Token
        Token ->> Token: 토큰 생성
    deactivate Token
    Token -->> Client: 대기열 토큰 응답
```
</details>

<details>
<summary><b>📌대기열 확인 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    QueueController ->> QueueService: 대기열 조회 요청
    QueueService ->> QueueRepository: queue_id, 순번 조회
    alt if 유효하지 않은 데이터
        QueueRepository -->> QueueController: invalidTokenException
    else if 순서가 되지 않음
        QueueRepository -->> QueueService: queueNotReady
        QueueService -->> QueueController: queueNotReady
    else if 순서 도달
        activate QueueRepository
            QueueRepository -->> QueueRepository: Queue 상태 업데이트(대기 -> 진입)
        deactivate QueueRepository
        QueueRepository -->> QueueService: queueReady
        QueueService -->> QueueController: queueReady
    end
```
</details>

<details>
<summary><b>📌콘서트 날짜, 좌석 조회 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    Client ->> Concert: 콘서트 날짜/좌석 조회 요청
    Concert ->> Token: 토큰 검증 요청
    activate Token
        Token ->> Token: 토큰 복호화
    deactivate Token
    opt if Token 상태 == 대기중
        Token -->> Client: 대기열 확인 필요 Exception
    end
    Token -->> Concert: 검증 성공
    activate Concert
        Concert ->> Concert: 콘서트 날짜/좌석 조회
    deactivate Concert
    Concert -->> Client: 
```
</details>

<details>
<summary><b>📌콘서트 좌석 예약 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    Client ->> Concert: 콘서트 좌석 예약 요청
    activate Concert
    Concert ->> Token: 토큰 검증 요청
    deactivate Concert
    activate Token
        Token ->> Token: 토큰 복호화
    opt if Token 상태 == 대기중
        Token -->> Client: queueNotReadyException
    end
    Token -->> Concert: 검증 성공
    deactivate Token
    activate Concert
        Concert ->> Concert: 좌석 예약 가능여부 확인
    opt if 좌석 상태 == 예약됨 or 결제됨
        Concert -->> Client: seatAlreadyReservatedException
    end
    Concert ->> Reservation: 콘서트 좌석 예약 정보 저장
    deactivate Concert
    activate Reservation
        Reservation ->> Reservation: 좌석 예약 정보 저장
        Reservation -->> Concert: 
    deactivate Reservation
    activate Concert
        Concert ->> Concert: 좌석 상태 변경: "예약 불가"
    Concert ->> Queue: Queue polling
    deactivate Concert
    activate Queue
        Queue ->> Queue: Queue polling
        Queue -->> Concert: 
    deactivate Queue
    Concert -->> Client: 예약 성공
```
</details>

<details>
<summary><b>📌잔액 조회 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    PaymentController ->> PaymentService: 잔액 조회 요청
    PaymentService ->> PaymentRepostiry: 잔액 조회
    activate PaymentRepostiry
        PaymentRepostiry ->> PaymentRepostiry: DB 조회
        PaymentRepostiry -->> PaymentService: 
    deactivate PaymentRepostiry
    PaymentService -->> PaymentController: 잔액 응답
```
</details>

<details>
<summary><b>📌잔액 충전 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    PaymentController ->> PaymentService: 잔액 충전 요청
    activate PaymentService
        PaymentService ->> PaymentService: amount 검증
    deactivate PaymentService
    opt if 유효하지 않은 amount
        PaymentService -->> PaymentController: invalidAmountException
    end
    PaymentService ->> PaymentRepostiry: 잔액 충전
    activate PaymentRepostiry
        PaymentRepostiry ->> PaymentRepostiry: DB 업데이트
        PaymentRepostiry -->> PaymentService: 
    deactivate PaymentRepostiry
    PaymentService -->> PaymentController: 충전된 잔액 응답
```
</details>

<details>
<summary><b>📌좌석 결제 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    Client ->> Payment: 좌석 결제 요청
    activate Payment
        Payment ->> Reservation: 요청 확인
    deactivate Payment
    activate Reservation
        Reservation ->> Reservation: DB 조회
        alt if 조회 실패
            Reservation -->> Client: invalidReservationException
        else if 만료된 예약정보
            Reservation -->> Client: expiredReservationException
        end
    Reservation -->> Payment: 예약 정보
    deactivate Reservation
    activate Payment
        Payment ->> Payment: 좌석 가격, 잔금 비교
        opt if 좌석 가격 > 잔금
            Payment -->> Client: insufficiantBalanceExcpetion
        end
        Payment ->> Payment: 잔액 차감
        Payment ->> Reservation: 예약상태 변경 요청
    deactivate Payment
    activate Reservation
        Reservation ->> Reservation: 예약 상태 변경: "결제됨"
    deactivate Reservation
    activate Payment
    Reservation -->> Payment: 
    Payment -->> Client: 결제 완료
    deactivate Payment
```
</details>

<details>
<summary><b>📌예약상태 스케줄러 시퀀스 다이어그램</b></summary>

```mermaid
sequenceDiagram
    autonumber
    ReservationScheduler ->> ReservationRepository: 결제되지 않은 만료된 예약건 조회
    activate ReservationRepository
        ReservationRepository ->> ReservationRepository: DB 조회
        ReservationRepository -->> ReservationScheduler: 
    deactivate ReservationRepository
    ReservationScheduler ->> ReservationRepository: 만료된 예약건 상태 변경
    activate ReservationRepository
        ReservationRepository ->> ReservationRepository: 예약 상태 변경: "만료됨"
    deactivate ReservationRepository
    ReservationRepository -->> ReservationScheduler: 
    ReservationScheduler ->> ConcertRepository: 좌석 상태 변경
    activate ConcertRepository        
        ConcertRepository ->> ConcertRepository: 좌석 상태 변경: "예약 가능"
        ConcertRepository -->> ReservationScheduler: 
    deactivate ConcertRepository
```
</details>
</details>
<details>
<summary><b>ERD</b></summary>
	
![Image](https://github.com/user-attachments/assets/cf4c4d34-0edf-4c10-b967-4122ddd07fb3)

</details>

---

### Step05
<details>
<summary>과제 내용</summary>

-  각 시나리오별 하기 **비즈니스 로직** 개발 및 **단위 테스트** 작성
    - `e-commerce` : 상품 조회, 주문/결제 기능, 포인트 충전 기능
    - `concert` : 콘서트 조회, 예약/결제 기능, 포인트 충전 기능

> **단위 테스트** 는 반드시 대상 객체/기능 에 대한 의존성만 존재해야 함
>
</details>

