package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertSeat;
import kr.hhplus.be.server.interfaces.api.common.APIException;
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
    public List<ConcertResult> getAllConcerts() {
        List<Concert> concerts = concertRepo.getAllConcerts();
        return concerts.stream()
                .map(ConcertResult::from)
                .toList();
    }

    // 콘서트 스케줄
    public List<ScheduleResult> getConcertSchedules(ConcertCommand command) {
        // 내가 메서드 형식으로 Exception 을 정의해놔서 아래처럼 ::(더블 콜론) 을 사용해서 메서드를 사용한거고
        concertRepo.getConcertById(command.getConcertId())
                .orElseThrow(APIException::concertNotFound);

        // Exception 을 Class 형식으로 사용한다면 람다 형식으로 사용한다
        // .orElseThrow(() -> new APIException("pageNotFound"))

        List<ConcertSchedule> schedules = concertRepo.getAllConcertSchedules(command.getConcertId());

        return schedules.stream()
                .map(ScheduleResult::from)
                .toList();
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

