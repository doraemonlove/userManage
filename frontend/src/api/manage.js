import request from '@/util/request'

export function getUserList(params) {
    return request({
        url: '/user',
        method: 'get',
        params
    })
}

export function queryUserByName(params) {
    return request({
        url: '/user/getByName',
        method: 'get',
        params
    })
}

export function addOrUpdateUser(params) {
    return request({
        url: '/user/saveOrUpdate',
        method: 'post',
        data: JSON.stringify(params),
        headers: {
            'Content-Type': 'application/json' // 注意：正确的 MIME 类型是 'application/json'
        }
    })
}


export function deleteUser(params) {
    return request({
        url: '/user/deleteByIds',
        method: 'delete',
        data: JSON.stringify(params),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
