package objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Convert {

	public static final String xmlFilePath = "test.xml"; // will change later
	private static Document document;
	private static DocumentBuilder documentBuilder;
	private static DocumentBuilderFactory documentFactory;

	public static void main(String[] args) {

		// String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		// + " <!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.1
		// Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">";
		// trying to make code for conversion
		// idea1: create a loop and get the variable name for each beginning and ending
		// idea2: brute force like write it all out, but its inefficient and bad (write
		// line by line)
		// idea3: use the textfile as a template
		// for (int i = 0; i <= )

		try {

			documentFactory = DocumentBuilderFactory.newInstance();

			documentBuilder = documentFactory.newDocumentBuilder();

			document = documentBuilder.newDocument();

			// root element
			Element score = document.createElement("score-partwise");
			document.appendChild(score);

			// employee element
			Element partList = document.createElement("part-list");
			score.appendChild(partList);

			Element scorePart = document.createElement("score-part");
			partList.appendChild(scorePart);

			// set an attribute to staff element
			Attr attr = document.createAttribute("id");
			attr.setValue("P1");
			scorePart.setAttributeNode(attr);

			// you can also use staff.setAttribute("id", "1") for this

			// partName element
			Element partName = document.createElement("part-name"); // may have to either have a loop or an array of the
																	// objects in order to do this.
			partName.appendChild(document.createTextNode("James")); // add instrument
			partList.appendChild(partName);
			
			// create the xml file
			// transform the DOM Object to an XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(xmlFilePath));
			
			
			
			
			// If you use
			// StreamResult result = new StreamResult(System.out);
			// the output will be pushed to the standard output ...
			// You can use that for debugging

			transformer.transform(domSource, streamResult);
			
			
			/*StringWriter stringWrite = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(stringWrite));
			
			String xml;
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			String output = stringWrite.getBuffer().toString().replace("\n|\r", "");
			xml = output;
			System.out.println(xml);
			*/
			
			
			
			String xml;
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer2 = tf.newTransformer();
			transformer2.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer2.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer2.setOutputProperty(OutputKeys.INDENT, "yes");

			StringWriter stringWrite = new StringWriter();
			transformer2.transform(new DOMSource(document), new StreamResult(stringWrite));

			String output = stringWrite.getBuffer().toString().replace("\n|\r", "");
			xml = output;
			System.out.println(xml);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}

/*
Transformer transformer = TransformerFactory.newInstance().newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");
transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//initialize StreamResult with File object to save to file
StreamResult result = new StreamResult(new StringWriter());
DOMSource source = new DOMSource(doc);
transformer.transform(source, result);
String xmlString = result.getWriter().toString();
System.out.println(xmlString);
*/