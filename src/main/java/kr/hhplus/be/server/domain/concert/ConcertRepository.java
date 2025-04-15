package kr.hhplus.be.server.domain.concert;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository {
    List<Concert> getAllConcerts();

}
