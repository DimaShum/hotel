package gp.course.vaadin.hotel.db;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import gp.course.vaadin.hotel.Hotel;

public class HotelDAOImpl implements HotelDAO {
	
	private static HotelDAOImpl instance;

	private final Logger logger = Logger.getLogger(getClass().getName());
	private SessionFactory sessionFactory;
	
	public HotelDAOImpl() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public static HotelDAOImpl getInstance() {
		if (instance == null) {
			instance = new HotelDAOImpl();
		}
		return instance;
	}
	
	@Override
	public void save(Hotel entity) {
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
	public void delete(Hotel entity) {
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
					.createQuery("select count(*) from Hotel")
					.uniqueResult();
		} catch(HibernateException e) {
			logger.info("Count error: " + e.getLocalizedMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hotel> findAll(String filterByName, String filterByAddress) {
		try(Session session = sessionFactory.openSession()) {
			return session
					.getNamedQuery("Hotel.byNameAndByAddress")
					.setParameter("filterByName", "%" + filterByName.toLowerCase() + "%")
					.setParameter("filterByAddress", "%" + filterByAddress.toLowerCase() + "%")
					.getResultList();
		} catch(HibernateException e) {
			logger.info("FindAll error: " + e.getLocalizedMessage());;
			return null;
		}
	}
}
