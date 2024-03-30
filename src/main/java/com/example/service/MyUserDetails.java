package com.example.service;

import com.example.entity.Student;
import com.example.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private StudentDao studentRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student student = studentRepository.findByUsername(username);
        if(student==null){
            throw new UsernameNotFoundException("Student not exists by Username");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        // if we have only single authority per user( otherwise we will set all the authorities using loop)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + student.getRole()));

        return new User(username, student.getPassword(), authorities);
    }
}
