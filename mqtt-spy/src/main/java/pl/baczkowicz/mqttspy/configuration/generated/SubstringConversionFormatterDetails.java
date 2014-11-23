//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.23 at 10:43:15 PM GMT 
//


package pl.baczkowicz.mqttspy.configuration.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;


/**
 * <p>Java class for SubstringConversionFormatterDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubstringConversionFormatterDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{http://baczkowicz.pl/mqtt-spy-configuration}SubstringFormatterDetails">
 *       &lt;sequence>
 *         &lt;element name="Format" type="{http://baczkowicz.pl/mqtt-spy-configuration}ConversionMethod"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubstringConversionFormatterDetails", propOrder = {
    "format"
})
public class SubstringConversionFormatterDetails
    extends SubstringFormatterDetails
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlElement(name = "Format", required = true)
    protected ConversionMethod format;

    /**
     * Default no-arg constructor
     * 
     */
    public SubstringConversionFormatterDetails() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public SubstringConversionFormatterDetails(final String startTag, final String endTag, final boolean keepTags, final ConversionMethod format) {
        super(startTag, endTag, keepTags);
        this.format = format;
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link ConversionMethod }
     *     
     */
    public ConversionMethod getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConversionMethod }
     *     
     */
    public void setFormat(ConversionMethod value) {
        this.format = value;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            ConversionMethod theFormat;
            theFormat = this.getFormat();
            toStringBuilder.append("format", theFormat);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SubstringConversionFormatterDetails)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final SubstringConversionFormatterDetails that = ((SubstringConversionFormatterDetails) object);
        equalsBuilder.append(this.getFormat(), that.getFormat());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SubstringConversionFormatterDetails)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getFormat());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SubstringConversionFormatterDetails copy = ((target == null)?((SubstringConversionFormatterDetails) createCopy()):((SubstringConversionFormatterDetails) target));
        super.copyTo(copy, copyBuilder);
        {
            ConversionMethod sourceFormat;
            sourceFormat = this.getFormat();
            ConversionMethod copyFormat = ((ConversionMethod) copyBuilder.copy(sourceFormat));
            copy.setFormat(copyFormat);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SubstringConversionFormatterDetails();
    }

}
