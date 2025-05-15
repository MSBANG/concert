package kr.hhplus.be.server.infrastructure.queue;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.hhplus.be.server.domain.queue.Queue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface QueueJPARepository extends JpaRepository<Queue, Long> {
    Queue findByUser_userIdAndConcert_concertId(long userId, long concertId);

    List<Queue> findAllByConcert_concertId(long concertId);

    long findByConcert_concertId(long concertId);
    long countByConcert_ConcertIdAndInProgressAndQueueIdLessThan(long concertId, boolean isInProgress, long queueId);
    long countByConcert_ConcertIdAndInProgress(long concertId, boolean isInProgress);
    void removeByQueueId(long queueId);
}
