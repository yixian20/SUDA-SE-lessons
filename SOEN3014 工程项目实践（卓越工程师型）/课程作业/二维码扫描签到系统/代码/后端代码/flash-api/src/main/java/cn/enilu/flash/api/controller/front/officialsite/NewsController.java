package cn.enilu.flash.api.controller.front.officialsite;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.enumeration.cms.BannerTypeEnum;
import cn.enilu.flash.bean.enumeration.cms.ChannelEnum;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.offcialsite.BannerVo;
import cn.enilu.flash.bean.vo.offcialsite.News;
import cn.enilu.flash.service.cms.ArticleService;
import cn.enilu.flash.service.cms.BannerService;
import cn.enilu.flash.utils.factory.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offcialsite/news")
public class NewsController extends BaseController {

    private final BannerService bannerService;

    private final ArticleService articleService;

    public NewsController(BannerService bannerService, ArticleService articleService) {
        this.bannerService = bannerService;
        this.articleService = articleService;
    }

    @GetMapping
    public Object list() {
        Map<String, Object> dataMap = new HashMap<>(10);
        BannerVo banner = bannerService.queryBanner(BannerTypeEnum.NEWS.getValue());
        dataMap.put("banner", banner);

        List<News> newsList = new ArrayList<>();
        Page<Article> articlePage = articleService.query(1, 10, ChannelEnum.NEWS.getId());

        for (Article article : articlePage.getRecords()) {
            News news = new News();
            news.setDesc(article.getTitle());
            news.setUrl("/article?id=" + article.getId());
            news.setSrc("static/images/icon/user.png");
            news.setId(article.getId());
            newsList.add(news);
        }

        dataMap.put("list", newsList);

        Map map = new HashMap(2);

        map.put("data", dataMap);
        return Rets.success(map);
    }

}
