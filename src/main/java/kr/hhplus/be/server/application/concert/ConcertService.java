package kr.hhplus.be.server.application.concert;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.domain.concert.ScheduleDTO;
import kr.hhplus.be.server.support.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// 제발 생각 많이 하지 말고 간단하게 하자
public class ConcertService {
    private final ConcertRepository concertRepo;

    @Transactional
    @Cacheable(value = "concertResult", cacheManager = "redisCacheManager")
    public ConcertResultList getAllConcerts() {
        List<Concert> concerts = concertRepo.getAllConcerts();

        return new ConcertResultList(concerts.stream()
                .map(ConcertResult::from)
                .toList());
    }

    // 콘서트 스케줄
    @Transactional
    @Cacheable(value = "scheduleResult", key = "#command", cacheManager = "redisCacheManager")
    public ScheduleResultList getConcertSchedules(ConcertCommand command) {
        // 내가 메서드 형식으로 Exception 을 정의해놔서 아래처럼 ::(더블 콜론) 을 사용해서 메서드를 사용한거고
        concertRepo.getConcertById(command.getConcertId())
                .orElseThrow(APIException::concertNotFound);

        // Exception 을 Class 형식으로 사용한다면 람다 형식으로 사용한다
        // .orElseThrow(() -> new APIException("pageNotFound"))
        List<ScheduleDTO> schedules = concertRepo.getAllConcertSchedules(command.getConcertId());

        return new ScheduleResultList(schedules.stream()
                .map(ScheduleResult::from)
                .toList());
    }

    // 콘서트 좌석
    public List<SeatResult> getConcertSeats(ScheduleCommand command) {
        concertRepo.getScheduleById(command.getScheduleId())
                .orElseThrow(APIException::scheduleNotFound);

        List<ConcertSeat> seats = concertRepo.getAllSeats(command.getScheduleId());
        return seats.stream()
                .map(SeatResult::from)
                .toList();
    }
}

