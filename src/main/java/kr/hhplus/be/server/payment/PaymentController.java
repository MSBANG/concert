package kr.hhplus.be.server.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.payment.dto.PaymentDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Tag(name="Payment", description="Payment Domain API")
public class PaymentController {
    @Operation(summary = "잔액 조회", description="잔액을 조회합니다")
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentDTO checkUserBalance(
            @RequestParam long user_id
    ) {
        return PaymentDTO.getDefault();
    }

    @Operation(summary = "잔액 충전", description="금액만큼 충전합니다")
    @PostMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentDTO chargeUserBalance(
            @RequestBody PaymentDTO request
    ) {
        return PaymentDTO.getDefault();
    }
}
