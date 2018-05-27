package gp.course.vaadin.hotel.views.forms;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import gp.course.vaadin.hotel.entities.Category;
import gp.course.vaadin.hotel.servises.CategoryDAOImpl;
import gp.course.vaadin.hotel.views.CategoryView;

import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class CategoryEditForm extends FormLayout {

	private CategoryView ui;
	private CategoryDAOImpl categoryDAOImpl = CategoryDAOImpl.getInstance();
	private Category category;
	private Binder<Category> binder = new Binder<>(Category.class);
	
	private TextField name = new TextField("Name");
	
	HorizontalLayout buttons = new HorizontalLayout();
	
	private Button save = new Button("Save", VaadinIcons.ENTER_ARROW);
	private Button close = new Button("Close", VaadinIcons.CLOSE_CIRCLE_O);
	
	public CategoryEditForm(CategoryView categoryView) {
		this.ui = categoryView;
		
		name.setWidth(100, Unit.PERCENTAGE);
		buttons.addComponents(save, close);
		buttons.setWidth(100, Unit.PERCENTAGE);
		save.setWidth(100, Unit.PERCENTAGE);
		close.setWidth(100, Unit.PERCENTAGE);
		
		addComponents(name, buttons);
		setSizeFull();
		setMargin(false);
		
		prepareFields();
		
		save.setEnabled(false);
		save.addClickListener(e -> save());
		
		binder.addStatusChangeListener(
				e -> save.setEnabled(binder.isValid()));
		
		close.addClickListener(e -> exit());
	}

	public void setCategory(Category category) {
		this.category = category;
		binder.readBean(this.category);
		setVisible(true);
	}
	
	private void save() {
		try {
			binder.writeBean(category);
			categoryDAOImpl.save(category);
			exit();
			Notification.show("Category was saved successfully!", Type.TRAY_NOTIFICATION);
		} catch(ValidationException e) {
			Notification.show("Unnable to save! " + e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	private void exit() {
		ui.updateCategoryList();
		setVisible(false);
		ui.delete.setEnabled(false);
		ui.edit.setEnabled(false);
	}
	
	public void prepareFields() {
		binder.forField(name)
				.asRequired("Category name may not be empty")
				.bind(Category::getName, Category::setName);
		
		name.setDescription("Category name");
		name.setPlaceholder("Type name of category");
	}
}
