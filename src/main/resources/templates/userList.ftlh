<#import "parts/common.ftlh" as c>

<@c.page>
    <#list users as user>
        <div class="list-group">
            <a href="/user/${user.id}" class="list-group-item list-group-item-action m-2">${user.username}</a>
        </div>
    </#list>
</@c.page>