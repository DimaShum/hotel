package gp.course.vaadin.hotel.db;

import java.util.List;

import gp.course.vaadin.hotel.Category;

public interface CategoryDAO extends DAO<Category> {
	
	List<Category> findAll(String filterByName);
}
