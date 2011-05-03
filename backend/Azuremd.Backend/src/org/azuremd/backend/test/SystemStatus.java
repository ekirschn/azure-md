
package org.azuremd.backend.test;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for systemStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="systemStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NONE"/>
 *     &lt;enumeration value="STARTING"/>
 *     &lt;enumeration value="READY"/>
 *     &lt;enumeration value="BUSY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "systemStatus")
@XmlEnum
public enum SystemStatus {

    NONE,
    STARTING,
    READY,
    BUSY;

    public String value() {
        return name();
    }

    public static SystemStatus fromValue(String v) {
        return valueOf(v);
    }

}
