package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.support.APIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class ConcertDomainUnitTest {
    @Test
    @DisplayName("예약 가능한 좌석은 정상적으로 reserve 호출됨")
    void reserveSuccessTest() {
        // given
        Concert concert = Concert.create("TEST CONCERT");
        ConcertSeat seat = ConcertSeat.create(null, concert, 1000L, true, 50);
        ReflectionTestUtils.setField(seat, "seatId", 1L);

        // when
        seat.reserve();

        // then
        assertThat(seat.isAvail()).isFalse(); // 예약 후 isAvail 이 false 되어야 함
    }

    @Test
    @DisplayName("이미 예약된 좌석은 reserve 호출 시 APIException 발생")
    void reserveFailWhenAlreadyReservedTest() {
        // given
        Concert concert = Concert.create("TEST CONCERT");
        ConcertSeat seat = ConcertSeat.create(null, concert, 10000L, false, 50);
        ReflectionTestUtils.setField(seat, "seatId", 2L);

        // when & then
        assertThatThrownBy(seat::reserve)
                .isInstanceOf(APIException.class); // 예외 타입만 확인
    }
}
