package TabToMXL;

import generated.ScorePartwise;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreWriter {

    Marshaller marshaller;

    public ScoreWriter() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ScorePartwise.class);
        this.marshaller = context.createMarshaller();
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                """                                        
                        <!DOCTYPE score-partwise PUBLIC "-//Recordare//DTD MusicXML Partwise//EN" "http://www.musicxml.org/dtds/partwise.dtd">
                                                
                        """);
    }
    public void writeToTempFile(ScorePartwise scorePartwise) throws JAXBException, IOException {
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String tDir = System.getProperty("java.io.tmpdir");
        marshaller.marshal(scorePartwise, new BufferedWriter(new FileWriter(tDir + "result.xml")));
    }

}
