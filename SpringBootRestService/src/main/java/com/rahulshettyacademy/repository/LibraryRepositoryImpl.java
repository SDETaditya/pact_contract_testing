package com.rahulshettyacademy.repository;

import java.util.ArrayList;
import java.util.List;

import com.rahulshettyacademy.controller.Library;

@SuppressWarnings("all")
public class LibraryRepositoryImpl implements LibraryRepositoryCustom{
	
	private final LibraryRepository repository;

	public LibraryRepositoryImpl(LibraryRepository repository) {
		this.repository = repository;
	}


	@Override
	public List<Library> findAllByAuthor(String authorName) {
		List<Library>bookswithAuthor = new ArrayList<Library>();
		List<Library>books =repository.findAll();
		for(Library item : books)
//			{
	if(item.getAuthor().equalsIgnoreCase(authorName))
	{
		bookswithAuthor.add(item);
	}
//			}
		
		return bookswithAuthor;
	}
	
	@Override
	public Library findByName(String bookName) {
		List<Library>books =repository.findAll();
		for(Library item : books)
//			{
	if(item.getBook_name().equalsIgnoreCase(bookName))
	{
		return item;
	}
//			}
		return null;
		
		
	}
	

}
