package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student, Integer> {

    public Student findByUsername(String username);

}
