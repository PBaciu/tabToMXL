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
 * <p>Java class for clef-sign.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="clef-sign">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="F"/>
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="percussion"/>
 *     &lt;enumeration value="TAB"/>
 *     &lt;enumeration value="jianpu"/>
 *     &lt;enumeration value="none"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "clef-sign")
@XmlEnum
public enum ClefSign {

    G("G"),
    F("F"),
    C("C"),
    @XmlEnumValue("percussion")
    PERCUSSION("percussion"),
    TAB("TAB"),
    @XmlEnumValue("jianpu")
    JIANPU("jianpu"),
    @XmlEnumValue("none")
    NONE("none");
    private final java.lang.String value;

    ClefSign(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ClefSign fromValue(java.lang.String v) {
        for (ClefSign c: ClefSign.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
