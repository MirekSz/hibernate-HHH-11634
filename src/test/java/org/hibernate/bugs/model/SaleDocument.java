package org.hibernate.bugs.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class SaleDocument {
	@Id
	@SequenceGenerator(name = "ID", sequenceName = "A")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID")
	private Long id;
	private String number;
	@JoinColumn(name = "ID_SALE_DOCUMENT", updatable = false)
	@OneToMany(fetch = FetchType.LAZY)
	private Set<SaleDocumentItem> items = new HashSet();
	@JoinColumn(name = "ID_SALE_DOCUMENT_CORRECTION", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private SaleDocument corerctionSubject;
	private BigDecimal totalPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Set<SaleDocumentItem> getItems() {
		return items;
	}

	public void setItems(Set<SaleDocumentItem> items) {
		this.items = items;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void addItem(SaleDocumentItem sdi) {
		if (sdi.getQuantity() == null || sdi.getQuantity() < 1) {
			throw new IllegalArgumentException();
		}
		this.getItems().add(sdi);
	}

	public SaleDocument getCorerctionSubject() {
		return corerctionSubject;
	}

	public void setCorerctionSubject(SaleDocument corerctionSubject) {
		this.corerctionSubject = corerctionSubject;
	}

}
