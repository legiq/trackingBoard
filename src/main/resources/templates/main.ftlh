<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div>
        <@l.logout />
        <div>Add new ticket</div>
        <form method="post">
            <input type="text" name="label" placeholder="Enter label">
            <input type="text" name="description" placeholder="Enter description">
            <input type="text" name="creator_id" placeholder="Enter creator id">
            <input type="text" name="executor_id" placeholder="Enter executor id">
            <input type="text" name="type" placeholder="Enter type">
            <input type="text" name="status" placeholder="Enter status">
            <input type="text" name="components" placeholder="Enter components in format '1 2 3'">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Save</button>
            <div>
                <a href="/task">Task</a>
            </div>
        </form>
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
                                <div>${ticket.label}</div>
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
                                <div>${ticket.label}</div>
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
                                <div>${ticket.label}</div>
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
                                <div>${ticket.label}</div>
                            </div>
                        </#if>
                    </#list>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</@c.page>
