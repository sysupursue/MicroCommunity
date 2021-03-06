package com.java110.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.smo.ownerLogin.IOwnerAppLoginSMO;
import com.java110.app.smo.propertyLogin.impl.wxLogin.IPropertyAppLoginSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.utils.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序登录处理类
 */
@RestController
@RequestMapping(path = "/app")
public class OwnerAppLoginController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(OwnerAppLoginController.class);

    @Autowired
    private IOwnerAppLoginSMO ownerAppLoginSMOImpl;


    /**
     * 微信登录接口
     *
     * @param postInfo
     * @param request
     */
    @RequestMapping(path = "/loginOwner", method = RequestMethod.POST)
    public ResponseEntity<String> loginOwner(@RequestBody String postInfo, HttpServletRequest request) {
        /*IPageData pd = (IPageData) request.getAttribute(CommonConstant.CONTEXT_PAGE_DATA);*/
        IPageData pd = PageData.newInstance().builder("", "", "", postInfo,
                "login", "", "", "",
                request.getHeader("APP_ID"));
        ResponseEntity<String> responseEntity = ownerAppLoginSMOImpl.doLogin(pd);
        JSONObject outParam = JSONObject.parseObject(responseEntity.getBody());
        pd.setToken(outParam.getString("token"));
        request.setAttribute(CommonConstant.CONTEXT_PAGE_DATA,pd);
        return responseEntity;
    }

}
