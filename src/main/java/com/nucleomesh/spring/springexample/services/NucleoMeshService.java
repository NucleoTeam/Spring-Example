package com.nucleomesh.spring.springexample.services;

import com.synload.nucleo.NucleoMesh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class NucleoMeshService {
  private NucleoMesh mesh;
  @PostConstruct
  public void init() {
    this.mesh = new NucleoMesh(
      "mcbans",
      "api",
      "192.168.1.29:2181",
      "192.168.1.29",
      9200
    );
    this.getMesh().start();
  }
  public NucleoMesh getMesh(){
    return mesh;
  }
}
