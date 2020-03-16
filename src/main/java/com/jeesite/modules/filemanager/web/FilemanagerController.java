package com.jeesite.modules.filemanager.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.service.FileUploadService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.filemanager.entity.Filemanager;
import com.jeesite.modules.filemanager.entity.FilemanagerFolder;
import com.jeesite.modules.filemanager.service.FilemanagerFolderService;
import com.jeesite.modules.filemanager.service.FilemanagerService;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping({"${adminPath}/filemanager/"})
public class FilemanagerController extends BaseController
{

  @Autowired
  private FilemanagerService filemanagerService;

  @Autowired
  private FileUploadService floadService;
  @Autowired
  private FilemanagerFolderService filemanagerFolderService;

  // @RequiresPermissions({"filemanager:filemanager:edit"})
  @PostMapping({"delete"})
  @ResponseBody
  public String delete(Filemanager filemanager)
  {
	  String[] ids=filemanager.getIds();
	  for(String id:ids){
		  
		  String[] file=id.split("_");
		  if(file[0].equals("folder")){
			  FilemanagerFolder fd=new FilemanagerFolder();
			  fd.setId(file[1]);
			  filemanagerFolderService.delete(fd);
		  }else{
			  FileUpload fload=new FileUpload();
			  fload.setId(file[1]);
			  floadService.delete(fload);
		  }
	  }
	 
	return renderResult(Global.TRUE, text("删除文件成功！"));
  }

  //@RequiresPermissions({"filemanager:filemanager:edit"})
  @PostMapping({"save"})
  @ResponseBody
  public String save(@Validated Filemanager filemanager)
  {
	  if(filemanager.getGroupType().equals("self")){
		  filemanager.setCreateBy(UserUtils.getUser().getUserCode());
	  }
	  FileUploadUtils.saveFileUpload(filemanager.getFolderId(), filemanager.getBizType());
		return renderResult(Global.TRUE, text("保存文件成功！"));
  }

 // @RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"listData"})
  @ResponseBody
  public Page<Filemanager> listData(Filemanager arg1, HttpServletRequest arg2, HttpServletResponse arg3)
  {
	  arg1.setPage(new Page<>(arg2, arg3));
	  if("self".equals(arg1.getGroupType())){
		  arg1.setCreateBy(UserUtils.getUser().getUserCode()); 
	  }
	 
		Page<Filemanager> page = filemanagerService.findPage(arg1);
		if(StringUtils.isNotBlank(arg1.getFolderId())){
			 FilemanagerFolder a = (FilemanagerFolder)this.filemanagerFolderService.get(arg1.getFolderId());
			 page.addOtherData("folder", a);
		}
    return page;
  }

  //@RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"form"})
  public String form(Filemanager filemanager, Model model)
  {
    if (StringUtils.isBlank(filemanager.getFolderId()))
      filemanager.setFolderId("");
    filemanager.setCreateBy(filemanager.getCurrentUser().getUserCode());
    filemanager.setOfficeCode(EmpUtils.getOffice().getOfficeCode());

    model.addAttribute("filemanager", filemanager);

    return "modules/filemanager/filemanagerForm";
  }

  //@RequiresPermissions({"filemanager"})
  @RequestMapping({"index", ""})
  public String index(Filemanager filemanager, Model model)
  {
    if (StringUtils.isBlank(filemanager.getGroupType()))
    {
      filemanager.setGroupType("global");
    }

    model.addAttribute("filemanager", filemanager);
	return "modules/filemanager/filemanagerIndex";
  }

 // @RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"moveForm"})
  public String moveForm(Filemanager filemanager, Model model)
  {
	  FilemanagerFolder  a =  this.filemanagerFolderService.get(filemanager.getFolderId());
    if ( a!= null){
    	 filemanager.setFileName(a.getFolderName());
    }
     
    String excludeCode="";
    if (filemanager.getIds() != null)
    {
    	String str="";
    	for(String id:filemanager.getIds()){
    		str+=id+",";
    		String[] file=id.split("_");
    		if(file[0].equals("folder")){//排除原始文件夹
    			excludeCode+=file[1]+",";
    		}
    		
    	}
    	if(excludeCode.length()>0){
    		excludeCode=excludeCode.substring(0, excludeCode.lastIndexOf(","));
    	}
    	
    	str=str.substring(0, str.lastIndexOf(","));
    	model.addAttribute("ids", str);
    }
    model.addAttribute("excludeCode", excludeCode);
    model.addAttribute("Filemanager", filemanager);
    
    return "modules/filemanager/filemanagerMoveForm";
  }

