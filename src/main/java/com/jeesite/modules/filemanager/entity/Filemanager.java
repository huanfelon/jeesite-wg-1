package com.jeesite.modules.filemanager.entity;

import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Table;

@Table(columns = { @com.jeesite.common.mybatis.annotation.Column(includeEntity = DataEntity.class) },
orderBy = "a.tree_sort,a.file_name,a.file_upload_id")
public class Filemanager extends DataEntity<Filemanager> {
	private String groupType;
	private String[] ids;
	public static final String GROUP_TYPE_SELF = "self";
	public static final String GROUP_TYPE_OFFICE = "office";
	private String fileUploadId;
	private String sharedId;
	public static final String GROUP_TYPE_GLOBAL = "global";
	private String fileType;
	private String parentCode;
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Long fileSize;
	private String folderId;
	private String officeCode;
	private String fileExtension;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBizType() {
		if ("self".equals(this.groupType)) {
			return "filemanager_"+this.groupType +"_" + this.createBy;
		}else{
			return "filemanager_"+this.groupType;
		}
	}

	public String getFileName() {
		return this.fileName;
	}

	public Filemanager(String id) {
		super(id);
	}

	public String[] getIds() {
		return this.ids;
	}

	public String getFileSizeFormat() {

        if(this.fileSize == null){
            return null;
        }
        
        DecimalFormat df = new DecimalFormat("0.000");
        double file=Double.valueOf(this.fileSize.toString());
        if (file < 1024) {
            return String.valueOf(file) + "B";
        } else {
        	file = file / 1024;
        }
        if (file < 1024) {
            //因为如果以Kb为单位的话，要保留最后3位小数，
            //因此，把此数乘以100之后再取余
            return df.format(file) + "KB";
        } else {
            //否则如果要以Mb为单位的，先除于1024再作同样的处理
        	file = file / 1024;
            return df.format(file) + "MB";
        }
	}

	public String getParentCode() {
		return this.parentCode;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setSharedId(String sharedId) {
		this.sharedId = sharedId;
	}

	public String getGroupType() {
		return this.groupType;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getFileExtension() {
		return this.fileExtension;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public void setFileUploadId(String fileUploadId) {
		this.fileUploadId = fileUploadId;
	}

	public String getFileUploadId() {
		return this.fileUploadId;
	}

	public String getFileType() {
		return this.fileType;
	}

	public Filemanager() {
		this(null);
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	@JsonIgnore
	public String getFileNameLike() {
		return this.fileName + "%";
	}

	public String getId() {
		if ("file".equals(this.fileType)) {
			return "file_" + this.fileUploadId;
		}else if("image".equals(this.fileType)){
			return "image_" + this.fileUploadId;
		}else{
			return "folder_" + this.folderId;
		}
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public String getFolderId() {
		return this.folderId;
	}

	public String getSharedId() {
		return this.sharedId;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
}