package com.mayflowertech.chilla.test.repo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.mayflowertech.chilla.entities.College;
import com.mayflowertech.chilla.repositories.ICollegeRepository;

@SpringBootTest
@Transactional  // Rollback the transaction after the test to keep the DB clean
public class CollegeRepositoryTest {

    @Autowired
    private ICollegeRepository collegeRepository;

    @Test
    public void testAddCollege() {
        // Create a new College
        College college = new College();
        college.setName("Test College");

        // Save the college using the repository
        College savedCollege = collegeRepository.save(college);

        // Assertions
        assertThat(savedCollege).isNotNull();
        assertThat(savedCollege.getId()).isNotNull();
        assertThat(savedCollege.getName()).isEqualTo("Test College");
    }

    @Test
    public void testFindCollegeById() {
        // Create and save a new College
        College college = new College();
        college.setName("Test College");
        College savedCollege = collegeRepository.save(college);

        // Find the college by ID
        College foundCollege = collegeRepository.findById(savedCollege.getId()).orElse(null);

        // Assertions
        assertThat(foundCollege).isNotNull();
        assertThat(foundCollege.getId()).isEqualTo(savedCollege.getId());
        assertThat(foundCollege.getName()).isEqualTo("Test College");
    }

    @Test
    public void testUpdateCollege() {
        // Create and save a new College
        College college = new College();
        college.setName("Test College");
        College savedCollege = collegeRepository.save(college);

        // Update the college name
        savedCollege.setName("Updated College Name");
        College updatedCollege = collegeRepository.save(savedCollege);

        // Assertions
        assertThat(updatedCollege).isNotNull();
        assertThat(updatedCollege.getId()).isEqualTo(savedCollege.getId());
        assertThat(updatedCollege.getName()).isEqualTo("Updated College Name");
    }

    @Test
    public void testDeleteCollege() {
        // Create and save a new College
        College college = new College();
        college.setName("Test College");
        College savedCollege = collegeRepository.save(college);

        // Delete the college
        collegeRepository.delete(savedCollege);

        // Try to find the college by ID
        College foundCollege = collegeRepository.findById(savedCollege.getId()).orElse(null);

        // Assertions
        assertThat(foundCollege).isNull();  // College should no longer exist
    }
}
