<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <div>Add new ticket</div>
        <form method="post">
            <input type="text" name="label" placeholder="Enter label">
            <input type="text" name="description" placeholder="Enter description">
            <input type="text" name="creator_login" placeholder="Enter creator login">
            <input type="text" name="executor_login" placeholder="Enter executor login">
            <input type="text" name="type" placeholder="Enter type">
            <input type="text" name="status" placeholder="Enter status">
            <input type="text" name="components" placeholder="Enter components in format '1 2 3'">
            <button type="submit">Save</button>
        </form>
    </div>
    <div>
        <form method="get">
            <#list tickets as ticket>
                <div>${ticket}</div>
            </#list>
        </form>
    </div>
    <a href="/task">Task</a>
</@c.page>
