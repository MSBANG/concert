package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// 제발 생각 많이 하지 말고 간단하게 하자
public class ConcertService {
    private final ConcertRepository concertRepo;

    // 콘서트 목록
    // 그냥 Result 에서 Product LIST 받고 그냥 넘겨줘도 사실 큰 문제는 없다 (필드가 다른게 없기 때문에)
    // 하지만 이렇게 하는 이유는 "객체지향" 적으로 설계하기 위해서다
    public List<ConcertResult.ConcertInfo> getAllConcerts() {
        List<Concert> concerts = concertRepo.getAllConcerts();
        return concerts.stream()
                .map(ConcertResult.ConcertInfo::from)
                .toList();
    }

    // 콘서트 스케줄

    // 콘서트 좌석
    // 콘서트 예약
}

