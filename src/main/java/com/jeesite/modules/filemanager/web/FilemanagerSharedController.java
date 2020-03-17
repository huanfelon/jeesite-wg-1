package com.jeesite.modules.filemanager.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.service.FileUploadService;
import com.jeesite.modules.filemanager.entity.FilemanagerFolder;
import com.jeesite.modules.filemanager.entity.FilemanagerShared;
import com.jeesite.modules.filemanager.service.FilemanagerFolderService;
import com.jeesite.modules.filemanager.service.FilemanagerSharedService;

@Controller
@RequestMapping({"${adminPath}/filemanager/filemanagerShared"})
public class FilemanagerSharedController extends BaseController
{

  @Autowired
  private FilemanagerSharedService filemanagerSharedService;
  
  @Autowired
  private FilemanagerFolderService filemanagerFolderService;
  
  @Autowired
  private FileUploadService fileUploadService;

  @RequiresPermissions({"filemanager:filemanagerShared:edit"})
  @RequestMapping({"delete"})
  @ResponseBody
  public String delete(FilemanagerShared filemanagerShared)
  {
	  FilemanagerShared fd=new FilemanagerShared();
	  fd.setId(filemanagerShared.getId());
	 // FilemanagerShared fd2=filemanagerSharedService.get(fd);
	  fd.setStatus("1");
	  fd.setUpdateBy(filemanagerShared.getCurrentUser().getUserCode());
	  fd.setUpdateDate(new Date());
	  filemanagerSharedService.updateStatus(fd);
	  return renderResult(Global.TRUE, text("删除文件共享成功！"));
  }

  @ModelAttribute
  public FilemanagerShared get(String id, boolean isNewRecord)
  {
    return (FilemanagerShared)this.filemanagerSharedService.get(id, isNewRecord);
  }

  @RequiresPermissions({"filemanager:filemanagerShared:view"})
  @RequestMapping({"form"})
  public String form(FilemanagerShared filemanagerShared, Model model)
  {

	  if (filemanagerShared.getIds() != null)
	    {
	    	String str="";
	    	for(String id:filemanagerShared.getIds()){
	    		str+=id+",";
	    	}
	    	str=str.substring(0, str.lastIndexOf(","));
	    	model.addAttribute("ids", str);
	    }
    model.addAttribute("filemanagerShared", filemanagerShared);
	return "modules/filemanager/filemanagerSharedForm";
  }

  @RequiresPermissions({"filemanager:filemanagerShared:view"})
  @RequestMapping({"listData"})
  @ResponseBody
  public Page<FilemanagerShared> listData(FilemanagerShared filemanagerShared, boolean myShared, HttpServletRequest request, HttpServletResponse response)
  {
	  
	if(myShared){
		filemanagerShared.setCreateBy(filemanagerShared.getCurrentUser().getUserCode());
	}else{
		filemanagerShared.setReceiveUserCode(filemanagerShared.getCurrentUser().getUserCode());
	}
	filemanagerShared.setPage(new Page<>(request, response));
	  
	return this.filemanagerSharedService.findPage(filemanagerShared);
  }

  @RequiresPermissions({"filemanager:filemanagerShared:view"})
  @RequestMapping({"list", ""})
  public String list(FilemanagerShared filemanagerShared, boolean myShared, Model model)
  {
    model.addAttribute("filemanagerShared", filemanagerShared);
    model.addAttribute("myShared", myShared);

    return "modules/filemanager/filemanagerSharedList";
  }

  @RequiresPermissions({"filemanager:filemanagerShared:edit"})
  @PostMapping({"save"})
  @ResponseBody
  public String save(@Validated FilemanagerShared filemanagerShared)
  {
	  //保存只会插入
	  String[] ids=filemanagerShared.getIds();
	  String usercode=filemanagerShared.getReceiveUserCode();
	  String username=filemanagerShared.getReceiveUserName();
	  String[] usercodes=usercode.split(",");
	  String[] usernames=username.split(",");
	  //此处查询文件名称
	  for(String id:ids){
		  String[] strtype=id.split("_");
		  String type=strtype[0];
		  if(type.equals("folder")){
			  //查询文件夹
			  FilemanagerFolder fold=new FilemanagerFolder();
			  fold.setId(strtype[1]);
			  fold=filemanagerFolderService.get(fold);
			  for(int i=0;i<usercodes.length;i++){
				  
				  FilemanagerShared fs=new FilemanagerShared();
				  fs.setFolderId(strtype[1]);
				  fs.setFileName(fold.getFolderName());
				  fs.setReceiveUserCode(usercodes[i]);
				  fs.setReceiveUserName(usernames[i]);
				  fs.setRemarks(filemanagerShared.getRemarks());
				  
				  filemanagerSharedService.insert(fs);
			  }
		  }else{
			//查询文件
			  FileUpload fold=new FileUpload();
			  fold.setId(strtype[1]);
			  fold=fileUploadService.get(fold);
			  for(int i=0;i<usercodes.length;i++){
				  
				  FilemanagerShared fs=new FilemanagerShared();
				  fs.setFileUploadId(strtype[1]);
				  fs.setFileName(fold.getFileName());
				  fs.setReceiveUserCode(usercodes[i]);
				  fs.setReceiveUserName(usernames[i]);
				  fs.setRemarks(filemanagerShared.getRemarks());
				  
				  filemanagerSharedService.insert(fs);
			  }
		  }
	  }
	  return renderResult(Global.TRUE, text("保存文件分享成功！"));
  }
  
}