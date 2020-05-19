<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Login :</label>
            <div class="col-sm-4">
                <input type="text" name="username" class="form-control" placeholder="login"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-4">
                <input type="password" name="password" class="form-control" placeholder="password"/>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="custom-control custom-radio">
                <input type="radio" id="Developer" name="role" class="custom-control-input" value="Developer"/>
                <label class="custom-control-label" for="Developer">Developer</label>
            </div>
            <div class="custom-control custom-radio">
                <input type="radio" id="QA" name="role" class="custom-control-input" value="QA"/>
                <label class="custom-control-label" for="QA">QA</label>
            </div>
            <div class="custom-control custom-radio">
                <input type="radio" id="BA" name="role" class="custom-control-input" value="BA"/>
                <label class="custom-control-label" for="BA">BA</label>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
        <button type="submit" class="btn btn-primary">
            <#if isRegisterForm>Create<#else>Sign in</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Sign Out</button>
    </form>
</#macro>