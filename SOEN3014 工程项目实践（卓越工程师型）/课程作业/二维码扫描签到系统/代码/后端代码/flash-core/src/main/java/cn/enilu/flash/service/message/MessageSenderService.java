package cn.enilu.flash.service.message;


import cn.enilu.flash.bean.entity.message.MessageSender;
import cn.enilu.flash.bean.entity.message.MessageTemplate;
import cn.enilu.flash.dao.message.MessageSenderRepository;
import cn.enilu.flash.dao.message.MessagetemplateRepository;
import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MessagesenderService
 *
 * @author enilu
 * @version 2019/05/17 0017
 */
@Service
public class MessageSenderService extends BaseService<MessageSender, Long, MessageSenderRepository> {

    private final MessageSenderRepository messageSenderRepository;

    private final MessagetemplateRepository messagetemplateRepository;

    public MessageSenderService(MessageSenderRepository messageSenderRepository, MessagetemplateRepository messagetemplateRepository) {
        this.messageSenderRepository = messageSenderRepository;
        this.messagetemplateRepository = messagetemplateRepository;
    }

    public void save(MessageSender messageSender) {
        messageSenderRepository.save(messageSender);
    }

    @Override
    public void delete(Long id) {
        List<MessageTemplate> templateList = messagetemplateRepository.findByIdMessageSender(id);
        if (templateList.isEmpty()) {
            messageSenderRepository.deleteById(id);
        } else {
            throw new RuntimeException("有模板使用该发送器，无法删除");
        }
    }

}

