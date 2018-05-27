package gp.course.vaadin.hotel.views;

import java.sql.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.MultiSelectionModel.SelectAllCheckBoxVisibility;
import com.vaadin.ui.components.grid.MultiSelectionModelImpl;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;

import gp.course.vaadin.hotel.entities.Hotel;
import gp.course.vaadin.hotel.servises.HotelDataProvider;
import gp.course.vaadin.hotel.views.forms.BulkUpdateForm;
import gp.course.vaadin.hotel.views.forms.HotelEditForm;

@SuppressWarnings("serial")
public class HotelView extends AbstractView {
	
	final TextField filterByAddress = new TextField();
	final Button bulkUpdate = new Button("Bulk update", VaadinIcons.BULLETS);
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final HotelEditForm hotelForm = new HotelEditForm(this);

	private BulkUpdateForm bulkUpdateForm = new BulkUpdateForm(this);
	private PopupView popup = new PopupView(null, bulkUpdateForm);

	private HotelDataProvider dataProvider = new HotelDataProvider();

	public HotelView() {

		filterByName.setPlaceholder("Filter by name");
		filterByName.setValueChangeMode(ValueChangeMode.LAZY);
		filterByName.addValueChangeListener(e -> dataProvider.setNameFilter(filterByName.getValue()));
		
		filterByAddress.setPlaceholder("Filter by address");
		filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
		filterByAddress.addValueChangeListener(e -> dataProvider.setAddressFilter(filterByAddress.getValue()));
		
		add.setCaption("Add hotel");
		add.addClickListener(e -> hotelForm.setHotel(new Hotel()));
		
		delete.setCaption("Delete hotel");
		delete.setEnabled(false);
		delete.addClickListener(e -> {
			Set<Hotel> delCandidates = new HashSet<>();
			delCandidates = hotelGrid.getSelectedItems();
			for (Hotel hotel : delCandidates) {
				hotelDAOImpl.delete(hotel);
			}
			delete.setEnabled(false);
			edit.setEnabled(false);
			updateHotelList();

		});
		
		edit.setCaption("Edit hotel");
		edit.setEnabled(false);
		edit.addClickListener(e -> {
			hotelForm.setVisible(true);
		});
		
		bulkUpdate.setEnabled(false);
		
		hotelGrid.removeColumn("category");
		hotelGrid.addColumn(hotel -> {
			return hotel.getCategory() == null ? new String("No category") : new String(hotel.getCategory().toString());
		}).setId("category").setCaption("Category");

		hotelGrid.removeColumn("operatesFrom");
		hotelGrid.addColumn(hotel -> new Date(hotel.getOperatesFrom()),
						new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH))
						.setId("operatesFrom").setCaption("Operates from");

		hotelGrid.removeColumn("url");
		hotelGrid.addColumn(hotel -> "<a target='_blank' href='" + hotel.getUrl() + "'>Go to website</a>",
				new HtmlRenderer()).setId("url").setCaption("Url");

		hotelGrid.setColumnOrder("name", "address", "rating", "category", "operatesFrom", "description", "url");
		
		hotelGrid.setSelectionMode(SelectionMode.MULTI);
		hotelGrid.asMultiSelect().addSelectionListener(e -> {
			if (e.getAllSelectedItems().isEmpty()) {
				delete.setEnabled(false);
				edit.setEnabled(false);
				bulkUpdate.setEnabled(false);
			}
			if (e.getAllSelectedItems().size() == 1) {
				delete.setEnabled(true);
				edit.setEnabled(true);
				bulkUpdate.setEnabled(false);
				hotelForm.setHotel(e.getAllSelectedItems().iterator().next());
			}
			if (e.getAllSelectedItems().size() > 1) {
				delete.setEnabled(true);
				edit.setEnabled(false);
				bulkUpdate.setEnabled(true);
			}

			hotelForm.setVisible(false);
		});
		
		MultiSelectionModelImpl<Hotel> model = (MultiSelectionModelImpl<Hotel>) hotelGrid.getSelectionModel();
		model.setSelectAllCheckBoxVisibility(SelectAllCheckBoxVisibility.VISIBLE);
		
		bulkUpdate.addClickListener(e -> {
			popup.setPopupVisible(true);
			popup.setHideOnMouseOut(false);
			hotelForm.setVisible(false);

			bulkUpdateForm.setSelectedHotels(hotelGrid.getSelectedItems());
		});
		
		hotelForm.setVisible(false);
		
		updateHotelList();

		//UI set
		HorizontalLayout controlsHotel = new HorizontalLayout();
		controlsHotel.addComponents(filterByName, filterByAddress, add, delete, edit, bulkUpdate);
		
		AbsoluteLayout popupLayer = new AbsoluteLayout();
		popupLayer.setSizeFull();
		popupLayer.addComponent(popup, "left: 50%");

		HorizontalLayout content = new HorizontalLayout();
		content.setSizeFull();
		content.setWidth(100, Unit.PERCENTAGE);
		content.setHeight(600, Unit.PIXELS);
		content.addComponents(hotelGrid, hotelForm);
		content.setExpandRatio(hotelGrid, 0.75f);
		content.setExpandRatio(hotelForm, 0.25f);
		
		hotelGrid.setSizeFull();
		hotelForm.setSizeFull();

		addComponents(controlsHotel, popupLayer, content);
	}

	public void updateHotelList() {
		hotelGrid.setDataProvider(dataProvider);
		hotelForm.setVisible(false);
		popup.setPopupVisible(false);
	}
}
