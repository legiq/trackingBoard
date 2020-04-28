<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
    <div class="list-group-item list-group-item-action ">
        <div>Id: ${ticket.id}</div>
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
    <#if ticket.type = "Story">
        <div class="list-group-item list-group-item-action ">
            <#list subTickets as subTicket>
                <div><a href="/ticket/${subTicket.id}">${subTicket.label}</a></div>
            </#list>
            <form method="post" action="/ticket/addSubTicket">
                <input type="text" name="subTicketId" placeholder="Enter ticket id">
                <input type="hidden" name="ticketId" value="${ticket.id}">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary ml-2">Add</button>
            </form>
        </div>
    </#if>
    <div class="list-group-item list-group-item-action ">Status: ${ticket.status}</div>
    <div class="list-group-item list-group-item-action ">
        <div>Components:</div>
        <#list ticket.components as component>
            <div>${component}</div>
        </#list>
    </div>
    <#if ticket.status != "Done" && ticket.status != "InTest" && ticket.isExecutor(user)>
        <form method="post" action="/ticket/forward">
            <input type="hidden" name="ticketId" value="${ticket.id}">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary float-right mt-2">Move forward--></button>
        </form>
    <#elseif ticket.status = "InTest" && user.role != "Developer">
        <form method="post" action="/ticket/forward">
            <input type="hidden" name="ticketId" value="${ticket.id}">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary float-right mt-2">Confirm--></button>
        </form>
        <form method="post" action="/ticket/reopen">
            <input type="hidden" name="ticketId" value="${ticket.id}">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary float-left mt-2"><--Reopen</button>
        </form>
        <a class="btn btn-primary mt-2 ml-2" data-toggle="collapse" href="#collapse" role="button"
           aria-expanded="false"
           aria-controls="collapseExample">
            Create bug
        </a>
        <div class="collapse" id="collapse">
            <div class="form-group mt-3">
                <form method="post" action="/ticket/bug">
                    <div class="form-group float-left">
                        <input type="text" name="label" placeholder="Enter label">
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="description" placeholder="Enter description">
                    </div>
                    <div class="form-group float-left">
                        <input type="text" name="executorLogin" placeholder="Enter executors">
                    </div>
                    <input type="hidden" name="type" value="Bug">
                    <input type="hidden" name="status" value="ToDo">
                    <input type="hidden" name="ticketId" value="${ticket.id}">
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group float-left">
                        <input type="text" name="components" placeholder="Enter components">
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Create</button>
                    </div>
                </form>
            </div>
        </div>
    </#if>
</@c.page>
