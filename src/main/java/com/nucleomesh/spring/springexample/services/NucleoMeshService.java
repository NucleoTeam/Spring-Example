package com.nucleomesh.spring.springexample.services;

import com.synload.nucleo.NucleoMesh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NucleoMeshService {
  private NucleoMesh mesh;
  @PostConstruct
  public void init() {
    this.mesh = new NucleoMesh(
      "module.2",
      "192.168.1.112:9092",
      "spring.example"
    );
    while(mesh.getHub().isReady()) {
      try {
        Thread.sleep(1L);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    this.mesh.getHub().run();
  }
  public NucleoMesh getMesh(){
    return mesh;
  }
}
