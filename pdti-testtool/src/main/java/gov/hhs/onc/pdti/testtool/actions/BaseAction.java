package gov.hhs.onc.pdti.testtool.actions;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;

import com.opensymphony.xwork2.ActionSupport;

@InterceptorRefs({ @InterceptorRef("defaultStack") })
public abstract class BaseAction extends ActionSupport {
}