package org.openmrs.module.mapperoverridedemo.page.controller;


import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.mapperoverridedemo.businesslogic.FetchAmbulanceData;
import org.openmrs.module.mapperoverridedemo.businesslogic.FetchDoctorData;
import org.openmrs.module.mapperoverridedemo.businesslogic.HoldAuthState;
import org.openmrs.module.mapperoverridedemo.businesslogic.RandomKeyGenerator;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/mapperoverridedemo")
public class RestControllerEndPoint extends BaseRestController {

    protected final Log log = LogFactory.getLog(getClass());
    private final Logger LOGGER = Logger.getLogger(RestController.class.getName());
    Handler handler = new ConsoleHandler();

    public static boolean authenticated = false;
    public static String UUID = "";

//_____________________
//999999990019
//MH04G-8388
//5100
//_____________________


//________________________
//DOC-ID: 89705
//HOC-MHA-58
//_________________________

// EmergencyLogin@123


    FetchAmbulanceData ambulanceData = new FetchAmbulanceData();
    FetchDoctorData fetchDoctorData = new FetchDoctorData();

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object get(WebRequest request ) throws Exception {

        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);

        ambulanceData.connect_to_db(FetchAmbulanceData.VEHICLE_DB);
        String decide = request.getParameter("did");
        String value = "";
        if (decide == null)
            decide = request.getParameter("toofa");


        LOGGER.log(Level.ALL, decide);

       if(decide.equals("doc")){
           String docId = request.getParameter("docid");
           String HospId = request.getParameter("hid");

           String hosfromdb = ambulanceData.get_hospital_data(HospId);
           String docfrodb = fetchDoctorData.fetch_doctor_data(docId);


           if(hosfromdb.equals("") || docfrodb.equals("")){
               return "-1";
           }
           else {
               return "1";
           }

       }else if(decide.equals("amb")){

           String ambid = request.getParameter("docid");
           String prmid = request.getParameter("hid");

           String ambfromdb = ambulanceData.get_vehicle_data(ambid);
           String prmfromdb = ambulanceData.get_paramedics_data(prmid);

           if(ambfromdb.equals("") || prmfromdb.equals("")){
               return "-1";
           }
           else {
               return "1";
           }
       }
        else if(StringUtils.isNumeric(decide)){
                String genotp = RandomKeyGenerator.getTOTPCode(RandomKeyGenerator.SecrectKey);
                System.out.println(genotp);
                if(genotp.equals(decide))
                    return "1";
            }
        return "-1";
    }

    @Override
    public String getNamespace() {
        return super.getNamespace();
    }

    @Override
    public String buildResourceName(String resource) {
        return super.buildResourceName(resource);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void post(WebRequest request ) throws Exception {
        System.out.println("Post method called!");

        UUID = request.getParameter("uuid");
        HoldAuthState holdAuthState =  HoldAuthState.getInstance();
        holdAuthState.setUuid(UUID);
        holdAuthState.setAuthenticated(true);
    }
}



