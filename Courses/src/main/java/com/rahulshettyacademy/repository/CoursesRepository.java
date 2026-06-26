package com.rahulshettyacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahulshettyacademy.controller.AllCourseData;

public interface CoursesRepository extends JpaRepository<AllCourseData, String>{

}
