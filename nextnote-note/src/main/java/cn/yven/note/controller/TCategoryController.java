package cn.yven.note.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import cn.yven.note.domain.TCategory;
import cn.yven.note.service.ITCategoryService;
import cn.yven.common.utils.poi.ExcelUtil;
import cn.yven.common.core.page.TableDataInfo;

/**
 * 分类Controller
 * 
 * @author yven
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/note/category")
public class TCategoryController extends BaseController
{
    @Autowired
    private ITCategoryService tCategoryService;

    /**
     * 查询分类列表
     */
    @PreAuthorize("@ss.hasPermi('note:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCategory tCategory)
    {
        startPage();
        List<TCategory> list = tCategoryService.selectTCategoryList(tCategory);
        return getDataTable(list);
    }

    /**
     * 导出分类列表
     */
    @PreAuthorize("@ss.hasPermi('note:category:export')")
    @Log(title = "分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCategory tCategory)
    {
        List<TCategory> list = tCategoryService.selectTCategoryList(tCategory);
        ExcelUtil<TCategory> util = new ExcelUtil<TCategory>(TCategory.class);
        util.exportExcel(response, list, "分类数据");
    }

    /**
     * 获取分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('note:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCategoryService.selectTCategoryById(id));
    }

    /**
     * 新增分类
     */
    @PreAuthorize("@ss.hasPermi('note:category:add')")
    @Log(title = "分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCategory tCategory)
    {
        return toAjax(tCategoryService.insertTCategory(tCategory));
    }

    /**
     * 修改分类
     */
    @PreAuthorize("@ss.hasPermi('note:category:edit')")
    @Log(title = "分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCategory tCategory)
    {
        return toAjax(tCategoryService.updateTCategory(tCategory));
    }

    /**
     * 删除分类
     */
    @PreAuthorize("@ss.hasPermi('note:category:remove')")
    @Log(title = "分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCategoryService.deleteTCategoryByIds(ids));
    }
}
