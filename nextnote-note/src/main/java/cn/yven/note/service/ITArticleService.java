package cn.yven.note.service;

import java.util.List;
import cn.yven.note.domain.TArticle;

/**
 * 文章管理Service接口
 * 
 * @author yven
 * @date 2025-03-24
 */
public interface ITArticleService 
{
    /**
     * 查询文章管理
     * 
     * @param id 文章管理主键
     * @return 文章管理
     */
    public TArticle selectTArticleById(Long id);

    /**
     * 查询文章管理列表
     * 
     * @param tArticle 文章管理
     * @return 文章管理集合
     */
    public List<TArticle> selectTArticleList(TArticle tArticle);

    /**
     * 新增文章管理
     * 
     * @param tArticle 文章管理
     * @return 结果
     */
    public int insertTArticle(TArticle tArticle);

    /**
     * 修改文章管理
     * 
     * @param tArticle 文章管理
     * @return 结果
     */
    public int updateTArticle(TArticle tArticle);

    /**
     * 批量删除文章管理
     * 
     * @param ids 需要删除的文章管理主键集合
     * @return 结果
     */
    public int deleteTArticleByIds(Long[] ids);

    /**
     * 删除文章管理信息
     * 
     * @param id 文章管理主键
     * @return 结果
     */
    public int deleteTArticleById(Long id);
}
