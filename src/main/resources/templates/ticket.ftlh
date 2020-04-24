<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="list-group-item list-group-item-action ">
        <div>Label: ${ticket.label}</div>
        <div>Date: ${ticket.time}</div>
        <div>Creator: ${ticket.creator.username}</div>
    </div>
    <div class="list-group-item list-group-item-action ">
        <#if ticket.creator.id = user.id>
            <form method="post">
                <div>Description: <input type="text" name="description" value="${ticket.description}"></div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Edit</button>
            </form>
        <#else>
            <div>${ticket.description}</div>
        </#if>
    </div>
    <div class="list-group-item list-group-item-action ">
        <div>Executors:</div>
        <#list ticket.executors as executor>
            <div>${executor.username}</div>
        </#list>
        <#if ticket.creator.id = user.id>
            <form method="post" action="/ticket/add">
                <input type="text" name="username" placeholder="Enter new executor">
                <input type="hidden" name="ticketId" value="${ticket.id}">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Add</button>
            </form>
        </#if>
    </div>
    <div class="list-group-item list-group-item-action ">Type: ${ticket.type}</div>
    <div class="list-group-item list-group-item-action ">Status: ${ticket.status}</div>
    <div class="list-group-item list-group-item-action ">
        <div>Components:</div>
        <#list ticket.components as component>
            <div>${component}</div>
        </#list>
    </div>

</@c.page>
