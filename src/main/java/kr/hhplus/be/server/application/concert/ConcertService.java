package kr.hhplus.be.server.application.concert;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.concert.command.*;
import kr.hhplus.be.server.domain.concert.domain.*;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.interfaces.api.common.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {
    ConcertRepository concertRepo;
    ConcertMapper concertMapper;

    @Autowired
    public ConcertService(ConcertRepository concertRepo, ConcertMapper concertMapper) {
        this.concertRepo = concertRepo;
        this.concertMapper = concertMapper;
    }

    // 콘서트 전체 목록 조회
    public List<ConcertCommand> getAllConcert(){
        List<Concert> concertList = concertRepo.getAllConcert();
        return concertMapper.concertListToCommandList(concertList);
    }

    // 콘서트 일정(dates) 조회
    public ConcertWithDateCommand getAllDates(long concertId) {
        ConcertWithDate concertWithDate = concertRepo.getConcertWithDate(concertId);
        return concertMapper.toCommand(concertWithDate);
    }

    // 콘서트 일정에 따른 seat 조회
    public ConcertDateWithSeatCommand getAllSeats(long dateId) {
        ConcertDateWithSeat concertWithDate = concertRepo.getConcertDateWithSeat(dateId);
        return concertMapper.toCommand(concertWithDate);
    }

    // 예약 조회
    public List<ReservationCommand> getAllReservations(long userId){
        List<Reservation> reservationList = concertRepo.getAllReservations(userId);
        return concertMapper.reservationListToCommandList(reservationList);
    }

    public ReservationCommand getReservationById(long reservationId){
        Reservation reservation = concertRepo.getReservationById(reservationId);
        return concertMapper.toCommand(reservation);
    }

    public ConcertSeatCommand getConcertSeatById(long seatId) {
        ConcertSeat concertSeat = concertRepo.getConcertSeatById(seatId);
        return concertMapper.toCommand(concertSeat);
    }
    
    // 좌석 예약
    @Transactional
    public ConcertSeatCommand reserveSeat(ReserveSeatCommand reserveSeatCommand) {
        // 좌석 예약 가능여부 확인
        if (!(concertRepo.getSeatIsAvail(reserveSeatCommand.seatId()))){
            throw APIException.seatAlreadyReserved();
        }
        // 콘서트 좌석 예약 정보 저장
        ReserveSeat reserveSeat  = concertMapper.toDomain(reserveSeatCommand);
        concertRepo.saveSeatReservation(reserveSeat);

        // 좌석 상태 변경: 예약 불가
        ConcertSeat concertSeat = concertRepo.getConcertSeatById(reserveSeatCommand.seatId());
        concertSeat.reserve();
        concertRepo.updateSeatIsAvail(concertSeat.getSeatId(), concertSeat.getIsAvail());

        return concertMapper.toCommand(concertSeat);
    }
}
