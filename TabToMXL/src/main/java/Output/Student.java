package Output;

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

			Element score = document.createElement("score-partwise");
			document.appendChild(score);

			Element partList = document.createElement("part-list");	
			score.appendChild(partList);

			Element scorePart = document.createElement("score-part");
			partList.appendChild(scorePart);
			Attr attr = document.createAttribute("id");
			attr.setValue("P1");
			scorePart.setAttributeNode(attr);

			Element partName = document.createElement("part-name"); // may have to either have a loop or an array of the objects in order to do this.
/*---->*/	partName.appendChild(document.createTextNode("INSTRUMENT")); // add instrument
			scorePart.appendChild(partName);
			
			/*
			for (drums) {
			Element scoreInstrument = document.createElement("part-name"); // may have to either have a loop or an array of the objects in order to do this.
//---->		scoreInstrument.appendChild(document.createTextNode("INSTRUMENT")); // add specific instrument
			scorePart.appendChild(scoreInstrument);
			
			}
			 */
			
			
			Element part = document.createElement("part");
			score.appendChild(part);
			Attr partNum = document.createAttribute("id");
			partNum.setValue("P1");
			part.setAttributeNode(partNum);
			
			/*for() {			//create a for loop for each measure then another for loop for each notes and staff-tuning(guitar) in the measure
				Element measure = document.createElement("measure");
				part.appendChild(measure);
				Attr num = document.createAttribute("number");	
//---->			num.setValue(MeasureNUM);				//Add Measure number here
				measure.setAttributeNode(num);
				
				Element attributes = document.createElement("attributes");
				measure.appendChild(attributes);
				
				Element divisions = document.createElement("divisions");
//---->			divisions.appendChild(document.createTextNode(divisionNum));	//input devision number
				attributes.appendChild(divisions);
				
				Element key = document.createElement("key");
				attributes.appendChild(key);
				Element fifths = document.createElement("fifths");
//---->			fifths.appendChild(document.createTextNode(fifthNum));			//input fifth number
 				key.appendChild(fifths);
			
				Element time = document.createElement("time");
				attributes.appendChild(time);
				Element beats = document.createElement("beats");
//---->			beats.appendChild(document.createTextNode(fifthNum));			//input beats number
 				time.appendChild(beats);
 				Element beatType = document.createElement("beat-type");
//---->			beatType.appendChild(document.createTextNode(fifthNum));			//input beatType number
 				time.appendChild(beatType);
 				
 				Element clef = document.createElement("clef");
				attributes.appendChild(clef);
				Element sign = document.createElement("sign");
//---->			sign.appendChild(document.createTextNode(signature));			//input sign String
 				clef.appendChild(sign);
 				Element line = document.createElement("beat-type");
//---->			line.appendChild(document.createTextNode(lineNum));			//input line number
 				clef.appendChild(line);
				
			}*/
			
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