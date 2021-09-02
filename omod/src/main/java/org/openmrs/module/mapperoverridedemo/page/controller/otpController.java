package org.openmrs.module.mapperoverridedemo.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.Servlet;

@Controller
@RequestMapping(value= "/module/mapperoverridedemo/otp")
public class otpController {


    protected final Log log = LogFactory.getLog(getClass());
    private final String SUCCESS_FORM_VIEW = "module/mapperoverridedemo/otp";

    @RequestMapping(method = RequestMethod.GET)
    public String onGet(){
        return SUCCESS_FORM_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onPost(){
        return SUCCESS_FORM_VIEW;
    }



}
