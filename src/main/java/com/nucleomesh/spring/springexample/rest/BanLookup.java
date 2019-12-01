package com.nucleomesh.spring.springexample.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nucleomesh.spring.springexample.services.NucleoMeshService;
import com.synload.nucleo.data.NucleoData;
import com.synload.nucleo.data.NucleoObject;
import com.synload.nucleo.event.NucleoResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

@RestController("/bans")
public class BanLookup {

    @Autowired
    NucleoMeshService meshService;

    Logger logger = LoggerFactory.getLogger(BanLookup.class);

    @GetMapping("/player")
    public DeferredResult<ResponseEntity<?>> request(@RequestParam("chains") String chains,
                                                     @RequestParam("objects") String objects,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        NucleoObject data = (NucleoObject) request.getAttribute("data");
        try {
            new ObjectMapper().readValue(objects, Map.class).forEach((x, y)->{
                data.set((String)x, y);
            });

            chains = "session.get.continue,permission.admin," + chains;
            DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
            meshService.getMesh().call(chains.split(","), data, new NucleoResponder() {
                @Override
                public void run(NucleoData data) {
                    result.setResult(ResponseEntity.ok(data));
                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
