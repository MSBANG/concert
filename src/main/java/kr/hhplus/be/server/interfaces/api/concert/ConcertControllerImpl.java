package kr.hhplus.be.server.interfaces.api.concert;


import kr.hhplus.be.server.application.concert.ConcertService;
import kr.hhplus.be.server.interfaces.api.common.APIResponse;
import kr.hhplus.be.server.interfaces.api.common.ResponseItem;
import kr.hhplus.be.server.interfaces.api.concert.dto.ConcertDTO;
import kr.hhplus.be.server.interfaces.api.concert.dto.ConcertDateWithSeatDTO;
import kr.hhplus.be.server.interfaces.api.concert.dto.ConcertSeatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/concert")
public class ConcertControllerImpl implements ConcertController {
    private final ConcertService concertService;
    private final ConcertMapper concertMapper;


    @Autowired
    public ConcertControllerImpl(ConcertService concertService, ConcertMapper concertMapper) {
        this.concertService = concertService;
        this.concertMapper = concertMapper;
    }

    @Override
    public APIResponse concertList() {
        List<ConcertDTO> data = Arrays.asList(
                ConcertDTO.getDefault(),
                ConcertDTO.getDefault()
        );
        ResponseItem<List<ConcertDTO>> responseItem = ResponseItem.of(data);
        return APIResponse.ok(responseItem);
    }

    @Override
    public ConcertDateWithSeatDTO concertSeat(
            @RequestHeader("X-queue-token") String queue_token,
            @RequestParam("date_id") long dateId
    ) {
        return ConcertDateWithSeatDTO.getDefault();
    }

    @Override
    public ConcertSeatDTO reserveSeat(
            @RequestHeader("X-queue-token") String queue_token,
            @PathVariable("seat_id") long seatId
    ) {
        return ConcertSeatDTO.getDefault();
    }
}
