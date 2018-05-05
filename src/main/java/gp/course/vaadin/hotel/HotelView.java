package gp.course.vaadin.hotel;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;

import gp.course.vaadin.hotel.db.HotelDAOImpl;

@SuppressWarnings("serial")
public class HotelView extends VerticalLayout implements View {

	final HotelDAOImpl hotelDAOImpl = HotelDAOImpl.getInstance();
	
	final TextField filterByName = new TextField();
	final TextField filterByAddress = new TextField();
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	final Button editHotel = new Button("Edit hotel");
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final HotelEditForm hotelForm = new HotelEditForm(this);
	
	public HotelView() {
		
		HorizontalLayout controlsHotel = new HorizontalLayout();
        controlsHotel.addComponents(filterByName, filterByAddress, addHotel, deleteHotel, editHotel);
        
        filterByName.setPlaceholder("Filter by name");
        filterByAddress.setPlaceholder("Filter by address");
        
        addHotel.addClickListener(e -> hotelForm.setHotel(new Hotel()));
		
		deleteHotel.setEnabled(false);
		editHotel.setEnabled(false);
		
		filterByName.addValueChangeListener(e -> updateHotelList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByAddress.addValueChangeListener(e -> updateHotelList());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
        updateHotelList();
        
        hotelGrid.removeColumn("category");
        hotelGrid.addColumn(hotel -> {
        		return hotel.getCategory() == null ? new String("No category") : new String(hotel.getCategory().toString());
        }).setId("category").setCaption("Category");
        
        hotelGrid.removeColumn("operatesFrom");
        hotelGrid.addColumn(hotel -> new Date(hotel.getOperatesFrom()), new DateRenderer("%1$tB %1$te, %1$tY",
                Locale.ENGLISH)).setId("operatesFrom").setCaption("Operates from");
        
        hotelGrid.removeColumn("url");
        hotelGrid.addColumn(hotel -> "<a target='_blank' href='" + hotel.getUrl() + "'>Go to website</a>",
        		new HtmlRenderer()).setId("url").setCaption("Url");
        
        hotelGrid.setColumnOrder("name", "address", "rating", "category", "operatesFrom", "description", "url");
        hotelGrid.setWidth(100, Unit.PERCENTAGE);
        
        hotelGrid.setSelectionMode(SelectionMode.MULTI);
        hotelGrid.asMultiSelect().addSelectionListener(e -> {
        	if (e.getAllSelectedItems().isEmpty()) {
        		deleteHotel.setEnabled(false);
        		editHotel.setEnabled(false);
        	}
        	if (e.getAllSelectedItems().size() == 1) {
        		deleteHotel.setEnabled(true);
        		editHotel.setEnabled(true);
        		hotelForm.setHotel(e.getAllSelectedItems().iterator().next());
        		
        	}
        	if (e.getAllSelectedItems().size() > 1) {
        		deleteHotel.setEnabled(true);
        		editHotel.setEnabled(false);
        	}
        	
        	editHotel.addClickListener(event -> {
    			hotelForm.setVisible(true);
    		});
        	hotelForm.setVisible(false);
        });
		
        deleteHotel.addClickListener(e -> {
        	Set<Hotel> delCandidates = new HashSet<>();
        	delCandidates = hotelGrid.getSelectedItems();
        	for (Hotel hotel : delCandidates) {
        		hotelDAOImpl.delete(hotel);
        	}
        	deleteHotel.setEnabled(false);
        	editHotel.setEnabled(false);
        	updateHotelList();
        	
        });
        
        hotelForm.setVisible(false);
        
        HorizontalLayout content = new HorizontalLayout();
        content.addComponents(hotelGrid, hotelForm);
        content.setSizeFull();
        content.setWidth(100.0f, Unit.PERCENTAGE);
        
        addComponents(controlsHotel, content);
        setSizeFull();
		setMargin(false);
	}
	
	public void updateHotelList() {
		List<Hotel> hotelList = hotelDAOImpl.findAll(filterByName.getValue(), filterByAddress.getValue());
    	hotelGrid.setItems(hotelList);
    	hotelForm.setVisible(false);
    }
}
