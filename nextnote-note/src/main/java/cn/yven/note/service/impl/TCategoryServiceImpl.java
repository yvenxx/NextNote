package cn.yven.note.service.impl;

import java.util.List;
import cn.yven.common.utils.DateUtils;
import cn.yven.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yven.note.mapper.TCategoryMapper;
import cn.yven.note.domain.TCategory;
import cn.yven.note.service.ITCategoryService;

/**
 * 分类Service业务层处理
 * 
 * @author yven
 * @date 2025-03-21
 */
@Service
public class TCategoryServiceImpl implements ITCategoryService 
{
    @Autowired
    private TCategoryMapper tCategoryMapper;

    /**
     * 查询分类
     * 
     * @param id 分类主键
     * @return 分类
     */
    @Override
    public TCategory selectTCategoryById(Long id)
    {
        return tCategoryMapper.selectTCategoryById(id);
    }

    /**
     * 查询分类列表
     * 
     * @param tCategory 分类
     * @return 分类
     */
    @Override
    public List<TCategory> selectTCategoryList(TCategory tCategory)
    {
        return tCategoryMapper.selectTCategoryList(tCategory);
    }

    /**
     * 新增分类
     * 
     * @param tCategory 分类
     * @return 结果
     */
    @Override
    public int insertTCategory(TCategory tCategory)
    {
        tCategory.setCreateTime(DateUtils.getNowDate());
        tCategory.setCreateBy(SecurityUtils.getUsername());
        return tCategoryMapper.insertTCategory(tCategory);
    }

    /**
     * 修改分类
     * 
     * @param tCategory 分类
     * @return 结果
     */
    @Override
    public int updateTCategory(TCategory tCategory)
    {
        tCategory.setUpdateTime(DateUtils.getNowDate());
        tCategory.setUpdateBy(SecurityUtils.getUsername());
        return tCategoryMapper.updateTCategory(tCategory);
    }

    /**
     * 批量删除分类
     * 
     * @param ids 需要删除的分类主键
     * @return 结果
     */
    @Override
    public int deleteTCategoryByIds(Long[] ids)
    {
        return tCategoryMapper.deleteTCategoryByIds(ids);
    }

    /**
     * 删除分类信息
     * 
     * @param id 分类主键
     * @return 结果
     */
    @Override
    public int deleteTCategoryById(Long id)
    {
        return tCategoryMapper.deleteTCategoryById(id);
    }
}
