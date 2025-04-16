package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


public class ConcertResult {
    @Getter
    public static class ConcertInfo {
        private final long concertId;
        private final String name;

        @Builder
        private ConcertInfo(long concertId, String name) {
            this.concertId = concertId;
            this.name = name;
        }

        public static ConcertInfo from(Concert concert) {

            return ConcertResult.ConcertInfo.builder()
                    .concertId(concert.getConcertId())
                    .name(concert.getName())
                    .build();
        }
    }

    @Getter
    public static class ScheduleInfo {
        private final long scheduleId;
        private final Concert concert;
        private final LocalDate startDate;

        @Builder
        private ScheduleInfo(long scheduleId, Concert concert, LocalDate startDate) {
            this.scheduleId = scheduleId;
            this.concert = concert;
            this.startDate = startDate;
        }

        public static ScheduleInfo from(ConcertSchedule schedule) {
            return ScheduleInfo.builder()
                    .scheduleId(schedule.getScheduleId())
                    .concert(schedule.getConcert())
                    .startDate(schedule.getStartDate())
                    .build();
        }

    }
}
