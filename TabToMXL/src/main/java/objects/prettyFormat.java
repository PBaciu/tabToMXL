package objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class prettyFormat {

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new FileInputStream(new File("test.xml")));		//there will be an unformatted xml file in the system
		//System.out.println(((File) doc).getAbsolutePath());
		prettyPrint(doc);
	}
	
	private static String prettyPrint(Document document) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		StringWriter strWriter = new StringWriter();
		
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(new File("test2.xml"));	//input new location when user choose
		transformer.transform(domSource, streamResult);
		
		//transformer.transform(source, result);
		System.out.println(strWriter.getBuffer().toString());
		
		
		
		System.out.println("-------------output done----------------");

		return strWriter.getBuffer().toString();

	}

}
