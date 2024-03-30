package com.example.serviceImpl;

import com.example.entity.Student;
import com.example.repository.StudentDao;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentRepository;
    @Override
    public Student registerStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getStudentList() {
        List<Student> stList = studentRepository.findAll();
        if(stList.isEmpty())
        {
            throw new RuntimeException("No students found");
        }

        return stList;
    }

    @Override
    public Student getStudentByUsername(String username) {
        Student student = studentRepository.findByUsername(username);
        if (student == null) throw new RuntimeException("Invalid username");

        return student;
    }

    @Override
    public Student deleteStudentById(Integer studentId) {
        Optional<Student> existingStudentOpt = studentRepository.findById(studentId);
        if(existingStudentOpt.isEmpty()){
            throw new RuntimeException("Invalid user id");
        }

        studentRepository.deleteById(studentId);

        return existingStudentOpt.get();
    }
}
