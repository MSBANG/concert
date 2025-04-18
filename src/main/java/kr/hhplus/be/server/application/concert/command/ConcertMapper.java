package kr.hhplus.be.server.application.concert.command;

import kr.hhplus.be.server.domain.concert.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConcertMapper {
    ConcertMapper INSTANCE = Mappers.getMapper( ConcertMapper.class );

    // Concert
    ConcertCommand toCommand(Concert concert);
    Concert toDomain(ConcertCommand command);
    List<ConcertCommand> concertListToCommandList(List<Concert> concertList);
    List<Concert> concertCommandListToDomainList(List<ConcertCommand> commandList);

    // ConcertDate
    ConcertDateCommand toCommand(ConcertDate concertDate);
    ConcertDate toDomain(ConcertDateCommand command);

    // ConcertDateWithSeat
    ConcertDateWithSeatCommand toCommand(ConcertDateWithSeat concertDateWithSeat);
    ConcertDateWithSeat toDomain(ConcertDateWithSeatCommand command);

    // ConcertSeat
    ConcertSeatCommand toCommand(ConcertSeat concertSeat);
    ConcertSeat toDomain(ConcertSeatCommand command);

    // ConcertWithDate
    ConcertWithDateCommand toCommand(ConcertWithDate concertWithDate);
    ConcertWithDate toDomain(ConcertWithDateCommand command);

    // ReserveSeat
    ReserveSeatCommand toCommand(ReserveSeat reserveSeat);
    ReserveSeat toDomain(ReserveSeatCommand command);

    // Reservation
    ReservationCommand toCommand(Reservation reservation);
    Reservation toDomain(ReservationCommand command);
    List<ReservationCommand> reservationListToCommandList(List<Reservation> reservationList);
    List<Reservation> reservationCommandListtoDomainList(List<ReservationCommand> commandList);
}
