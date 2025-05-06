package kr.hhplus.be.server.application.queue;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import kr.hhplus.be.server.infrastructure.queue.QueueTokenGenerator;
import kr.hhplus.be.server.support.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final QueueRepository queueRepo;
    private final ConcertRepository concertRepo;
    private final QueueTokenGenerator queueTokenGenerator;

    // 콘서트 한개에 대한 최대 동시 진입 가능 인원
    private final long MAX_QUEUEING_NUM = 10;

    @Transactional
    public QueueResult getQueueToken(QueueCommand command) {
        long concertId = command.getConcert().getConcertId();
        concertRepo.getConcertById(concertId).orElseThrow(
                APIException::concertNotFound
        );
        if (!(concertRepo.getConcertIsAvailByConcertId(concertId))) {
            throw APIException.allSeatReserved();
        }

        Queue queue = Queue.create(command.getUser(), command.getConcert(), false);
        long queueId = queueRepo.save(queue);
        long queueWaitingNum = queueRepo.getQueueWaitingCount(queueId, concertId);

        if (queueWaitingNum == 0 && queueRepo.getQueueInProgressCount(concertId) < MAX_QUEUEING_NUM) {
            queue.approachQueue();
        }
        String queueToken = queueTokenGenerator.encode(queue);
        return QueueResult.of(queueToken, queue.getExpireIn(), queueWaitingNum, queue.isInProgress());
    }

    public QueueResult getQueueStatus(String queueToken) {
        Queue queue = queueTokenGenerator.decode(queueToken, Queue.class);
        String newToken = null;
        long queueWaitingNum = queueRepo.getQueueWaitingCount(
                queue.getQueueId(),
                queue.getConcert().getConcertId()
        );
        if (queueWaitingNum == 0 && queueRepo.getQueueInProgressCount(queue.getConcert().getConcertId()) < MAX_QUEUEING_NUM) {
            queue.approachQueue();
            newToken = queueTokenGenerator.encode(queue);
        }
        return QueueResult.of(newToken, queue.getExpireIn(), queueWaitingNum, queue.isInProgress());
    }
}
