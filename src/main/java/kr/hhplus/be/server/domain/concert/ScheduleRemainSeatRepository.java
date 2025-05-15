package kr.hhplus.be.server.domain.concert;

import java.util.Set;

public interface ScheduleRemainSeatRepository {
    void setRemainSeat(long scheduleId, Long seatRemain);
    Long decrSeat(long scheduleId);
    Long incrSeat(long scheduleId);
    long getSeatRemain(long scheduleId);
    void setSoldOutSchedule(long concertId, String concertName, long scheduleId, Long timeForSoldOut);
    Set<String> getTodaySoldOutScheduleSet();
    void removeTodaySoldOutRank();
}
