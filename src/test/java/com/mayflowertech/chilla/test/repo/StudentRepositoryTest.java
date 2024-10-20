package com.mayflowertech.chilla.test.repo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mayflowertech.chilla.entities.College;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.enums.Gender;
import com.mayflowertech.chilla.repositories.ICollegeRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;

@SpringBootTest
//@Transactional  // Rollback the transaction after the test to keep the DB clean
public class StudentRepositoryTest {

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private ICollegeRepository collegeRepository;
    
    @Test
    public void testAddStudent() {
        // Create a new College
        College college = new College();
        college.setName("Test College2");

        // Save the college first
        College savedCollege = collegeRepository.save(college);

        // Now create a new Student and assign the saved College
        Student student = new Student();
        student.setAge(20);
        student.setCourse("Computer Science");
        student.setCompletionYear("2024");
        student.setGender(Gender.MALE);
        student.setCollege(savedCollege);  // Set the saved College

        //mandatory
//        student.setEmail("student2@test.com");
//        student.setUsername("student2");
//        student.setPassword("guest");
//        
        
        // Save the student
        Student savedStudent = studentRepository.save(student);

        // Assertions
        assertThat(savedStudent).isNotNull();
        //assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getCollege()).isEqualTo(savedCollege);
    }
    
}
