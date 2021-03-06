package com.isilona.restapi.web.controller;

import com.isilona.common.util.QueryConstants;
import com.isilona.common.web.controller.AbstractController;
import com.isilona.common.web.controller.ISortingController;
import com.isilona.restapi.persistence.model.Host;
import com.isilona.restapi.service.IHostService;
import com.isilona.restapi.util.HostmanagerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = HostmanagerMappings.HOSTS)
public class HostRecordController extends AbstractController<Host> implements ISortingController<Host> {

    @Autowired
    private IHostService service;

    public HostRecordController() {
        super(Host.class);
    }

    // API

    // find - all/paginated

    @Override
    @RequestMapping(params = {QueryConstants.PAGE, QueryConstants.SIZE, QueryConstants.SORT_BY}, method = RequestMethod.GET)
    @ResponseBody
//    @Secured(Privileges.ROLE_HOST_RECORD_READ)
    public List<Host> findAllPaginatedAndSorted(
            @RequestParam(value = QueryConstants.PAGE) final int page,
            @RequestParam(value = QueryConstants.SIZE) final int size,
            @RequestParam(value = QueryConstants.SORT_BY) final String sortBy,
            @RequestParam(value = QueryConstants.SORT_ORDER) final String sortOrder,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response
    ) {
        return findPaginatedAndSortedInternal(page, size, sortBy, sortOrder, uriBuilder, response);
    }

    @Override
    @RequestMapping(params = {QueryConstants.PAGE, QueryConstants.SIZE}, method = RequestMethod.GET)
    @ResponseBody
//    @Secured(Privileges.ROLE_HOST_RECORD_READ)
    public List<Host> findAllPaginated(
            @RequestParam(value = QueryConstants.PAGE) final int page,
            @RequestParam(value = QueryConstants.SIZE) final int size,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response
    ) {
        return findPaginatedInternal(page, size, uriBuilder, response);
    }

    @Override
    @RequestMapping(params = {QueryConstants.SORT_BY}, method = RequestMethod.GET)
    @ResponseBody
//    @Secured(Privileges.ROLE_HOST_RECORD_READ)
    public List<Host> findAllSorted(
            @RequestParam(value = QueryConstants.SORT_BY) final String sortBy,
            @RequestParam(value = QueryConstants.SORT_ORDER) final String sortOrder
    ) {
        return findAllSortedInternal(sortBy, sortOrder);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
//    @Secured(Privileges.ROLE_HOST_RECORD_READ)
    public List<Host> findAll(
            final HttpServletRequest request,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response
    ) {
        return findAllInternal(request, uriBuilder, response);
    }

    // find - one

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
//    @Secured(Privileges.ROLE_HOST_RECORD_READ)
    public Host findOne(
            @PathVariable("id") final Long id,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response
    ) {
        return findOneInternal(id, uriBuilder, response);
    }

    // find - one by name

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
//    @Secured(Privileges.ROLE_HOST_RECORD_READ)
    public Host findOneByName(
            @RequestParam("name") final String name
    ) {
        return service.findByName(name);
    }

    // create

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @RequestBody @Valid final Host resource,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response
    ) {
        createInternal(resource, uriBuilder, response);
    }

    // update

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
//    @Secured(Privileges.ROLE_HOST_RECORD_WRITE)
    public void update(
            @PathVariable("id") final Long id,
            @RequestBody @Valid final Host resource
    ) {
        updateInternal(id, resource);
    }

    // delete

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @Secured(Privileges.ROLE_HOST_RECORD_WRITE)
    public void delete(
            @PathVariable("id") final Long id
    ) {
        deleteByIdInternal(id);
    }

    // Spring

    @Override
    protected final IHostService getService() {
        return service;
    }

}
