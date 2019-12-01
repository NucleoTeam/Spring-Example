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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class RootPage {
    @Autowired
    NucleoMeshService meshService;

    Logger logger = LoggerFactory.getLogger(RootPage.class);

    @GetMapping("/prev")
    public DeferredResult<ResponseEntity<?>> getPrev() {
        NucleoObject data = new NucleoObject();
        data.set("wow", "works?");
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        meshService.getMesh().call("popcorn.poppy", data, new NucleoResponder() {
            @Override
            public void run(NucleoData data) {
                result.setResult(ResponseEntity.ok(data));
            }
        });
        return result;
    }

    @GetMapping("/timeout")
    public DeferredResult<ResponseEntity<?>> getTimeout() {
        NucleoObject data = new NucleoObject();
        data.set("wow", "works?");
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        meshService.getMesh().call(new String[]{"information.hits", "information.x"}, data, new NucleoResponder() {
            @Override
            public void run(NucleoData data) {
                result.setResult(ResponseEntity.ok(data));
            }
        });
        return result;
    }

    @GetMapping("/")
    public DeferredResult<ResponseEntity<?>> getRoot() {
        NucleoObject data = new NucleoObject();
        data.set("wow", "works?");
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        meshService.getMesh().call("information.hits", data, new NucleoResponder() {
            @Override
            public void run(NucleoData data) {
                result.setResult(ResponseEntity.ok(data));
            }
        });
        return result;
    }

    @GetMapping("/c")
    public DeferredResult<ResponseEntity<?>> getChange() {
        NucleoObject data = new NucleoObject();
        data.set("wow", "works?");
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        meshService.getMesh().call(
            new String[]{
                "information.hits",
                "[information.changeme/information.popcorn/information.test]"
            },
            data,
            new NucleoResponder() {
                @Override
                public void run(NucleoData data) {
                    result.setResult(ResponseEntity.ok(data));
                }
            }
        );
        return result;
    }

    @GetMapping("/chain")
    public DeferredResult<ResponseEntity<?>> chainBreak() {
        NucleoObject data = new NucleoObject();
        data.set("stop", "chain");
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        meshService.getMesh().call("information.changeme", data, new NucleoResponder() {
            @Override
            public void run(NucleoData data) {
                result.setResult(ResponseEntity.ok(data));
            }
        });
        return result;
    }

    @PostMapping("/req")
    public DeferredResult<ResponseEntity<?>> request(@RequestParam("chains") String chains,
                                                     @RequestParam("objects") String objects,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        NucleoObject data = (NucleoObject) request.getAttribute("data");
        try {
            new ObjectMapper().readValue(objects, Map.class).forEach((x,y)->{
                data.set((String)x, y);
            });
            chains = "session.get.continue,permission.admin," + chains;
            DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
            meshService.getMesh().call(chains.split(","), data, new NucleoResponder() {
                @Override
                public void run(NucleoData data) {
                    result.setResult(ResponseEntity.ok(data.getObjects().getObjects()));
                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ExceptionHandler({Exception.class})
    public Object handleErrors() {
        return new Object[]{"error", "form incomplete"};
    }
}
