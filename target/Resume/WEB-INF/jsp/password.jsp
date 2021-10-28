<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="vacancy" tagdir="/WEB-INF/tags" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.password.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container">
        <div class="row">
            <div class="col-5 offset-3">
                <form>
                    <label>Password: </label>
                    <input name="password" type="password" onChange="onChange()" /> <br />
                    <label>Confirm : </label><br />
                    <input name="confirm"  type="password" onChange="onChange()" />
                    <input type="submit" />
                </form>
                <div class="modal-body">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" onChange="onChange()">
                        <span class="fa fa-close"></span>
                        Отменить
                    </button>
                    <button type="button" class="btn btn-info" onclick="sendRefresh()">
                        Обновить
                        <span class="spinner-border spinner-border-sm" id="spinner" style="visibility: hidden"></span>
                    </button>
                </div>
                <%--
                                <h3>${register ? 'регистрация' : 'профиль'} ${user.name}</h3>
                                <form:form class="form-group" modelAttribute="user" method="post" action="${register ? 'profile/register' : 'profile'}"
                                           charset="utf-8" accept-charset="UTF-8">
                                    <vacancy:inputField labelCode="Имя" name="name"/>
                                    <vacancy:inputField labelCode="email" name="email"/>
                                    <vacancy:inputField labelCode="Пароль" name="password" inputType="password"/>
                                    &lt;%&ndash;<input labelCode="Пароль" name="password" inputType="password"/>&ndash;%&gt;
                                    <div class="text-right">
                                        <a class="btn btn-secondary" href="#" onclick="window.history.back()">
                                            <span class="fa fa-close"></span>
                                            Выйти
                                        </a>
                                        <button type="submit" class="btn btn-primary">
                                            <span class="fa fa-check"></span>
                                            Сохранить
                                        </button>
                                    </div>
                                </form:form>
                --%>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
