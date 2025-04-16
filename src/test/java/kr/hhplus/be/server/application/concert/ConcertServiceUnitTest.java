package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.interfaces.api.common.APIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ConcertServiceUnitTest {

	@Mock
	private ConcertRepository concertRepo;

	@InjectMocks
	private ConcertService concertService;

	@Test
	@DisplayName("concertId에 해당하는 Concert가 없을 때 예외 발생")
	void testConcertNotFoundException() {
		// given
		long invalidConcertId = 999L;
		ConcertCommand command = ConcertCommand.of(invalidConcertId);

		Mockito.when(concertRepo.getConcertById(invalidConcertId))
				.thenReturn(Optional.empty());

		// when & then
		Assertions.assertThrows(APIException.class, () -> {
			concertService.getConcertSchedules(command);
		});
		Mockito.verify(concertRepo).getConcertById(invalidConcertId);
	}

	@Test
	@DisplayName("concertId에 해당하는 ConcertSchedules가 정상적으로 변환되어 반환되는지 확인")
	void testGetConcertSchedulesSuccess() {
		// given
		long concertId = 1L;
		String concertName = "TestConcertName";
		Concert concert = Concert.create(concertId, concertName); // 테스트용 도메인 생성
		ReflectionTestUtils.setField(concert, "concertId", concertId);

		ConcertCommand concertCommand = ConcertCommand.of(concertId);

		ConcertSchedule schedule1 = ConcertSchedule.create(101L, concert, LocalDate.of(2025, 5, 1));
		ConcertSchedule schedule2 = ConcertSchedule.create(102L, concert, LocalDate.of(2025, 6, 1));

		List<ConcertSchedule> schedules = List.of(schedule1, schedule2);

		Mockito.when(concertRepo.getConcertById(concertId))
				.thenReturn(Optional.of(concert));
		Mockito.when(concertRepo.getAllConcertSchedules(concertId))
				.thenReturn(schedules);

		// when
		List<ScheduleResult> result = concertService.getConcertSchedules(concertCommand);

		// then
		Assertions.assertEquals(2, result.size());

		Assertions.assertEquals(101L, result.get(0).getScheduleId());
		Assertions.assertEquals(LocalDate.of(2025, 5, 1), result.get(0).getStartDate());

		Assertions.assertEquals(102L, result.get(1).getScheduleId());
		Assertions.assertEquals(LocalDate.of(2025, 6, 1), result.get(1).getStartDate());

		Mockito.verify(concertRepo).getConcertById(concertId);
		Mockito.verify(concertRepo).getAllConcertSchedules(concertId);
	}

	@Test
	@DisplayName("scheduleId에 해당하는 Schedule이 없을 때 예외 발생")
	void testScheduleNotFoundException() {
		// given
		long invalidScheduleId = 999L;
		ScheduleCommand command = ScheduleCommand.fromScheduleId(invalidScheduleId);

		Mockito.when(concertRepo.getScheduleById(invalidScheduleId))
				.thenReturn(Optional.empty());

		// when & then
		Assertions.assertThrows(APIException.class, () -> {
			concertService.getConcertSeats(command);
		});
		Mockito.verify(concertRepo).getScheduleById(invalidScheduleId);
	}

	@Test
	@DisplayName("scheduleId에 해당하는 ConcertSeats가 정상적으로 변환되어 반환되는지 확인")
	void testGetConcertSeatsSuccess() {
		// given
		long scheduleId = 1L;
		ScheduleCommand command = ScheduleCommand.fromScheduleId(scheduleId);

		ConcertSchedule concertSchedule = new ConcertSchedule(); // Assuming Schedule exists with valid data
		ReflectionTestUtils.setField(concertSchedule, "scheduleId", scheduleId);
		ConcertSeat seat1 = ConcertSeat.create(101L, concertSchedule, 100L, true, 1);
		ConcertSeat seat2 = ConcertSeat.create(102L, concertSchedule, 150L, false, 2);

		List<ConcertSeat> seats = List.of(seat1, seat2);

		// `concertRepo.getScheduleById`가 정상적으로 Schedule을 반환하도록 설정
		Mockito.when(concertRepo.getScheduleById(scheduleId))
				.thenReturn(Optional.of(concertSchedule));

		// `concertRepo.getAllSeats`가 ConcertSeat 리스트를 반환하도록 설정
		Mockito.when(concertRepo.getAllSeats(scheduleId))
				.thenReturn(seats);

		// when
		List<SeatResult> result = concertService.getConcertSeats(command);
		System.out.println(result.toString());

		// then
		Assertions.assertEquals(2, result.size());

		Assertions.assertEquals(101L, result.get(0).getSeatId());
		Assertions.assertEquals(100L, result.get(0).getPrice());
		Assertions.assertTrue(result.get(0).isAvail());
		Assertions.assertEquals(1, result.get(0).getSeatNum());

		Assertions.assertEquals(102L, result.get(1).getSeatId());
		Assertions.assertEquals(150L, result.get(1).getPrice());
		Assertions.assertFalse(result.get(1).isAvail());
		Assertions.assertEquals(2, result.get(1).getSeatNum());

		Mockito.verify(concertRepo).getScheduleById(scheduleId);
		Mockito.verify(concertRepo).getAllSeats(scheduleId);
	}
}
