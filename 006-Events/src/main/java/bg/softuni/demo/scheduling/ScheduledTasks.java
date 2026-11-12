package bg.softuni.demo.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    private final AtomicInteger fixedRateRuns = new AtomicInteger();

    // <second> <minute> <hour> <day-of-month> <month> <day-of-week>: every hour at minute 5
    @Scheduled(cron = "0 5 * * * ?")
    public void showTimeWithCron() {
        LOGGER.info("Cron: {}", LocalDateTime.now());
    }

    // starts every N millis, without waiting for the previous run to finish
    @Scheduled(fixedRateString = "${schedules.fixed-rate:5000}")
    public void showTimeWithFixedRate() {
        fixedRateRuns.incrementAndGet();
        LOGGER.info("Fixed rate: {}", LocalDateTime.now());
    }

    // waits N millis after the previous run finished; the first run is after the initial delay
    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void showTimeWithFixedDelay() {
        LOGGER.info("Fixed delay: {}", LocalDateTime.now());
    }

    public int getFixedRateRuns() {
        return fixedRateRuns.get();
    }
}
