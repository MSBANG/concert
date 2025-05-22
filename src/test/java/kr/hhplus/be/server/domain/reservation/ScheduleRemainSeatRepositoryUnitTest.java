package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.domain.concert.ScheduleRemainSeatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ScheduleRemainSeatRepositoryUnitTest {
    @Autowired
    ScheduleRemainSeatRepository scheduleRemainSeatRepository;

    @Test
    @DisplayName("저장된 순서에 상관없이 올바른 순서로 매진 날짜가 정렬됨")
    void testDailySoldOutScheduleRank() {
        List<Long> scheduleIdList = Arrays.asList(1L, 8L, 7L, 6L, 2L, 5L, 3L, 10L, 9L, 4L);
        Set<String> expectedScheduleRank = new HashSet<>();
        for (int i=0; i<10; i++) {
            Long timeForSoldOut = scheduleIdList.get(i) * 1000;
            scheduleRemainSeatRepository.setSoldOutSchedule(1L, "testConcert", scheduleIdList.get(i), timeForSoldOut);
            expectedScheduleRank.add("scheduleId:%s:soldOutTime".formatted(i+1));
        }
        Assertions.assertEquals(expectedScheduleRank, scheduleRemainSeatRepository.getTodaySoldOutScheduleSet());
    }
}
