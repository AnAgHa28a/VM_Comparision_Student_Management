package com.example.demo.model;

import jakarta.persistence.*;



@Entity
@Table(name = "marks")
public class Marks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer internal1;

    @Column(nullable = false)
    private Integer internal2;

    @Column(nullable = false)
    private Integer endsem;

    private Integer total;

    private String grade;

    // ======= Getters =======
    public Long getId() {
        return id;
    }

    public Integer getInternal1() {
        return internal1;
    }

    public Integer getInternal2() {
        return internal2;
    }

    public Integer getEndsem() {
        return endsem;
    }

    public Integer getTotal() {
        return total;
    }

    public String getGrade() {
        return grade;
    }

    // ======= Setters =======
    public void setId(Long id) {
        this.id = id;
    }

    public void setInternal1(Integer internal1) {
        this.internal1 = internal1;
    }

    public void setInternal2(Integer internal2) {
        this.internal2 = internal2;
    }

    public void setEndsem(Integer endsem) {
        this.endsem = endsem;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
