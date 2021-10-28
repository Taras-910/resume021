<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="name" required="true" description="Name of corresponding property in bean object" %>
<%@ attribute name="labelCode" required="true" description="Field label" %>
<%@ attribute name="inputType" required="false" description="Input type" %>

<spring:bind path="${name}">
    <div class="form-group ${status.error ? 'error' : '' }">
        <label class="col-form-label">${labelCode}</label>
        <form:input path="${name}" type="${(empty inputType)?'text':inputType}" class="form-control ${status.error ? 'is-invalid' : '' }"/>
        <div class="invalid-feedback">${status.errorMessage}</div>
    </div>
</spring:bind>

<%--<spring:bind path="${name}">
    <div class="form-group ${status.error ? 'error' : '' }">
        <label class="col-form-label">${labelCode}</label>
        <c:choose>
            &lt;%&ndash;<c:when test="${inputType == 'password'}"><form:password path="${name}" class="form-control is-invalid"/></c:when>&ndash;%&gt;
            <c:when test="${inputType == 'password'}"><form:password path="${name}" class="form-control is-invalid"/></c:when>
            <c:when test="${inputType == 'number'}"><form:input path="${name}" type="number" class="form-control is-invalid"/></c:when>
            <c:otherwise><form:input path="${name}" class="form-control is-invalid"/></c:otherwise>
        </c:choose>
        <div class="invalid-feedback">${status.errorMessage}</div>
    </div>
</spring:bind>--%>
