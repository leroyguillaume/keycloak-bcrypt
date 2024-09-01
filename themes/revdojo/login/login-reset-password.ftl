<#import "template.ftl" as layout>
    <@layout.registrationLayout displayInfo=social.displayInfo; section>
        <#if section=="form">
            <div class="login-container">
                <div class="login-card">
                    <img src="https://webinarinc-central.s3.us-west-1.amazonaws.com/public/company_logo/revdojo_logo.png" alt="RevDojo Logo" class="revdojo-logo">
                    <div class="login-card__title">Reset Password</div>
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
                        <form id="resetPasswordForm" action="${url.loginAction}" method="post">
                            <input type="email" id="username" class="login-card__input" placeholder="Email" name="username" tabindex="1">
                            <div id="email-error" class="error-message" style="display: none; color: red; margin-bottom: 20px;"></div>
                            <button type="submit" class="login-card__button">Send Reset Link</button>
                            <button class="sign-up-card__button" onclick="window.location.href = '${client.baseUrl}'; return false;" formnovalidate>Go back to Login</button>
                        </form>
                    </div>
                </div>
            </div>
            <script>
            document.getElementById('resetPasswordForm').addEventListener('submit', function(event) {
                event.preventDefault();
                const emailField = document.getElementById('username');
                const emailError = document.getElementById('email-error');
                const email = emailField.value;

                // Clear any previous error message
                emailError.style.display = 'none';
                emailError.textContent = '';

                // Check if the email is valid (simple regex check)
                if (!email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
                    emailError.textContent = 'Please enter a valid email address.';
                    emailError.style.display = 'block';
                    return;
                }

                // Make an AJAX request to check if the email exists
                fetch('https://central-staging.webinarinc.com/api/verify-email', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email: email })
                })
                .then(response => response.json())
                .then(data => {
                    console.log(data)
                    if (data.exists) {
                        // If email exists, submit the form
                        document.getElementById('resetPasswordForm').submit();
                    } else {
                        // If email does not exist, show an error message
                        emailError.textContent = 'This email does not exist.';
                        emailError.style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('Error checking email:', error);
                    emailError.textContent = 'An error occurred while checking the email. Please try again later.';
                    emailError.style.display = 'block';
                });
            });
            </script>
        </#if>
    </@layout.registrationLayout>
