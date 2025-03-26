import request from '@/utils/request'

// 查询文章管理列表
export function listArticle(query) {
    return request({
        url: '/note/article/list',
        method: 'get',
        params: query
    })
}

// 查询分类列表
export function listCategory(query) {
    return request({
        url: '/note/category/public/list-category',
        method: 'get',
        params: query
    })
}
