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

<script type="text/javascript">


var is_doc_valid = false;

const d = [
  [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
  [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
  [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
  [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
  [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
  [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
  [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
  [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
  [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
  [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
]

// permutation table
const p = [
  [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
  [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
  [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
  [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
  [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
  [4, 2, 8, 6, 5, 7, 3, 9, 0, 1],
  [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
  [7, 0, 4, 6, 9, 1, 3, 2, 5, 8]
]


function validate_pan_card(panNumber){
	var panVal = panNumber;
	var re = new RegExp('([A-Z]){5}([0-9]){4}([A-Z]){1}');
	return re.test(panVal.toUpperCase());
}






function validate_adhaar(aadharNumber) {
  let c = 0
  let invertedArray = aadharNumber.split('').map(Number).reverse()

  invertedArray.forEach((val, i) => {
	  c = d[c][p[(i % 8)][val]]
  })

  return (c === 0)
}

function change_text_labels() {

	var para_or_doc = jQuery('#mid_select').val();

	if(para_or_doc === "1"){
		change_label(document.getElementById('id_number_one'), "Ambulance reg. no.");
		change_label(document.getElementById('id_number_two'), "Paramedic reg. no.");
		document.getElementById('idType').value = "amb";

	}
	else if(para_or_doc === "2"){
		change_label(document.getElementById('id_number_one'), "Doctor reg. no.");
		change_label(document.getElementById('id_number_two'), "Hospital reg. no.");

		document.getElementById('idType').value = "doc";
	}
}


function change_label(field_id, new_label) {
	field_id.innerHTML = new_label;
}


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


function submit_form() {
        jQuery('#login-form').submit();
}


    jQuery('#view_p').click(function() {
        var validtype = jQuery('#pid_select').val();
		if(validtype === "1"){
		    if(jQuery("#id_input").val() === "")
		        alert("ID field cannot be empty!");
			var is_adhaar_valid = validate_adhaar(jQuery("#id_input").val())
			if(!is_adhaar_valid){

				alert("Invalid Adhaar Card number")
				document.getElementById('id_input').value = ""
			}
		}
		else if(validtype === "2"){
			var is_valid_pan_card = validate_pan_card(jQuery("#id_input").val())
		    alert("Invalid PAN Card number")
			document.getElementById('id_input').value = ""
		}

            var id_1 = document.getElementById('idType').value;
            var id_2 = document.getElementById('id_1').value;
            var id_3 = document.getElementById('id_2').value;

           var is_submit = false;
           jQuery.ajax({
                type: 'GET',
                url: 'http://localhost:8080/openmrs/ws/rest/v1/mapperoverridedemo',
                data: {did: id_1 , docid: id_2, hid: id_3},
                success: function(data) {

                    if(data === "1"){
                            var twofa = window.prompt("Enter 2FA code");
                            jQuery.ajax({
                                 type:'GET',
                                 url:'http://localhost:8080/openmrs/ws/rest/v1/mapperoverridedemo',
                                 data: {toofa: twofa},
                                 success: function(data) {
                                    console.log(data);
                                    if(data === "1") {

                                        id = jQuery("#id_input").val();

                                        jQuery.ajax({
                                            type:'GET',
                                            dataType: 'json',
                                            url: 'http://localhost:8080/openmrs/ws/rest/v1/patient',
                                            data: {v:'default', identifier:id},
                                            success: function(data) {
                                                response  = JSON.stringify(data);
                                                response = JSON.parse(response);

                                                if(response.results.length === 0) {
                                                    alert("Patient not found in Database");
                                                }
                                                else {
                                                 redirect_url = "http://localhost:8080/openmrs/coreapps/clinicianfacing/patient.page?patientId="
                                                 uuid = response.results[0]['uuid'];

                                                    jQuery.ajax({
                                                        type: 'POST',
                                                        url: 'http://localhost:8080/openmrs/ws/rest/v1/mapperoverridedemo',
                                                        data: {uuid: uuid},
                                                        success: function(data) {
                                                        console.log("SUCCESS" + data);

                                                        submit_form();

                                                        }
                                                    });



                                                }


                                            }
                                        });
                                    }

                                 },
                                 error: function(data) {
                                 console.log(data);
                                 }
                            });
                    }
                },
               error: function(data) {
                console.log("error");
               }
           });



        });
    });

</script>


<style>
#main_title {
	padding: 5px;
	color:white;
	font-size:30px;
	text-align: center;

}

#loginButton {
  display: inline-block;
  align-items: center;
  justify-content: center;
  margin-right:120px;
  margin-top:15px;
}
.shadow-sm {
    display: inline-block;
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    width: 800px;
    height: 680px;
    margin: auto;
    background: #FFFFFF;
}

#get_details {
	padding: 20px;
	width: 450px;
	margin: auto;
	margin-top: 60px;
	border: 1px solid;
	border-radius: 3px;
	border-color: var(--omrs-color-interaction-minus-one);

}

.logo_bar {
        display: inline-block;
        position: fixed;
    	margin-left: -16px;
    	margin-top: -16px;
    	height: 65px;
    	width: 800px;
    	background:  blue;
    	border-top-left-radius: 3px;
    	border-top-right-radius: 3px;
    	/*box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24); */
    	box-shadow: 0px -1px 2px rgba(0, 0, 0, 0.24);
}

</style>


<div id="body-wrapper" class="shadow-sm p-3 mb-5 rounded">
		<div class="logo_bar">
			<h4 id="main_title" class="omrs-type-body-regular">Emergency Health Access Login Page</h4>
		</div>

    <div id="content">
        <form id="login-form" method="post" autocomplete="off">

        	<div name="get_details" id="get_details">
        		<div id="patient_id_form_1" class="d-flex justify-content-center">
            		<div class="form-group">
            		    <div class="control-label">
            				<label for="pid">Patient ID type</label>
            					<select class="form-control" id="pid_select" name="pid_select">
            						<option value="1">Adhaar card</option>
            						<option value="2">PAN card</option>
            					</select>
            			</div>
            			 <br>
            			 <div class="control-label">
            				<label for="id_number" id="id_number">ID number</label>
            			       <input type="text" class="form-control" name="id_input" id="id_input">
            			 </div>
            		</div>
            </div>


		    <div id="medical_id_form_2" class="d-flex justify-content-center">

			<div class="form-group">
			    <div class="control-label">
				<label for="mp">Medical Profession</label>
						<select class="form-control" id="mid_select" onchange="change_text_labels()">
							<option value ="1">Paramedic</option>
							<option value ="2">Doctor</option>
						</select>
			    </div>
			    <br>
			    <div class="control-label">
				<label for="id_number" id="id_number_one">Ambulance reg. no.</label>
			    	<input type="text" class="form-control" name="amb_reg_no" id="id_1">
				</div>
				<br>
			    <div class="control-label">
				<label for="id_number" id="id_number_two">Paramedic reg. no.</label>
			    	<input type="text" class="form-control" name="hsptl_reg_no" id="id_2">
				</div>
			    <input type="hidden" id="idType" name="idType" value="doc">
			</div>
		</div>
                <p>
                    <button type="button"  id="view_p" class="confirm" style="display: block;margin: 0 auto">View Patient</button>
                </p>
            <input type="hidden" name="redirectUrl" value="${redirectUrl}" />
        </form>


	</div>

</div>




</body>
</html>
