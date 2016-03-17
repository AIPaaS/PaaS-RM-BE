package com.ai.paas.cpaas.rm.manage.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.cpaas.rm.interfaces.IMgmtOpenService;
import com.ai.paas.cpaas.rm.vo.Attributes;
import com.ai.paas.cpaas.rm.vo.MesosInstance;
import com.ai.paas.cpaas.rm.vo.MesosSlave;
import com.ai.paas.cpaas.rm.vo.OpenResourceParamVo;
import com.ai.paas.cpaas.rm.vo.WebHaproxy;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})
public class MgmtOpenServiceTest {

  @Autowired
  IMgmtOpenService service;

  public void testSelect() {
    String result = service.queryLog("7");
    System.out.println(result);
  }

  @Test
  public void openServiceTest() {
    OpenResourceParamVo paramVo = new OpenResourceParamVo();
    paramVo.setClusterId("7");
    paramVo.setClusterName("pheonixcenter");
    paramVo.setDomain("asiainfo.com");
    paramVo.setExternalDomain("asia.com");
    paramVo.setLoadVirtualIP("10.1.1.10");
    paramVo.setAid("dev");
    paramVo.setDataCenter("south-center");
    MesosInstance master = new MesosInstance();
    master.setId(0);
    master.setIp("10.1.241.127");
    master.setRoot("root");
    master.setPasswd("Mfjjbsq7!@#");
    master.setZone("center");

    MesosInstance master1 = new MesosInstance();
    master1.setId(0);
    master1.setIp("10.1.241.128");
    master1.setRoot("root");
    master1.setPasswd("Mfjjbsq7!@#");
    master1.setZone("center");

    MesosInstance master2 = new MesosInstance();
    master2.setId(0);
    master2.setIp("10.1.241.129");
    master2.setRoot("root");
    master2.setPasswd("Mfjjbsq7!@#");
    master2.setZone("center");

    List<MesosInstance> masters = new ArrayList<MesosInstance>();
    masters.add(master);
    masters.add(master1);
    masters.add(master2);

    MesosSlave slave = new MesosSlave();
    slave.setId(0);
    slave.setIp("10.1.241.130");
    slave.setRoot("root");
    slave.setPasswd("Mfjjbsq7!@#");
    slave.setZone("web");
    slave
        .setAttributes("ds:sth;jf:nj;rack:2;ex:ex1;model:hp-150;cpu:xe3;mem:ddr3;disk:ssd;netband:1G;");
    slave.setCpuTotal(8);
    slave.setCpuOffer(6);
    slave.setMemTotal(8096);
    slave.setMemOffer(6144);
    List<MesosSlave> slaves = new ArrayList<MesosSlave>();
    slaves.add(slave);

    Attributes attributes1 = new Attributes();
    attributes1.setZone("web");
    attributes1.setNetwork("172.18.0.0/16");

    Attributes attributes2 = new Attributes();
    attributes2.setZone("db");
    attributes2.setNetwork("172.19.0.0/16");

    Attributes attributes3 = new Attributes();
    attributes3.setZone("center");
    attributes3.setNetwork("172.20.0.0/16");
    List<Attributes> attributesList = new ArrayList<Attributes>();
    attributesList.add(attributes1);
    attributesList.add(attributes2);
    attributesList.add(attributes3);
    paramVo.setAttributesList(attributesList);
    paramVo.setMesosMaster(masters);
    paramVo.setMesosSlave(slaves);
    paramVo.setUseAgent(Boolean.TRUE);
    paramVo.setImagePath("/aifs01/docker");

    WebHaproxy proxy = new WebHaproxy();
    proxy.setLoadOnly(Boolean.TRUE);
    proxy.setVirtualIp("10.1.241.14");

    List<MesosInstance> agents = new ArrayList<MesosInstance>();
    agents.add(master);
    agents.add(master1);
    proxy.setHosts(agents);
    paramVo.setWebHaproxy(proxy);
    Gson gson = new Gson();
    // String param = gson.toJson(paramVo);
    // System.out.println();
    String param =
        "{\"externalDomain\":\"asiainfo.com\",\"dataCenter\":\"res-be-data\",\"imagePath\":\"/aifs01/docker\",\"loadVirtualIP\":\"10.1.1.10\",\"attributesList\":[{\"zone\":\"center\",\"network\":\"172.18.0.0/16\"},{\"zone\":\"visit\",\"network\":\"172.19.0.0/16\"},{\"zone\":\"web\",\"network\":\"172.21.0.0/16\"},{\"zone\":\"db\",\"network\":\"172.20.0.0/16\"}],\"clusterId\":\"121\",\"useAgent\":true,\"clusterName\":\"res-test\",\"domain\":\"home.com\",\"mesosMaster\":[{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"ip\":\"10.1.241.127\",\"root\":\"root\",\"id\":1},{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"ip\":\"10.1.241.128\",\"root\":\"root\",\"id\":2},{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"ip\":\"10.1.241.129\",\"root\":\"root\",\"id\":3}],\"mesosSlave\":[{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"cpuTotal\":800,\"memTotal\":8192,\"ip\":\"10.1.241.127\",\"root\":\"root\",\"memOffer\":4096,\"attributes\":\"ds:88;jf:74;rack:6;ex:111;cpu:8;mem:8192;disk:692224;netband:5000\",\"cpuOffer\":600,\"id\":1},{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"cpuTotal\":400,\"memTotal\":8192,\"ip\":\"10.1.241.128\",\"root\":\"root\",\"memOffer\":4096,\"attributes\":\"ds:88;jf:74;rack:122;ex:112;cpu:54;mem:8192;disk:352256;netband:3333\",\"cpuOffer\":300,\"id\":2}],\"webHaproxy\":{\"hosts\":[{\"passwd\":\"Mfjjbsq7!@#\",\"ip\":\"10.1.241.126\",\"root\":\"root\",\"id\":1},{\"passwd\":\"Mfjjbsq7!@#\",\"ip\":\"10.1.241.130\",\"root\":\"root\",\"id\":2}],\"loadOnly\":true,\"virtualIp\":\"10.1.241.14\"},\"aid\":\"dev\"}";
    service.openService(param);

  }


