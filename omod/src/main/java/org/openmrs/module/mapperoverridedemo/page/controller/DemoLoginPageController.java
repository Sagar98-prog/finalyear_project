package org.openmrs.module.mapperoverridedemo.page.controller;

import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.COOKIE_NAME_LAST_SESSION_LOCATION;
import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.REQUEST_PARAMETER_NAME_REDIRECT_URL;
import static org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.emrapi.utils.GeneralUtils;
import org.openmrs.module.mapperoverridedemo.businesslogic.HoldAuthState;
import org.openmrs.module.referenceapplication.ReferenceApplicationConstants;
import org.openmrs.module.referenceapplication.ReferenceApplicationWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * Copied from referenceapplication LoginController with no changes
 */
public class DemoLoginPageController {
	//see TRUNK-4536 for details why we need this
	private static final String GET_LOCATIONS = "Get Locations";

	// RA-592: don't use PrivilegeConstants.VIEW_LOCATIONS
	private static final String VIEW_LOCATIONS = "View Locations";

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping("/login.htm")
	public String overrideLoginpage() {
		//TODO The referer should actually be captured from here since we are doing a redirect
		return "forward:/" + ReferenceApplicationConstants.MODULE_ID + "/login.page";

	}

	public String get(PageModel model,
	                  UiUtils ui,
	                  PageRequest pageRequest,
	                  @CookieValue(value = COOKIE_NAME_LAST_SESSION_LOCATION, required = false) String lastSessionLocationId,
	                  @SpringBean("locationService") LocationService locationService,
	                  @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService) {


		if (Context.isAuthenticated()) {
			return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "home");
		}

