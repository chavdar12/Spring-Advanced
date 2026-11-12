package bg.softuni.demo.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    private final AtomicInteger loads = new AtomicInteger();

    @Cacheable("students")
    public List<Student> getAllStudents() {
        LOGGER.info("Loading students the slow way...");
        loads.incrementAndGet();
        return List.of(new Student("Pesho", 23), new Student("Anna", 21));
    }

    @CacheEvict(cacheNames = "students", allEntries = true)
    public void refresh() {
        LOGGER.info("Students cache cleared.");
    }

    public int getLoads() {
        return loads.get();
    }
}