 // @RequiresPermissions({"filemanager:filemanager:edit"})
  @PostMapping({"move"})
  @ResponseBody
  public String move(Filemanager filemanager)
  {
	  String[] ids=filemanager.getIds();
	  for(String id:ids){
		  String[] file=id.split("_");
		  if(file[0].equals("folder")){
			  FilemanagerFolder fd=new FilemanagerFolder();
			  fd.setId(file[1]);
			  fd=filemanagerFolderService.get(fd);
			  //转移父类
			  FilemanagerFolder fd2=new FilemanagerFolder();
			  fd2.setId(filemanager.getFolderId());
			  fd.setParent(fd2);
			  filemanagerFolderService.save(fd);
		  }else{
			  FileUpload fload=new FileUpload();
			  fload.setId(file[1]);
			  fload=floadService.get(fload);
			  fload.setBizKey(filemanager.getFolderId());
			  floadService.update(fload);
		  }
		 
	  }
	  return renderResult(Global.TRUE, text("移动文件成功！"));
  }

  //@RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"list"})
  public String list(Filemanager filemanager, Model model)
  {
    if (StringUtils.isBlank(filemanager.getGroupType()))
    {
      filemanager.setGroupType("global");
    }
    
    model.addAttribute("filemanager", filemanager);
   	return "modules/filemanager/filemanagerList";
  }
  
  //@RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"list2"})
  public String list2(Filemanager filemanager,String imgType, Model model)
  {
    model.addAttribute("filemanager", filemanager);
    model.addAttribute("imgType", imgType);
   	return "modules/filemanager/filemanagerList2";
  }
  
  //==============================手机端=====================================
  //发票上传识别页
  //@RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"appForm"})
  public String appForm(Filemanager filemanager, Model model){
	  if (StringUtils.isBlank(filemanager.getGroupType()))
	    {
	      filemanager.setGroupType("invoice");
	    }
	  model.addAttribute("filemanager", filemanager);
    return "modules/app/mfujianForm";
  }
  
//手机发票识别上传功能(单据外上传没有biz_key,需自动生成)
  //@RequiresPermissions({"filemanager:filemanager:edit"})
  @PostMapping({"appSave"})
  @ResponseBody
  public String appSave(@Validated Filemanager filemanager){
	    String  str= filemanagerService.appSave(filemanager);
	  return renderResult(Global.TRUE, text("上传文件成功!"),str);
  }
  
  //@RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"appForm2"})
  public String appForm2(Filemanager filemanager, Model model){
	  if (StringUtils.isBlank(filemanager.getGroupType()))
	    {
	      filemanager.setGroupType("nc");
	    }
	  model.addAttribute("filemanager", filemanager);
    return "modules/app/mfujianForm2";
  }
  
  //手机附件界面
  //@RequiresPermissions({"filemanager:filemanager:view"})
  @RequestMapping({"appList"})
  public String appList(Filemanager filemanager,@RequestParam String appid,@RequestParam String vbillno, String isEdit, Model model){
    if (StringUtils.isBlank(filemanager.getGroupType())){
        filemanager.setGroupType(appid);//此处appid为nc
    }
    //根据单据号获取文件id
  	FilemanagerFolder fd=new FilemanagerFolder();
  	fd.setGroupType(appid);
  	 fd.setParentCode("0");
  	 fd.setFolderName(vbillno);
  	 fd.getSqlMap().getDataScope().addFilter("dsf", "a.folder_name ='"+vbillno+"'");
  	 List<FilemanagerFolder> flist=filemanagerFolderService.findList(fd);
  	
  	 if(flist.size()>1){
  		 model.addAttribute("title","系统错误");
  		 model.addAttribute("message2","该文件存在多个，请检查！");
  		 return "/error/401";
  	 }
  	 if(flist.size()<=0){
  		 //创建文件目录
  		 FilemanagerFolder nfd=new FilemanagerFolder();
  		 nfd.setFolderName(vbillno);
  		 nfd.setGroupType(appid);
  		 filemanagerFolderService.save(nfd);
  		 
  		 //重新查询出来
  		FilemanagerFolder fd2=new FilemanagerFolder();
  		fd2.setGroupType(appid);
  		fd2.setParentCode("0");
  		fd2.setFolderName(vbillno);
  		fd2.getSqlMap().getDataScope().addFilter("dsf", "a.folder_name ='"+vbillno+"'");
  		flist=filemanagerFolderService.findList(fd2);
  	 }
  	  String foldid=flist.get(0).getId();
      filemanager.setGroupType(appid);
      filemanager.setFolderId(foldid);
      model.addAttribute("filemanager", filemanager);  
     
     if(!isEdit.equals("true")){
    	isEdit="false";
     }
    model.addAttribute("isEdit", isEdit);
	return "modules/app/mfujian";
  }
  
