package net.fasolato.mina.minatest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StatusController {
    private static Logger log = LogManager.getLogger(StatusController.class);

    @Autowired
    MinaServerConfig minaServerConfig;

    @RequestMapping(value = "/api/v1/status", method = RequestMethod.GET)
    public ResponseEntity<String> getStatus() {
        if (minaServerConfig == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(minaServerConfig.serverStatus(), HttpStatus.OK);
    }
}
