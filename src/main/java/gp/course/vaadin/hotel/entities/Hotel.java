package gp.course.vaadin.hotel.entities;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "HOTEL")
@NamedQueries({
    @NamedQuery(name = "Hotel.byNameAndByAddress",
    		query = "SELECT x FROM Hotel AS x WHERE LOWER(x.name) LIKE :filterByName AND LOWER(x.address) LIKE :filterByAddress")
})
public class Hotel extends AbstractEntity {
	
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
	
	@Embedded
	private PaymentMethod paymentMethod;
		
	@Column(name = "URL")
	private String url;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	public Hotel() {
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

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
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

	@Override
	public String toString() {
		return name + " " + rating +"stars " + address;
	}
}