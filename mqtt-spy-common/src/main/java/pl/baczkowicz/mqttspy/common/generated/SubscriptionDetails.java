//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.10 at 11:34:44 PM GMT 
//


package pl.baczkowicz.mqttspy.common.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * <p>Java class for SubscriptionDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubscriptionDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="topic" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="qos" type="{http://baczkowicz.pl/mqtt-spy/common}qos" default="0" />
 *       &lt;attribute name="script" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubscriptionDetails")
public class SubscriptionDetails
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlAttribute(name = "topic", required = true)
    protected String topic;
    @XmlAttribute(name = "qos")
    protected Integer qos;
    @XmlAttribute(name = "script")
    protected String script;

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    /**
     * Gets the value of the qos property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getQos() {
        if (qos == null) {
            return  0;
        } else {
            return qos;
        }
    }

    /**
     * Sets the value of the qos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQos(Integer value) {
        this.qos = value;
    }

    /**
     * Gets the value of the script property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScript() {
        return script;
    }

    /**
     * Sets the value of the script property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScript(String value) {
        this.script = value;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theTopic;
            theTopic = this.getTopic();
            toStringBuilder.append("topic", theTopic);
        }
        {
            Integer theQos;
            theQos = this.getQos();
            toStringBuilder.append("qos", theQos);
        }
        {
            String theScript;
            theScript = this.getScript();
            toStringBuilder.append("script", theScript);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SubscriptionDetails)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SubscriptionDetails that = ((SubscriptionDetails) object);
        equalsBuilder.append(this.getTopic(), that.getTopic());
        equalsBuilder.append(this.getQos(), that.getQos());
        equalsBuilder.append(this.getScript(), that.getScript());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SubscriptionDetails)) {
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
        hashCodeBuilder.append(this.getTopic());
        hashCodeBuilder.append(this.getQos());
        hashCodeBuilder.append(this.getScript());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SubscriptionDetails copy = ((target == null)?((SubscriptionDetails) createCopy()):((SubscriptionDetails) target));
        {
            String sourceTopic;
            sourceTopic = this.getTopic();
            String copyTopic = ((String) copyBuilder.copy(sourceTopic));
            copy.setTopic(copyTopic);
        }
        {
            Integer sourceQos;
            sourceQos = this.getQos();
            Integer copyQos = ((Integer) copyBuilder.copy(sourceQos));
            copy.setQos(copyQos);
        }
        {
            String sourceScript;
            sourceScript = this.getScript();
            String copyScript = ((String) copyBuilder.copy(sourceScript));
            copy.setScript(copyScript);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SubscriptionDetails();
    }

}
