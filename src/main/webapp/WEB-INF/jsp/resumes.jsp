<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.common.js" defer></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.resumes.js" defer></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.freshen.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-md-">
    <div class="container-fluid row-cols-md-10">
        <div class="card border-dark">
            <div class="card-body pb-1" style="width:100%; background-color: #e5e7e7">
                <div class="container rounded-lg" style="width:90%;">
                    <div class="row justify-content-md-between align-items-center align-items-baseline">
                        <form class="col-sm-8 form-row needs-validation" id="filter">
                            <div class="col-md-4 mb-3 col-form-label">
                                <input class="form-control" type="text" name="language" id="language"
                                       list="language_name" style="width:98%;border:2px solid #0397ba"
                                       placeholder="<spring:message code="resume.print"/> language...">
                                <datalist id="language_name">
                                    <option value='all' selected>all</option>
                                    <option value='Java'>Java</option>
                                    <option value='Ruby'>Ruby</option>
                                    <option value='Kotlin'>Kotlin</option>
                                    <option value='Python'>Python</option>
                                    <option value='JavaScript'>JavaScript</option>
                                    <option value='TypeScript'>TypeScript</option>
                                    <option value='Php'>Php</option>
                                    <option value='React'>React</option>
                                    <option value='Angular'>Angular</option>
                                    <option value='Android'>Android</option>
                                    <option value='C++'>C++</option>
                                    <option value='C#'>C#</option>
                                    <option value='.Net'>.Net</option>
                                    <option value='Ios'>Ios</option>
                                    <option value='Node.js'>Node.js</option>
                                    <option value='Sql'>Sql</option>
                                    <option value='Goland'>Goland</option>
                                    <option value='Ruby on rails'>Ruby on rails</option>
                                    <option value='Scala'>Scala</option>
                                    <option value='Asw'>Asw</option>
                                    <option value='Azure'>Azure</option>
                                    <option value='Html&css'>Html&css</option>
                                    <option value='Html'>Html</option>
                                    <option value='css'>css</option>
                                    <option value='Spark'>Spark</option>
                                    <option value='React native'>React native</option>
                                    <option value='Vue.js'>Vue.js</option>
                                    <option value='Flutter'>Flutter</option>
                                    <option value='Elixir'>Elixir</option>
                                    <option value='Hadoop'>Hadoop</option>
                                </datalist>
                            </div>
                            <div class="col-md-4 mb-3 col-form-label">
                                <input class="form-control" type="text" name="level" id="level" list="level_name"
                                       style="width:101%;border:2px solid #0397ba"
                                       placeholder="<spring:message code="resume.print"/> level...">
                                <datalist id="level_name">
                                    <option value='all' selected><spring:message code="resume.experience_all"/>all</option>
                                    <option value='Trainee'><spring:message code="resume.experience_no"/></option>
                                    <option value='Junior'><spring:message code="resume.experience_1"/></option>
                                    <option value='Middle'><spring:message code="resume.experience_2"/></option>
                                    <option value='Senior'><spring:message code="resume.experience_3"/></option>
                                    <option value='Expert'><spring:message code="resume.experience_5"/></option>
                                </datalist>
                            </div>
                            <div class="col-md-4 mb-3 col-form-label">
                                <input class="form-control" type="text" name="workplace" id="workplace" list="city_name"
                                       style="width:101%;border:2px solid #0397ba"
                                       placeholder="<spring:message code="resume.print"/> workplace...">
                                <datalist id="city_name">
                                    <option value='all' selected>all</option>
                                    <option value='Киев'>Киев</option>
                                    <option value='Минск'>Минск</option>
                                    <option value='Львов'>Львов</option>
                                    <option value='Харьков'>Харьков</option>
                                    <option value='Днепр'>Днепр</option>
                                    <option value='Украина'>Украина</option>
                                    <option value='remote'>удаленно</option>
                                    <option value='foreign'>другие страны</option>
                                </datalist>
                            </div>
                        </form>
                        <div class="col-sm-2">
                            <button class="btn-sm btn-outline-info" onclick="updateFilteredTable()">
                                <span class="fa fa-filter"></span>
                                <th><spring:message code="resume.filter"/></th>
                            </button>
                        </div>
                        <div class="col-md-2 ml-md-auto">
                            <button class="btn-sm btn-outline-danger" onclick="clearFilter()">
                                <span class="fa fa-remove"></span>
                                <th><spring:message code="common.reset"/></th>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="card-body pb-0">
            <div class="row card-footer justify-content-between" style="width: 103%">
                <div class="col">
                    <sec:authorize access="hasRole('ADMIN')">
                    <button class="col-md-2 btn btn-primary mt-2" onclick="add()">
                        <span class="fa fa-plus text-left"></span>
                        <th><spring:message code="resume.add"/></th>
                    </button>
                    </sec:authorize>
                </div>
                <button class=" col-md-2 btn btn-info bs-popover-right mt-2" onclick="refreshDB()">
                    <span class="fa fa-refresh text-left pull-right"></span>
                    <th><spring:message code="resume.refresh_db"/></th>
                </button>
            </div>
        </div>
        <table class="table table-striped table-bordered" id="datatable" style="width: 100%">
            <div class="row">
                <thead>
                <tr style="text-align: center;">
                    <th hidden>id</th>
                    <th hidden>link</th>
                    <th class="col-auto" style="text-align: center;"><spring:message code="resume.position"/></th>
                    <th class="col-auto"><h7><spring:message code="resume.name"/></h7></th>
                    <th class="col-auto"><h7><spring:message code="resume.age"/></h7></th>
                    <th class="col-auto"><h7><spring:message code="resume.address"/></h7></th>
                    <th class="col-auto"><h7><spring:message code="resume.salary"/></h7></th>
                    <th class="col-auto" style="text-align: center;"><spring:message code="resume.skills"/></th>
                    <th class="col-auto" style="text-align: center;"><h7><spring:message code="resume.previous_experience"/></h7></th>
                    <th class="col-auto text-nowrap"><spring:message code="resume.date"/></th>
                    <th class="toVote"></th>
                    <th hidden>work place</th>
                    <th hidden>language</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
            </div>
        </table>
        <br>
    </div>
