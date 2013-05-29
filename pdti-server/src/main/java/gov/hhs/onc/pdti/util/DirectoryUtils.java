package gov.hhs.onc.pdti.util;

import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;

public abstract class DirectoryUtils {
    private final static String XML_SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

    private final static QName XML_SCHEMA_STRING = new QName(XML_SCHEMA_NS, "string");

    public static Dn replaceAncestorDn(Dn dn, Dn newAncestorDn) throws LdapInvalidDnException
    {
        List<Rdn> rdns = dn.getRdns().subList(0, dn.size() - newAncestorDn.size());
        rdns.addAll(newAncestorDn.getRdns());
        
        return new Dn(rdns.toArray(new Rdn[rdns.size()]));
    }
    
    public static JAXBElement<String> getStackTraceJaxbElement(Throwable th) {
        return new JAXBElement<>(XML_SCHEMA_STRING, String.class, ExceptionUtils.getStackTrace(th));
    }
}
