package kr.hhplus.be.server.domain.queue;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface QueueRepository {
    List<Queue> getQueuesByConcertId(long concertId);

    long getQueueWaitingNum(long queueId, long concertId);
    Queue getQueueByConcertIdAndUserId(long userId, long concertId);
    long save(Queue queue);
    void remove(Queue queue);
    Optional<Queue> getQueueById(long queueId);
}
