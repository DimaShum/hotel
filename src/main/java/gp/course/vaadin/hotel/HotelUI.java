package gp.course.vaadin.hotel;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import gp.course.vaadin.hotel.views.CategoryView;
import gp.course.vaadin.hotel.views.HotelView;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("mytheme")
@SpringUI
public class HotelUI extends UI {
	
	final VerticalLayout layout = new VerticalLayout();
	final HotelView hotelView = new HotelView();
	final CategoryView categoryView = new CategoryView();
	
	final static String HOTEL_VIEW = "";
	final static String CATEGORY_VIEW = "categories";
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
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
    
    @WebListener
    public static class HotelContextLoaderListener extends ContextLoaderListener {	
    }
    
    
    @Configuration
    @EnableVaadin
    public static class HotelConfiguration {
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    public static class HotelUIServlet extends SpringVaadinServlet {
    }
}
