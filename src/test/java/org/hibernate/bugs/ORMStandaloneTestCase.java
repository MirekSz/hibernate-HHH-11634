package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.bugs.model.Product;
import org.hibernate.bugs.model.SaleDocument;
import org.hibernate.bugs.model.SaleDocumentItem;
import org.hibernate.bugs.model.SaleDocumentSummary;
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
		session.save(saleDocumentItem);

		Product product = new Product();
		session.save(product);

		SaleDocument saleDocument = new SaleDocument();
		session.save(saleDocument);

		SaleDocument correction = new SaleDocument();
		session.save(correction);
		saleDocument.setCorerctionSubject(correction);

		SaleDocumentSummary saleDocumentsummary = new SaleDocumentSummary();
		session.save(saleDocumentsummary);

		saleDocumentItem.setProduct(product);
		saleDocumentItem.setSaleDocument(saleDocument);
		saleDocumentItem.setSummary(saleDocumentsummary);

		beginTransaction.commit(); // PRIMARY_KEY VIOLATION, set
									// hibernate.order_inserts = false and works
	}

}
