package bg.softuni.demo.scheduling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "schedules.fixed-rate=50")
class ScheduledTasksTests {

    @Autowired
    ScheduledTasks scheduledTasks;

    @Test
    void fixedRateTask_runsRepeatedly() throws InterruptedException {
        long deadline = System.currentTimeMillis() + 5000;
        while (scheduledTasks.getFixedRateRuns() < 3 && System.currentTimeMillis() < deadline) {
            Thread.sleep(20);
        }
        assertThat(scheduledTasks.getFixedRateRuns()).isGreaterThanOrEqualTo(3);
    }
}
