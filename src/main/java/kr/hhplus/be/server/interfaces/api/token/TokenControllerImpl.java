package kr.hhplus.be.server.interfaces.api.token;


import kr.hhplus.be.server.interfaces.api.token.dto.TokenDTO;
import kr.hhplus.be.server.interfaces.api.token.dto.TokenDomainRequestBodyDTO.TokenCreateRequestBodyDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenControllerImpl implements TokenController {
    @Override
    public TokenDTO CreateToken(
            @RequestBody TokenCreateRequestBodyDTO request
    ) {
        return TokenDTO.getDefault();
    }
}