		String redirectUrl = getStringSessionAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, pageRequest.getRequest());
		if (StringUtils.isBlank(redirectUrl))
			redirectUrl = pageRequest.getRequest().getParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL);

		if (StringUtils.isBlank(redirectUrl)) {
			redirectUrl = getRedirectUrlFromReferer(pageRequest);
		}

		if (redirectUrl == null)
			redirectUrl = "";

		model.addAttribute(REQUEST_PARAMETER_NAME_REDIRECT_URL, redirectUrl);
		Location lastSessionLocation = null;
		try {
			Context.addProxyPrivilege(VIEW_LOCATIONS);
			Context.addProxyPrivilege(GET_LOCATIONS);
			model.addAttribute("locations", appFrameworkService.getLoginLocations());
			lastSessionLocation = locationService.getLocation(Integer.valueOf(lastSessionLocationId));
		}
		catch (NumberFormatException ex) {
			// pass
		}
		finally {
			Context.removeProxyPrivilege(VIEW_LOCATIONS);
			Context.removeProxyPrivilege(GET_LOCATIONS);
		}

		model.addAttribute("lastSessionLocation", lastSessionLocation);
		return null;

	}

	private String getRedirectUrlFromReferer(PageRequest pageRequest) {
		String referer = pageRequest.getRequest().getHeader("Referer");
		String redirectUrl = "";
		if (referer != null) {
			if (referer.contains("http://") || referer.contains("https://")) {
				try {
					URL refererUrl = new URL(referer);
					String refererPath = refererUrl.getPath();
					String refererContextPath = refererPath.substring(0, refererPath.indexOf('/', 1));
					if (StringUtils.equals(pageRequest.getRequest().getContextPath(), refererContextPath)) {
						redirectUrl = refererPath;
					}
				}
				catch (MalformedURLException e) {
					log.error(e.getMessage());
				}
			} else {
				redirectUrl = pageRequest.getRequest().getHeader("Referer");
			}
		}
		return StringEscapeUtils.escapeHtml(redirectUrl);
	}

	public String post(@RequestParam(value = "id_input", required = false) String username,
	                   @RequestParam(value = "amb_reg_no", required = false) String password,
	                   @RequestParam(value = "hsptl_reg_no", required = false)String regno,
	                   @RequestParam(value = "sessionLocation", required = false) Integer sessionLocationId,
	                   @SpringBean("locationService") LocationService locationService, UiUtils ui, PageRequest pageRequest,
	                   UiSessionContext sessionContext) {

		// redirect url = /openmrs/referenceapplication/login.page
		// /openmrs/referenceapplication/home.page?noredirect=true
//		String redirectUrl = pageRequest.getRequest().getParameter(REQUEST_PARAMETER_NAME_REDIRECT_URL);
		//redirectUrl = getRelativeUrl(redirectUrl, pageRequest);

		String redirectUrl = "/openmrs/referenceapplication/home.page?noredirect=true";
		redirectUrl = "";

		System.out.println(pageRequest.getRequest().getRequestURL().toString());
		sessionLocationId = 6;


		Location sessionLocation = null; //locationService.getLocation(6);
//		System.out.println(sessionLocation == null);
//		LocationTag tag = new LocationTag();
//		tag.setLocationTagId(6);
//		sessionLocation.addTag(tag);

//		System.out.println("TAG: "+sessionLocation.hasTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_LOGIN));


		if (sessionLocationId != null) {
			try {

				// TODO as above, grant this privilege to Anonymous instead of using a proxy privilege
				Context.addProxyPrivilege(VIEW_LOCATIONS);
				Context.addProxyPrivilege(GET_LOCATIONS);
//				sessionLocation = locationService.getLocation(sessionLocationId);
			}
			finally {
				Context.removeProxyPrivilege(VIEW_LOCATIONS);
				Context.removeProxyPrivilege(GET_LOCATIONS);
			}
		}

		//TODO uncomment this to replace the if clause after it
		if (true) {
			// Set a cookie, so next time someone logs in on this machine, we can default to that same location
			pageRequest.setCookieValue(COOKIE_NAME_LAST_SESSION_LOCATION, sessionLocationId.toString());

			try {

				Context.authenticate("Emergency_Login", "EmergencyLogin@123");

				if (Context.isAuthenticated()) {
					if (log.isDebugEnabled())
						log.debug("User has successfully authenticated");

					sessionContext.setSessionLocation(locationService.getLocation(6));

					// set the locale based on the user's default locale
					Locale userLocale = GeneralUtils.getDefaultLocale(Context.getUserContext().getAuthenticatedUser());
					System.out.println("NULL"+ userLocale);
					if (userLocale != null) {
						Context.getUserContext().setLocale(userLocale);
						pageRequest.getResponse().setLocale(userLocale);
						new CookieLocaleResolver().setDefaultLocale(userLocale);
					}

					if (StringUtils.isNotBlank(redirectUrl)) {
						//don't redirect back to the login page on success nor an external url
						if (redirectUrl.contains("login.")) {
							if (log.isDebugEnabled())
								log.debug("Redirecting user to " + redirectUrl);

//							return "redirect:" + redirectUrl;
						} else {
							if (log.isDebugEnabled())
								log.debug("Redirect contains 'login.', redirecting to home page");
						}
					}

					HoldAuthState holdAuthState = HoldAuthState.getInstance();


					if (holdAuthState.isAuthenticated()) {
						if(!holdAuthState.getUuid().isEmpty()) {
							String redirect = "coreapps/clinicianfacing/patient.page?patientId=";
							return "redirect:"+redirect+holdAuthState.getUuid();
						}
					}
					else {
						Context.logout();
						Context.closeSession();
					}
//					return "redirect:"+"coreapps/clinicianfacing/patient.page?patientId="+"6ddbe87c-8188-443e-b498-2722f53992ea";

					return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
				}
			}
			catch (ContextAuthenticationException ex) {

				if (log.isDebugEnabled())
					log.debug("Failed to authenticate user");

				pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
						ui.message(ReferenceApplicationConstants.MODULE_ID + ".error.login.fail"));

				pageRequest.setProviderNameOverride("mapperoverridedemo");

			}

		} else if (sessionLocation == null) {
			pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
					ui.message("referenceapplication.login.error.locationRequired"));
		} else {
			// the UI shouldn't allow this, but protect against it just in case
			pageRequest.getSession().setAttribute(ReferenceApplicationWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
					ui.message("referenceapplication.login.error.invalidLocation", sessionLocation.getName()));
		}

		if (log.isDebugEnabled())
			log.debug("Sending user back to login page");

		//TODO limit login attempts by IP Address
		System.out.println("Ending");
		pageRequest.getSession().setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
		return "redirect:" + ui.pageLink(ReferenceApplicationConstants.MODULE_ID, "login");
	}

	private String getStringSessionAttribute(String attributeName, HttpServletRequest request) {
		String attributeValue =  String.valueOf(request.getSession().getAttribute(attributeName));
		request.getSession().removeAttribute(attributeName);
		return attributeValue;
	}

	public String getRelativeUrl(String url, PageRequest pageRequest) {
		if (url == null)
			return null;

		if (url.startsWith("/") || (!url.startsWith("http://") && !url.startsWith("https://"))) {
			return url;
		}

		//This is an absolute url, discard the protocal, domain name/host and port section
		int indexOfContextPath = url.indexOf(pageRequest.getRequest().getContextPath());
		if (indexOfContextPath >= 0) {
			url = url.substring(indexOfContextPath);
			log.debug("Relative redirect:" + url);

			return url;
		}

		return null;
	}
}
