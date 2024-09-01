<#import "template.ftl" as layout>
    <@layout.registrationLayout displayInfo=social.displayInfo; section>
        <#if section=="form">
            <style>
            .password-wrapper {
                position: relative;
            }

            .login-card__input {
                width: 100%;
                padding-right: 40px;
                /* Adjust space for the icon */
            }

            .toggle-password-icon {
                position: absolute;
                right: 10px;
                top: 35%;
                transform: translateY(-50%);
                cursor: pointer;
                font-size: 20px;
                color: #000;
                /* Adjust color as needed */
            }
            </style>
            <div class="login-container">
                <div class="login-card">
                    <img src="https://webinarinc-central.s3.us-west-1.amazonaws.com/public/company_logo/revdojo_logo.png" alt="RevDojo Logo" class="revdojo-logo">
                    <div class="login-card__title">Welcome</div>
                    <!-- Message Display Code -->
                    <#if message?has_content>
                        <div class="alert alert-${message.type}">
                            <#if message.type=='success'><span class="${properties.kcFeedbackSuccessIcon!}"></span></#if>
                            <#if message.type=='warning'><span class="${properties.kcFeedbackWarningIcon!}"></span></#if>
                            <#if message.type=='error'><span class="${properties.kcFeedbackErrorIcon!}"></span></#if>
                            <#if message.type=='info'><span class="${properties.kcFeedbackInfoIcon!}"></span></#if>
                            <div class="v-alert v-theme--light text-error v-alert--density-compact v-alert--variant-tonal text-caption" role="alert" hide-icon="" style="margin-bottom:15px">
                                <!---->
                                <span class="v-alert__underlay"></span>
                                <!---->
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
                            <input type="email" id="username" class="login-card__input" placeholder="Email" name="username" tabindex="1">
                            <div class="password-wrapper">
                                <input type="password" id="password" class="login-card__input" placeholder="${msg("password")}" name="password" tabindex="2">
                                <span id="togglePassword" class="toggle-password-icon">
                                    <i class="mdi mdi-eye-off-outline"></i>
                                </span>
                            </div>
                            <div class="forgot-password">
                                <a href="${url.loginResetCredentialsUrl}" class="reset-password-link">
                                    ${msg("doForgotPassword")}
                                </a>
                            </div>
                            <button type="submit" class="login-card__button">
                                ${msg("doLogIn")}
                            </button>
                            <button class="sign-up-card__button" onclick="window.location.href = 'https://shop.revdojo.com/'; return false;">Sign up</button>
                        </form>
                    </div>
                </div>
            </div>
            <script>
            document.getElementById("togglePassword").addEventListener("click", function() {
                const passwordField = document.getElementById("password");
                const icon = this.querySelector("i");
                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    icon.classList.remove("mdi-eye-off-outline");
                    icon.classList.add("mdi-eye-outline");
                } else {
                    passwordField.type = "password";
                    icon.classList.remove("mdi-eye-outline");
                    icon.classList.add("mdi-eye-off-outline");
                }
            });
            window.onload = function() {
                localStorage.clear();
                sessionStorage.clear();
            };
            </script>
        </#if>
    </@layout.registrationLayout>