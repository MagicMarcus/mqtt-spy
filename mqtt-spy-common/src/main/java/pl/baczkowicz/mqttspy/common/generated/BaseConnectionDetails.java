//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.10 at 11:34:44 PM GMT 
//


package pl.baczkowicz.mqttspy.common.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
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
 * <p>Java class for BaseConnectionDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseConnectionDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServerURI" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ClientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UserCredentials" type="{http://baczkowicz.pl/mqtt-spy/common}UserCredentials" minOccurs="0"/>
 *         &lt;element name="LastWillAndTestament" type="{http://baczkowicz.pl/mqtt-spy/common}BaseMqttMessage" minOccurs="0"/>
 *         &lt;element name="CleanSession" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ConnectionTimeout" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="KeepAliveInterval" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseConnectionDetails", propOrder = {
    "serverURI",
    "clientID",
    "userCredentials",
    "lastWillAndTestament",
    "cleanSession",
    "connectionTimeout",
    "keepAliveInterval"
})
@XmlSeeAlso({
    BaseConnectionDetailsWithSubscriptionsAndScripts.class,
    NamedBaseConnectionDetailsWithPubSub.class
})
public class BaseConnectionDetails
    implements CopyTo, Copyable, Equals, HashCode, ToString
{

    @XmlElement(name = "ServerURI", required = true)
    protected String serverURI;
    @XmlElement(name = "ClientID", required = true)
    protected String clientID;
    @XmlElement(name = "UserCredentials")
    protected UserCredentials userCredentials;
    @XmlElement(name = "LastWillAndTestament")
    protected BaseMqttMessage lastWillAndTestament;
    @XmlElement(name = "CleanSession")
    protected Boolean cleanSession;
    @XmlElement(name = "ConnectionTimeout")
    protected Integer connectionTimeout;
    @XmlElement(name = "KeepAliveInterval")
    protected Integer keepAliveInterval;

    /**
     * Gets the value of the serverURI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerURI() {
        return serverURI;
    }

    /**
     * Sets the value of the serverURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerURI(String value) {
        this.serverURI = value;
    }

    /**
     * Gets the value of the clientID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Sets the value of the clientID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientID(String value) {
        this.clientID = value;
    }

    /**
     * Gets the value of the userCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link UserCredentials }
     *     
     */
    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    /**
     * Sets the value of the userCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserCredentials }
     *     
     */
    public void setUserCredentials(UserCredentials value) {
        this.userCredentials = value;
    }

    /**
     * Gets the value of the lastWillAndTestament property.
     * 
     * @return
     *     possible object is
     *     {@link BaseMqttMessage }
     *     
     */
    public BaseMqttMessage getLastWillAndTestament() {
        return lastWillAndTestament;
    }

    /**
     * Sets the value of the lastWillAndTestament property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseMqttMessage }
     *     
     */
    public void setLastWillAndTestament(BaseMqttMessage value) {
        this.lastWillAndTestament = value;
    }

    /**
     * Gets the value of the cleanSession property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCleanSession() {
        return cleanSession;
    }

    /**
     * Sets the value of the cleanSession property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCleanSession(Boolean value) {
        this.cleanSession = value;
    }

    /**
     * Gets the value of the connectionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the value of the connectionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setConnectionTimeout(Integer value) {
        this.connectionTimeout = value;
    }

    /**
     * Gets the value of the keepAliveInterval property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * Sets the value of the keepAliveInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKeepAliveInterval(Integer value) {
        this.keepAliveInterval = value;
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theServerURI;
            theServerURI = this.getServerURI();
            toStringBuilder.append("serverURI", theServerURI);
        }
        {
            String theClientID;
            theClientID = this.getClientID();
            toStringBuilder.append("clientID", theClientID);
        }
        {
            UserCredentials theUserCredentials;
            theUserCredentials = this.getUserCredentials();
            toStringBuilder.append("userCredentials", theUserCredentials);
        }
        {
            BaseMqttMessage theLastWillAndTestament;
            theLastWillAndTestament = this.getLastWillAndTestament();
            toStringBuilder.append("lastWillAndTestament", theLastWillAndTestament);
        }
        {
            Boolean theCleanSession;
            theCleanSession = this.isCleanSession();
            toStringBuilder.append("cleanSession", theCleanSession);
        }
        {
            Integer theConnectionTimeout;
            theConnectionTimeout = this.getConnectionTimeout();
            toStringBuilder.append("connectionTimeout", theConnectionTimeout);
        }
        {
            Integer theKeepAliveInterval;
            theKeepAliveInterval = this.getKeepAliveInterval();
            toStringBuilder.append("keepAliveInterval", theKeepAliveInterval);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof BaseConnectionDetails)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final BaseConnectionDetails that = ((BaseConnectionDetails) object);
        equalsBuilder.append(this.getServerURI(), that.getServerURI());
        equalsBuilder.append(this.getClientID(), that.getClientID());
        equalsBuilder.append(this.getUserCredentials(), that.getUserCredentials());
        equalsBuilder.append(this.getLastWillAndTestament(), that.getLastWillAndTestament());
        equalsBuilder.append(this.isCleanSession(), that.isCleanSession());
        equalsBuilder.append(this.getConnectionTimeout(), that.getConnectionTimeout());
        equalsBuilder.append(this.getKeepAliveInterval(), that.getKeepAliveInterval());
    }

    public boolean equals(Object object) {
        if (!(object instanceof BaseConnectionDetails)) {
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
        hashCodeBuilder.append(this.getServerURI());
        hashCodeBuilder.append(this.getClientID());
        hashCodeBuilder.append(this.getUserCredentials());
        hashCodeBuilder.append(this.getLastWillAndTestament());
        hashCodeBuilder.append(this.isCleanSession());
        hashCodeBuilder.append(this.getConnectionTimeout());
        hashCodeBuilder.append(this.getKeepAliveInterval());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final BaseConnectionDetails copy = ((target == null)?((BaseConnectionDetails) createCopy()):((BaseConnectionDetails) target));
        {
            String sourceServerURI;
            sourceServerURI = this.getServerURI();
            String copyServerURI = ((String) copyBuilder.copy(sourceServerURI));
            copy.setServerURI(copyServerURI);
        }
        {
            String sourceClientID;
            sourceClientID = this.getClientID();
            String copyClientID = ((String) copyBuilder.copy(sourceClientID));
            copy.setClientID(copyClientID);
        }
        {
            UserCredentials sourceUserCredentials;
            sourceUserCredentials = this.getUserCredentials();
            UserCredentials copyUserCredentials = ((UserCredentials) copyBuilder.copy(sourceUserCredentials));
            copy.setUserCredentials(copyUserCredentials);
        }
        {
            BaseMqttMessage sourceLastWillAndTestament;
            sourceLastWillAndTestament = this.getLastWillAndTestament();
            BaseMqttMessage copyLastWillAndTestament = ((BaseMqttMessage) copyBuilder.copy(sourceLastWillAndTestament));
            copy.setLastWillAndTestament(copyLastWillAndTestament);
        }
        {
            Boolean sourceCleanSession;
            sourceCleanSession = this.isCleanSession();
            Boolean copyCleanSession = ((Boolean) copyBuilder.copy(sourceCleanSession));
            copy.setCleanSession(copyCleanSession);
        }
        {
            Integer sourceConnectionTimeout;
            sourceConnectionTimeout = this.getConnectionTimeout();
            Integer copyConnectionTimeout = ((Integer) copyBuilder.copy(sourceConnectionTimeout));
            copy.setConnectionTimeout(copyConnectionTimeout);
        }
        {
            Integer sourceKeepAliveInterval;
            sourceKeepAliveInterval = this.getKeepAliveInterval();
            Integer copyKeepAliveInterval = ((Integer) copyBuilder.copy(sourceKeepAliveInterval));
            copy.setKeepAliveInterval(copyKeepAliveInterval);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new BaseConnectionDetails();
    }

}
