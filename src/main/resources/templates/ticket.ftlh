<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="list-group-item list-group-item-action ">${ticket.id}</div>
    <div class="list-group-item list-group-item-action ">${ticket.label}</div>
    <div class="list-group-item list-group-item-action ">${ticket.description}</div>
    <div class="list-group-item list-group-item-action ">${ticket.creator}</div>
    <div class="list-group-item list-group-item-action "><#list ticket.executors as executor>
            <div>${executor}</div>
        </#list>
    </div>
    <div class="list-group-item list-group-item-action ">${ticket.type}</div>
    <div class="list-group-item list-group-item-action ">${ticket.status}</div>
    <div class="list-group-item list-group-item-action "><#list ticket.components as component>
            <div>${component}</div>
        </#list>
    </div>
    <div class="list-group-item list-group-item-action ">${ticket.time}</div>
</@c.page>
