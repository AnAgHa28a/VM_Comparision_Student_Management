package com.example.demo.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Timer;

import java.lang.management.ManagementFactory;
import java.util.List;

import com.example.demo.model.Marks;
import com.example.demo.repository.MarksRepository;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class MarksCalculationService {

    @Autowired
private JdbcTemplate jdbcTemplate;

@Autowired
private MarksRepository marksRepository;

@Autowired
private MeterRegistry meterRegistry;

@PersistenceContext
private EntityManager entityManager;

private static final int BATCH_SIZE = 100;

public int calculateTotal(int i1, int i2, int endsem) {
    double computed = (0.5 * i1) + (0.5 * i2) + (0.6 * endsem);
    return (int) Math.ceil(computed);
}

public String calculateGrade(int total) {
    if (total >= 90) return "S";
    else if (total >= 75) return "A";
    else if (total >= 60) return "B";
    else if (total >= 50) return "C";
    else if (total >= 40) return "D";
    else return "F";
}

@Transactional
public void calculateAndUpdateMarks() {
    meterRegistry.counter("student_app.calculatemarks.calls").increment();

    Timer.Sample sample = Timer.start(meterRegistry);

    List<Marks> allMarks = marksRepository.findAll();
    meterRegistry.gauge("student_app.calculatemarks.batchsize", allMarks.size());

    Runtime runtime = Runtime.getRuntime();
    long usedMemoryStart = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    meterRegistry.gauge("student_app.memory.used_mb.start", usedMemoryStart);

    double cpuLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
    meterRegistry.gauge("student_app.cpu.loadaverage", cpuLoad);

    for (int i = 0; i < allMarks.size(); i++) {
        Marks mark = allMarks.get(i);

        int total = calculateTotal(mark.getInternal1(), mark.getInternal2(), mark.getEndsem());
        String grade = calculateGrade(total);

        if (mark.getTotal() != null && mark.getTotal() == total &&
            mark.getGrade() != null && mark.getGrade().equals(grade)) {
            continue;
        }
        System.out.println("Processing mark id=" + mark.getId());

        mark.setTotal(total);
        mark.setGrade(grade);
        entityManager.merge(mark);

        if (i % BATCH_SIZE == 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }

    entityManager.flush();
    entityManager.clear();

    long usedMemoryEnd = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    meterRegistry.gauge("student_app.memory.used_mb.end", usedMemoryEnd);

    sample.stop(meterRegistry.timer("student_app.calculatemarks.duration"));
}

@Transactional
public void insertRandomMarks() {
    String sql = """
        INSERT INTO marks (enrollment_id, internal1, internal2, endsem, total, grade)
        SELECT 
            e.id,
            FLOOR(10 + (RAND() * 31)),
            FLOOR(10 + (RAND() * 31)),
            FLOOR(60 + (RAND() * 31)),
            0,                                 
            NULL                                
        FROM enrollments e
    """;

    jdbcTemplate.execute(sql);
    meterRegistry.counter("student_app.markrecords.inserted").increment();
}

public void deleteAllMarks() {
    jdbcTemplate.execute("DELETE FROM marks");
    meterRegistry.counter("student_app.markrecords.deleted").increment();
}

public void deleteAllgpa() {
    jdbcTemplate.execute("DELETE FROM gpa");
    meterRegistry.counter("student_app.gparecords.deleted").increment();
}

public void calculateAndUpdateSGPA() {
    meterRegistry.counter("student_app.sgpa.calculations").increment();

    String sql = """
        REPLACE INTO gpa (student_id, semester, sgpa)
        SELECT 
            s.id AS student_id,
            e.semester,
            ROUND(SUM(
                CASE m.grade
                    WHEN 'S' THEN 10
                    WHEN 'A' THEN 9
                    WHEN 'B' THEN 8
                    WHEN 'C' THEN 7
                    WHEN 'D' THEN 6
                    WHEN 'F' THEN 0
                    ELSE 0
                END * c.credits
            ) / SUM(c.credits), 2) AS sgpa
        FROM student s
        JOIN enrollments e ON s.id = e.student_id
        JOIN course c ON e.course_id = c.id
        JOIN marks m ON m.enrollment_id = e.id
        GROUP BY s.id, e.semester;
    """;

    Timer.Sample sample = Timer.start(meterRegistry);
    jdbcTemplate.update(sql);
    sample.stop(meterRegistry.timer("student_app.db.sgpa.write_latency"));
}


}
