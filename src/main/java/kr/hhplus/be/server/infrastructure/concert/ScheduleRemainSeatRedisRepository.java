package kr.hhplus.be.server.infrastructure.concert;

import kr.hhplus.be.server.domain.concert.ScheduleRemainSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ScheduleRemainSeatRedisRepository implements ScheduleRemainSeatRepository {
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public void setRemainSeat(long scheduleId, Long seatRemain) {
        stringRedisTemplate.opsForValue().setIfAbsent(getRemainSeatKey(scheduleId), seatRemain.toString());
    }

    @Override
    public Long decrSeat(long scheduleId) {
        return stringRedisTemplate.opsForValue().decrement(this.getRemainSeatKey(scheduleId));
    }

    @Override
    public Long incrSeat(long scheduleId) {
        return stringRedisTemplate.opsForValue().increment(this.getRemainSeatKey(scheduleId));
    }

    @Override
    public long getSeatRemain(long scheduleId) {
        String seatRemain = stringRedisTemplate.opsForValue().get(this.getRemainSeatKey(scheduleId));
        if (seatRemain == null) {
            throw new IllegalArgumentException();
        }
        return Long.parseLong(seatRemain);
    }

    @Override
    public void setSoldOutSchedule(long concertId, String concertName, long scheduleId, Long timeForSoldOut) {
        stringRedisTemplate.opsForZSet().add(
                "dailySoldOutRank",
                getSoldOutScheduleKey(concertId, concertName, scheduleId),
                (double) timeForSoldOut
        );
    }

    @Override
    public Set<String> getTodaySoldOutScheduleSet() {
        return stringRedisTemplate.opsForZSet().range("dailySoldOutRank", 0, -1);
    }

    @Override
    public void removeTodaySoldOutRank() {
        stringRedisTemplate.delete("dailySoldOutRank");
    }

    private String getRemainSeatKey(long scheduleId) {
        return "scheduleId:%s:remainSeat".formatted(scheduleId);
    }

    private String getSoldOutScheduleKey(long concertId, String concertName, long scheduleId) {
        return "concertId:%s:concertName:%s:scheduleId:%s:soldOutTime".formatted(concertId, concertName, scheduleId);
    }
}
