package kr.hhplus.be.server.support.distributedLock;


import kr.hhplus.be.server.application.payment.PaymentCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedisDistributedLockIntegrationTest {
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("concert-redis-1")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private RedisDistributedLock redisDistributionLock;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void acquire_createsLockKeyInRedis() throws InterruptedException {
        PaymentCommand.Pay pay = PaymentCommand.Pay.of(1L, 100L);
        String expectedKey = "Lock:paymentUserId:1";

        redisDistributionLock.acquire(pay);

        Assertions.assertTrue(redisTemplate.hasKey(expectedKey));
    }
}
