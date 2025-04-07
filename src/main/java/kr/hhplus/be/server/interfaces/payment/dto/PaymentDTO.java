package kr.hhplus.be.server.interfaces.payment.dto;

public record PaymentDTO(
        long user_id,
        long amount
) {
    public static PaymentDTO getDefault(){ return new PaymentDTO(1L, 1_000_000L);};
}
