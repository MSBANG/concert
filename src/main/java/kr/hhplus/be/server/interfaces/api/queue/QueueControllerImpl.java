package kr.hhplus.be.server.interfaces.api.queue;


import kr.hhplus.be.server.interfaces.api.token.dto.TokenDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueControllerImpl implements QueueController {
    @Override
    public TokenDTO CreateToken(
            @RequestHeader("X-queue-token") String queue_token
    ) {
        return TokenDTO.getDefault();
    }
}
