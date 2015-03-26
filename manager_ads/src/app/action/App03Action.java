package app.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import manager.common.bean.InfoValue;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.bean.App03DataTrans;
import app.bean.AppBean;
import app.bean.AppConfigBean;
import app.logic.App02LogicIF;
import app.logic.App03LogicIF;

@Controller
public class App03Action {
	private InfoValue info;
	
	private List<AppBean> listAppBean;
	
	private AppBean appUpdate;
	
	private AppConfigBean appConfig;
	
	private String message;
	
	@Autowired
	private App03LogicIF app03Logic;
	
	@Autowired
	private App02LogicIF app02Logic;
	
	@Action(value="/APP03_init"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP03",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String init() {
		listAppBean = app03Logic.getListAppOfUser(String.valueOf(info.getUser().getId()));
		App03DataTrans dataTrans = new App03DataTrans();
		dataTrans.setListAppBean(listAppBean);
		info.setDataTrans(dataTrans);
		
		return "success";
	}
	
	@Action(value="/APP03_detailApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP03_2",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String detailApp(){
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String appId = request.getParameter("appId");
		if (appId == null || "".equals(appId)) {
			return "failure";
		}
		App03DataTrans dataTrans = (App03DataTrans) info.getDataTrans();
		List<AppBean> listApp = dataTrans.getListAppBean();
		for (AppBean appBean : listApp) {
			if (appId.equals(appBean.getAppId())) {
				appUpdate = appBean;
				dataTrans.setAppDetail(appUpdate);
				break;
			}
		}
		
		return "success";
	}
	
	@Action(value="/APP03_getConfigBean"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",type="json")
			})
	public String getConfigBean() throws JsonParseException, JsonMappingException, IOException {
		App03DataTrans dataTrans = (App03DataTrans) info.getDataTrans();
		AppBean appDetail = dataTrans.getAppDetail();
		if (appDetail != null) {
			/*appConfig = new ObjectMapper().readValue(appDetail.getConfig(), AppConfigBean.class);*/
			appConfig = app02Logic.createAppConfigFromStr(appDetail.getConfig());
			appConfig.setNameConfig("Config");
		}
		return "success";
	}
	
	@Action(value="/APP03_updateApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP03_2",type="tiles")
				, @Result(name="failure",location="APP03_2", type="tiles")
			})
	public String updateApp() throws Exception{
		String dataConfig = ServletActionContext.getRequest().getParameter("dataConfig");
		
		AppConfigBean appConfig = new ObjectMapper().readValue(dataConfig, AppConfigBean.class);
		String config = app02Logic.createJSONObjectFromAppConfig(appConfig).toString();
		
		App03DataTrans dataTrans = (App03DataTrans) info.getDataTrans();
		AppBean appDetail = dataTrans.getAppDetail();
		appDetail.setVersion(appUpdate.getVersion());
		appDetail.setUrl(appUpdate.getUrl());
		appDetail.setConfig(config);
		boolean isUpdated = app03Logic.updateApp(appDetail, String.valueOf(info.getUser().getId()));
		
		appUpdate = appDetail;
		if (isUpdated) {
			message = "Update Success";
			return "success";
		} else {
			message = "Update Failure";
			return "failure";
		}
	}
	
	public InfoValue getInfo() {
		return info;
	}
	
	public void setInfo(InfoValue info) {
		this.info = info;
	}

	public List<AppBean> getListAppBean() {
		return listAppBean;
	}

	public void setListAppBean(List<AppBean> listAppBean) {
		this.listAppBean = listAppBean;
	}

	public AppBean getAppUpdate() {
		return appUpdate;
	}

	public void setAppUpdate(AppBean appUpdate) {
		this.appUpdate = appUpdate;
	}

	public AppConfigBean getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfigBean appConfig) {
		this.appConfig = appConfig;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}	
