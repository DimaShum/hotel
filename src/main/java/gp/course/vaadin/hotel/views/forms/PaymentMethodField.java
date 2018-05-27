package gp.course.vaadin.hotel.views.forms;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import gp.course.vaadin.hotel.entities.PaymentMethod;

@SuppressWarnings("serial")
public class PaymentMethodField extends CustomField<PaymentMethod> {
	
	private static final String CREDIT_CARD = "Credit card";
	private static final String CASH = "Cash";

	final RadioButtonGroup<String> radioButton = new RadioButtonGroup<>();
	final TextField textField = new TextField();
	final Label label = new Label("Payment will be<br>made directly in the hotel", ContentMode.HTML);
	
	private PaymentMethod value;
	private PaymentMethod oldValue;
	
	private boolean isChanged = false;
	
	public PaymentMethodField(String caption) {
		setCaption(caption);
	}
	
	public void setOldValue(PaymentMethod value) {
		if (this.oldValue == null) {
			this.oldValue = new PaymentMethod();
		}
		if (value != null) {
			this.oldValue.setPaymentMethod(value.getPaymentMethod());
			this.oldValue.setPercent(value.getPercent());
		}
	}
	
	@Override
	public PaymentMethod getValue() {
		return value;
	}

	@Override
	protected Component initContent() {
		if (value == null) {
			value = new PaymentMethod();
		}
		radioButton.setItems(CREDIT_CARD, CASH);
		radioButton.addValueChangeListener(e -> {

			isChanged = e.isUserOriginated();
			if (e.isUserOriginated()) {
				
				if (e.getValue() != value.getPaymentMethod()) {
					setOldValue(value);
					value.setPaymentMethod(e.getValue());
					updateValues();
					showNotification();
				}
			}
		});
		
		textField.setValueChangeMode(ValueChangeMode.LAZY);
		textField.addValueChangeListener(e -> {
			String stringValue = e.getValue();
			if (stringValue != null && !stringValue.equals("") && stringValue.matches("^\\d+")) {
				int value = Integer.parseInt(stringValue);
				if (value >= 0 && value <= 100) {
					if (e.isUserOriginated()) {
						setOldValue(this.value);
					}
					this.value.setPercent(value);
					showNotification();
				} else {
					Notification.show("Value need to be between 0 and 100!", Type.WARNING_MESSAGE);
				}
				
			} else {
				Notification.show("Value need to be a number!", Type.WARNING_MESSAGE);
			}
		});
		
		VerticalLayout component = new VerticalLayout();
		component.addComponents(radioButton, textField, label);
		component.setMargin(false);
		return component;
	}
	
	private void updateValues() {
		if (getValue() != null && value.getPaymentMethod() != null) {
			radioButton.setSelectedItem(value.getPaymentMethod());
			if (value.getPaymentMethod().equals(CREDIT_CARD)) {
				textField.setValue(Integer.toString(value.getPercent()));
				textField.setVisible(true);
				label.setVisible(false);
			} else {
				value.setPercent(0);
				textField.setVisible(false);
				label.setVisible(true);
			}
		}
	}
	
	private void showNotification() {
		if (isChanged) {
			StringBuffer message = new StringBuffer();
			if (oldValue.getPaymentMethod().equals(CASH)) {
				message.append("There was " + oldValue.getPaymentMethod());
			} else {
				message.append("There was " + oldValue.getPaymentMethod() + ": "  + oldValue.getPercent() + "%");
			}
			if (value.getPaymentMethod().equals(CASH)) {
				message.append(", but became " + value.getPaymentMethod());
			} else {
				message.append(", but became " + value.getPaymentMethod() + ": " + value.getPercent() + "%");
			}
			Notification.show(message.toString(), Type.TRAY_NOTIFICATION);
		}
	}

	@Override
	protected void doSetValue(PaymentMethod value) {
		if (value == null) {
			this.value = new PaymentMethod();
		} else {
			this.value = value;
		}
		setOldValue(value);
		isChanged = false;
		updateValues();
	}
}
