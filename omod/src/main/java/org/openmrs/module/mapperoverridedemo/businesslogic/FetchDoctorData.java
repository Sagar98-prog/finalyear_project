package org.openmrs.module.mapperoverridedemo.businesslogic;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FetchDoctorData {


    // 89705
    private final String REST_GET_URL = "https://mciindia.org/MCIRest/open/getDataFromService?service=searchDoctor?registrationNo=%s";
    private final OkHttpClient httpClient = new OkHttpClient();

    public String fetch_doctor_data(String reg_id) throws Exception {
        String rest_url = String.format(REST_GET_URL, reg_id);
        Request request = new Request.Builder().url(rest_url).build();
        Response response =  httpClient.newCall(request).execute();
//        System.out.println(response.body().string());
        return response.body().string();
    }
}
