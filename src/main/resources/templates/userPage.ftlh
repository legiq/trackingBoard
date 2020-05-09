<#import "parts/common.ftlh" as c>

<@c.page>
    User editor
    <div>Id: ${targetUser.id}</div>
    <form method="post">
        <div>Name: <input type="text" name="username" value="${targetUser.username}"/></div>
        <div>Password: <input type="text" name="password" value="${targetUser.password}"/></div>
        <#list roles as role>
            <#if targetUser.role = role>
                <div class="custom-control custom-radio">
                    <input type="radio" id="${role}" name="roleRadio" class="custom-control-input" value="${role}"
                           checked/>
                    <label class="custom-control-label" for="${role}">${role}</label>
                </div>
            <#else>
                <div class="custom-control custom-radio">
                    <input type="radio" id="${role}" name="roleRadio" class="custom-control-input" value="${role}"/>
                    <label class="custom-control-label" for="${role}">${role}</label>
                </div>
            </#if>
        </#list>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Save</button>
    </form>
    <#if targetUser.isEnabled()>
        <form method="post" action="/user/block">
            <input type="hidden" name="disableId" value="${targetUser.id}"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Block</button>
        </form>
    <#else>
        <form method="post" action="/user/unblock">
            <input type="hidden" name="enableId" value="${targetUser.id}"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Unblock</button>
        </form>
    </#if>
</@c.page>