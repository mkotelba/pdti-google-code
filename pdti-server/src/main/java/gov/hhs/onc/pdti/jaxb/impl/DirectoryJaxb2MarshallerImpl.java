package gov.hhs.onc.pdti.jaxb.impl;

import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

@Component("jaxb2Marshaller")
@Scope("singleton")
public class DirectoryJaxb2MarshallerImpl implements DirectoryJaxb2Marshaller {
    @Autowired
    private Jaxb2Marshaller marshaller;

    @Override
    public String marshal(Object obj) throws XmlMappingException {
        StringResult strResult = new StringResult();

        this.marshaller.marshal(obj, strResult);

        return strResult.toString();
    }

    @Override
    public Object unmarshal(String str) throws XmlMappingException {
        return this.marshaller.unmarshal(new StringSource(str));
    }
}
