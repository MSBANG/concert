package kr.hhplus.be.server.interfaces.api.payment;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaySeatReservationRequestDTO;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Payment", description = "Payment Domain API")
interface PaymentController {
    @Operation(summary = "잔액 조회", description = "잔액을 조회합니다")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    PaymentDTO checkUserBalance(
            @RequestParam long user_id
    );

    @Operation(summary = "잔액 충전", description = "금액만큼 충전합니다")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    PaymentDTO chargeUserBalance(
            @RequestBody PaymentDTO request
    );

    @Operation(summary = "좌석 결제", description = "충전된 잔액으로 좌석 예약건을 결제합니다.")
    @PostMapping(value = "/paySeat", produces = MediaType.APPLICATION_JSON_VALUE)
    PaymentDTO paySeat(
            @RequestBody PaySeatReservationRequestDTO request
    );
}
