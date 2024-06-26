package com.futuresubject.admin.repository;

import com.futuresubject.common.entity.Entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    @Query("SELECT u.facultyName FROM Faculty AS u")
    public List<String> listOfFacultyName();

    @Query("SELECT u  FROM Faculty AS u WHERE u.facultyName = ?1 ")
    Faculty findByFacultyName(String facultyName);

    @Modifying
    @Query("DELETE FROM Faculty AS u WHERE u.facultyName = ?1 ")
    void deleteByFacultyName(String facultyName);

    @Query("SELECT u.id  FROM Faculty AS u WHERE u.facultyName = ?1")
    public Integer findId(String facultyName);
}
