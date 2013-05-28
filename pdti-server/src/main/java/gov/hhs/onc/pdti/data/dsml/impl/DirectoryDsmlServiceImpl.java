package gov.hhs.onc.pdti.data.dsml.impl;

import gov.hhs.onc.pdti.data.dsml.DirectoryDsmlException;
import gov.hhs.onc.pdti.data.dsml.DirectoryDsmlService;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import javax.xml.bind.JAXBElement;
import org.apache.directory.api.dsmlv2.engine.Dsmlv2Engine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

@Scope("singleton")
@Service("dsmlService")
public class DirectoryDsmlServiceImpl implements DirectoryDsmlService {
    private final static Logger LOGGER = Logger.getLogger(DirectoryDsmlServiceImpl.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Autowired
    private DirectoryJaxb2Marshaller jaxb2Marshaller;

    @Override
    public BatchResponse processDsml(Dsmlv2Engine dsmlEngine, BatchRequest batchReq) throws DirectoryDsmlException {
        try {
            String batchReqStr = this.jaxb2Marshaller.marshal(this.objectFactory.createBatchRequest(batchReq));

            LOGGER.debug("Processing DSML batch request (id=" + batchReq.getRequestID() + ").");
            LOGGER.trace("Processing DSML batch request string:\n" + batchReqStr);

            JAXBElement<BatchResponse> batchRespElement = (JAXBElement<BatchResponse>) this.jaxb2Marshaller
                    .unmarshal(dsmlEngine.processDSML(batchReqStr));
            BatchResponse batchResp = batchRespElement.getValue();
            String batchRespStr = this.jaxb2Marshaller.marshal(batchRespElement);

            LOGGER.debug("Processed DSML batch request (id=" + batchReq.getRequestID()
                    + ") into DSML batch response (num=" + batchResp.getBatchResponses().size() + ").");
            LOGGER.trace("Processed DSML batch request into DSML batch response string:\n" + batchRespStr);

            return batchResp;
        } catch (XmlMappingException | XmlPullParserException e) {
            throw new DirectoryDsmlException("Unable to process DSML batch transaction.", e);
        }
    }
}
