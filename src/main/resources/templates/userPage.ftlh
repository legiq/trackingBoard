<#import "parts/common.ftlh" as c>

<@c.page>
    User editor
    <div>Id: ${id}</div>
    <form method="post">
        <div>Name: <input type="text" name="username" value="${username}"/></div>
        <div>Password: <input type="text" name="password" value="${password}"/></div>
        <#list roles as role>
            <#if userRole = role>
                <div class="custom-control custom-radio">
                    <input type="radio" id="${role}" name="roleRadio" class="custom-control-input" value="${role}" checked/>
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
    <form method="post" action="/user/delete">
        <input type="hidden" name="deleteId" value="${id}"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Delete</button>
    </form>
</@c.page>