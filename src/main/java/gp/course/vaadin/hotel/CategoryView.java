package gp.course.vaadin.hotel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

@SuppressWarnings("serial")
public class CategoryView extends VerticalLayout implements View {
	 
	final CategoryService categoryService = CategoryService.getInstance();
		
	final TextField filterByName = new TextField();
	final Button addCategory = new Button("Add category");
	final Button deleteCategory = new Button("Delete category");
	final Button editCategory = new Button("Edit category");
	final Grid<Category> categoryGrid = new Grid<>(Category.class);
	private CategoryEditForm categoryForm = new CategoryEditForm(this);
	
	public CategoryView() {
		
		setSizeFull();
		setMargin(false);

        HorizontalLayout controlsCategory = new HorizontalLayout();
        controlsCategory.addComponents(filterByName, addCategory, deleteCategory, editCategory);
        
        filterByName.setPlaceholder("Filter by name");
        
        addCategory.addClickListener(e -> categoryForm.setCategory(new Category()));
        
        deleteCategory.setEnabled(false);
        editCategory.setEnabled(false);
        
        
        filterByName.addValueChangeListener(e -> updateCategoryList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        updateCategoryList();
        
        categoryGrid.setWidth(100.0f, Unit.PERCENTAGE);
        
        categoryGrid.setSelectionMode(SelectionMode.MULTI);
        categoryGrid.asMultiSelect().addValueChangeListener(e ->{
        	if (e.getValue() == null || !e.getValue().isEmpty()) {
        		deleteCategory.setEnabled(false);
        		editCategory.setEnabled(false);
        	}
        	if (e.getValue().size() == 1) {
        		deleteCategory.setEnabled(true);
        		editCategory.setEnabled(true);
        		editCategory.addClickListener(ev -> {
        			categoryForm.setCategory(e.getValue().iterator().next());
                });
        	}
        	if (e.getValue().size() > 1) {
        		deleteCategory.setEnabled(true);
        		editCategory.setEnabled(false);
        	}
        });
        
        deleteCategory.addClickListener(e -> {
        	Set<Category> delCandidates = new HashSet<>();
        	delCandidates = categoryGrid.getSelectedItems();
        	for (Category category : delCandidates) {
        		categoryService.delete(category);
        	}
        	deleteCategory.setEnabled(false);
        	editCategory.setEnabled(false);
        	
        	updateCategoryList();
        });
        
        categoryForm.setVisible(false);
        
        HorizontalLayout content = new HorizontalLayout();
        content.addComponents(categoryGrid, categoryForm);
        
        content.setSizeFull();
        content.setWidth(100.0f, Unit.PERCENTAGE);
        
        addComponents(controlsCategory, content);
	}

	public void updateCategoryList() {
    	List<Category> categoryList = categoryService.findAll(filterByName.getValue());
    	categoryGrid.setItems(categoryList);
    	categoryForm.setVisible(false);
    }
}
