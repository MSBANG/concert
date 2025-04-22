package kr.hhplus.be.server.application.queue;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class QueueResult {

    private final long waitingNum;
    private final boolean entryPossible;
    private final LocalDateTime expiresIn;

    private final String queueToken;

    @Builder
    private QueueResult(String queueToken, long waitingNum, boolean entryPossible, LocalDateTime expiresIn) {
        this.queueToken = queueToken;
        this.waitingNum = waitingNum;
        this.entryPossible = entryPossible;
        this.expiresIn = expiresIn;

    }

    public static QueueResult of(
            @Nullable String queueToken,
            @Nullable LocalDateTime expiresIn,
            @NonNull Long waitingNum,
            @NonNull Boolean entryPossible
    ) {
        return QueueResult.builder()
                .queueToken(queueToken)
                .waitingNum(waitingNum)
                .expiresIn(expiresIn)
                .entryPossible(entryPossible)
                .build();
    }

}
