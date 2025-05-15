package kr.hhplus.be.server.interfaces.api.concert;

import kr.hhplus.be.server.application.concert.ConcertCommand;
import kr.hhplus.be.server.application.concert.ConcertService;
import kr.hhplus.be.server.interfaces.api.common.APIResponse;
import kr.hhplus.be.server.interfaces.api.common.ResponseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/concert")
public class ConcertController implements ConcertControllerDocs{
    private final ConcertService concertService;

    @Autowired
    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @Override
    public APIResponse concertList() {
        List<ConcertResponse> concertResponses = concertService.getAllConcerts().getConcertResults().stream()
                .map(ConcertResponse::from)
                .toList();
        return APIResponse.ok(ResponseItem.of(concertResponses));
    }

    @Override
    public APIResponse concertSchedules(long concertId) {
        List<ScheduleResponse> scheduleResponses = concertService.getConcertSchedules(ConcertCommand.of(concertId)).getScheduleList().stream()
                .map(ScheduleResponse::from)
                .toList();
        return APIResponse.ok(ResponseItem.of(scheduleResponses));
    }
}
