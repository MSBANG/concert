package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.queue.QueueTokenGenerator;
import kr.hhplus.be.server.interfaces.api.common.ResponseCodeEnum;
import kr.hhplus.be.server.support.APIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepo;

    @Mock
    private ConcertRepository concertRepo;

    @Mock
    private QueueTokenGenerator queueTokenGenerator;

    @InjectMocks
    private QueueService queueService;

    private final long concertId = 1L;
    private final long userId = 100L;
    private final Concert concert = Concert.create("테스트 콘서트");
    private final User user = new User(userId);

    @Test
    @DisplayName("콘서트가 존재하지 않으면 예외 발생")
    void testConcertNotFound() {
        QueueCommand command = QueueCommand.of(user, concert);
        long concertId = concertRepo.saveConcert(concert);

        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.empty());

        APIException apiException = Assertions.assertThrows(APIException.class, () -> {
            queueService.getQueueToken(command);
        });
        Assertions.assertEquals(ResponseCodeEnum.CONCERT_NOT_FOUND.getMessage(), apiException.getMessage());
        Mockito.verify(concertRepo).getConcertById(concertId);
    }

    @Test
    @DisplayName("모든 좌석이 예약되었으면 예외 발생")
    void testAllSeatReserved() {
        QueueCommand command = QueueCommand.of(user, concert);
        long concertId = concertRepo.saveConcert(concert);

        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.of(concert));
        Mockito.when(concertRepo.getConcertIsAvailByConcertId(concertId)).thenReturn(false);

        APIException apiException = Assertions.assertThrows(APIException.class, () -> {
            queueService.getQueueToken(command);
        });

        Assertions.assertEquals(ResponseCodeEnum.ALL_SEATS_RESERVED.getMessage(), apiException.getMessage());
        Mockito.verify(concertRepo).getConcertIsAvailByConcertId(concertId);
    }

    @Test
    @DisplayName("대기 인원이 MAX_QUEUEING_NUM 미만이면 즉시 진입 처리 및 expiresIn 설정")
    void testQueueImmediateEntry() {
        QueueCommand command = QueueCommand.of(user, concert);
        Queue queue = Queue.create(user, concert, false);
        long queueId = 42L;
        long concertId = concertRepo.saveConcert(concert);
        concertRepo.saveConcert(concert);
        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.of(concert));
        Mockito.when(concertRepo.getConcertIsAvailByConcertId(concertId)).thenReturn(true);
        Mockito.when(queueRepo.save(Mockito.any(Queue.class))).thenReturn(queueId);
        Mockito.when(queueRepo.getQueueWaitingCount(queueId, concertId)).thenReturn(5L);
        Mockito.when(queueTokenGenerator.encode(Mockito.any(Queue.class))).thenReturn("dummyToken");

        QueueResult result = queueService.getQueueToken(command);

        Assertions.assertTrue(result.isEntryPossible());
        Assertions.assertNotNull(result.getExpiresIn());
        Assertions.assertEquals("dummyToken", result.getQueueToken());

        Mockito.verify(queueRepo).getQueueWaitingCount(queueId, concertId);
    }

    @Test
    @DisplayName("대기 인원이 MAX_QUEUEING_NUM 이상이면 대기 상태 유지")
    void testQueueWaiting() {
        QueueCommand command = QueueCommand.of(user, concert);
        Queue queue = Queue.create(user, concert, false);
        long concertId = concertRepo.saveConcert(concert);
        long queueId = 99L;
        concertRepo.saveConcert(concert);
        Mockito.when(concertRepo.getConcertById(concertId)).thenReturn(Optional.of(concert));
        Mockito.when(concertRepo.getConcertIsAvailByConcertId(concertId)).thenReturn(true);
        Mockito.when(queueRepo.save(Mockito.any(Queue.class))).thenReturn(queueId);
        Mockito.when(queueRepo.getQueueWaitingCount(queueId, concertId)).thenReturn(10L); // 초과
        Mockito.when(queueTokenGenerator.encode(Mockito.any(Queue.class))).thenReturn("waitingToken");

        QueueResult result = queueService.getQueueToken(command);

        Assertions.assertFalse(result.isEntryPossible());
        Assertions.assertNull(result.getExpiresIn());
        Assertions.assertEquals(10L, result.getWaitingNum());

        Mockito.verify(queueRepo).getQueueWaitingCount(queueId, concertId);
        Mockito.verify(queueTokenGenerator).encode(Mockito.any(Queue.class));
    }
}
