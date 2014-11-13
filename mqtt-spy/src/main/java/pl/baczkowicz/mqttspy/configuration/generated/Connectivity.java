//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.13 at 10:34:27 PM GMT 
//


package pl.baczkowicz.mqttspy.configuration.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 * <p>Java class for Connectivity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Connectivity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Connection" type="{http://baczkowicz.pl/mqtt-spy-configuration}UserInterfaceMqttConnectionDetailsV010"/>
 *         &lt;element name="ConnectionV2" type="{http://baczkowicz.pl/mqtt-spy-configuration}UserInterfaceMqttConnectionDetails"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Connectivity", propOrder = {
    "connectionOrConnectionV2"
})
public class Connectivity
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlElements({
        @XmlElement(name = "Connection", type = UserInterfaceMqttConnectionDetailsV010 .class),
        @XmlElement(name = "ConnectionV2", type = UserInterfaceMqttConnectionDetails.class)
    })
    protected List<Object> connectionOrConnectionV2;

    /**
     * Gets the value of the connectionOrConnectionV2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectionOrConnectionV2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectionOrConnectionV2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserInterfaceMqttConnectionDetailsV010 }
     * {@link UserInterfaceMqttConnectionDetails }
     * 
     * 
     */
    public List<Object> getConnectionOrConnectionV2() {
        if (connectionOrConnectionV2 == null) {
            connectionOrConnectionV2 = new ArrayList<Object>();
        }
        return this.connectionOrConnectionV2;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<Object> theConnectionOrConnectionV2;
            theConnectionOrConnectionV2 = this.getConnectionOrConnectionV2();
            toStringBuilder.append("connectionOrConnectionV2", theConnectionOrConnectionV2);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Connectivity)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Connectivity that = ((Connectivity) object);
        equalsBuilder.append(this.getConnectionOrConnectionV2(), that.getConnectionOrConnectionV2());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Connectivity)) {
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
        hashCodeBuilder.append(this.getConnectionOrConnectionV2());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final Connectivity copy = ((target == null)?((Connectivity) createCopy()):((Connectivity) target));
        {
            List<Object> sourceConnectionOrConnectionV2;
            sourceConnectionOrConnectionV2 = this.getConnectionOrConnectionV2();
            List<Object> copyConnectionOrConnectionV2 = ((List<Object> ) copyBuilder.copy(sourceConnectionOrConnectionV2));
            copy.connectionOrConnectionV2 = null;
            List<Object> uniqueConnectionOrConnectionV2l = copy.getConnectionOrConnectionV2();
            uniqueConnectionOrConnectionV2l.addAll(copyConnectionOrConnectionV2);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new Connectivity();
    }

}
