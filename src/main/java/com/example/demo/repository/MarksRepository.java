package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Marks;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    
}
