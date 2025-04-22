package kr.hhplus.be.server.infrastructure.queue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryAdaptor implements QueueRepository {
    @PersistenceContext
    private final EntityManager em;

    private final QueueJPARepository queueJPARepo;

    @Override
    public List<Queue> getQueuesByConcertId(long concertId) {
        return queueJPARepo.findAllByConcert_concertId(concertId);
    }

    @Override
    public long getQueueWaitingNum(long queueId, long concertId) {
        return queueJPARepo.countByConcert_concertIdAndQueueIdLessThan(concertId, queueId);
    }

    @Override
    public long getOrSave(Queue queue) {
        Queue queueInDB = this.getQueueByConcertIdAndUserId(
                queue.getUser().getUserId(),
                queue.getConcert().getConcertId()
        );
        if (queueInDB != null) {
            return queue.getQueueId();
        }
        em.persist(queue);
        em.flush();
        return queue.getQueueId();
    }

    @Override
    public Queue getQueueByConcertIdAndUserId(long userId, long concertId) {
        return queueJPARepo.findByUser_userIdAndConcert_concertId(userId, concertId);
    }
}
