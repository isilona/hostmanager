package com.isilona.restapi.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static com.isilona.restapi.util.HostmanagerMappings.Singular;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class RedirectController {

    public RedirectController() {
        super();
    }

    // API

    @RequestMapping(value = Singular.PRIVILEGE)
    public ResponseEntity<Void> privilegeToPrivileges(final HttpServletRequest request) {
        return singularToPlural(request);
    }

    @RequestMapping(value = Singular.ROLE)
    public ResponseEntity<Void> roleToRoles(final HttpServletRequest request) {
        return singularToPlural(request);
    }

    @RequestMapping(value = Singular.USER)
    public ResponseEntity<Void> userToUsers(final HttpServletRequest request) {
        return singularToPlural(request);
    }

    @RequestMapping(value = Singular.HOST)
    public ResponseEntity<Void> hostToHosts(final HttpServletRequest request) {
        return singularToPlural(request);
    }

    // util

    private final ResponseEntity<Void> singularToPlural(final HttpServletRequest request) {
        final String correctUri = request.getRequestURL().toString() + "s";
        final HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.add(HttpHeaders.LOCATION, correctUri);

        return new ResponseEntity<>(responseHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

}