  public void freeresourcesTest() {
    String param =
        "{\"externalDomain\":\"asiainfo.com\",\"dataCenter\":\"res-be-data\",\"imagePath\":\"/aifs01/docker\",\"loadVirtualIP\":\"10.1.1.10\",\"attributesList\":[{\"zone\":\"center\",\"network\":\"172.18.0.0/16\"},{\"zone\":\"visit\",\"network\":\"172.19.0.0/16\"},{\"zone\":\"web\",\"network\":\"172.21.0.0/16\"},{\"zone\":\"db\",\"network\":\"172.20.0.0/16\"}],\"clusterId\":\"121\",\"useAgent\":true,\"clusterName\":\"res-test\",\"domain\":\"home.com\",\"mesosMaster\":[{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"ip\":\"10.1.241.127\",\"root\":\"root\",\"id\":1},{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"ip\":\"10.1.241.128\",\"root\":\"root\",\"id\":2},{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"ip\":\"10.1.241.129\",\"root\":\"root\",\"id\":3}],\"mesosSlave\":[{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"cpuTotal\":800,\"memTotal\":8192,\"ip\":\"10.1.241.127\",\"root\":\"root\",\"memOffer\":4096,\"attributes\":\"ds:88;jf:74;rack:6;ex:111;cpu:8;mem:8192;disk:692224;netband:5000\",\"cpuOffer\":600,\"id\":1},{\"passwd\":\"Mfjjbsq7!@#\",\"zone\":\"center\",\"cpuTotal\":400,\"memTotal\":8192,\"ip\":\"10.1.241.128\",\"root\":\"root\",\"memOffer\":4096,\"attributes\":\"ds:88;jf:74;rack:122;ex:112;cpu:54;mem:8192;disk:352256;netband:3333\",\"cpuOffer\":300,\"id\":2}],\"webHaproxy\":{\"hosts\":[{\"passwd\":\"Mfjjbsq7!@#\",\"ip\":\"10.1.241.126\",\"root\":\"root\",\"id\":1},{\"passwd\":\"Mfjjbsq7!@#\",\"ip\":\"10.1.241.130\",\"root\":\"root\",\"id\":2}],\"loadOnly\":true,\"virtualIp\":\"10.1.241.14\"},\"aid\":\"dev\"}";
    service.freeResourcesService(param);
  }
}
