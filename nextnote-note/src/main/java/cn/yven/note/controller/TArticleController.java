package cn.yven.note.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.yven.common.annotation.Anonymous;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.yven.common.annotation.Log;
import cn.yven.common.core.controller.BaseController;
import cn.yven.common.core.domain.AjaxResult;
import cn.yven.common.enums.BusinessType;
import cn.yven.note.domain.TArticle;
import cn.yven.note.service.ITArticleService;
import cn.yven.common.utils.poi.ExcelUtil;
import cn.yven.common.core.page.TableDataInfo;

/**
 * 文章管理Controller
 * 
 * @author yven
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/note/article")
public class TArticleController extends BaseController
{
    @Autowired
    private ITArticleService tArticleService;

    /**
     * 查询文章管理列表
     */
    @PreAuthorize("@ss.hasPermi('note:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(TArticle tArticle)
    {
        startPage();
        List<TArticle> list = tArticleService.selectTArticleList(tArticle);
        return getDataTable(list);
    }

    /**
     * 导出文章管理列表
     */
    @PreAuthorize("@ss.hasPermi('note:article:export')")
    @Log(title = "文章管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TArticle tArticle)
    {
        List<TArticle> list = tArticleService.selectTArticleList(tArticle);
        ExcelUtil<TArticle> util = new ExcelUtil<TArticle>(TArticle.class);
        util.exportExcel(response, list, "文章管理数据");
    }

    /**
     * 获取文章管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('note:article:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tArticleService.selectTArticleById(id));
    }

    /**
     * 新增文章管理
     */
    @PreAuthorize("@ss.hasPermi('note:article:add')")
    @Log(title = "文章管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TArticle tArticle)
    {
        return toAjax(tArticleService.insertTArticle(tArticle));
    }

    /**
     * 修改文章管理
     */
    @PreAuthorize("@ss.hasPermi('note:article:edit')")
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TArticle tArticle)
    {
        return toAjax(tArticleService.updateTArticle(tArticle));
    }

    /**
     * 删除文章管理
     */
    @PreAuthorize("@ss.hasPermi('note:article:remove')")
    @Log(title = "文章管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tArticleService.deleteTArticleByIds(ids));
    }
}
