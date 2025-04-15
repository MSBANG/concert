package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import lombok.Builder;
import lombok.Getter;


public class ConcertResult {
    @Getter
    public static class ConcertInfo {
        long concertId;
        String name;

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
}
