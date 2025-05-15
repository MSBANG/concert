package kr.hhplus.be.server.infrastructure.concert;

import kr.hhplus.be.server.domain.concert.ScheduleRemainSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleRemainSeatRedisRepository implements ScheduleRemainSeatRepository {
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public void decrSeat(long scheduleId) {
        stringRedisTemplate.opsForValue().decrement(this.getRemainSeatKey(scheduleId));
    }

    @Override
    public void incrSeat(long scheduleId) {
        stringRedisTemplate.opsForValue().increment(this.getRemainSeatKey(scheduleId));
    }

    @Override
    public long getSeatRemain(long scheduleId) {
        String seatRemain = stringRedisTemplate.opsForValue().get(this.getRemainSeatKey(scheduleId));
        if (seatRemain == null) {
            throw new IllegalArgumentException();
        }
        return Long.parseLong(seatRemain);
    }

    private String getRemainSeatKey(long scheduleId) {
        return "schedule:%s:remainSeat".formatted(scheduleId);
    }
}
