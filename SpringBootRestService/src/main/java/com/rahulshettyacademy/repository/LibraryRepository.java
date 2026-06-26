package com.rahulshettyacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahulshettyacademy.controller.Library;
public interface LibraryRepository extends JpaRepository<Library, String>,LibraryRepositoryCustom{

}
