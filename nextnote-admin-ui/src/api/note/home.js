import request from '@/utils/request'

// 查询文章管理列表
export function listArticle(query) {
    return request({
        url: '/home/public/list-article',
        method: 'get',
        params: query
    })
}

// 查询分类列表
export function listCategory(query) {
    return request({
        url: '/home/public/list-category',
        method: 'get',
        params: query
    })
}

// 查询分类详细
export function getCategoryById(id) {
    return request({
        url: '/home/public/getCategoryById',
        params: { id: id },
        method: 'get'
    })
}

// 获取文章详情
export function getArticleById(id) {
    return request({
        url: '/home/public/getArticleById',
        params: { id: id },
        method: 'get'
    })
}
