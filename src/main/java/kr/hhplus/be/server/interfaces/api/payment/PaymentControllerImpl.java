package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.interfaces.api.payment.dto.PaySeatReservationRequestDTO;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentControllerImpl implements PaymentController {
    @Override
    public PaymentDTO checkUserBalance(
            @RequestParam long user_id
    ) {
        return PaymentDTO.getDefault();
    }

    @Override
    public PaymentDTO chargeUserBalance(
            @RequestBody PaymentDTO request
    ) {
        return PaymentDTO.getDefault();
    }

    @Override
    public PaymentDTO paySeat(
            @RequestBody PaySeatReservationRequestDTO request
    ) {
        return PaymentDTO.getDefault();
    }
}