  @RequestMapping({"listAppData"})
  @ResponseBody
  public Page<Filemanager> listAppData(Filemanager filemanager,HttpServletRequest arg2, HttpServletResponse arg3){
	  
	  filemanager.setPage(new Page<>(arg2, arg3));
	  filemanager.setCreateBy(UserUtils.getUser().getUserCode());
		Page<Filemanager> page = filemanagerService.findPage(filemanager);
		if(StringUtils.isNotBlank(filemanager.getFolderId())){
			 FilemanagerFolder a = (FilemanagerFolder)this.filemanagerFolderService.get(filemanager.getFolderId());
			 page.addOtherData("folder", a);
		}
		return page;
  }
  
  
  //==============================手机端end=====================================
  
  /**
   * 影像预览
   * @param filemanager
   * @param model
   * @return
   */
  //@RequiresPermissions({"filemanager"})
  @RequestMapping({"viewImg"})
  public String viewImg(Filemanager filemanager,String appid,String vbillno,String isWin, Model model)
  {
	 if(appid==null||appid.equals("")||vbillno==null||vbillno.equals("")){
		 return "/error/400";
	 }
	 //根据单据号获取文件id
	 FilemanagerFolder fd=new FilemanagerFolder();
	 fd.setGroupType(appid);
	 fd.setParentCode("0");
	 fd.setFolderName(vbillno);
	 fd.getSqlMap().getDataScope().addFilter("dsf", "a.folder_name ='"+vbillno+"'");
	 List<FilemanagerFolder> flist=filemanagerFolderService.findList(fd);
	 if(flist.size()<=0){
		 model.addAttribute("title","无单据附件");
		 model.addAttribute("message2","当前单据未上传任何附件！");
		 return "/error/401";
	 }
	 if(flist.size()>1){
		 model.addAttribute("title","系统错误");
		 model.addAttribute("message2","该文件存在多个，请检查！");
		 return "/error/401";
	 }
	 String foldid=flist.get(0).getId();
     filemanager.setGroupType(appid);
     filemanager.setFolderId(foldid);
    model.addAttribute("filemanager", filemanager);
    model.addAttribute("imgType", "view");
    //判断是否小窗口模式
    boolean ismin=Boolean.valueOf(isWin);
    if(ismin){
    	return "modules/filemanager/filemanagerList2";
    }
	return "modules/filemanager/filemanagerViewIndex";
  }
  
  /**
   * 影像编辑
   * @param filemanager
   * @param model
   * @param isWin 是否窗口模式
   * @return
   */
  //@RequiresPermissions({"filemanager"})
  @RequestMapping({"addImg"})
  public String addImg(Filemanager filemanager,String appid,String vbillno,String isWin, Model model)
  {
	//根据单据号获取文件id
	FilemanagerFolder fd=new FilemanagerFolder();
	fd.setGroupType(appid);
	 fd.setParentCode("0");
	 fd.setFolderName(vbillno);
	 fd.getSqlMap().getDataScope().addFilter("dsf", "a.folder_name ='"+vbillno+"'");
	 List<FilemanagerFolder> flist=filemanagerFolderService.findList(fd);
	
	 if(flist.size()>1){
		 model.addAttribute("title","系统错误");
		 model.addAttribute("message2","该文件存在多个，请检查！");
		 return "/error/401";
	 }
	 if(flist.size()<=0){
		 //创建文件目录
		 FilemanagerFolder nfd=new FilemanagerFolder();
		 nfd.setFolderName(vbillno);
		 nfd.setGroupType(appid);
		 filemanagerFolderService.save(nfd);
		 
		 //重新查询出来
		 FilemanagerFolder fd2=new FilemanagerFolder();
		fd2.setGroupType(appid);
		fd2.setParentCode("0");
		fd2.setFolderName(vbillno);
		fd2.getSqlMap().getDataScope().addFilter("dsf", "a.folder_name ='"+vbillno+"'");
		flist=filemanagerFolderService.findList(fd2);
	 }
	String foldid=flist.get(0).getId();
    filemanager.setGroupType(appid);
    filemanager.setFolderId(foldid);
    model.addAttribute("filemanager", filemanager);
    model.addAttribute("imgType", "add");
    //判断是否小窗口模式
    boolean ismin=Boolean.valueOf(isWin);
    if(ismin){
    	return "modules/filemanager/filemanagerList2";
    }
	return "modules/filemanager/filemanagerViewIndex";
  }
  
}