package Output;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.bind.JAXB;
 
@XmlRootElement
public class Student{
 
   String name;
   int age;
   int id;

   public String getName(){
      return name;
   }

   @XmlElement
   public void setName(String name){
      this.name = name;
   }

   public int getAge(){
      return age;
   }

   @XmlElement
   public void setAge(int age){
      this.age = age;
   }

   public int getId(){
      return id;
   }

   @XmlAttribute
   public void setId(int id){
      this.id = id;
   }
}

class JAXBDemo {
	   public static void main(String[] args) throws IOException {
	      // create student object
	      Student st = new Student();
	      st.setAge(12);
	      st.setName("Sania");
	      InputStream in = null;
	         
	      try {
	         // create new URL
	         String s = "http://www.tutorialspoint.com/result.xml";
	         URL url = new URL(s);
	            
	         // saves student object to the file
	         JAXB.marshal(st, url);
	            
	         // create new input stram
	         in = (InputStream) url.openStream();
	            
	         int i=0;
	            
	         // read till the end of the xml file
	         while((i=in.read())!=-1) {
	            // convert integer to character
	            char c = (char)i;
	               
	            // print
	            System.out.print(c);
	         }
	      }catch(Exception ex) {
	         ex.printStackTrace();
	      }
	   }
	}