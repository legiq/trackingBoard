<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button"
           aria-expanded="false"
           aria-controls="collapseExample">
            Add new ticket
        </a>
        <div class="collapse" id="collapseExample">
            <div class="form-group mt-3">
                <form method="post">
                    <div class="form-group float-left">
                        <input type="text" name="label" placeholder="Enter label">
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="description" placeholder="Enter description">
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="executor_login" placeholder="Enter executor login">
                    </div>
                    <#if user.role = "Developer">
                        <div class="form-group float-left">
                            <input type="text" name="type" placeholder="Enter type">
                        </div>
                    <#else>
                        <input type="hidden" name="type" value="Bug">
                    </#if>
                    <div class="form-group float-left">
                        <input type="text" name="status" placeholder="Enter status">
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="components" placeholder="Enter components">
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
                            <div class="form-group">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticket_id" value="${ticket.id}">
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
                            <div class="form-group">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticket_id" value="${ticket.id}">
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
                            <div class="form-group">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticket_id" value="${ticket.id}">
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
                            <div class="form-group">
                                <a href="/ticket/${ticket.id}">${ticket.label}</a>
                                <#if ticket.creator.username = name>
                                    <div>
                                        <form method="post" action="/delete">
                                            <input type="hidden" name="ticket_id" value="${ticket.id}">
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
