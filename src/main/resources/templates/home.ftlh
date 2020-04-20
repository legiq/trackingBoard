<#import "parts/common.ftlh" as c>

<@c.page>
    <div><a href="/main">Main</a></div>
    <div><a href="/task">Task</a></div>
    <#list users as user>
        <div>${user}</div>
    </#list>
</@c.page>