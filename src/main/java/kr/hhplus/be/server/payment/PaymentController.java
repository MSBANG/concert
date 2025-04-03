package kr.hhplus.be.server.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.payment.dto.PaymentDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@Tag(name="Payment", description="Payment Domain API")
public class PaymentController {
    @Operation(summary = "잔액 조회", description="잔액을 조회합니다")
    @PostMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentDTO checkUserBalance(
            @RequestParam long user_id
    ) {
        return PaymentDTO.getDefault();
    }
}
