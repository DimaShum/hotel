package gp.course.vaadin.hotel;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.TextRenderer;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class HotelUI extends UI {
	
	final VerticalLayout layout = new VerticalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final TextField filterByName = new TextField();
	final TextField filterByAddress = new TextField();
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	private HotelEditForm form = new HotelEditForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	// UI configuration
        setContent(layout);
        
        HorizontalLayout controls = new HorizontalLayout();
        controls.addComponents(filterByName, filterByAddress, addHotel, deleteHotel);
        
        filterByName.setPlaceholder("Filter by name");
        filterByAddress.setPlaceholder("Filter by address"); 
        deleteHotel.setEnabled(false);
        
        HorizontalLayout content = new HorizontalLayout();
        content.addComponents(hotelGrid,form);
        content.setWidth(100, Unit.PERCENTAGE);
        
        form.setVisible(false);
        
        layout.addComponents(controls, content);
        
        hotelGrid.removeColumn("url");
        hotelGrid.addColumn(hotel -> "<a href='" + hotel.getUrl() + "'>Go to website</a>",
        		new HtmlRenderer()).setId("url").setCaption("Url");
        hotelGrid.setColumnOrder("name", "address", "rating", "category", "description", "url");
        hotelGrid.setWidth(100, Unit.PERCENTAGE);
        
        hotelGrid.asSingleSelect().addValueChangeListener(e -> {
        	if (e.getValue() != null) {
        		deleteHotel.setEnabled(true);
        		form.setHotel(e.getValue());
        	}
        });
        
        deleteHotel.addClickListener(e -> {
        	Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
        	hotelService.delete(delCandidate);
        	deleteHotel.setEnabled(false);
        	updateList();
        	form.setVisible(false);
        });
        
        //data
        filterByName.addValueChangeListener(e -> updateList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByAddress.addValueChangeListener(e -> updateList());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
        updateList();
        
        addHotel.addClickListener(e -> form.setHotel(new Hotel()));
    }
    
    public void updateList() {
    	List<Hotel> hotelList = hotelService.findAll(filterByName.getValue(), filterByAddress.getValue());
    	hotelGrid.setItems(hotelList);
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {
    }
}
