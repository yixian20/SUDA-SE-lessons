package cn.enilu.flash.service.message.sms.tencent;

import cn.enilu.flash.bean.enumeration.ConfigKeyEnum;
import cn.enilu.flash.cache.ConfigCache;
import cn.enilu.flash.service.message.sms.SmsSender;
import cn.enilu.flash.utils.Lists;
import cn.enilu.flash.utils.StringUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
@Service
public class TencentSmsSender implements SmsSender {

    private final ConfigCache configCache;

    public TencentSmsSender(ConfigCache configCache) {
        this.configCache = configCache;
    }

    @Override
    public boolean sendSms(String tplCode, String receiver, LinkedHashMap params, String content) {
        Integer appid = Integer.valueOf((String) configCache.get(ConfigKeyEnum.API_TENCENT_SMS_APPID.getValue()));
        String appkey = (String) configCache.get(ConfigKeyEnum.API_TENCENT_SMS_APPKEY.getValue());
        String smsSign = (String) configCache.get(ConfigKeyEnum.API_TENCENT_SMS_SIGN.getValue());
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = null;
        try {
            if (StringUtil.isNotEmpty(tplCode)) {
                ArrayList list = (ArrayList) Lists.newArrayList(params.values());
                //根据指定模板id发送短信
                result = ssender.sendWithParam("86", receiver,
                        Integer.valueOf(tplCode), list, smsSign, "", "");
            } else {
                //发送固定内容短信
                result = ssender.send(0, "86", receiver,
                        content, "", "");

            }
            log.info(result.errMsg);
            return result.result == 0;
        } catch (Exception e) {
            log.error("发送短信异常", e);
        }

        return false;
    }

}
