package gp.course.vaadin.hotel.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class PaymentMethod implements Serializable{
	
	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod = "Cash";
	@Column(name = "PERCENT")
	private int percent = 0;
	
	public PaymentMethod() {
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public String toString() {
		if (this.paymentMethod.equals("Credit card")) {
			return this.paymentMethod + ": " + percent + "%";
		}
		return this.paymentMethod;
	}
}
