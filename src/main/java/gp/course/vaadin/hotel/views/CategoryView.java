package gp.course.vaadin.hotel.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import gp.course.vaadin.hotel.entities.Category;
import gp.course.vaadin.hotel.views.forms.CategoryEditForm;

import com.vaadin.ui.Grid.SelectionMode;

@SuppressWarnings("serial")
public class CategoryView extends AbstractView {
	 
	final Grid<Category> categoryGrid = new Grid<>(Category.class);
	private CategoryEditForm categoryForm = new CategoryEditForm(this);
	
	public CategoryView() {
		
        filterByName.setPlaceholder("Filter by name");
        filterByName.addValueChangeListener(e -> updateCategoryList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        
        add.setCaption("Add category");
        add.addClickListener(e -> categoryForm.setCategory(new Category()));
        
        delete.setCaption("Delete category");
        delete.setEnabled(false);
        delete.addClickListener(e -> {
        	Set<Category> delCandidates = new HashSet<>();
        	delCandidates = categoryGrid.getSelectedItems();
        	for (Category category : delCandidates) {
        		categoryDAOImpl.delete(category);
        	}
        	delete.setEnabled(false);
        	edit.setEnabled(false);
        	
        	updateCategoryList();
        });
        
        edit.setCaption("Edit category");
        edit.setEnabled(false);
        
        categoryGrid.setSelectionMode(SelectionMode.MULTI);
        categoryGrid.asMultiSelect().addValueChangeListener(e ->{
        	if (e.getValue() == null || !e.getValue().isEmpty()) {
        		delete.setEnabled(false);
        		edit.setEnabled(false);
        	}
        	if (e.getValue().size() == 1) {
        		delete.setEnabled(true);
        		edit.setEnabled(true);
        		edit.addClickListener(ev -> {
        			categoryForm.setCategory(e.getValue().iterator().next());
                });
        	}
        	if (e.getValue().size() > 1) {
        		delete.setEnabled(true);
        		edit.setEnabled(false);
        	}
        });
        
        categoryForm.setVisible(false);
        
        updateCategoryList();
        
        //UI set
        HorizontalLayout controlsCategory = new HorizontalLayout();
        controlsCategory.addComponents(filterByName, add, delete, edit);
        
        HorizontalLayout content = new HorizontalLayout();
        content.addComponents(categoryGrid, categoryForm);
        
        content.setSizeFull();
        content.setWidth(100, Unit.PERCENTAGE);
		content.setHeight(600, Unit.PIXELS);
		content.addComponents(categoryGrid, categoryForm);
		content.setExpandRatio(categoryGrid, 0.75f);
		content.setExpandRatio(categoryForm, 0.25f);
		
		categoryGrid.setSizeFull();
        
        addComponents(controlsCategory, content);

	}

	public void updateCategoryList() {
    	List<Category> categoryList = categoryDAOImpl.findAll(filterByName.getValue());
    	categoryGrid.setItems(categoryList);
    	categoryForm.setVisible(false);
    }
}
