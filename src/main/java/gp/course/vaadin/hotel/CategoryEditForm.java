package gp.course.vaadin.hotel;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import gp.course.vaadin.hotel.db.CategoryDAOImpl;

import com.vaadin.ui.TextField;

public class CategoryEditForm extends FormLayout {

	private static final long serialVersionUID = 11L;
	private CategoryView ui;
	private CategoryDAOImpl categoryDAOImpl = CategoryDAOImpl.getInstance();
//	private CategoryService categoryService = CategoryService.getInstance();
	private Category category;
	private Binder<Category> binder = new Binder<>(Category.class);
	
	private TextField name = new TextField("Name");
	
	private Button save = new Button("Save");
	private Button close = new Button("Close");
	
	public CategoryEditForm(CategoryView categoryView) {
		this.ui = categoryView;
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(save, close);
		
		addComponents(name, buttons);
		
		name.setWidth(50.0f, Unit.PERCENTAGE);
		
		
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
//			categoryService.save(category);
			exit();
			Notification.show("Category was saved successfully!", Type.TRAY_NOTIFICATION);
		} catch(ValidationException e) {
			Notification.show("Unnable to save! " + e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	private void exit() {
		ui.updateCategoryList();
		setVisible(false);
		ui.deleteCategory.setEnabled(false);
		ui.editCategory.setEnabled(false);
	}
	
	public void prepareFields() {
		binder.forField(name)
				.asRequired("Category name may not be empty")
				.bind(Category::getName, Category::setName);
		
		name.setDescription("Hotel name");
		name.setPlaceholder("Type name of hotel");
	}
}
