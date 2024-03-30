package com.example.controller;

import com.example.entity.Student;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Swagger UI
//http://localhost:8080/swagger-ui/index.html#/student-controller/getStudentsList

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentService studentService;


    @GetMapping("/signIn")
    public ResponseEntity<String> getLoggedInCustomerDetailsHandler(Authentication auth){

//        System.out.println(auth); // this Authentication object having Principle object details

        Student student= studentService.getStudentByUsername(auth.getName());

        return new ResponseEntity<>(student.getName()+" Logged In Successfully", HttpStatus.ACCEPTED);


    }
    @PostMapping("/save")
    public ResponseEntity<Student> save(@RequestBody Student st){

        st.setPassword(passwordEncoder.encode(st.getPassword()));

        Student savedSt = studentService.registerStudent(st);

        return new ResponseEntity<>(savedSt, HttpStatus.CREATED);

    }

    @GetMapping("/getStudentsList")
    public ResponseEntity<List<Student>> getStudentsList(){
        List<Student> stList = studentService.getStudentList();

        return new ResponseEntity<>(stList, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Student> getEmployeeByUserNameHandler(@PathVariable String username){

        Student existingStudent = studentService.getStudentByUsername(username);

        return new ResponseEntity<>(existingStudent, HttpStatus.OK);

    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Student> deleteStudentById(@PathVariable Integer studentId){
        Student deletedStudent = studentService.deleteStudentById(studentId);

        return new ResponseEntity<>(deletedStudent, HttpStatus.OK);
    }
}
