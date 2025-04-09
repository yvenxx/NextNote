package cn.yven.note.service.impl;

import java.util.List;
import cn.yven.common.utils.DateUtils;
import cn.yven.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.yven.note.mapper.TArticleMapper;
import cn.yven.note.domain.TArticle;
import cn.yven.note.service.ITArticleService;

/**
 * 文章管理Service业务层处理
 * 
 * @author yven
 * @date 2025-03-24
 */
@Service
public class TArticleServiceImpl implements ITArticleService 
{
    @Autowired
    private TArticleMapper tArticleMapper;

    /**
     * 查询文章管理
     * 
     * @param id 文章管理主键
     * @return 文章管理
     */
    @Override
    public TArticle selectTArticleById(Long id)
    {
        return tArticleMapper.selectTArticleById(id);
    }

    /**
     * 查询文章管理列表
     * 
     * @param tArticle 文章管理
     * @return 文章管理
     */
    @Override
    public List<TArticle> selectTArticleList(TArticle tArticle)
    {
        return tArticleMapper.selectTArticleList(tArticle);
    }

    /**
     * 新增文章管理
     * 
     * @param tArticle 文章管理
     * @return 结果
     */
    @Override
    public int insertTArticle(TArticle tArticle)
    {
        tArticle.setCreateTime(DateUtils.getNowDate());
        tArticle.setCreateBy(SecurityUtils.getUsername());
        return tArticleMapper.insertTArticle(tArticle);
    }

    /**
     * 修改文章管理
     * 
     * @param tArticle 文章管理
     * @return 结果
     */
    @Override
    public int updateTArticle(TArticle tArticle)
    {
        tArticle.setUpdateTime(DateUtils.getNowDate());
        return tArticleMapper.updateTArticle(tArticle);
    }

    /**
     * 批量删除文章管理
     * 
     * @param ids 需要删除的文章管理主键
     * @return 结果
     */
    @Override
    public int deleteTArticleByIds(Long[] ids)
    {
        return tArticleMapper.deleteTArticleByIds(ids);
    }

    /**
     * 删除文章管理信息
     * 
     * @param id 文章管理主键
     * @return 结果
     */
    @Override
    public int deleteTArticleById(Long id)
    {
        return tArticleMapper.deleteTArticleById(id);
    }
}
