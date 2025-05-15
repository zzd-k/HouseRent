import request from '@/utils/request'

export function getContractList(params) {


    return request.get('/contract/list', { params })
}
export function signContractById(id,role) {
    return request({
        url: `/contract/sign/${id}`,
        method: 'put',
        params: { role }
    })
}


export function createContractService(data) {
    return request.post('/contract',  data )
}
// 获取合同详情
export function getContractDetail(id) {
    return request({
        url: `/contract/${id}`,
        method: 'get'
    })
}

export function getContractsByLandlordAndTenant(landlordId, tenantId) {
    const params = { landlordId };
    if (tenantId !== undefined && tenantId !== null) {
        params.tenantId = tenantId;
    }
    return request({
        url: '/contract/landlord',
        method: 'get',
        params
    });
}
export function getContractsByTenantId(tenantId) {
    return request({
        url: '/contract/tenant',
        method: 'get',
        tenantId
    })
}

export const getTenantListByHouseIdService = (houseId) => {
    return request.get(`/front/tenantListByHouse/${houseId}`)
}