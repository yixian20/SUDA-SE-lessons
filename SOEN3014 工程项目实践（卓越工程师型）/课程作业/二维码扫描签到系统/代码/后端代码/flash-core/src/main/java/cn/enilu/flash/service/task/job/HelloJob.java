package cn.enilu.flash.service.task.job;

import cn.enilu.flash.bean.entity.system.Cfg;
import cn.enilu.flash.service.system.CfgService;
import cn.enilu.flash.service.task.JobExecuter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * HelloJob
 *
 * @author zt
 * @version 2018/12/30 0030
 */
@Slf4j
@Component
public class HelloJob extends JobExecuter {

    private final CfgService cfgService;

    public HelloJob(CfgService cfgService) {
        this.cfgService = cfgService;
    }

    @Override
    public void execute(Map<String, Object> dataMap) throws Exception {
        Cfg cfg = cfgService.get(1L);
        cfg.setCfgDesc("update by " + DateUtil.getTime());
        cfgService.update(cfg);
        log.info("hello :" + JsonUtil.toJson(dataMap));
    }

}
