package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.MarksCalculationService;

import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("/api/marks")
public class MarksController {

    @Autowired
    private MarksCalculationService calculationService;
    
    @Autowired
    private MeterRegistry meterRegistry;

    @PostMapping("/insert-marks")
    public ResponseEntity<String> insertRandomMarks() {
        meterRegistry.gauge("student_app.insert_marks_api.call.start", 1); // Passing constant value when api call starts
        calculationService.insertRandomMarks();
        meterRegistry.gauge("student_app.insert_marks_api.call.end", 1); // Passing constant value when api call ends
        return ResponseEntity.ok("Random marks inserted from enrollments.");
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllMarks() {
        meterRegistry.gauge("student_app.delete_marks_api.call.start", 1); // Passing constant value when api call starts
        calculationService.deleteAllMarks();
        calculationService.deleteAllgpa();
        meterRegistry.gauge("student_app.delete_marks_api.call.end", 1); // Passing constant value when api call ends
        return ResponseEntity.ok("All marks and gpa deleted.");
    }

    @PostMapping("/calculate-marks")
    public ResponseEntity<String> calculateAllMarks() {
        long start = System.currentTimeMillis();
        meterRegistry.gauge("student_app.calculate_marks.call.start", 1); // Passing constant value when api call starts
        calculationService.calculateAndUpdateMarks();
        long end = System.currentTimeMillis();
        meterRegistry.gauge("student_app.calculate_marks.call.time_taken", (end - start)); // time taken for calculate marks
        meterRegistry.gauge("student_app.calculate_marks.call.end", 1); // Passing constant value when api call ends
        
        return ResponseEntity.ok("Marks updated successfully in " + (end - start) + " ms.");
    }

     @PostMapping("/calculate-sgpa")
    public ResponseEntity<String> calculateSGPA() {
        try {
            long start = System.currentTimeMillis();
            meterRegistry.gauge("student_app.calculate_gpa.call.start", 1); // Passing constant value when api call starts
            calculationService.calculateAndUpdateSGPA();
            long end = System.currentTimeMillis();
             meterRegistry.gauge("student_app.calculate_gpa.call.time_taken", (end - start)); // time taken for calculate marks
             meterRegistry.gauge("student_app.calculate_gpa.call.end", 1); // Passing constant value when api call ends
            return ResponseEntity.ok("SGPA calculation and update completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error during SGPA calculation: " + e.getMessage());
        }
    }
}
