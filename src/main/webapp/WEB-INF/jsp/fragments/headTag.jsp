<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Поиск резюме</title>
    <base href="${pageContext.request.contextPath}/"/>

    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.23/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="resources/css/style.css">
    <link rel="stylesheet" href="webjars/bootstrap/4.5.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="webjars/noty/3.1.4/demo/font-awesome/css/font-awesome.min.css">
    <link rel="shortcut icon" href="resources/images/icon-vacancy.png">
    <link rel="stylesheet" href="webjars/noty/3.1.4/lib/noty.css"/>
    <link rel="https://cdnjs.cloudflare.com/ajax/libs/material-components-web/4.0.0/material-components-web.min.css">
    <link rel="https://cdn.datatables.net/1.10.23/css/dataTables.material.min.css">
    <%--http://stackoverflow.com/a/24070373/548473--%>
    <script type="text/javascript" src="webjars/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/4.5.3/js/bootstrap.min.js" defer></script>
    <script type="text/javascript" src="webjars/noty/3.1.4/lib/noty.min.js" defer></script>
    <script type="text/javascript" src="https://cdn.datatables.net/1.10.23/js/jquery.dataTables.min.js" defer></script>
    <script type="https://cdn.datatables.net/1.10.23/js/dataTables.material.min.js" defer></script>
</head>
