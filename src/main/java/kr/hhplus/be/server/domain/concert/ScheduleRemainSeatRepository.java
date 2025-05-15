package kr.hhplus.be.server.domain.concert;

public interface ScheduleRemainSeatRepository {
    void decrSeat(long scheduleId);
    void incrSeat(long scheduleId);
    long getSeatRemain(long scheduleId);
}
