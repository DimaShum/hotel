package gp.course.vaadin.hotel;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable, Cloneable {

	private Long id;
	
	private String name;
	
	public Category() {
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String category) {
		this.name = category;
	}
	
	public Category(String name) {
		this.name = name;
	}
	
	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	protected Category clone() throws CloneNotSupportedException {
		return (Category) super.clone();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
