package org.hibernate.bugs.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Product {
	@Id
	@SequenceGenerator(name = "ID3", sequenceName = "C")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID3")
	private Long id;
	@Column(unique = true)
	private String name;
	private String description;
	private Integer quantity;
	private BigDecimal price;

	public Product(String name, String description, int quentity, BigDecimal price) {
		this.name = name;
		this.description = description;
		this.setQuantity(quentity);
		this.price = price;
	}

	public Product() {

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
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
