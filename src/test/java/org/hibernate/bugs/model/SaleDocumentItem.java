package org.hibernate.bugs.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class SaleDocumentItem {
	@Id
	@SequenceGenerator(name = "ID2", sequenceName = "B")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID2")
	private Long id;
	private Integer lp;
	@ManyToOne(optional = true)
	private Product product;
	@JoinColumn(name = "ID_SALE_DOCUMENT", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private SaleDocument saleDocument;
	@JoinColumn(name = "ID_SALE_DOCUMENT_SUMAMRY", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private SaleDocumentSummary summary;
	private Integer quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLp() {
		return lp;
	}

	public void setLp(Integer lp) {
		this.lp = lp;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public SaleDocument getSaleDocument() {
		return saleDocument;
	}

	public void setSaleDocument(SaleDocument saleDocument) {
		this.saleDocument = saleDocument;
	}

	public SaleDocumentSummary getSummary() {
		return summary;
	}

	public void setSummary(SaleDocumentSummary summary) {
		this.summary = summary;
	}

}
