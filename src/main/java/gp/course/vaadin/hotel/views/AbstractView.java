package gp.course.vaadin.hotel.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import gp.course.vaadin.hotel.servises.CategoryDAOImpl;
import gp.course.vaadin.hotel.servises.HotelDAOImpl;

@SuppressWarnings("serial")
public class AbstractView extends VerticalLayout implements View {
	
	public final HotelDAOImpl hotelDAOImpl = HotelDAOImpl.getInstance();
	final CategoryDAOImpl categoryDAOImpl = CategoryDAOImpl.getInstance();

	final TextField filterByName = new TextField();
	final Button add = new Button(VaadinIcons.PLUS);
	public final Button delete = new Button(VaadinIcons.TRASH);
	public final Button edit = new Button(VaadinIcons.PENCIL);
	
	public AbstractView() {
		setSizeFull();
		setMargin(false);
	}
}
