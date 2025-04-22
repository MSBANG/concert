package kr.hhplus.be.server.domain.queue;


import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    List<Queue> getQueuesByConcertId(long concertId);

    long getQueueWaitingCount(long queueId, long concertId);

    long getQueueInProgressCount(long concertId);

    long save(Queue queue);

    void remove(Queue queue);

    Optional<Queue> getQueueById(long queueId);
}
