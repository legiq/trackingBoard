<#import "parts/common.ftlh" as c>

<@c.page>
    <#list users as user>
        <div>${user}</div>
    </#list>
</@c.page>