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
        </form>
    </div>
    <div>
        <#list tickets as ticket>
            <div class="list-group">
                <div class="list-group-item list-group-item-action m-2">${ticket}</div>
            </div>
        </#list>
    </div>
    <a href="/task">Task</a>
</@c.page>
