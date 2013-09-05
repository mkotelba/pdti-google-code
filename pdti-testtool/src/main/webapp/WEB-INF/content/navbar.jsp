<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <div class="brand">
	            <img src="<s:url value="/img/pdti-logo.png"/>" id="title-img"/>
	            <s:text name="title"/>
            </div>
            <ul class="nav">
                <li id="home"><a href="<s:url value="/"/>"><i class="icon-home icon-white"></i> <s:text name="home"/></a></li>
                <li id="test"><a href="<s:url action="test"/>"><i class="icon-play icon-white"></i> <s:text name="test"/></a></li>
            </ul>
            <ul class="nav pull-right">
                <li id="fat-menu" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown"><s:text name="about"/> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="http://google.com" target="_blank"><s:text name="user.guide"/></a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="http://nytimes.com" target="_blank"><s:text name="faq"/></a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="https://code.google.com/p/pdti/" target="_blank"><s:text name="project"/></a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="https://groups.google.com/forum/?fromgroups#!forum/provider-directories-test-implementation" target="_blank"><s:text name="groups"/></a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="http://jira.siframework.org/browse/MSPDTI" target="_blank"><s:text name="jira"/></a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>