package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.application.concert.command.ConcertMapper;
import kr.hhplus.be.server.application.concert.command.ConcertSeatCommand;
import kr.hhplus.be.server.application.concert.command.ReserveSeatCommand;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.domain.ConcertSeat;
import kr.hhplus.be.server.domain.concert.domain.ReserveSeat;
import kr.hhplus.be.server.interfaces.api.common.APIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepo;

    @Mock
    private ConcertMapper concertMapper;

    @InjectMocks
    private ConcertService concertService;

    @Test
    void 예약된_좌석은_예약할_수_없다() {
        // given
        ReserveSeatCommand command = new ReserveSeatCommand(1L, 1L);
        when(concertRepo.getSeatIsAvail(command.seatId())).thenReturn(false);

        // when & then
        assertThrows(APIException.class, () -> concertService.reserveSeat(command));
        verify(concertRepo, never()).saveSeatReservation(any());
        verify(concertRepo, never()).updateSeatIsAvail(anyLong(), anyBoolean());
    }

    @Test
    void 좌석이_예약불가_상태로_변경되어야_한다() {
        // given
        ReserveSeatCommand command = new ReserveSeatCommand(2L, 2L);
        ReserveSeat reserveSeat = new ReserveSeat(command.seatId(), command.userId()); // 예시 도메인
        ConcertSeat concertSeat = new ConcertSeat(command.seatId(), 1L, true, 10000L); // 예시 응답

        // 좌석 예약 가능여부 확인
        when(concertRepo.getSeatIsAvail(command.seatId())).thenReturn(true);
        when(concertMapper.toDomain(command)).thenReturn(reserveSeat);


        when(concertRepo.getConcertSeatById(command.seatId())).thenReturn(concertSeat);

        // when
        ConcertSeatCommand result = concertService.reserveSeat(command);

        // then
        verify(concertRepo).updateSeatIsAvail(eq(command.seatId()), eq(false));
    }

    @Test
    void 예약_성공시_ConcertSeatCommand_객체가_정상적으로_반환된다() {
        // given
        ReserveSeatCommand command = new ReserveSeatCommand(3L, 3L);
        ReserveSeat reserveSeat = new ReserveSeat(command.seatId(), 3L);
        ConcertSeat concertSeat = new ConcertSeat(command.seatId(), 1L, true, 10000L);
        ConcertSeatCommand expectedCommand = new ConcertSeatCommand(command.seatId(), 1L, false, 10000L);

        when(concertRepo.getSeatIsAvail(command.seatId())).thenReturn(true);
        when(concertMapper.toDomain(command)).thenReturn(reserveSeat);
        when(concertRepo.getConcertSeatById(command.seatId())).thenReturn(concertSeat);
        when(concertMapper.toCommand(concertSeat)).thenReturn(expectedCommand);

        // when
        ConcertSeatCommand result = concertService.reserveSeat(command);

        // then
        assertEquals(expectedCommand, result);
        verify(concertRepo).saveSeatReservation(reserveSeat);
        verify(concertRepo).updateSeatIsAvail(command.seatId(), false);
        verify(concertMapper).toCommand(concertSeat);
    }
}
