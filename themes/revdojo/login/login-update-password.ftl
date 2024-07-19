<#import "template.ftl" as layout>
    <@layout.registrationLayout displayInfo=social.displayInfo; section>
        <#if section=="title">
            <#elseif section=="header">
                <#elseif section=="form">
                    <div class="login-container">
                        <div class="login-card">
                            <img src="https://webinarinc-central.s3.us-west-1.amazonaws.com/public/company_logo/revdojo_logo.png" alt="RevDojo Logo" class="revdojo-logo">
                            <div class="login-card__title">Update Password</div>
                            <!-- Message Display Code -->
                            <#if message?has_content>
                                <div class="alert alert-${message.type}">
                                    <#if message.type=='success'><span class="${properties.kcFeedbackSuccessIcon!}"></span></#if>
                                    <#if message.type=='warning'><span class="${properties.kcFeedbackWarningIcon!}"></span></#if>
                                    <#if message.type=='error'><span class="${properties.kcFeedbackErrorIcon!}"></span></#if>
                                    <#if message.type=='info'><span class="${properties.kcFeedbackInfoIcon!}"></span></#if>
                                    <div class="v-alert v-theme--light text-error v-alert--density-compact v-alert--variant-tonal text-caption" role="alert" hide-icon="" style="margin-bottom:15px">
                                        <span class="v-alert__underlay"></span>
                                        <div class="v-alert__prepend">
                                            <i class="<#if message.type == 'success'>mdi-check-circle</#if>
                                          <#if message.type == 'warning'>mdi-alert</#if>
                                          <#if message.type == 'error'>mdi-close-circle</#if>
                                          <#if message.type == 'info'>mdi-information</#if> mdi v-icon notranslate v-theme--light" aria-hidden="true" density="compact" style="font-size: 28px; height: 28px; width: 28px;"></i>
                                        </div>
                                        <div class="v-alert__content">
                                            <span class="message-text">
                                                ${message.summary}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                            <div class="login-card__content">
                                <form action="${url.loginAction}" method="post">
                                    <input type="text" readonly value="this is not a login form" style="display: none;">
                                    <input type="password" readonly value="this is not a login form" style="display: none;">
                                    <div class="login-card__input-group">
                                        <label for="password-new" class="login-card__label">
                                            ${msg("passwordNew")}
                                        </label>
                                        <input type="password" id="password-new" name="password-new" class="login-card__input" autofocus autocomplete="off" />
                                    </div>
                                    <div class="login-card__input-group">
                                        <label for="password-confirm" class="login-card__label">
                                            ${msg("passwordConfirm")}
                                        </label>
                                        <input type="password" id="password-confirm" name="password-confirm" class="login-card__input" autocomplete="off" />
                                    </div>
                                    <div class="login-card__button-group">
                                        <input class="login-card__button" type="submit" value="${msg("doSubmit")}" />
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <script>
                    function handleLoginClick(event) {
                        event.preventDefault();
                        window.location.href = '${client.baseUrl}';
                    }
                    </script>
        </#if>
    </@layout.registrationLayout>