package cn.yven.note.controller;

import cn.yven.common.annotation.Anonymous;
import cn.yven.common.core.controller.BaseController;
import cn.yven.common.core.domain.AjaxResult;
import cn.yven.note.domain.TArticle;
import cn.yven.note.domain.TCategory;
import cn.yven.note.domain.vo.TCategoryVO;
import cn.yven.note.service.ITArticleService;
import cn.yven.note.service.ITCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.yven.common.core.page.TableDataInfo;

import java.util.List;

import static cn.yven.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/home")
public class HomeController extends BaseController {
    @Autowired
    private ITCategoryService tCategoryService;

    @Autowired
    private ITArticleService tArticleService;

    /**
     * 查询文章管理列表
     */
    @Anonymous
    @GetMapping("/public/list-article")
    public TableDataInfo list(TArticle tArticle)
    {
        // 分页查询
        startPage();
        List<TArticle> list = tArticleService.selectTArticleList(tArticle);
        return getDataTable(list);
    }

    /**
     * home页面查询分类列表
     * @param tCategory
     */
    @Anonymous
    @GetMapping("/public/list-category")  // 为了确保路径正确
    public AjaxResult categoryAndCountList(TCategory tCategory) {
        List<TCategoryVO> list = tCategoryService.selectTCategoryAndCountList(tCategory);
        return AjaxResult.success(list);
    }

    /**
     * home页面查询分类详细
     * @param id
     */
    @Anonymous
    @GetMapping("/public/getCategoryById")
    public AjaxResult getCategoryById(Long id) {
        TCategory tCategory = tCategoryService.selectTCategoryById(id);
        return AjaxResult.success(tCategory);
    }

    /**
     * 获取文章详情
     * @param id
     */
    @Anonymous
    @GetMapping("/public/getArticleById")
    public AjaxResult getArticleById(Long id) {
        TArticle tArticle = tArticleService.selectTArticleById(id);
        return AjaxResult.success(tArticle);
    }
}
