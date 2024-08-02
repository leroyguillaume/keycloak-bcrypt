<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <link rel="icon" href="https://webinarinc-central.s3.us-west-1.amazonaws.com/public/company_logo/revdojo_favicon.ico">
        <title>RevDojo Login</title>
        <script defer="defer" src="https://webinarinc-central.s3.us-west-1.amazonaws.com/okta-login/revdojo-release3.5.0/dist/js/chunk-vendors.5d3a0ff8.js"></script>
        <script defer="defer" src="https://webinarinc-central.s3.us-west-1.amazonaws.com/okta-login/revdojo-release3.5.0/dist/js/app.45c863af.js"></script>
        <link href="https://webinarinc-central.s3.us-west-1.amazonaws.com/okta-login/revdojo-release3.5.0/dist/css/app.57038db0.css" rel="stylesheet">
        <link href="https://webinarinc-central.s3.us-west-1.amazonaws.com/okta-login/revdojo-release3.5.0/dist/css/chunk-vendors.9e311aa4.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/@mdi/font@5.x/css/materialdesignicons.min.css" rel="stylesheet">
        <style type="text/css" id="vuetify-theme-stylesheet">
        :root {
            --v-theme-background: 255, 255, 255;
            --v-theme-background-overlay-multiplier: 1;
            --v-theme-surface: 255, 255, 255;
            --v-theme-surface-overlay-multiplier: 1;
            --v-theme-surface-variant: 66, 66, 66;
            --v-theme-surface-variant-overlay-multiplier: 2;
            --v-theme-on-surface-variant: 238, 238, 238;
            --v-theme-primary: 98, 0, 238;
            --v-theme-primary-overlay-multiplier: 2;
            --v-theme-primary-darken-1: 55, 0, 179;
            --v-theme-primary-darken-1-overlay-multiplier: 2;
            --v-theme-secondary: 3, 218, 198;
            --v-theme-secondary-overlay-multiplier: 1;
            --v-theme-secondary-darken-1: 1, 135, 134;
            --v-theme-secondary-darken-1-overlay-multiplier: 1;
            --v-theme-error: 176, 0, 32;
            --v-theme-error-overlay-multiplier: 2;
            --v-theme-info: 33, 150, 243;
            --v-theme-info-overlay-multiplier: 1;
            --v-theme-success: 76, 175, 80;
            --v-theme-success-overlay-multiplier: 1;
            --v-theme-warning: 251, 140, 0;
            --v-theme-warning-overlay-multiplier: 1;
            --v-theme-on-background: 0, 0, 0;
            --v-theme-on-surface: 0, 0, 0;
            --v-theme-on-primary: 255, 255, 255;
            --v-theme-on-primary-darken-1: 255, 255, 255;
            --v-theme-on-secondary: 0, 0, 0;
            --v-theme-on-secondary-darken-1: 255, 255, 255;
            --v-theme-on-error: 255, 255, 255;
            --v-theme-on-info: 255, 255, 255;
            --v-theme-on-success: 255, 255, 255;
            --v-theme-on-warning: 255, 255, 255;
            --v-border-color: 0, 0, 0;
            --v-border-opacity: 0.12;
            --v-high-emphasis-opacity: 0.87;
            --v-medium-emphasis-opacity: 0.6;
            --v-disabled-opacity: 0.38;
            --v-idle-opacity: 0.04;
            --v-hover-opacity: 0.04;
            --v-focus-opacity: 0.12;
            --v-selected-opacity: 0.08;
            --v-activated-opacity: 0.12;
            --v-pressed-opacity: 0.12;
            --v-dragged-opacity: 0.08;
            --v-theme-kbd: 33, 37, 41;
            --v-theme-on-kbd: 255, 255, 255;
            --v-theme-code: 245, 245, 245;
            --v-theme-on-code: 0, 0, 0;
        }

        .v-theme--light {
            color-scheme: normal;
            --v-theme-background: 255, 255, 255;
            --v-theme-background-overlay-multiplier: 1;
            --v-theme-surface: 255, 255, 255;
            --v-theme-surface-overlay-multiplier: 1;
            --v-theme-surface-variant: 66, 66, 66;
            --v-theme-surface-variant-overlay-multiplier: 2;
            --v-theme-on-surface-variant: 238, 238, 238;
            --v-theme-primary: 98, 0, 238;
            --v-theme-primary-overlay-multiplier: 2;
            --v-theme-primary-darken-1: 55, 0, 179;
            --v-theme-primary-darken-1-overlay-multiplier: 2;
            --v-theme-secondary: 3, 218, 198;
            --v-theme-secondary-overlay-multiplier: 1;
            --v-theme-secondary-darken-1: 1, 135, 134;
            --v-theme-secondary-darken-1-overlay-multiplier: 1;
            --v-theme-error: 176, 0, 32;
            --v-theme-error-overlay-multiplier: 2;
            --v-theme-info: 33, 150, 243;
            --v-theme-info-overlay-multiplier: 1;
            --v-theme-success: 76, 175, 80;
            --v-theme-success-overlay-multiplier: 1;
            --v-theme-warning: 251, 140, 0;
            --v-theme-warning-overlay-multiplier: 1;
            --v-theme-on-background: 0, 0, 0;
            --v-theme-on-surface: 0, 0, 0;
            --v-theme-on-primary: 255, 255, 255;
            --v-theme-on-primary-darken-1: 255, 255, 255;
            --v-theme-on-secondary: 0, 0, 0;
            --v-theme-on-secondary-darken-1: 255, 255, 255;
            --v-theme-on-error: 255, 255, 255;
            --v-theme-on-info: 255, 255, 255;
            --v-theme-on-success: 255, 255, 255;
            --v-theme-on-warning: 255, 255, 255;
            --v-border-color: 0, 0, 0;
            --v-border-opacity: 0.12;
            --v-high-emphasis-opacity: 0.87;
            --v-medium-emphasis-opacity: 0.6;
            --v-disabled-opacity: 0.38;
            --v-idle-opacity: 0.04;
            --v-hover-opacity: 0.04;
            --v-focus-opacity: 0.12;
            --v-selected-opacity: 0.08;
            --v-activated-opacity: 0.12;
            --v-pressed-opacity: 0.12;
            --v-dragged-opacity: 0.08;
            --v-theme-kbd: 33, 37, 41;
            --v-theme-on-kbd: 255, 255, 255;
            --v-theme-code: 245, 245, 245;
            --v-theme-on-code: 0, 0, 0;
        }

        .v-theme--dark {
            color-scheme: dark;
            --v-theme-background: 18, 18, 18;
            --v-theme-background-overlay-multiplier: 1;
            --v-theme-surface: 33, 33, 33;
            --v-theme-surface-overlay-multiplier: 1;
            --v-theme-surface-variant: 189, 189, 189;
            --v-theme-surface-variant-overlay-multiplier: 2;
            --v-theme-on-surface-variant: 66, 66, 66;
            --v-theme-primary: 187, 134, 252;
            --v-theme-primary-overlay-multiplier: 2;
            --v-theme-primary-darken-1: 55, 0, 179;
            --v-theme-primary-darken-1-overlay-multiplier: 1;
            --v-theme-secondary: 3, 218, 197;
            --v-theme-secondary-overlay-multiplier: 2;
            --v-theme-secondary-darken-1: 3, 218, 197;
            --v-theme-secondary-darken-1-overlay-multiplier: 2;
            --v-theme-error: 207, 102, 121;
            --v-theme-error-overlay-multiplier: 2;
            --v-theme-info: 33, 150, 243;
            --v-theme-info-overlay-multiplier: 2;
            --v-theme-success: 76, 175, 79;
            --v-theme-success-overlay-multiplier: 2;
            --v-theme-warning: 255, 160, 0;
            --v-theme-warning-overlay-multiplier: 2;
            --v-theme-on-background: 255, 255, 255;
            --v-theme-on-surface: 255, 255, 255;
            --v-theme-on-primary: 0, 0, 0;
            --v-theme-on-primary-darken-1: 255, 255, 255;
            --v-theme-on-secondary: 0, 0, 0;
            --v-theme-on-secondary-darken-1: 0, 0, 0;
            --v-theme-on-error: 0, 0, 0;
            --v-theme-on-info: 0, 0, 0;
            --v-theme-on-success: 0, 0, 0;
            --v-theme-on-warning: 0, 0, 0;
            --v-border-color: 255, 255, 255;
            --v-border-opacity: 0.12;
            --v-high-emphasis-opacity: 0.87;
            --v-medium-emphasis-opacity: 0.6;
            --v-disabled-opacity: 0.38;
            --v-idle-opacity: 0.04;
            --v-hover-opacity: 0.04;
            --v-focus-opacity: 0.12;
            --v-selected-opacity: 0.08;
            --v-activated-opacity: 0.12;
            --v-pressed-opacity: 0.16;
            --v-dragged-opacity: 0.08;
            --v-theme-kbd: 33, 37, 41;
            --v-theme-on-kbd: 255, 255, 255;
            --v-theme-code: 245, 245, 245;
            --v-theme-on-code: 0, 0, 0;
        }
        </style>
        <style>
        @font-face {
            font-weight: 400;
            font-style: normal;
            font-family: circular;
            src: url('chrome-extension://liecbddmkiiihnedobmlmillhodjkdmb/fonts/CircularXXWeb-Book.woff2') format('woff2');
        }

        @font-face {
            font-weight: 700;
            font-style: normal;
            font-family: circular;
            src: url('chrome-extension://liecbddmkiiihnedobmlmillhodjkdmb/fonts/CircularXXWeb-Bold.woff2') format('woff2');
        }

        body,
        html {
            height: 100%;
            margin: 0;
            font-family: circular, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #fff;
        }

        .login-container {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 100%;
            height: 100%;
        }

        .login-card {
            width: 100%;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            background-color: #fff;
        }

        .login-card__title {
            font-size: 18px;
            font-weight: regular;
            text-align: center;
            margin-bottom: 40px;
        }

        .login-card__content {
            display: flex;
            flex-direction: column;
        }

        .login-card__input {
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 20px;
            outline: none;
            width: 100%;
        }

        .login-card__input:focus {
            border-color: rgb(220, 52, 34);
            box-shadow: 0 0 5px rgba(98, 0, 238, 0.5);
        }

        .login-card__button {
            padding: 10px;
            font-size: 16px;
            color: #fff;
            background-color: rgb(220, 52, 34);
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        .sign-up-card__button {
            margin-top: 20px;
            margin-bottom: 30px;
            padding: 10px;
            font-size: 16px;
            width: 100%;
            border: 1px solid rgb(220, 52, 34);
            border-radius: 4px;
            cursor: pointer;
        }

        .forgot-password {
            color: rgb(220, 52, 34);
            text-align: left;
            margin-bottom: 35px;
            margin-top: 10px;
            cursor: pointer;
        }

        .reset-password-link {
            color: rgb(220, 52, 34);
            text-align: left;
            margin-bottom: 35px;
            margin-top: 10px;
            cursor: pointer;
        }

        .revdojo-logo {
            width: 200px;
            height: auto;
            margin: 20px auto;
            display: block;
        }
        </style>
        <script type="module">
        import { checkCookiesAndSetTimer } from "${url.resourcesPath}/js/authChecker.js";

        checkCookiesAndSetTimer(
            "${url.ssoLoginInOtherTabsUrl?no_esc}"
        );
    </script>
    </head>

    <body>
        <#nested "header">
            <div class="login-content" style="background-image: url(&quot;${url.resourcesPath}/img/background.svg&quot;);">
                <div class="box">
                    <#nested "form">
                </div>
            </div>
    </body>

    </html>
</#macro>