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
