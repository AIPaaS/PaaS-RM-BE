package com.ai.paas.cpaas.rm.manage.service.zookeeper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.ai.paas.cpaas.rm.util.AnsibleCommand;
import com.ai.paas.cpaas.rm.util.OpenPortUtil;
import com.ai.paas.cpaas.rm.util.TaskUtil;
import com.ai.paas.cpaas.rm.vo.MesosInstance;
import com.ai.paas.cpaas.rm.vo.OpenResourceParamVo;

public class ModifyZkConf implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    InputStream in =
        OpenPortUtil.class.getResourceAsStream("/playbook/zookeeper/zookeeperinstall.yml");
    String content = TaskUtil.getFile(in);
    OpenResourceParamVo openParam = TaskUtil.createOpenParam(chunkContext);
    Boolean useAgent = openParam.getUseAgent();
    TaskUtil.uploadFile("zookeeperinstall.yml", content, useAgent);

    List<MesosInstance> mesosMaster = openParam.getMesosMaster();
    MesosInstance instance = mesosMaster.get(0);
    String password = instance.getPasswd();
    List<String> configvars = new ArrayList<String>();
    configvars.add("ansible_ssh_pass=" + password);
    configvars.add("ansible_become_pass=" + password);
    StringBuffer lines = new StringBuffer();
    lines
        .append("lines=['clientPort=2181','initLimit=10','syncLimit=5','dataDir=/home/rczkp01/zookeeper/data','dataLogDir=/home/rczkp01/zookeeper/log'");
    for (int i = 0; i < mesosMaster.size(); i++) {
      lines.append(",'server." + (i + 1) + "=" + mesosMaster.get(i).getIp() + ":2888:3888'");
    }
    lines.append("]");
    configvars.add(lines.toString());
    AnsibleCommand zookeeperinstall =
        new AnsibleCommand(TaskUtil.getSystemProperty("filepath") + "/zookeeperinstall.yml",
            "root", configvars);
    TaskUtil.executeCommand(zookeeperinstall.toString(), useAgent);
    return RepeatStatus.FINISHED;
  }

}
