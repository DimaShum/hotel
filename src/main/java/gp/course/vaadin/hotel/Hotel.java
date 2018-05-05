package gp.course.vaadin.hotel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@SuppressWarnings("serial")
@Entity
@Table(name = "HOTEL")
@NamedQueries({
    @NamedQuery(name = "Hotel.byNameAndByAddress",
    		query = "SELECT x FROM Hotel AS x WHERE LOWER(x.name) LIKE :filterByName AND LOWER(x.address) LIKE :filterByAddress")
})
public class Hotel implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Version
	@Column(name = "OPTLOCK")
	private Long version;
	
	@Column(name = "NAME")
	private String name = "";
	
	@Column(name = "ADDRESS")
	private String address = "";
	
	@Column(name = "RATING")
	private Integer rating;
	
	@Column(name = "OPERATES_FROM")
	private Long operatesFrom;

	@ManyToOne
	@JoinColumn(name = "CATEGORY")
	private Category category;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	public Hotel() {
	}
	
//	public Hotel(Long id, String name, String address, Integer rating, Long operatesFrom, Category category, String url, String description) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.address = address;
//		this.rating = rating;
//		this.operatesFrom = operatesFrom;
//		this.category = category;
//		this.url = url;
//		this.description = description;
//	}

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return name + " " + rating +"stars " + address;
	}

	@Override
	protected Hotel clone() throws CloneNotSupportedException {
		return (Hotel) super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	
	public Long getOperatesFrom() {
		return operatesFrom;
	}

	public void setOperatesFrom(Long operatesFrom) {
		this.operatesFrom = operatesFrom;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) { 
		this.description = description;
	}


}