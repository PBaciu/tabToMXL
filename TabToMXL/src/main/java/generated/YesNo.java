//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.02.25 at 08:30:22 PM EST 
//


package generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for yes-no.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="yes-no">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="yes"/>
 *     &lt;enumeration value="no"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "yes-no")
@XmlEnum
public enum YesNo {

    @XmlEnumValue("yes")
    YES("yes"),
    @XmlEnumValue("no")
    NO("no");
    private final java.lang.String value;

    YesNo(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static YesNo fromValue(java.lang.String v) {
        for (YesNo c: YesNo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
