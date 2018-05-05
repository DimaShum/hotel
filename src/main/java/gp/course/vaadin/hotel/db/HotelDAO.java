package gp.course.vaadin.hotel.db;

import java.util.List;

import gp.course.vaadin.hotel.Hotel;

public interface HotelDAO extends DAO<Hotel> {
	
	List<Hotel> findAll(String filterByName, String filterByAddress);
}
