package cn.enilu.flash.api.controller.message;

import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.message.MessageSender;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.message.MessageSenderService;
import cn.enilu.flash.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/message/sender")
public class MessageSenderController {

    private final MessageSenderService messageSenderService;

    public MessageSenderController(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.MSG_SENDER})
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String className) {
        Page<MessageSender> page = new PageFactory<MessageSender>().defaultPage();
        page.addFilter("name", name);
        page.addFilter("className", className);
        page = messageSenderService.queryPage(page);
        page.setRecords(page.getRecords());
        return Rets.success(page);
    }

    @GetMapping(value = "/queryAll")
    @RequiresPermissions(value = {Permission.MSG_SENDER})
    public Object queryAll() {
        return Rets.success(messageSenderService.queryAll());
    }

    @PostMapping
    @BussinessLog(value = "编辑消息发送者", key = "name")
    @RequiresPermissions(value = {Permission.MSG_SENDER_EDIT})
    public Object save(@RequestBody @Valid MessageSender messageSender) {
        if (messageSender.getId() != null) {
            MessageSender old = messageSenderService.get(messageSender.getId());
            old.setName(messageSender.getName());
            old.setClassName(messageSender.getClassName());
            messageSenderService.update(old);
        } else {
            MessageSender old = messageSenderService.get(SearchFilter.build("className", messageSender.getClassName()));
            if (old != null) {
                return Rets.failure("改短信发送器已存在，请勿重复添加");
            }
            messageSenderService.insert(messageSender);
        }
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除消息发送者", key = "id")
    @RequiresPermissions(value = {Permission.MSG_SENDER_DEL})
    public Object remove(Long id) {
        if (id < 4) {
            return Rets.failure("禁止删除初始化数据");
        }
        try {
            messageSenderService.delete(id);
            return Rets.success();
        } catch (Exception e) {
            return Rets.failure(e.getMessage());
        }
    }

}