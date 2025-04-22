package kr.hhplus.be.server.application.queue;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QueueCommand {
    private final User user;
    private final Concert concert;

    @Builder
    private QueueCommand(User user, Concert concert) {
        this.user = user;
        this.concert = concert;
    }

    public static QueueCommand of(User user, Concert concert) {
        return QueueCommand.builder()
                .user(user)
                .concert(concert)
                .build();
    }


}
