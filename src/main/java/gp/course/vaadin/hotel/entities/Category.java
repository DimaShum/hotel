package gp.course.vaadin.hotel.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "CATEGORY")
@NamedQueries({
    @NamedQuery(name = "Category.byName", query = "SELECT x FROM Category AS x WHERE LOWER(x.name) LIKE :filterByName")
})
public class Category extends AbstractEntity {

	private String name;
	
	public Category() {
	}
	
	public Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String category) {
		this.name = category;
	}
		
	@Override
	public String toString() {
		return name;
	}
}
