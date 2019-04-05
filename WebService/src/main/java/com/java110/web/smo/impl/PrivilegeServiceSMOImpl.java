package com.java110.web.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.util.Assert;
import com.java110.core.context.IPageData;
import com.java110.web.core.BaseComponentSMO;
import com.java110.web.smo.IPrivilegeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("privilegeServiceSMOImpl")
public class PrivilegeServiceSMOImpl extends BaseComponentSMO implements IPrivilegeServiceSMO {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查询 权限组
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> listPrivilegeGroup(IPageData pd) {

        Assert.hasLength(pd.getUserId(),"用户未登录请先登录");

        ResponseEntity<String> storeInfo = super.getStoreInfo(pd,restTemplate);

        if(storeInfo.getStatusCode() != HttpStatus.OK){
            return storeInfo;
        }
        // 商户返回信息
        JSONObject storeInfoObj = JSONObject.parseObject(storeInfo.getBody());

        String  storeId = storeInfoObj.getString("storeId");
        String  storeTypeCd = storeInfoObj.getString("storeTypeCd");

        //根据商户ID查询 权限组信息


        ResponseEntity<String> privilegeGroup = super.callCenterService(restTemplate,pd,"",
                ServiceConstant.SERVICE_API_URL+"/api/query.store.privilegeGroup?storeId="+storeId +"&storeTypeCd="+storeTypeCd, HttpMethod.GET);
        if(privilegeGroup.getStatusCode() != HttpStatus.OK){
            return privilegeGroup;
        }

        JSONObject privilegeGroupObj = JSONObject.parseObject(privilegeGroup.getBody().toString());

        Assert.jsonObjectHaveKey(privilegeGroupObj,"privilegeGroups","查询菜单未返回privilegeGroups节点");

        JSONArray privilegeGroups = privilegeGroupObj.getJSONArray("privilegeGroups");

        return new ResponseEntity<String>(privilegeGroups.toJSONString(),HttpStatus.OK);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}