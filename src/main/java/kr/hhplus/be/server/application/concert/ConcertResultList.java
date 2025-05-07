package kr.hhplus.be.server.application.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResultList {
    List<ConcertResult> concertResults;
}
