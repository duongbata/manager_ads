package app.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.bean.App05DataTrans;
import app.bean.GroupAppBean;
import app.bean.OSConfigBean;
import app.logic.App05LogicIF;
import manager.common.bean.InfoValue;
import manager.common.bean.UserBean;

@Controller
public class App05Action {
	private InfoValue info;
	
	private GroupAppBean groupAppEdit;
	
	@Autowired
	private App05LogicIF app05Logic;
	
	@Action(value="/APP05_init"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String init() {
		App05DataTrans dataTrans = new App05DataTrans();
		dataTrans.setListGroupApp(app05Logic.listAllGroup());
		info.setDataTrans(dataTrans);
		
		return "success";
	}
	
	@Action(value="/APP05_initEditGroupApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_2",type="tiles")
				, @Result(name="failure",location="ERROR", type="tiles")
			})
	public String initEditGroupApp() {
		String groupId = ServletActionContext.getRequest().getParameter("groupId");
		groupAppEdit = app05Logic.getGroupAppById(groupId);
		App05DataTrans dataTrans = (App05DataTrans) info.getDataTrans();
		
		Map<String, String> mapOs = new HashMap<String, String>();
		mapOs.put(APP01Action.OS_IOS_ID, "Ios");
		mapOs.put(APP01Action.OS_ANDROID_ID, "Android");
		mapOs.put(APP01Action.OS_WINDOWS_ID, "Windows");
		mapOs.put("-1", "");
		dataTrans.setMapOs(mapOs);
		
		List<UserBean> listAllDev = new ArrayList<UserBean>();
		listAllDev.addAll(app05Logic.listAllDev());
		dataTrans.setListDev(listAllDev);
		
		return "success";
	}
	
	@Action(value="/APP05_editGroupApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_2",type="tiles")
				, @Result(name="failure",location="APP05_2", type="tiles")
			})
	public String editGroupApp(){
		//Validate
		String message = app05Logic.validateGroupAppEdit(groupAppEdit);
		if (message != null) {
			App05DataTrans app05DataTrans = (App05DataTrans) info.getDataTrans();
			app05DataTrans.setMessage(message);
			return "failure";
		}
		if (groupAppEdit.getListOsConfig() == null) {
			groupAppEdit.setListOsConfig(new ArrayList<OSConfigBean>());
		}
		//update
		try {
			app05Logic.updateGroupApp(groupAppEdit, String.valueOf(info.getUser().getId()));
		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return "success";
	}
	
	@Action(value="/APP05_initDetailApp"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
			}
			, results={
				@Result(name="success",location="APP05_3",type="tiles")
				, @Result(name="failure",location="APP05_2", type="tiles")
			})
	public String initDetailApp() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String uid = request.getParameter("uid");
		String appId = request.getParameter("appId");
		if (uid == null || appId == null) {
			return "failure";
		}
		System.out.println(uid + "----" + appId);
		return "success";
	}
	
	public InfoValue getInfo() {
		return info;
	}
	
	public void setInfo(InfoValue info) {
		this.info = info;
	}

	public GroupAppBean getGroupAppEdit() {
		return groupAppEdit;
	}

	public void setGroupAppEdit(GroupAppBean groupAppEdit) {
		this.groupAppEdit = groupAppEdit;
	}
}
