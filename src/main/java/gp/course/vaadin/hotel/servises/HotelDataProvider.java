package gp.course.vaadin.hotel.servises;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import gp.course.vaadin.hotel.entities.Hotel;

@SuppressWarnings("serial")
public class HotelDataProvider extends AbstractBackEndDataProvider<Hotel, String> {
	
	private HotelDAOImpl hotelDAOImpl = HotelDAOImpl.getInstance();
	
	private String nameFilter = "";
	private String addressFilter = "";
	
	public void setNameFilter(String name) {
		if (name != null) {
			this.nameFilter = name;
		}
		refreshAll();
	}
	
	public void setAddressFilter(String address) {
		if (address != null) {
			this.addressFilter = address;
		}
		refreshAll();
	}

	@Override
	protected Stream<Hotel> fetchFromBackEnd(Query<Hotel, String> query) {
		return hotelDAOImpl.findAll(nameFilter, addressFilter).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Hotel, String> query) {
		return (int)hotelDAOImpl.count(nameFilter, addressFilter);
	}


}
