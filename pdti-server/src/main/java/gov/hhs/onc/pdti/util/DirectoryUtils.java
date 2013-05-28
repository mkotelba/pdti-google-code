package gov.hhs.onc.pdti.util;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.exception.ExceptionUtils;

public abstract class DirectoryUtils {
    private final static String XML_SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

    private final static QName XML_SCHEMA_STRING = new QName(XML_SCHEMA_NS, "string");

    public static JAXBElement<String> getStackTraceJaxbElement(Throwable th) {
        return new JAXBElement<>(XML_SCHEMA_STRING, String.class, ExceptionUtils.getStackTrace(th));
    }
}
