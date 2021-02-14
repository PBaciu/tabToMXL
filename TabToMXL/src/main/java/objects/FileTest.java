package objects;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FileTest {
	
	@Test
	public void testFileExist() {
		File file = new File("test.xml");
		assertTrue(file.exists());
		
	}
	
	@Test
	public void testFileDifferAfterFormat2() throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc1 = db.parse(new File("test.xml"));
		doc1.normalizeDocument();

		Document doc2 = db.parse(new File("test2.xml"));
		doc2.normalizeDocument();

		Assert.assertTrue(doc1.isEqualNode(doc2));
	}
	
	@Test
	public void testFileDifferAfterFormat() throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc1 = db.parse(new File("test.xml"));
		doc1.normalizeDocument();

		Document doc2 = db.parse(new File("test2.xml"));
		doc2.normalizeDocument();

		Assert.assertTrue(doc1.isEqualNode(doc2));
	}

}

/*
@Test
public void testgoSoutWest1() {
	Map city = new Map (10, 10);
	String actual = city.getPath(5,5, 4, 1, "");
	assertTrue(isPathCorrect(actual, 5, 5, 4, 1));		
}
*/