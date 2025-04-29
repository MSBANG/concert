package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.interfaces.api.common.ResponseCodeEnum;
import kr.hhplus.be.server.support.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentDomainTest {

    @Test
    @DisplayName("잔액이 부족하면 use 시 APIException.insufficientBalance() 예외가 발생해야 한다")
    void test_use_with_insufficient_balance_should_throw_exception() {
        // given
        Payment payment = Payment.create(1L, 500); // 500원 보유

        // when & then
        assertThatThrownBy(() -> payment.use(1000)) // 1000원 사용 시도
                .isInstanceOf(APIException.class)
                .hasMessageContaining(ResponseCodeEnum.INSUFFICIENT_BALANCE.getMessage());
    }

    @Test
    @DisplayName("충전 금액이 0 이하이면 charge 시 APIException.insufficientAmount() 예외가 발생해야 한다")
    void test_charge_with_non_positive_amount_should_throw_exception() {
        // given
        Payment payment = Payment.create(1L, 1000);

        // when & then
        assertThatThrownBy(() -> payment.charge(0)) // 0원 충전 시도
                .isInstanceOf(APIException.class)
                .hasMessageContaining(ResponseCodeEnum.INSUFFICIENT_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("정상적인 use 시 잔액이 감소해야 한다")
    void test_use_should_decrease_balance() {
        // given
        Payment payment = Payment.create(1L, 1000);

        // when
        payment.use(300);

        // then
        assertThat(payment.getBalance()).isEqualTo(700);
    }

    @Test
    @DisplayName("정상적인 charge 시 잔액이 증가해야 한다")
    void test_charge_should_increase_balance() {
        // given
        Payment payment = Payment.create(1L, 1000);

        // when
        payment.charge(500);

        // then
        assertThat(payment.getBalance()).isEqualTo(1500);
    }
}
