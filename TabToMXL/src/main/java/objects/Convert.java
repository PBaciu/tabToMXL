package objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Convert {

	public static final String xmlFilePath = "test.xml"; // will change later
	private Document document;
	private DocumentBuilder documentBuilder;
	private DocumentBuilderFactory documentFactory;

	public void convert() {

		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "		<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.1 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">";
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

			// set an attribute to staff elementExperimenting with documentbuilder Forgot what that prof said about class again
			Attr attr = document.createAttribute("id");
			attr.setValue("10");
			partList.setAttributeNode(attr);

			// you can also use staff.setAttribute("id", "1") for this

			// firstname element
			Element firstName = document.createElement("firstname");
			firstName.appendChild(document.createTextNode("James"));
			partList.appendChild(firstName);

			// lastname element
			Element lastname = document.createElement("lastname");
			lastname.appendChild(document.createTextNode("Harley"));
			partList.appendChild(lastname);

			// email element
			Element email = document.createElement("email");
			email.appendChild(document.createTextNode("james@example.org"));
			partList.appendChild(email);

			// department elements
			Element department = document.createElement("department");
			department.appendChild(document.createTextNode("Human Resources"));
			partList.appendChild(department);

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

			System.out.println("Done creating XML File");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}
