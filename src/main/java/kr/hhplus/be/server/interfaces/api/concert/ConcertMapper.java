package kr.hhplus.be.server.interfaces.api.concert;

import kr.hhplus.be.server.application.concert.command.ConcertCommand;
import kr.hhplus.be.server.interfaces.api.concert.dto.ConcertDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConcertMapper {
    ConcertMapper INSTANCE = Mappers.getMapper( ConcertMapper.class );

    ConcertCommand toCommand(ConcertDTO concertDTO);

    ConcertDTO toDTO(ConcertCommand concertCommand);
}