</div>
<sec:authorize access="hasRole('ADMIN')">
<%--add ResumeTo--%>
<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="resume.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>

            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">
                    <div class="form-group not_update_visible">
                        <label for="title" class="col-form-label"><spring:message code="resume.position"/></label>
                        <input type="text" class="form-control" id="title" name="title"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.position_"/>">
                    </div>
                    <div class="form-group not_update_visible">
                        <label for="name" class="col-form-label"><spring:message code="resume.name"/></label>
                        <input type="text" class="form-control" id="name" name="name"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.name_"/>">
                    </div>
                    <div class="form-group not_update_visible">
                        <label for="age" class="col-form-label"><spring:message code="resume.age"/></label>
                        <input type="text" class="form-control" id="age" name="age"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.age_"/>">
                    </div>
                    <div class="form-group not_update_visible">
                        <label for="address" class="col-form-label"><spring:message code="resume.address"/></label>
                        <input type="text" class="form-control" id="address" name="address"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.address_"/>">

                    </div>
                    <div class="form-group">
                        <label for="salary" class="col-form-label"><spring:message code="resume.salary_from"/></label>
                        <input type="number" class="form-control" id="salary" name="salary"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.salary_from"/>">
                    </div>
                    <div class="form-group">
                        <label for="skills" class="col-form-label"><spring:message code="resume.skills"/></label>
                        <input type="text" class="form-control" id="skills" name="skills"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.skills_"/>">
                    </div>
                    <div class="form-group">
                        <label for="workBefore" class="col-form-label"><spring:message code="resume.previous_experience"/></label>
                        <input type="text" class="form-control" id="workBefore" name="workBefore"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.previous_experience_"/>">
                    </div>
                    <div class="form-group">
                        <label for="url" class="col-form-label"><spring:message code="resume.url"/></label>
                        <input type="text" class="form-control" id="url" name="url"
                               placeholder="https://www.example.com">
                    </div>
                    <div class="form-group">
                        <input type="hidden" class="form-control" id="releaseDate" name="releaseDate">
                    </div>
                    <div class="form-group">
                        <label for="languageCode" class="col-form-label"><spring:message code="resume.language"/></label>
                        <input type="text" class="form-control" id="languageCode" name="language"
                               placeholder="<spring:message code="resume.print"/> <spring:message code="resume.language_"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.close"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>
