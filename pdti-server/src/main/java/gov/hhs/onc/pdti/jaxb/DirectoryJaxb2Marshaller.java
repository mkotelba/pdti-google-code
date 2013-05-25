package gov.hhs.onc.pdti.jaxb;

import org.springframework.oxm.XmlMappingException;

public interface DirectoryJaxb2Marshaller {
    public String marshal(Object obj) throws XmlMappingException;

    public Object unmarshal(String str) throws XmlMappingException;
}
