package testdel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class FileTest2 {
	
	@Test
	public void testFileExist() {
		Convert2.createFile();
		File file = new File("test.xml");
		assertTrue(file.exists());
		
	}
	
	@Test
	public void testFile_text() throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc1 = db.parse(new File("test.xml"));

		Document doc2 = db.parse(new File("test2.xml"));
		Document doc3 = db.parse(new File("test3.xml"));
		System.out.println(doc1.equals(doc3));
		System.out.println(doc2.toString());
		//Assert.assertTrue(doc1.

		Assert.assertTrue(doc1.isEqualNode(doc2));			//unsure
	}
	
	@Test
	public void testFileDifferAfterFormat() throws IOException, ParserConfigurationException, SAXException {
		final File expected = new File("test.xml");
	    final File output = new File("test2.xml");
	    Assert.assertNotSame(FileUtils.readLines(expected), FileUtils.readLines(output));
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