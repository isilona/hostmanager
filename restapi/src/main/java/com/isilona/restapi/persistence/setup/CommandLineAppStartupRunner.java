package com.isilona.restapi.persistence.setup;


import com.isilona.restapi.persistence.model.Host;
import com.isilona.restapi.service.IHostService;
import com.isilona.restapi.util.HostmanagerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);


    @Value("${test.host.ip:192.168.0.1}")
    String testHostIP;

    @Value("${dev.host.ip:192.168.0.2}")
    String devHostIP;

    @Autowired
    private IHostService hostService;

    @Override
    public void run(String... args) {


        if (logger.isInfoEnabled()) {
            logger.info("Application started with command-line arguments: {} .", Arrays.toString(args));
            logger.info("To kill this application, press Ctrl + C.");
        }

        logger.info("Executing Setup");

        createHostRecords();

        logger.info("Setup Done");
    }


    // host records
    public void createHostRecords() {
        createHostRecordIfNotexist(HostmanagerConstants.TEST_HOST_NAME, testHostIP);
        createHostRecordIfNotexist(HostmanagerConstants.DEV_HOST_NAME, devHostIP);
    }

    void createHostRecordIfNotexist(String name, String ip) {
        final Host entity = new Host();
        entity.setName(name);
        entity.setIp(ip);

        if (hostService.findByName(name) == null) {
            hostService.create(entity);
        }
    }
}