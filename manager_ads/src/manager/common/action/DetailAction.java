package manager.common.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import manager.ADV01.bean.AdsBean;
import manager.ADV02.bean.AdsAdnetworkBean;
import manager.ADV02.bean.Child;
import manager.ADV02.bean.Parent;
import manager.common.bean.InfoValue;
import manager.common.bean.UserBean;
import manager.common.dao.GenericDao;
import manager.common.logic.BaseLogicIF;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


@Controller
public class DetailAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2787456428410904207L;

	@Autowired
	private BaseLogicIF baseLogic;
	
	@Autowired
	private GenericDao genericDao;
	
	private InfoValue info;
	
	private String name;
	
	/*Upload file*/
	private File file;
	
	private String contentType;
	
	private String fileName;
	
	//spring security
	private String username;
	
	private String password;
	
	/*Upload multiple file*/
	private List<File> files = new ArrayList<File>();
	
	private List<String> contents = new ArrayList<String>();
	
	private List<String> names = new ArrayList<String>();
	
	private List<UserBean> listUser = new ArrayList<UserBean>();
	
	private UserBean userBean = new UserBean();
	
	private List<Parent> listParent = new ArrayList<Parent>();
	
	
	private List<AdsBean> listAds = new ArrayList<AdsBean>();
	
	private List<AdsAdnetworkBean> listNetwork = new ArrayList<AdsAdnetworkBean>();

	@Action(value="/detail"
			, interceptorRefs={
				@InterceptorRef(value="scope",params={"key","infoValue","session","info","autoCreateSession","true"})
				, @InterceptorRef("basicStack")
				
			}
			,results={
				@Result(name="success",location="DETAIL",type="tiles")
			})
	public String detail(){
		info = new InfoValue();
		info.setMessage("trung");
		listUser = genericDao.listUser();
		AdsBean ads = new AdsBean();
		ads.setId("1");
		ads.setName("aaa");
		listAds.add(ads);
		AdsBean bbb = new AdsBean();
		bbb.setId("2");
		bbb.setName("bbbb");
		listAds.add(bbb);
		System.out.println(baseLogic.getMessage());
		AdsAdnetworkBean net1 = new AdsAdnetworkBean("5play");
		net1.setPerForAdmob("213");
		listNetwork.add(net1);
		AdsAdnetworkBean net2 = new AdsAdnetworkBean("admob");
		net2.setPerForAdmob("324");
		listNetwork.add(net2);
		Parent p1 = new Parent();
		p1.setName("nsndf");
		listParent.add(p1);
		Parent p2 = new Parent();
		p2.setName("sfsdf");
		listParent.add(p2);
		return "success";
	}
	
	@Action(value="/callAjax",results={@Result(name="success",type="json",params={"root","userBean"})})
	public String callAjax() throws IOException {
		String name = getName();
		userBean = new UserBean();
		userBean.setId("1");
		userBean.setName("trung");
		setName(new ObjectMapper().writeValueAsString("Hello nguyen thanh trung"));
		return "success";
	}
	
	@Action(value="/newRow"
			,results={
					@Result(name="success",location="DETAIL",type="tiles")
			})
	public String newRow() {
		List<UserBean> list = getListUser();
		System.out.println(list.size());
		return "success";
	}
	
	@Action(value="/doUpload"
			,interceptorRefs={
					@InterceptorRef(value="fileUpload", params={"allowedTypes","image/jpeg,image/gif,image/png","maximumSize","5120000"})
					, @InterceptorRef("basicStack")
			}
			, results={
					@Result(name="success",location="DETAIL",type="tiles")
			})
	public String uploadFile(){
		String path = "D:\\upload\\struts2\\";
		File destFile = new File(path, fileName);
		try {
			FileUtils.copyFile(file, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@Action(value="/doMultiUpload"
			, interceptorRefs={
					@InterceptorRef(value="fileUpload", params={"allowedTypes","image/jpeg,image/gif,image/png","maximumSize","5120000"})
					, @InterceptorRef("basicStack")
			}
			, results={
					@Result(name="success",location="DETAIL",type="tiles")
			})
	public String uploadMultiFile() {
		List<File> files = this.files;
		List<String> names = this.names;
		System.out.println(files.size()+":"+names.size());
		return "success";
	}
	
	@Action(value="/confirms"
			, results={
					@Result(name="success",location="DETAIL",type="tiles")
			})
	public String confirm() {
		int size = listAds.size();
		System.out.println(size);
		return "success";
	}
	
	//spring security
	@Action(value="/dangnhap"
			, results={
					@Result(name="success",location="DANGNHAP",type="tiles")
			})
	public String dangnhap() {
		
		return "success";
	}
	
	@Action(value="/admin"
			, results={
					@Result(name="success",location="ADMIN",type="tiles")
			})
	public String admin() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println((user.getUsername()));
		System.out.println(user.getPassword());
		return "success";
	}
	
	public InfoValue getInfo() {
		return info;
	}

	public void setInfo(InfoValue info) {
		this.info = info;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<UserBean> getListUser() {
		return listUser;
	}
	
	public void setListUser(List<UserBean> listUser) {
		this.listUser = listUser;
	}
	/*public void setBaseLogic(BaseLogicIF baseLogic) {
		this.baseLogic = baseLogic;
	}*/

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	 public void setUpload(File file) {
         this.file = file;
    }
 
      public void setUploadContentType(String contentType) {
         this.contentType = contentType;
      }
 
      public void setUploadFileName(String filename) {
         this.fileName = filename;
      }
      
    public void setUploads(List<File> files) {
    	this.files = files;
    }
    
    public void setUploadsContentType(List<String> contents) {
    	this.contents = contents;
    }
    
    public void setUploadsFileName(List<String> names) {
    	this.names = names;
    }

	public List<AdsAdnetworkBean> getListNetwork() {
		return listNetwork;
	}

	public void setListNetwork(List<AdsAdnetworkBean> listNetwork) {
		this.listNetwork = listNetwork;
	}
	
	public List<AdsBean> getListAds() {
		return listAds;
	}

	public void setListAds(List<AdsBean> listAds) {
		this.listAds = listAds;
	}

	public List<Parent> getListParent() {
		return listParent;
	}

	public void setListParent(List<Parent> listParent) {
		this.listParent = listParent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
