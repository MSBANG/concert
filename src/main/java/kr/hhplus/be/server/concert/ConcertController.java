package kr.hhplus.be.server.concert;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.concert.dto.ConcertDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/concert")
@Tag(name="Concert", description="Concert Domain API")
public class ConcertController {
    @Operation(summary = "콘서트 목록 조회", description="콘서트 목록 조회")
    @GetMapping(value="", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ConcertDTO> ConcertList() {
        return Arrays.asList(
                ConcertDTO.getDefault(),
                ConcertDTO.getDefault()
        );
    }
}
