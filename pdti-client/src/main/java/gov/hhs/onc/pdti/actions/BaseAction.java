package gov.hhs.onc.pdti.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;

@InterceptorRefs({ @InterceptorRef("defaultStack") })
public class BaseAction extends ActionSupport {

}