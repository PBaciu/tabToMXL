//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.02.25 at 08:30:22 PM EST 
//


package generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{}score-header"/>
 *         &lt;element name="part" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="measure" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;group ref="{}music-data"/>
 *                           &lt;attGroup ref="{}measure-attributes"/>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attGroup ref="{}part-attributes"/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{}document-attributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "partList",
    "part"
})
@XmlRootElement(name = "score-partwise")
public class ScorePartwise {
    @XmlElement(name = "part-list", required = true)
    protected PartList partList;
    @XmlElement(name = "part", required = true)
    protected List<ScorePartwise.Part> part;
    @XmlAttribute(name = "version")
    protected java.lang.String version;

    /**
     * Gets the value of the partList property.
     * 
     * @return
     *     possible object is
     *     {@link PartList }
     *     
     */
    public PartList getPartList() {
        return partList;
    }

    /**
     * Sets the value of the partList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartList }
     *     
     */
    public void setPartList(PartList value) {
        this.partList = value;
    }

    /**
     * Gets the value of the part property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the part property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPart().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScorePartwise.Part }
     * 
     * 
     */
    public List<ScorePartwise.Part> getPart() {
        if (part == null) {
            part = new ArrayList<ScorePartwise.Part>();
        }
        return this.part;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getVersion() {
        if (version == null) {
            return "1.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setVersion(java.lang.String value) {
        this.version = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="measure" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;group ref="{}music-data"/>
     *                 &lt;attGroup ref="{}measure-attributes"/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{}part-attributes"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "measure"
    })
    public static class Part {

        @XmlElement(required = true)
        protected List<ScorePartwise.Part.Measure> measure;
        @XmlAttribute(name = "id", required = true)
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Object id;

        /**
         * Gets the value of the measure property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the measure property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMeasure().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ScorePartwise.Part.Measure }
         * 
         * 
         */
        public List<ScorePartwise.Part.Measure> getMeasure() {
            if (measure == null) {
                measure = new ArrayList<ScorePartwise.Part.Measure>();
            }
            return this.measure;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setId(Object value) {
            this.id = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;group ref="{}music-data"/>
         *       &lt;attGroup ref="{}measure-attributes"/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "noteOrBackupOrForward",
                "barline"
        })
        public static class Measure {

            @XmlElements({
                @XmlElement(name = "note", type = Note.class),
                @XmlElement(name = "backup", type = Backup.class),
                @XmlElement(name = "forward", type = Forward.class),
                @XmlElement(name = "direction", type = Direction.class),
                @XmlElement(name = "attributes", type = Attributes.class),
                @XmlElement(name = "harmony", type = Harmony.class),
                @XmlElement(name = "figured-bass", type = FiguredBass.class),
                @XmlElement(name = "print", type = Print.class),
                @XmlElement(name = "sound", type = Sound.class),
                @XmlElement(name = "barline", type = Barline.class),
                @XmlElement(name = "grouping", type = Grouping.class),
                @XmlElement(name = "link", type = Link.class),
                @XmlElement(name = "bookmark", type = Bookmark.class)
            })
            protected List<Object> noteOrBackupOrForward;
            @XmlAttribute(name = "number", required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "token")
            protected java.lang.String number;
            @XmlAttribute(name = "implicit")
            protected YesNo implicit;
            @XmlAttribute(name = "non-controlling")
            protected YesNo nonControlling;
            @XmlAttribute(name = "width")
            protected BigDecimal width;
            protected Barline barline;

            /**
             * Gets the value of the noteOrBackupOrForward property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the noteOrBackupOrForward property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getNoteOrBackupOrForward().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Note }
             * {@link Backup }
             * {@link Forward }
             * {@link Direction }
             * {@link Attributes }
             * {@link Harmony }
             * {@link FiguredBass }
             * {@link Print }
             * {@link Sound }
             * {@link Barline }
             * {@link Grouping }
             * {@link Link }
             * {@link Bookmark }
             * 
             * 
             */
            public List<Object> getNoteOrBackupOrForward() {
                if (noteOrBackupOrForward == null) {
                    noteOrBackupOrForward = new ArrayList<Object>();
                }
                return this.noteOrBackupOrForward;
            }

            /**
             * Gets the value of the number property.
             * 
             * @return
             *     possible object is
             *     {@link java.lang.String }
             *     
             */
            public java.lang.String getNumber() {
                return number;
            }

            /**
             * Sets the value of the number property.
             * 
             * @param value
             *     allowed object is
             *     {@link java.lang.String }
             *     
             */
            public void setNumber(java.lang.String value) {
                this.number = value;
            }

            /**
             * Gets the value of the implicit property.
             * 
             * @return
             *     possible object is
             *     {@link YesNo }
             *     
             */
            public YesNo getImplicit() {
                return implicit;
            }

            /**
             * Sets the value of the implicit property.
             * 
             * @param value
             *     allowed object is
             *     {@link YesNo }
             *     
             */
            public void setImplicit(YesNo value) {
                this.implicit = value;
            }

            /**
             * Gets the value of the nonControlling property.
             * 
             * @return
             *     possible object is
             *     {@link YesNo }
             *     
             */
            public YesNo getNonControlling() {
                return nonControlling;
            }

            /**
             * Sets the value of the nonControlling property.
             * 
             * @param value
             *     allowed object is
             *     {@link YesNo }
             *     
             */
            public void setNonControlling(YesNo value) {
                this.nonControlling = value;
            }

            /**
             * Gets the value of the width property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getWidth() {
                return width;
            }

            /**
             * Sets the value of the width property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setWidth(BigDecimal value) {
                this.width = value;
            }

            public Barline getBarline() {
                return barline;
            }

            public void setBarline(Barline barline) {
                this.barline = barline;
            }
        }

    }

}
