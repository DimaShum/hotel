package gp.course.vaadin.hotel.servises;

import java.util.List;

import gp.course.vaadin.hotel.entities.Category;

public interface CategoryDAO extends DAO<Category> {
	
	List<Category> findAll(String filterByName);
}
