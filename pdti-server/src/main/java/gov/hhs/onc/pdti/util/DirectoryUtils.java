package gov.hhs.onc.pdti.util;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.directory.api.dsmlv2.reponse.ErrorResponse;
import org.apache.directory.api.dsmlv2.reponse.ErrorResponse.ErrorResponseType;

public abstract class DirectoryUtils {
    private final static String XML_SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";

    private final static QName XML_SCHEMA_STRING = new QName(XML_SCHEMA_NS, "string");

    private final static ErrorResponse ERR_RESP = new ErrorResponse(-1, null, null);
    
    public static String getErrorResponseTypeDesc(ErrorResponseType errRespType)
    {
        return ERR_RESP.getTypeDescr(errRespType);
    }
    
    public static JAXBElement<String> getStackTraceJaxbElement(Throwable th) {
        return new JAXBElement<>(XML_SCHEMA_STRING, String.class, ExceptionUtils.getStackTrace(th));
    }
}
