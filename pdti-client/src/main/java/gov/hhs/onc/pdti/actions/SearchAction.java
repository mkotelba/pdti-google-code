package gov.hhs.onc.pdti.actions;

import gov.hhs.onc.pdti.ws.api.AttributeValueAssertion;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.Filter;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import gov.hhs.onc.pdti.ws.api.SearchResponse;
import gov.hhs.onc.pdti.ws.api.SearchResultEntry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
    @Result(name="success", location="results.jsp"),
    @Result(name="input", location="results.jsp"),
    @Result(name="error", location="results.jsp")
  })
public class SearchAction extends BaseAction {

    private static final Logger LOGGER = Logger.getLogger(SearchAction.class);
    private static final String NEWLINE = "\n";
    private static final String OU = "ou=";
    private static final String COMMA = ",";
    private static final String SUBTREE = "wholeSubtree";
    private static final String SINGLE_LEVEL = "singleLevel";
    private static final String DEREF_FINDING_BASE_OBJ = "derefFindingBaseObj";
    
    private ObjectFactory objectFactory  = new ObjectFactory();

    private String typeToSearch;
    private String searchAttribute;
    private String searchString;
    private List<SearchResultEntry> searchResultEntries;
    private String errorMessage;

    public String execute() {
        URL wsdlUrl = null;
        try {
            wsdlUrl = new URL(getText("provider.directory.wsdl.url"));
        } catch(MalformedURLException malformedURLException) {
            LOGGER.error(malformedURLException);
            throw new RuntimeException(malformedURLException);
        }
        ProviderInformationDirectoryService fedDirService = new ProviderInformationDirectoryService(wsdlUrl);
        BatchResponse batchResponse = fedDirService.getProviderInformationDirectoryPortSoap()
                .providerInformationQueryRequest(buildBatchRequest());
        List<JAXBElement<?>> batchResponseJAXBElements = batchResponse.getBatchResponses();
        for(JAXBElement batchResponseJAXBElement : batchResponseJAXBElements) {
            Object value = batchResponseJAXBElement.getValue();
            if(value instanceof SearchResponse) {
                SearchResponse searchResponse = (SearchResponse)value;
                if(null != searchResponse.getSearchResultDone().getErrorMessage()) {
                    errorMessage += searchResponse.getSearchResultDone().getErrorMessage() + NEWLINE;
                } else {
                    searchResultEntries = searchResponse.getSearchResultEntry();
                }
            } else if(value instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse)value;
                errorMessage += errorResponse.getMessage() + NEWLINE;
            } else {
                throw new RuntimeException("Unknown JAXBElement value: " + value.getClass() + ".");
            }
        }
        if(null != errorMessage) {
            return ERROR;
        }
        return SUCCESS;
    }

    private BatchRequest buildBatchRequest() {
        BatchRequest batchRequest = objectFactory.createBatchRequest();
        SearchRequest searchRequest = objectFactory.createSearchRequest();
        searchRequest.setDn(OU + typeToSearch + COMMA + getText("dn"));
        searchRequest.setScope(SINGLE_LEVEL);
        searchRequest.setDerefAliases(DEREF_FINDING_BASE_OBJ);
        Filter filter = objectFactory.createFilter();
        AttributeValueAssertion attributeValueAssertion = objectFactory.createAttributeValueAssertion();
        attributeValueAssertion.setName(searchAttribute);
        attributeValueAssertion.setValue(searchString);
        filter.setEqualityMatch(attributeValueAssertion);
        searchRequest.setFilter(filter);
        batchRequest.getBatchRequests().add(searchRequest);
        return batchRequest;
    }

    public String getTypeToSearch() {
        return typeToSearch;
    }
    
    public void setTypeToSearch(String typeToSearch) {
        this.typeToSearch = typeToSearch;
    }

    public String getSearchAttribute() {
        return searchAttribute;
    }

    public void setSearchAttribute(String searchAttribute) {
        this.searchAttribute = searchAttribute;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<SearchResultEntry> getSearchResultEntries() {
        return searchResultEntries;
    }

    public void setSearchResultEntries(List<SearchResultEntry> searchResultEntries) {
        this.searchResultEntries = searchResultEntries;
    }

}
