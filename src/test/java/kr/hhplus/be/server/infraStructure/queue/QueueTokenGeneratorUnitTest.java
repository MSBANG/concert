package kr.hhplus.be.server.infraStructure.queue;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.queue.Queue;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.queue.QueueTokenGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QueueTokenGeneratorUnitTest {
    @Autowired
    QueueTokenGenerator queueTokenGenerator;

    @Test
    void queueTokenGeneratorUnitTest() {
        User user = new User(1L);
        Concert concert = Concert.create("AI 콘서트");
        Queue queue = Queue.create(user, concert, true);
        String queueToken = queueTokenGenerator.encode(queue);

        Queue decodedQueue = queueTokenGenerator.decode(queueToken, Queue.class);

        Assertions.assertEquals(decodedQueue.getQueueId(), queue.getQueueId());
        Assertions.assertEquals(decodedQueue.getConcert().getName(), queue.getConcert().getName());
        Assertions.assertEquals(decodedQueue.getUser().getUserId(), queue.getUser().getUserId());
        Assertions.assertEquals(decodedQueue.getExpireIn(), queue.getExpireIn());
        Assertions.assertEquals(decodedQueue.isInProgress(), queue.isInProgress());
    }
}
