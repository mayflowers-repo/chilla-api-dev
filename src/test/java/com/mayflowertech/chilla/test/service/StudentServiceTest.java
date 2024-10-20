package com.mayflowertech.chilla.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.mayflowertech.chilla.entities.College;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.enums.Gender;
import com.mayflowertech.chilla.repositories.ICollegeRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.services.impl.StudentService;

public class StudentServiceTest {

    @Mock
    private IStudentRepository studentRepository;

    @Mock
    private ICollegeRepository collegeRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private College college;

    @BeforeEach
    void setUp() {
        college = new College();
        college.setId(UUID.randomUUID());
        college.setName("Test College");
        college = collegeRepository.save(college);
        Student student = new Student();
        student.setAge(20);
        student.setCourse("Computer Science");
        student.setCompletionYear("2024");
        student.setGender(Gender.MALE);
        student.setCollege(college); 
//        
//        student.setEmail("student2@test.com");
//        student.setUsername("student2");
//        student.setPassword("guest");
        
    }
    


    void testCreateStudentWhenCollegeExists() {
        // Arrange
        when(collegeRepository.existsById(college.getId())).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student createdStudent = studentService.createStudent(student);

        // Assert
        assertNotNull(createdStudent);
        //assertEquals(student.getFirstName(), createdStudent.getFirstName());
        verify(collegeRepository, times(1)).existsById(college.getId());
        verify(collegeRepository, never()).save(any(College.class));
        verify(studentRepository, times(1)).save(student);
    }

    void testCreateStudentWhenCollegeDoesNotExist() {
        // Arrange
        when(collegeRepository.existsById(college.getId())).thenReturn(false);
        when(collegeRepository.save(any(College.class))).thenReturn(college);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student createdStudent = studentService.createStudent(student);

        // Assert
        assertNotNull(createdStudent);
        //assertEquals(student.getUsername(), createdStudent.getUsername());
        verify(collegeRepository, times(1)).existsById(college.getId());
        verify(collegeRepository, times(1)).save(college);
        verify(studentRepository, times(1)).save(student);
    }


    void testGetAllStudents() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertNotNull(students);
        assertEquals(1, students.size());
        //assertEquals(student.getUsername(), students.get(0).getUsername());
        verify(studentRepository, times(1)).findAll();
    }
}
