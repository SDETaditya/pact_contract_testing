package com.rahulshettyacademy.service;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.rahulshettyacademy.controller.Library;
import com.rahulshettyacademy.repository.LibraryRepository;
@Service
@SuppressWarnings("null")
public class LibraryService {

	private final LibraryRepository repository;

	public LibraryService(LibraryRepository repository) {
		this.repository = repository;
	}
	public String buildId(String isbn,int aisle)
	{
		if(isbn.startsWith("Z"))
		{
			return "OLD"+isbn+aisle;
		}
		
		return isbn+aisle;
	}
	public boolean checkBookAlreadyExist(String id)
	{
		Optional<Library> lib=repository.findById(id);
		if(lib.isPresent())
			return true;
		else
			return false;
		
		
	}
	public Library getBookById(String id)
	{
		return repository.findById(id).get();
	}
}
