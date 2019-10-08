package com.nucleomesh.spring.springexample.services;

import com.synload.nucleo.NucleoMesh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class NucleoMeshService {
  private NucleoMesh mesh;
  public static String getEnv(String env, String default_value){
    String temp = System.getenv(env);
    if(temp!=null){
      return temp;
    }
    return default_value;
  }
  @PostConstruct
  public void init() {
    this.mesh = new NucleoMesh(
        getEnv("MESH_NAME", "mcbans"),
        getEnv("SERVICE_NAME", "api"),
        getEnv("ZOOKEEPER", "192.168.1.7:2181"),
        getEnv("ELASTIC_HOST", "192.168.1.7"),
        Integer.valueOf(
            getEnv("ELASTIC_PORT", "9200")
        )
    );
  }
  public NucleoMesh getMesh(){
    return mesh;
  }
}
