package cn.yven.note.service;

import java.util.List;
import cn.yven.note.domain.TCategory;
import cn.yven.note.domain.vo.TCategoryVO;

/**
 * 分类Service接口
 * 
 * @author yven
 * @date 2025-03-21
 */
public interface ITCategoryService 
{
    /**
     * 查询分类
     * 
     * @param id 分类主键
     * @return 分类
     */
    public TCategory selectTCategoryById(Long id);

    /**
     * 查询分类列表
     * 
     * @param tCategory 分类
     * @return 分类集合
     */
    public List<TCategory> selectTCategoryList(TCategory tCategory);

    /**
     * 查询分类列表
     * 
     * @param tCategory 分类
     * @return 分类集合
     */
    public List<TCategoryVO> selectTCategoryAndCountList(TCategory tCategory);

    /**
     * 新增分类
     * 
     * @param tCategory 分类
     * @return 结果
     */
    public int insertTCategory(TCategory tCategory);

    /**
     * 修改分类
     * 
     * @param tCategory 分类
     * @return 结果
     */
    public int updateTCategory(TCategory tCategory);

    /**
     * 批量删除分类
     * 
     * @param ids 需要删除的分类主键集合
     * @return 结果
     */
    public int deleteTCategoryByIds(Long[] ids);

    /**
     * 删除分类信息
     * 
     * @param id 分类主键
     * @return 结果
     */
    public int deleteTCategoryById(Long id);
}
