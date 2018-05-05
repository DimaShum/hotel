package gp.course.vaadin.hotel.db;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import gp.course.vaadin.hotel.Category;

public class CategoryDAOImpl implements CategoryDAO {

	private static CategoryDAOImpl instance;

	private final Logger logger = Logger.getLogger(getClass().getName());
	private SessionFactory sessionFactory;
	
	public CategoryDAOImpl() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public static CategoryDAOImpl getInstance() {
		if (instance == null) {
			instance = new CategoryDAOImpl();
		}
		return instance;
	}
	
	@Override
	public void save(Category entity) {
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			if (entity.getId() == null) {
				session.persist(entity);
			} else {
				session.merge(entity);
			}
			tx.commit();
		} catch(HibernateException e) {
			logger.info("Save error: " + e.getLocalizedMessage());
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public void delete(Category entity) {
		Transaction tx = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(entity);
			tx.commit();
		} catch(HibernateException e) {
			logger.info("Delete error: " + e.getLocalizedMessage());;
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Long count() {
		try(Session session = sessionFactory.openSession()) {
			return (Long) session
					.createQuery("select count(*) from Category")
					.uniqueResult();
		} catch(HibernateException e) {
			logger.info("Count error: " + e.getLocalizedMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> findAll(String filterByName) {
		try(Session session = sessionFactory.openSession()) {
			List<Category> list = session
					.getNamedQuery("Category.byName")
					.setParameter("filterByName", "%" + filterByName.toLowerCase() + "%")
					.getResultList();
			return (list == null) ? Collections.EMPTY_LIST : list;
		} catch(HibernateException e) {
			logger.info("FindAll error: " + e.getLocalizedMessage());;
			return null;
		}
	}
}
