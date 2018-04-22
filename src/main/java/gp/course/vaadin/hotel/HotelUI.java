package gp.course.vaadin.hotel;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mytheme")
public class HotelUI extends UI {
	
	final VerticalLayout layout = new VerticalLayout();
	final HotelView hotelView = new HotelView();
	final CategoryView categoryView = new CategoryView();
	
	final static String HOTEL_VIEW = "";
	final static String CATEGORY_VIEW = "categories";
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	//MenuBar MODE
//    	
//    	hotelView.setVisible(true);
//		categoryView.setVisible(false);
//    	
//    	MenuBar menu = new MenuBar();
//    	menu.addItem("Hotel", VaadinIcons.BUILDING, e -> {
//    		hotelView.updateHotelList();
//    		hotelView.hotelForm.refreshCategory();
//    		hotelView.setVisible(true);
//    		categoryView.setVisible(false);
//    	});
//    	menu.addItem("Category",VaadinIcons.ACADEMY_CAP, e -> {
//    		hotelView.setVisible(false);
//    		categoryView.updateCategoryList();
//    		categoryView.setVisible(true);
//    	});
//    	menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
//        
//        layout.addComponents(menu, hotelView, categoryView);
//        layout.setWidth(100.0f, Unit.PERCENTAGE);
//        
//        setContent(layout);
    	
    	//Navigator MODE
    	getPage().setTitle("Hotel Page");
    	
    	VerticalLayout content = new VerticalLayout();
    	content.setSizeFull();
    	content.setMargin(false);
    	
    	Navigator navigator = new Navigator(this, content);
    	navigator.addView(HOTEL_VIEW, hotelView);
    	navigator.addView(CATEGORY_VIEW, categoryView);
    	
    	MenuBar menu = new MenuBar();
    	menu.addItem("Hotel", VaadinIcons.BUILDING, e -> {
    		getPage().setTitle("Hotel Page");
    		
    		hotelView.updateHotelList();
    		hotelView.hotelForm.refreshCategory();
    		
    		navigator.navigateTo(HOTEL_VIEW);
    	});
    	menu.addItem("Category",VaadinIcons.ACADEMY_CAP, e -> {
    		getPage().setTitle("Category Page");
    		
    		categoryView.updateCategoryList();
    		
    		navigator.navigateTo(CATEGORY_VIEW);
    	});
    	menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
    	
      layout.addComponents(menu, content);
      
      setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "NavigatorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class NavigatorUIServlet extends VaadinServlet {
    }
}
