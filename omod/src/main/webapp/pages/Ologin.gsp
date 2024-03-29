<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeCss("referenceapplication", "login.css")
%>

<!DOCTYPE html>
<html>
<head>
    <title>${ ui.message("referenceapplication.login.title") }</title>
    <link rel="shortcut icon" type="image/ico" href="/${ ui.contextPath() }/images/openmrs-favicon.ico"/>
    <link rel="icon" type="image/png\" href="/${ ui.contextPath() }/images/openmrs-favicon.png"/>
    ${ ui.resourceLinks() }
</head>
<body>
<script type="text/javascript">
    var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
</script>


${ ui.includeFragment("referenceapplication", "infoAndErrorMessages") }

<script type="text/javascript">
    jQuery(function() {
        updateSelectedOption = function() {
            jQuery('#sessionLocation li').removeClass('selected');

            var sessionLocationVal = jQuery('#sessionLocationInput').val();
            if(sessionLocationVal != null && sessionLocationVal != "" && sessionLocationVal != 0){
                jQuery('#sessionLocation li[value|=' + sessionLocationVal + ']').addClass('selected');
            }
        };

        updateSelectedOption();

        jQuery('#sessionLocation li').click( function() {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });

        jQuery('#login-form').submit(function(e) {
            var sessionLocationVal = jQuery('#sessionLocationInput').val();

            if (!sessionLocationVal) {
                jQuery('#sessionLocationError').show();
                e.preventDefault();
            }
        });

        jQuery('#username').focus();

        var cannotLoginController = emr.setupConfirmationDialog({
                                                                    selector: '#cannotLoginPopup',
                                                                    actions: {
                                                                        confirm: function() {
                                                                            cannotLoginController.close();
                                                                        }
                                                                    }
                                                                });

        jQuery('a#cantLogin').click(function() {
            cannotLoginController.show();
        });

        pageReady = true;
    });
</script>

<header>
    <div class="logo">
        <a href="${ui.pageLink("referenceapplication", "home")}">
            <img src="${ui.resourceLink("referenceapplication", "images/openMrsLogo.png")}"/>
        </a>
    </div>
</header>

<div id="body-wrapper">
    <h2>This shows that I am overriding the login page</h2>
    <div id="content">
        <form id="login-form" method="post" autocomplete="off">
            <fieldset>

                <legend>
                    <i class="icon-lock small"></i>
                    ${ ui.message("referenceapplication.login.loginHeading") }
                </legend>

                <p class="left">
                    <label for="username">
                        ${ ui.message("referenceapplication.login.username") }:
                    </label>
                    <input id="username" type="text" name="username" placeholder="${ ui.message("referenceapplication.login.username.placeholder") }"/>
                </p>

                <p class="left">
                    <label for="password">
                        ${ ui.message("referenceapplication.login.password") }:
                    </label>
                    <input id="password" type="password" name="password" placeholder="${ ui.message("referenceapplication.login.password.placeholder") }"/>
                </p>

                <p class="clear">
                    <label for="sessionLocation">
                        ${ ui.message("referenceapplication.login.sessionLocation") }: <span class="location-error" id="sessionLocationError" style="display: none">${ui.message("referenceapplication.login.error.locationRequired")}</span>
                    </label>
                <ul id="sessionLocation" class="select">
                    <% locations.sort { ui.format(it) }.each { %>
                    <li id="${it.name}" value="${it.id}">${ui.format(it)}</li>
                    <% } %>
                </ul>
            </p>

                <input type="hidden" id="sessionLocationInput" name="sessionLocation"
                    <% if (lastSessionLocation != null) { %> value="${lastSessionLocation.id}" <% } %> />

                <p></p>
                <p>
                    <input id="loginButton" class="confirm" type="submit" value="${ ui.message("referenceapplication.login.button") }"/>
                </p>
                <p>
                    <a id="cantLogin" href="javascript:void(0)">
                        <i class="icon-question-sign small"></i>
                        ${ ui.message("referenceapplication.login.cannotLogin") }
                    </a>
                </p>

            </fieldset>

            <input type="hidden" name="redirectUrl" value="${redirectUrl}" />

        </form>

    </div>
</div>

</body>
</html>
