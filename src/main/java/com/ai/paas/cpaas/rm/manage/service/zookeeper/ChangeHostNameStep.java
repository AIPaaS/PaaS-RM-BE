package com.ai.paas.cpaas.rm.manage.service.zookeeper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.ai.paas.cpaas.rm.util.AnsibleCommand;
import com.ai.paas.cpaas.rm.util.TaskUtil;
import com.ai.paas.cpaas.rm.vo.MesosInstance;
import com.ai.paas.cpaas.rm.vo.MesosSlave;
import com.ai.paas.cpaas.rm.vo.OpenResourceParamVo;

public class ChangeHostNameStep implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    // 上传hostnamectl.yml
    InputStream in = ChangeHostNameStep.class.getResourceAsStream("/playbook/hostnamectl.yml");
    String content = TaskUtil.getFile(in);
    OpenResourceParamVo openParam = TaskUtil.createOpenParam(chunkContext);
    Boolean useAgent = openParam.getUseAgent();
    TaskUtil.uploadFile("hostnamectl.yml", content, useAgent);
    List<MesosInstance> mesosMaster = openParam.getMesosMaster();
    List<MesosSlave> mesosSlave = openParam.getMesosSlave();
    StringBuffer shellContext = TaskUtil.createBashFile();
    for (int i = 0; i < mesosMaster.size(); i++) {
      MesosInstance instance = mesosMaster.get(i);
      String ip = instance.getIp();
      String name =
          (String) chunkContext.getStepContext().getStepExecution().getJobExecution()
              .getExecutionContext().get(ip);
      this.genCommand(instance, shellContext, name);
    }
    for (int i = 0; i < mesosSlave.size(); i++) {
      MesosSlave instance = mesosSlave.get(i);
      String ip = instance.getIp();
      String name =
          (String) chunkContext.getStepContext().getStepExecution().getJobExecution()
              .getExecutionContext().get(ip);
      this.genCommand(instance, shellContext, name);
    }
    // 将shellContext传到服务端，并执行
    TaskUtil.executeFile("changehostnames", shellContext.toString(), useAgent);
    return RepeatStatus.FINISHED;
  }

  public void genCommand(MesosInstance instance, StringBuffer shellContext, String hosts) {
    List<String> vars = new ArrayList<String>();
    StringBuffer hostname = new StringBuffer();
    hostname.append("hostname=");
    hostname.append(hosts);
    String hostsParam = "hosts=" + hosts;
    String password = "ansible_ssh_pass=" + instance.getPasswd();
    vars.add(hostname.toString());
    vars.add(password);
    vars.add(hostsParam);
    AnsibleCommand ansibleCommand =
        new AnsibleCommand(TaskUtil.getSystemProperty("filepath") + "/hostnamectl.yml",
            instance.getRoot(), vars);
    shellContext.append(ansibleCommand.toString());
    shellContext.append(System.lineSeparator());
  }
}
