<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <div class="form-group col-md-6">
            <form method="post" action="/filter" class="form-inline">
                <input type="text" name="filterByType" class="form-control" value="${filterByType!}"
                       placeholder="Type"/>
                <input type="text" name="filterByTime" class="form-control" value="${filterByTime!}"
                       placeholder="Time"/>
                <input type="text" name="filterByCreator" class="form-control" value="${filterByCreator!}"
                       placeholder="Creator"/>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button"
           aria-expanded="false"
           aria-controls="collapseExample">
            Add new ticket
        </a>
        <div class="collapse" id="collapseExample">
            <div class="form-group mt-3">
                <form method="post" action="/main">
                    <div class="form-group float-left">
                        <input type="text" name="label" placeholder="Enter label"/>
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="description" placeholder="Enter description"/>
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="executorLogin" placeholder="Enter executors"/>
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="type" placeholder="Enter type"/>
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="status" placeholder="Enter status"/>
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="components" placeholder="Enter components"/>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary ml-2">Save</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="float-left m-3">
        <table class="table table-bordered table-striped m-2 fixed">
            <col width="200px"/>
            <tr>
                <th scope="col">
                    <div class="form-group">
                        <label>ToDo</label>
                    </div>
                </th>
            </tr>
            <tbody>
            <tr>
                <td>
                    <#list tickets as ticket>
                        <#if ticket.status = "ToDo">
                            <div class="form-group" id="ticket" data-id="${ticket.id}">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name || isAdmin>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticketId" value="${ticket.id}"/>
                                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-primary">Delete</button>
                                        </form>
                                    </div>
                                </#if>
                            </div>
                        </#if>
                    </#list>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="float-left m-3">
        <table class="table table-bordered table-striped m-2 fixed">
            <col width="200px"/>
            <tr>
                <th scope="col">
                    <div class="form-group">
                        <label>InProgress</label>
                    </div>
                </th>
            </tr>
            <tbody>
            <tr>
                <td>
                    <#list tickets as ticket>
                        <#if ticket.status = "InProgress">
                            <div class="form-group" id="ticket" data-id="${ticket.id}">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name || isAdmin>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticketId" value="${ticket.id}"/>
                                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-primary">Delete</button>
                                        </form>
                                    </div>
                                </#if>
                            </div>
                        </#if>
                    </#list>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="float-left m-3">
        <table class="table table-bordered table-striped m-2 fixed">
            <col width="200px"/>
            <tr>
                <th scope="col">
                    <div class="form-group">
                        <label>InTest</label>
                    </div>
                </th>
            </tr>
            <tbody>
            <tr>
                <td>
                    <#list tickets as ticket>
                        <#if ticket.status = "InTest">
                            <div class="form-group" id="ticket" data-id="${ticket.id}">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name || isAdmin>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticketId" value="${ticket.id}"/>
                                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-primary">Delete</button>
                                        </form>
                                    </div>
                                </#if>
                            </div>
                        </#if>
                    </#list>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="float-left m-3">
        <table class="table table-bordered table-striped m-2 fixed">
            <col width="200px"/>
            <tr>
                <th scope="col">
                    <div class="form-group">
                        <label>Done</label>
                    </div>
                </th>
            </tr>
            <tbody>
            <tr>
                <td>
                    <#list tickets as ticket>
                        <#if ticket.status = "Done">
                            <div class="form-group" id="ticket" data-id="${ticket.id}">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name || isAdmin>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticketId" value="${ticket.id}"/>
                                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                            <button type="submit" class="btn btn-primary">Delete</button>
                                        </form>
                                    </div>
                                </#if>
                            </div>
                        </#if>
                    </#list>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</@c.page>
