package gp.course.vaadin.hotel.db;

public interface DAO<T> {
	
	void save(T entity);
	void delete(T entity);
	Long count();
}
