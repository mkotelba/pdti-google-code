<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<div id="testResults">
    <table id="testResultsTable">
        <tr>
            <th class="testResultsHeader"><s:text name="testsuite.header.text"/></th>
            <th class="testResultsHeader"><s:text name="testcase.header.text"/></th>
            <th class="testResultsHeader"><s:text name="result.header.text"/></th>
        </tr>
        <s:iterator value="testResults" var="testResult">
            <tr class="testResultsRow">
                <s:iterator value="testResult">
                    <td class="testResultsData"><s:property/></td>
                </s:iterator>
            </tr>
        </s:iterator>
    </table>
    <div class="backContainer">
        <a href="<s:url value="/"/>"><s:text name="back"/></a>
    </div>
</div>