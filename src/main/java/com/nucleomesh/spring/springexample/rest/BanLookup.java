package com.nucleomesh.spring.springexample.rest;

import com.nucleomesh.spring.springexample.services.NucleoMeshService;
import com.synload.nucleo.data.NucleoData;
import com.synload.nucleo.data.NucleoObject;
import com.synload.nucleo.event.NucleoResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("bans")
public class BanLookup {

    @Autowired
    NucleoMeshService meshService;

    Logger logger = LoggerFactory.getLogger(BanLookup.class);

    @GetMapping("/player")
    public DeferredResult<ResponseEntity<?>> request(@RequestParam String id,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        NucleoObject data = (NucleoObject) request.getAttribute("data");
        List<Object> players = new ArrayList<>();
        players.add(Integer.valueOf(id).intValue());
        data.set("player_list", players);

        try {
            DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
            meshService.getMesh().call(new String[]{"player.get.playerid"}, data, new NucleoResponder() {
                @Override
                public void run(NucleoData data) {
                    result.setResult(ResponseEntity.ok(data.getObjects().getObjects().get("players")));
                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
