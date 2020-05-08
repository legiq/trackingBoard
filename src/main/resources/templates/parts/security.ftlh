<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.username
    role = user.role
    isAdmin = user.isAdmin()
    >
<#else>
    <#assign
    name = "unknown"
    role = "unknown"
    isAdmin = false
    >
</#if>