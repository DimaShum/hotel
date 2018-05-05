package gp.course.vaadin.hotel.depricated;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import gp.course.vaadin.hotel.Category;

public class CategoryService {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo_hotels");
	
	private static CategoryService instance;
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());
	
	

	private HashMap<Long, Category> categories = new HashMap<>();

	public static CategoryService getInstance() {
		if (instance == null) {
			instance = new CategoryService();
		}
		return instance;
	}
	
	public boolean isExist(Category category) {
		return categories.containsValue(category);
	}

	public synchronized List<Category> findAll(String stringFilter) {
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("Category.byName", Category.class)
				.setParameter("filter", "%" + stringFilter.toLowerCase() + "%")
				.getResultList();
	}

	public synchronized long count() {
		return categories.size();
	}

	public synchronized void delete(Category obj) {
		if (obj == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			Category category = em.find(Category.class, obj.getId());
			em.remove(category);
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Category hasn't removed!");
		}
	}

	public synchronized void save(Category obj) {
		if (obj == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}
		try {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			if (obj.getId() == null) {
				em.persist(obj);
				LOGGER.log(Level.SEVERE, "Category was successfully created.");
			} else {
				em.merge(obj);
				LOGGER.log(Level.SEVERE, "Category was successfully rewrited.");
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Category hasn't saved!");
		}
	}
}
