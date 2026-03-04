<#assign mainVarCap = entity?uncap_first?replace(package.ModuleName, "")>
<#assign mainVar = mainVarCap?uncap_first>
import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>',
    method: 'post',
    data
  })
}

export function del(data) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>',
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>',
    method: 'put',
    data
  })
}

export function getById(id) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>/' + id,
    method: 'get'
  })
}

export function listByPage(params) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>',
    method: 'get',
    params
  })
}

export function listByNoPage(params) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>/noPaging',
    method: 'get',
    params
  })
}

export function download(params) {
  return request({
    url: 'api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>/download',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
