package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.interfaces.api.common.APIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