<%--update RowResume--%>
<div class="modal fade" tabindex="-1" id="updateRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header box">
                <h4 class="modal-title"><spring:message code="resume.edit"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsUpdateForm">
                    <input type="hidden" id="idUpdate" name="id">
                    <div class="form-group not_update_visible">
                        <label for="title" class="col-form-label"><spring:message code="resume.position"/></label>
                        <input type="text" class="form-control" id="titleUpdate" name="title">
                    </div>
                    <div class="form-group not_update_visible">
                        <input type="hidden" class="form-control" id="nameUpdate" name="name">
                    </div>
                    <div class="form-group">
                        <label for="age" class="col-form-label"><spring:message code="resume.age"/></label>
                        <input type="text" class="form-control" id="ageUpdate" name="age">
                    </div>
                    <div class="form-group not_update_visible">
                        <input type="text" class="form-control" id="addressUpdate" name="address">
                    </div>
                    <div class="form-group">
                        <label for="salary" class="col-form-label"><spring:message code="resume.salary_from"/></label>
                        <input type="number" class="form-control" id="salaryUpdate" name="salary">
                    </div>
                    <div class="form-group">
                        <label for="skills" class="col-form-label"><spring:message code="resume.skills"/></label>
                        <input type="text" class="form-control" id="skillsUpdate" name="skills">
                    </div>
                    <div class="form-group not_update_visible">
                        <label for="workBefore" class="col-form-label"><spring:message code="resume.previous_experience"/></label>
                        <input type="text" class="form-control" id="lastWorkUpdate" name="workBefore">
                    </div>
                    <div class="form-group">
                        <label for="url" class="col-form-label"><spring:message code="resume.url"/></label>
                        <input type="text" class="form-control" id="urlUpdate" name="url"
                               placeholder="https://www.example.com">
                    </div>
                    <div class="form-group">
                        <input type="hidden" class="form-control" id="releaseDateUpdate" name="releaseDate">
                    </div>
                    <div class="form-group">
                        <input type="hidden" class="form-control" id="languageCodeUpdate" name="language">
                    </div>
                    <div class="form-group">
                        <input type="hidden" class="form-control" id="workplaceUpdate" name="workplace">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.close"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="updateResumeTo()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>
<%--delete RowResume--%>
<div class="modal fade" tabindex="-1" id="deleteRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header box" style="text-align: center;">
                <h6><em><spring:message code="common.confirm_label"/></em></h6>
            </div>
            <div class="modal-body">
                <form id="detailsDeleteForm">
                    <label for="idDelete" ><em><spring:message code="common.confirm"/></em></label>
                    <input type="hidden" id="idDelete" name="id">
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.close"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="deleteResumeTo()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.delete"/>
                </button>
            </div>
        </div>
    </div>
