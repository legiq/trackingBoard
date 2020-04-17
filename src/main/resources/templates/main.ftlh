<#import "parts/common.ftlh" as c>

<@c.page>
    <div>
        <form method="post">
            <input type="text" name="login" placeholder="Enter login">
            <input type="text" name="password" placeholder="Enter password">
            <input type="text" name="role" placeholder="Enter role">
            <button type="submit">Save</button>
        </form>
    </div>
    <div>
        <form method="get">
            <#list users as user>
                <div>${user}</div>
            </#list>
        </form>
    </div>
    <a href="/task">Task</a>
</@c.page>
