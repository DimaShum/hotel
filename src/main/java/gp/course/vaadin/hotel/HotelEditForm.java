package gp.course.vaadin.hotel;


import java.time.LocalDate;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class HotelEditForm extends FormLayout {
	
	private static final long serialVersionUID = 1L;
	private HotelView ui;
	private HotelService hotelService = HotelService.getInstance();
	private CategoryService categoryService = CategoryService.getInstance();
	private Hotel hotel;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);

	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("OperatesFrom");
	private NativeSelect<Category> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");
	
	private Button save = new Button("Save");
	private Button close = new Button("Close");
	
	public HotelEditForm(HotelView hotelView) {
		this.ui = hotelView;
				
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(save, close);
		
		addComponents(name, address, rating, operatesFrom, category, url, description, buttons);
		
		name.setWidth(50.0f, Unit.PERCENTAGE);
		address.setWidth(50.0f, Unit.PERCENTAGE);
		rating.setWidth(50.0f, Unit.PERCENTAGE);
		operatesFrom.setWidth(50.0f, Unit.PERCENTAGE);
		url.setWidth(50.0f, Unit.PERCENTAGE);
		description.setWidth(50.0f, Unit.PERCENTAGE);
		
		prepareFields();
		refreshCategory();
		
		save.setEnabled(false);
		save.addClickListener(e -> save());
		
		binder.addStatusChangeListener(
				e -> save.setEnabled(binder.isValid()));
		
		close.addClickListener(e -> exit());
	}
	
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		//refreshCategory();
		binder.readBean(this.hotel);
		
		setVisible(true);
		
	}
	
	private void save() {
		try {
			binder.writeBean(hotel);
			hotelService.save(hotel);
			exit();
			Notification.show("Hotel was saved successfully!", Type.TRAY_NOTIFICATION);
		} catch(ValidationException e) {
			Notification.show("Unable to save! " + e.getMessage(), Type.ERROR_MESSAGE);
		}	
	}
	
	private void exit() {
		ui.updateHotelList();
		setVisible(false);
		ui.deleteHotel.setEnabled(false);
		ui.editHotel.setEnabled(false);
	}
	
	public void refreshCategory() {
		category.setItems(categoryService.findAll());
	}
	
	@SuppressWarnings("serial")
	public void prepareFields() {
		binder.forField(name)
				.asRequired("Please enter a name")
				.bind(Hotel::getName, Hotel::setName);
		binder.forField(address)
				.asRequired("Addres may not be empty")
				.withValidator(new StringLengthValidator("The address is too short", 5, null))
				.bind(Hotel::getAddress, Hotel::setAddress); 
		binder.forField(rating)
				.asRequired("Rating may not be empty")
				.withNullRepresentation("")
				.withConverter(new StringToIntegerConverter("Rating must be between 0 and 5"))
				.withValidator(new IntegerRangeValidator("Rating must be between 0 and 5", 0, 5))
				.bind(Hotel::getRating, Hotel::setRating);
		binder.forField(operatesFrom)
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
		binder.forField(category)
				.asRequired("You should choose...")
				.bind(Hotel::getCategory, Hotel::setCategory);
		binder.forField(url)
				.asRequired("Url may not be empty")
				.bind(Hotel::getUrl, Hotel::setUrl);
		binder.forField(description)
				.bind(Hotel::getDescription, Hotel::setDescription);
		
		name.setDescription("Hotel name");
		name.setPlaceholder("Type name of hotel");
		
		address.setDescription("Hotel address");
		address.setPlaceholder("Type where the hotel is situated");
		
		rating.setDescription("Hotel rating between 0 and 5");
		rating.setPlaceholder("Type rating from 0 to 5");
		
		operatesFrom.setDescription("Date in the past when hotel was opened");
		operatesFrom.setPlaceholder("Choose date in the past");
		
		
		category.setDescription("Kind of hotel");
		category.setEmptySelectionAllowed(false);
		
		url.setDescription("Hotel's website");
		url.setPlaceholder("Type url");
		
		description.setDescription("Descriptions of the hotel");
		description.setPlaceholder("Type few words about the hotel");
	}
}