</div>
</sec:authorize>
<%--refresh--%>
<div class="modal fade" tabindex="-1" id="refreshRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header box">
                <h5 class="modal-title"><spring:message code="resume.refresh_db_by"/></h5>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsRefreshForm">
                    <div class="form-group">
                        <label type="hidden" for="recordedDate" class="col-form-label"></label>
                        <input type="hidden" class="form-control" id="recordedDate" name="recordedDate">
                    </div>
                    <div class="form-group">
                        <label for="languageTask">
                            <h7 class="btn-outline-info"><em><spring:message code="resume.select_or_print_your"/> language...</em></h7>
                        </label>
                        <input class="form-control" type="text" name="language" id="languageTask" list="language_name_2"
                               placeholder="<spring:message code="resume.please_enter_language"/>">
                        <datalist id="language_name_2">
                            <option value='all' selected>all</option>
                            <option value='Java'>Java</option>
                            <option value='Ruby'>Ruby</option>
                            <option value='Kotlin'>Kotlin</option>
                            <option value='Python'>Python</option>
                            <option value='JavaScript'>JavaScript</option>
                            <option value='TypeScript'>TypeScript</option>
                            <option value='Php'>Php</option>
                            <option value='React'>React</option>
                            <option value='Angular'>Angular</option>
                            <option value='Android'>Android</option>
                            <option value='C++'>C++</option>
                            <option value='C#'>C#</option>
                            <option value='.Net'>.Net</option>
                            <option value='Ios'>Ios</option>
                            <option value='Node.js'>Node.js</option>
                            <option value='Sql'>Sql</option>
                            <option value='Goland'>Goland</option>
                            <option value='Ruby on rails'>Ruby on rails</option>
                            <option value='Scala'>Scala</option>
                            <option value='Asw'>Asw</option>
                            <option value='Azure'>Azure</option>
                            <option value='Html&css'>Html&css</option>
                            <option value='css'>css</option>
                            <option value='Html'>Html</option>
                            <option value='Spark'>Spark</option>
                            <option value='React native'>React native</option>
                            <option value='Vue.js'>Vue.js</option>
                            <option value='Flutter'>Flutter</option>
                            <option value='Elixir'>Elixir</option>
                            <option value='Hadoop'>Hadoop</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label for="levelTask">
                            <h7 class="btn-outline-info"><em>level...</em></h7>
                        </label>
                        <input class="form-control" type="text" name="level" id="levelTask" list="level_2"
                               placeholder="<spring:message code="resume.please_enter_level"/>">
                        <datalist id="level_2">
                            <option value='Trainee'><spring:message code="resume.experience_no"/></option>
                            <option value='Junior'><spring:message code="resume.experience_1"/></option>
                            <option value='Middle'><spring:message code="resume.experience_2"/></option>
                            <option value='Senior'><spring:message code="resume.experience_3"/></option>
                            <option value='Expert'><spring:message code="resume.experience_5"/></option>
                            <option value='all'><spring:message code="resume.experience_all"/>all</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label for="workplaceTask">
                            <h7 class="btn-outline-info"><em><spring:message code="resume.select_or_print_your"/> location...</em></h7>
                        </label>
                        <input class="form-control" type="text" name="workplace" id="workplaceTask" list="city_name_2"
                               placeholder="<spring:message code="resume.please_enter_workplace"/>">
                        <datalist id="city_name_2">
                            <option value='Киев'>Киев</option>
                            <option value='Минск'>Минск</option>
                            <option value='Львов'>Львов</option>
                            <option value='Харьков'>Харьков</option>
                            <option value='Днепр'>Днепр</option>
                            <option value='Украина'>Украина</option>
                            <option value='remote'>удаленно</option>
                            <option value='foreign'>другие страны</option>
                            <option value='all'>all</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label type="hidden" for="userId" class="col-form-label"></label>
                        <input type="hidden" class="form-control" id="userId" name="userId">
                    </div>
                </form>
            </div>
            <span class="d-flex justify-content-center" id="spinner2" style="visibility: hidden">
                <h7><em>и<spring:message code="resume.wait"/></em><h6 class="fa fa-filter text-info"> <spring:message code="resume.filter"/></h6>
                </h7>
            </span>
            <h7 class="modal-title"></h7>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.close"/>
                </button>
                <button type="button" class="btn btn-info" onclick="sendRefresh()">
                    <spring:message code="resume.refresh"/>
                    <span class="spinner-border spinner-border-sm" id="spinner1" style="visibility: hidden"></span>
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
