package gp.course.vaadin.hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
	
	private static CategoryService instance;
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	private HashMap<Long, Category> categories = new HashMap<>();
	private long nextId = 0;

	private CategoryService() {
		save(new Category("Hotel"));
		save(new Category( "Hostel"));
		save(new Category("GuestHouse"));
		save(new Category("Appartments"));
	}

	public static CategoryService getInstance() {
		if (instance == null) {
			instance = new CategoryService();
		}
		return instance;
	}
	
	public boolean isExist(Category category) {
		return categories.containsValue(category);
	}

	public synchronized List<Category> findAll() {
		return findAll(null);
	}

	public synchronized List<Category> findAll(String stringFilter) {
		ArrayList<Category> arrayList = new ArrayList<>();
		for (Category category : categories.values()) {
			boolean passesFilter = filter(category, stringFilter);
			if (passesFilter) {
				arrayList.add(category);
			}
		}
		Collections.sort(arrayList, new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}
	
	private boolean filter(Object obj, String stringFilter) {
		boolean result = (stringFilter == null || stringFilter.isEmpty())
				|| obj.toString().toLowerCase().contains(stringFilter.toLowerCase());
		return result;
	}

	public synchronized long count() {
		return categories.size();
	}

	public synchronized void delete(Category value) {
		categories.remove(value.getId());
	}

	public synchronized void save(Category entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Category) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		categories.put(entry.getId(), entry);
	}
}
