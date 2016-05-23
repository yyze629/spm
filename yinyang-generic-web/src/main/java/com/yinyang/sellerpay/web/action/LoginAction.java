package com.yinyang.sellerpay.web.action;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.dhgate.common.util.DHgateSession;
import com.dhgate.logging.Log;
import com.dhgate.logging.LogFactory;
import com.dhgate.merchant.dto.UserSessionDTO;
import com.dhgate.skeleton.web.action.BaseAction;
import com.opensymphony.xwork2.ActionSupport;
import com.yinyang.sellerpay.web.form.CallbackBusinessForm;
import com.yinyang.sellerpay.web.form.LoginForm;
import com.yinyang.sellerpay.web.service.LoginDelegate;
import com.yinyang.sellerpay.web.service.PayEaseDelegate;
import com.yinyang.sellerpay.web.util.AppConstant;
import com.yinyang.sellerpay.web.util.Constant;
import com.yinyang.sellerpay.web.util.DHDiamondManager;
import com.yinyang.sellerpay.web.util.DHgateSellerSessionFactory;
import com.yinyang.sellerpay.web.util.PayEaseConstant;
import com.yinyang.sellerpay.web.util.ProdManageConstant;

/**
 * 登录Action
 * 
 * @Title: LoginAction.java
 * @Package com.yinyang.sellerpay.web.action
 * @author yinyang@dhgate.com
 * @date 2016年1月18日 下午5:57:03
 * @version V1.0
 */
//@Controller 
//@Scope("prototype") 
@ParentPackage("default")
@Namespace("/logined")
public class LoginAction extends BaseAction{//ActionSupport{//extends BaseAction {
	
	private static final long serialVersionUID = 8113261741491629552L;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	protected Map<String, Object> session;
	protected Integer selectpagesize;
	protected Integer page;
	//protected String dhpath;
	protected final Log logger = LogFactory.getLogger(LoginAction.class);
	
	private LoginForm loginForm;
	
	//@Autowired
	LoginDelegate loginDelegate;

	public LoginAction() {
	}
	
	//		    @Result(name="LP",location="/content/store/storeflowlp.vm", type="velocity") 
	//@Action(value = "login", results = { @Result(name = "success", type = "velocity", location = "/content/template/main.vm")})
	
	@Action(value = "login1", results = { 
			@Result(name="success",location="/content/template/main.vm", type="velocity"),
		    @Result(name="error",location="/content/exception.jsp", type="velocity")
			})
	public String login1() throws Exception{
		
		try {
			if(loginForm == null){
				loginForm = new LoginForm();
			}
			logger.info("登录入参,loginForm:"+loginForm.toString());
			Boolean result = this.loginDelegate.isGetLoginOn(loginForm.getLoginName(), loginForm.getLoginPassword());
			if(!result){
				request.setAttribute("errorMsg", "密码或用户名不正确!");
			}
		} catch (Exception e) {
			logger.error("登录异常,"+e);
		}
		return SUCCESS;
	}
	
	//@Result(name="success",location="/content/template/main.vm", type="velocity"),
	//@Result(name="success",location="/content/jsptemplate/main.jsp", type="dispatcher"),
	@Action(value = "login", results = { 
			@Result(name="success",location="/template/main.vm", type="velocity"),
			@Result(name="error",location="/content/exception.jsp", type="velocity")
	})
	public String login() throws Exception{
		
		try {
			if(loginForm == null){
				loginForm = new LoginForm();
			}
			logger.info("登录入参,loginForm:"+loginForm.toString());
			//Boolean result = this.loginDelegate.isGetLoginOn(loginForm.getLoginName(), loginForm.getLoginPassword());
			Boolean result = true;
			if(!result){
				request.setAttribute("errorMsg", "密码或用户名不正确!");
			}
		} catch (Exception e) {
			logger.error("登录异常,"+e);
		}
		return SUCCESS;
	}
	
	private String getMinimalSessionField(String field, String minimalSessionString) {
		String fieldVal = null;
		if (field != null && minimalSessionString != null) {
			String[] keyVals = minimalSessionString.split(String.valueOf((char) 28));
			if (keyVals != null && keyVals.length > 0) {
				for (String keyVal : keyVals) {
					if (keyVal != null && keyVal.trim().length() > 0) {
						String[] keyValArry = keyVal.split(String.valueOf((char) 29));
						if (keyValArry != null && keyValArry.length == 2) {
							if (field.equalsIgnoreCase(keyValArry[0])) {
								fieldVal = keyValArry[1];
								break;
							}
						}
					}
				}
			}
		}
		return fieldVal;
	}
	
	@Action(value = "call", results = { @Result(name = "success", type = "velocity", location = "/content/template/main.vm")})
	public String callbackBusiness() throws Exception{
		String dispatcheAction = SUCCESS;
		try {
			if(loginForm == null){
				loginForm = new LoginForm();
			}
			logger.info("登录入参,loginForm:"+loginForm.toString());
			
			
			request.setAttribute("errorMsg", "");
		} catch (Exception e) {
			logger.error("登录异常,"+e);
		}
		return dispatcheAction;
	}

	/**
	 * Convenience method to get the request
	 * 
	 * @return current request
	 */
	protected HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Convenience method to get the response
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse() {
		return this.response;
	}

	/*@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
		NumberFormat number = NumberFormat.getInstance();
		number.setMinimumFractionDigits(2);
		number.setMaximumFractionDigits(2);
		number.setRoundingMode(RoundingMode.HALF_UP);
		this.request.setAttribute("number", number);
	}*/

	/*@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}*/

	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void put(String key, Object obj) {
		request.setAttribute(key, obj);
	}
	
	public Integer getSelectpagesize() {
		return selectpagesize;
	}
	public void setSelectpagesize(Integer selectpagesize) {
		this.selectpagesize = selectpagesize;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	
	public LoginForm getLoginForm() {
		return loginForm;
	}

	public void setLoginForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}

	
	
}
