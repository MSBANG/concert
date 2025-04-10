package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.concert.command.ConcertMapper;
import kr.hhplus.be.server.domain.payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    ConcertMapper INSTANCE = Mappers.getMapper( ConcertMapper.class );

    PaymentCommand toCommand(Payment payment);
    Payment toDomain(PaymentCommand paymentCommand);
}
