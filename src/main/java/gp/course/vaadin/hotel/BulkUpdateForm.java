package gp.course.vaadin.hotel;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.DateToLongConverter;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import gp.course.vaadin.hotel.db.CategoryDAOImpl;

@SuppressWarnings("serial")
public class BulkUpdateForm extends VerticalLayout {
	
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private CategoryDAOImpl categoryDAOImpl = CategoryDAOImpl.getInstance();
	
	private Label title = new Label("<h2><b>Bulk update</b></h2> \n <hr/>", ContentMode.HTML);
	private NativeSelect<String> field = new NativeSelect<>();
	private TextField textField = new TextField();
	private DateField dateField = new DateField();
	private NativeSelect<Category> categoryField = new NativeSelect<>();
	private TextArea textArea = new TextArea();
	private Button update = new Button("Update");
	private Button cancel = new Button("Cancel");
	
	private HotelView hotelView;
	private Hotel hotel = new Hotel();
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	private Set<Hotel> hotels;
	
	private List<String> namesOfFields = new ArrayList<>();
	
	public BulkUpdateForm(HotelView hView) {
		this.hotelView = hView;
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(update, cancel);
		addComponents(title, field, textField, dateField, categoryField, textArea, buttons);
		
		setWidth(300, Unit.PIXELS);
		title.setWidth(270, Unit.PIXELS);
		field.setWidth(270, Unit.PIXELS);
		textField.setWidth(270, Unit.PIXELS);
		dateField.setWidth(270, Unit.PIXELS);
		categoryField.setWidth(270, Unit.PIXELS);
		textArea.setWidth(270, Unit.PIXELS);
		buttons.setWidth(270, Unit.PIXELS);
		update.setWidth(125, Unit.PIXELS);
		cancel.setWidth(125, Unit.PIXELS);
		
		hideAllInputField();
		
		Field[] fields = hotel.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (!f.getName().equals("id") && !f.getName().equals("version")) {
				namesOfFields.add(f.getName());
			}
		}
		
		field.setItems(namesOfFields);
		field.addSelectionListener(e -> {
			clearFields();
			hideAllInputField();
			
			if (field.getValue() != null) {
				prepareField(field.getValue());
			}
		});
		
		update.addClickListener(e -> update());
		cancel.addClickListener(e -> cancel());
	}
	
	public void setSelectedHotels(Set<Hotel> selectedHotels) {
		this.hotels = selectedHotels;

		field.setEmptySelectionCaption("Please select field...");
		field.setSelectedItem("Please select field...");
		
		categoryField.setItems(categoryDAOImpl.findAll(""));
		categoryField.setEmptySelectionAllowed(false);
		
		clearFields();
		prepareField("Please select field...");
	}
	
	private void hideAllInputField() {
		textField.setVisible(false);
		dateField.setVisible(false);
		categoryField.setVisible(false);
		textArea.setVisible(false);
	}
	
	private void clearFields() {
		textField.clear();
		dateField.clear();
		categoryField.clear();
		textArea.clear();
	}
	
	private void prepareField(String nameOfField) {

		hotel = new Hotel();
		binder = new Binder<>(Hotel.class);
		binder.readBean(hotel);
		
		switch (nameOfField) {
			case "name" : {
				textField.setVisible(true);
				binder.forField(textField)
						.asRequired("Please enter a name")
						.bind(Hotel::getName, Hotel::setName);
				break;
			}
			case "address" : {
				textField.setVisible(true);
				binder.forField(textField)
						.asRequired("Addres may not be empty")
						.withValidator(new StringLengthValidator("The address is too short", 5, null))
						.bind(Hotel::getAddress, Hotel::setAddress); 
				break;
			}
			case "rating" : {
				textField.setVisible(true);
				binder.forField(textField)
						.asRequired("Rating may not be empty")
						.withNullRepresentation("")
						.withConverter(new StringToIntegerConverter("Rating must be between 0 and 5"))
						.withValidator(new IntegerRangeValidator("Rating must be between 0 and 5", 0, 5))
						.bind(Hotel::getRating, Hotel::setRating);
				break;
			}
			case "operatesFrom" : {
				dateField.setVisible(true);
				binder.forField(dateField)
						.asRequired("Date may not be empty")
						.withValidator(new Validator<LocalDate>() {
							@Override
							public ValidationResult apply(LocalDate value, ValueContext context) {
								if (value.isBefore(LocalDate.now())) {
									return ValidationResult.ok();
								}
								return ValidationResult.error("You should choose date in the past");
							}
						})
						.withConverter(new LocalDateToDateConverter())
						.withConverter(new DateToLongConverter())
						.bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
				break;
			}			
			case "category" : {
				categoryField.setVisible(true);
				binder.forField(categoryField)
						.asRequired("You should choose...")
						.withValidator(new Validator<Category>() {
							@Override
							public ValidationResult apply(Category value, ValueContext context) {
								if (value.getId() == null) {
									return ValidationResult.error("You should choose any category!");
								}
								return ValidationResult.ok();
							}
						})
//		 				.withNullRepresentation(nullCategory)
						.bind(Hotel::getCategory, Hotel::setCategory);
				break;
			}
			case "url" : {
				textField.setVisible(true);
				binder.forField(textField)
						.asRequired("Url may not be empty")
						.bind(Hotel::getUrl, Hotel::setUrl);
				break;
			}
			case "description" : {
				textArea.setVisible(true);
				binder.forField(textArea)
						.bind(Hotel::getDescription, Hotel::setDescription);
				break;
			}
		}
	}
	
	private void update() {
		if (!namesOfFields.contains(field.getValue())) {
			Notification.show("You need to choose any field or click \"Cancel\"", Type.WARNING_MESSAGE);
			return;
		}
		if (!binder.isValid()) {
			Notification.show("You need fill in the field or click \"Cancel\"", Type.WARNING_MESSAGE);
			return;
		}
		if (binder.isValid()) {
			try {
				binder.writeBean(hotel);
				saveCanges(field.getValue());
				Notification.show("Changes has successfuly updated :)", Type.TRAY_NOTIFICATION);
				cancel();
			} catch (ValidationException ex) {
				Notification.show("Changes hasn't updated :(", Type.ERROR_MESSAGE);
				logger.info("Bean cannot be write: " + ex.getLocalizedMessage());
			}
		}
	}
	
	private void saveCanges(String nameOfField) {
		Object obj = null;
		try {
			Field f = hotel.getClass().getDeclaredField(nameOfField);
			f.setAccessible(true);
			obj = f.get(hotel);
		} catch (Exception ex) {
			logger.info("Field cannot be read: " + ex.getLocalizedMessage());
		}
		for (Hotel h : hotels) {
			try {
				Field f = h.getClass().getDeclaredField(nameOfField);
				f.setAccessible(true);
				f.set(h, obj);
			} catch(Exception ex) {
				logger.info("Field cannot be write: " + ex.getLocalizedMessage());
			}
			hotelView.hotelDAOImpl.save(h);
		} 
	}
	
	private void cancel() {
		hotel = null;
		binder = null;
		hotelView.updateHotelList();
	}
}
