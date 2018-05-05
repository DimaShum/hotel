package gp.course.vaadin.hotel;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@SuppressWarnings("serial")
@Entity
@Table(name = "CATEGORY")
@NamedQueries({
    @NamedQuery(name = "Category.byName", query = "SELECT x FROM Category AS x WHERE LOWER(x.name) LIKE :filterByName")
})
public class Category implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Version
	@Column(name = "OPTLOCK")
	private Long version;
	
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
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(this.id == null) {
            return false;
        }

        if (obj instanceof Category && obj.getClass().equals(getClass())) {
            return this.id.equals(((Category) obj).id);
        }

        return false;
    }
	
	@Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }
	
	@Override
	public String toString() {
		return name;
	}
}
