package gp.course.vaadin.hotel.servises;

import java.util.List;

import gp.course.vaadin.hotel.entities.Hotel;

public interface HotelDAO extends DAO<Hotel> {
	
	List<Hotel> findAll(String filterByName, String filterByAddress);
}
