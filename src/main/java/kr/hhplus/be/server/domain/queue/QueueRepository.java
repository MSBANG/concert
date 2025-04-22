package kr.hhplus.be.server.domain.queue;


import java.util.List;

public interface QueueRepository {
    List<Queue> getQueuesByConcertId(long concertId);

    long getQueueWaitingNum(long queueId, long concertId);
    long getOrSave(Queue queue);
    Queue getQueueByConcertIdAndUserId(long userId, long concertId);
}
