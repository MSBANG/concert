package kr.hhplus.be.server.infrastructure.queue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.queue.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    // 같은 콘서트에, 아직 예약을 진행하지 않은 queue Count
    @Override
    public long getQueueWaitingCount(long queueId, long concertId) {
        return queueJPARepo.countByConcert_ConcertIdAndInProgressAndQueueIdLessThan(concertId, false, queueId);
    }

    @Override
    public long getQueueInProgressCount(long concertId) {
        return queueJPARepo.countByConcert_ConcertIdAndInProgress(concertId, true);
    }


    @Override
    public long save(Queue queue) {
        em.persist(queue);
        em.flush();
        return queue.getQueueId();
    }

    @Override
    public void remove(Queue queue) {
//        em.remove(queue);
        queueJPARepo.removeByQueueId(queue.getQueueId());
    }

    @Override
    public Optional<Queue> getQueueById(long queueId) {
        return queueJPARepo.findById(queueId);
    }
}
