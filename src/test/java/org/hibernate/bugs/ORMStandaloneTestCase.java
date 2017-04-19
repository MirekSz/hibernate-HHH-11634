package org.hibernate.bugs;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.TestForIssue;
import org.junit.Before;
import org.junit.Test;

import sun.tools.jstat.Operator;

/**
 * This template demonstrates how to develop a standalone test case for
 * Hibernate ORM. Although this is perfectly acceptable as a reproducer, usage
 * of ORMUnitTestCase is preferred!
 */
@TestForIssue(jiraKey = "HHH-11634")
public class ORMStandaloneTestCase {

	private SessionFactory sf;

	@Before
	public void setup() {
		StandardServiceRegistryBuilder srb = new StandardServiceRegistryBuilder()
				.applySetting("hibernate.show_sql", "true").applySetting("hibernate.format_sql", "true")
				.applySetting("hibernate.hbm2ddl.auto", "create").applySetting(Environment.STATEMENT_BATCH_SIZE, "10")
				.applySetting("hibernate.order_inserts", "true");

		Metadata metadata = new MetadataSources(srb.build()).addAnnotatedClass(SaleDocument.class)
				.addAnnotatedClass(SaleDocumentItem.class).addAnnotatedClass(SaleDocumentSummary.class)
				.addAnnotatedClass(Product.class).addAnnotatedClass(Operator.class).buildMetadata();

		sf = metadata.buildSessionFactory();
	}

	@Test
	public void hhh11634Test() throws Exception {
		Session session = sf.openSession();
		Transaction beginTransaction = session.beginTransaction();

		SaleDocumentItem saleDocumentItem = new SaleDocumentItem();
		session.persist(saleDocumentItem);

		SaleDocumentSummary saleDocumentsummary = new SaleDocumentSummary();
		session.persist(saleDocumentsummary);

		saleDocumentsummary.addItem(saleDocumentItem);

		Product product = new Product();
		session.persist(product);
		saleDocumentItem.setProduct(product);

		SaleDocument saleDocument = new SaleDocument();
		session.persist(saleDocument);
		saleDocument.addItem(saleDocumentItem);

		SaleDocument correction = new SaleDocument();
		session.persist(correction);

		saleDocument.setCorerctionSubject(correction);

		beginTransaction.commit(); // PRIMARY_KEY VIOLATION, set
									// hibernate.order_inserts = false and works
	}

	@Entity(name = "Product")
	public static class Product {

		@Id
		@GeneratedValue
		private Long id;
		@Column(unique = true)
		private String name;
		private String description;
		private Integer quantity;
		private BigDecimal price;

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

	@Entity(name = "SaleDocument")
	public static class SaleDocument {

		@Id
		@GeneratedValue
		private Long id;
		private String number;
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "saleDocument")
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
			this.getItems().add(sdi);
			sdi.setSaleDocument(this);
		}

		public SaleDocument getCorerctionSubject() {
			return corerctionSubject;
		}

		public void setCorerctionSubject(SaleDocument corerctionSubject) {
			this.corerctionSubject = corerctionSubject;
		}
	}

	@Entity(name = "SaleDocumentItem")
	public class SaleDocumentItem {

		@Id
		@GeneratedValue
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

	@Entity(name = "SaleDocumentSummary")
	public class SaleDocumentSummary {

		@Id
		@GeneratedValue
		private Long id;
		private String number;
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "summary")
		private Set<SaleDocumentItem> items = new HashSet();
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
			this.getItems().add(sdi);
			sdi.setSummary(this);
		}
	}

}
