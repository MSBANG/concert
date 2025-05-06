package kr.hhplus.be.server.domain.queue;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseEntity;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Queue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long queueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concertId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Concert concert;
    // True : 예약 진행중, False : 대기중

    @Column(name = "is_in_progress", columnDefinition = "boolean default false")
    private boolean inProgress;

    // 대기열 진입한 상태로 변경되고, expireIn 안에 예약을 진행하지 않으면 토큰 무효화
    // 사용자가 계속 Queue 확인을 하지 않으면 무효화 => 스케줄러 필요
    private LocalDateTime expireIn;

    @Builder
    private Queue(long queueId, User user, Concert concert, boolean inProgress) {
        this.queueId = queueId;
        this.user = user;
        this.concert = concert;
        this.inProgress = inProgress;
    }

    public static Queue of(Long queueId, User user, Concert concert, boolean inProgress) {
        return Queue.builder()
                .queueId(queueId)
                .user(user)
                .concert(concert)
                .inProgress(inProgress)
                .build();
    }

    public static Queue create(User user, Concert concert, boolean inProgress) {
        return Queue.builder()
                .user(user)
                .concert(concert)
                .inProgress(inProgress)
                .build();
    }
    
    // Queue 상태 변경 (예약진행 중, )
    public void approachQueue() {
        this.inProgress = true;
        this.expireIn = LocalDateTime.now().plusMinutes(5L);
    }
}
