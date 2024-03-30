package com.example.service;

import com.example.entity.Student;

import java.util.List;

public interface StudentService {

    public Student registerStudent(Student student);

    public List<Student> getStudentList();

    public Student getStudentByUsername(String username);

    public Student deleteStudentById(Integer studentId);
}
