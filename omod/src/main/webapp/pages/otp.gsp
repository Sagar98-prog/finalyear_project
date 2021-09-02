<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeCss("referenceapplication", "login.css")
    ui.includeCss("mapperoverridedemo", "openmrs-esm-styleguide.css")
%>

<!DOCTYPE html>
<html>
<head>
    <title>${ ui.message("referenceapplication.login.title") }</title>
    <link rel="shortcut icon" type="image/ico" href="/${ ui.contextPath() }/images/openmrs-favicon.ico"/>
    <link rel="icon" type="image/png\" href="/${ ui.contextPath() }/images/openmrs-favicon.png"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"/>
    ${ ui.resourceLinks() }
</head>
<body>
<script type="text/javascript">
    var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
</script>

${ ui.includeFragment("referenceapplication", "infoAndErrorMessages") }