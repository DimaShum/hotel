package gp.course.vaadin.hotel.servises;

public interface DAO<T> {
	
	void save(T entity);
	void delete(T entity);
	Long count();
}
