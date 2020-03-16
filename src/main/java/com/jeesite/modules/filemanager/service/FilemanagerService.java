package com.jeesite.modules.filemanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdGenerate;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.service.FileUploadService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.filemanager.dao.FilemanagerDao;
import com.jeesite.modules.filemanager.entity.Filemanager;

@Service
@Transactional(readOnly=true)
public class FilemanagerService extends BaseService
{

  @Autowired
  private FilemanagerDao filemanagerDao;

  @Autowired
  private FilemanagerSharedService filemanagerSharedService;

  @Autowired
  private FilemanagerFolderService filemanagerFolderService;

  @Autowired
  private FileUploadService fileUploadService;

  @Transactional(readOnly=false)
  public void save(Filemanager filemanager)
  {
  }
  
  

  @Transactional(readOnly=false)
  public void move(Filemanager filemanager)
  {
  }

  @Transactional(readOnly=false)
  public void delete(Filemanager filemanager)
  {
  }

  @SuppressWarnings("unchecked")
public Page<Filemanager> findPage(Filemanager filemanager)
  {
	  Page<Filemanager> page=(Page<Filemanager>)filemanager.getPage();
	  List<Filemanager> list=filemanagerDao.findListNew(filemanager);
	  if(list==null||list.size()<=0){
		  return page;
	  }
	  page.setList(list);
	  return page;
  }
  
  public List<Filemanager> findList(Filemanager filemanager)
  {
	  List<Filemanager> list=filemanagerDao.findListNew(filemanager);
	  return list;
  }

  //手机发票识别上传功能
  @Transactional(readOnly=false)
  public String appSave(Filemanager filemanager){
    filemanager.setCreateBy(filemanager.getCurrentUser().getUserCode()); 
    if (StringUtils.isBlank(filemanager.getFolderId())) {
		filemanager.setFolderId(IdGenerate.uuid());
	}
    FileUploadUtils.saveFileUpload(filemanager.getFolderId(), filemanager.getBizType());
    
    FileUpload lfFileUpload =new FileUpload();
    lfFileUpload.setBizKey(filemanager.getFolderId());
    List<FileUpload> fList= fileUploadService.findList(lfFileUpload);
    String url=fList.get(0).getFileUrl();
   	String sysurl=Global.getConfig("sys.systempath", "http://127.0.0.1:8080/js");
    String str =sysurl+url + "&"+filemanager.getFolderId();
    return str;
  }
}