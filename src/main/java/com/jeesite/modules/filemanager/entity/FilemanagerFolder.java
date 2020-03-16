package com.jeesite.modules.filemanager.entity;

import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Table(name = "${_prefix}filemanager_folder", alias = "a", columns = {
		@com.jeesite.common.mybatis.annotation.Column(name = "id", attrName = "id", label = "编号", isPK = true),
		@com.jeesite.common.mybatis.annotation.Column(includeEntity = TreeEntity.class),
		@com.jeesite.common.mybatis.annotation.Column(name = "folder_name", attrName = "folderName", label = "文件夹名", queryType = com.jeesite.common.mybatis.mapper.query.QueryType.LIKE, isTreeName = true),
		@com.jeesite.common.mybatis.annotation.Column(name = "group_type", attrName = "groupType", label = "分组类型"),
		@com.jeesite.common.mybatis.annotation.Column(name = "office_code", attrName = "officeCode", label = "部门编码"),
		@com.jeesite.common.mybatis.annotation.Column(includeEntity = com.jeesite.common.entity.DataEntity.class),
		@com.jeesite.common.mybatis.annotation.Column(includeEntity = com.jeesite.common.entity.BaseEntity.class) },
		extWhereKeys="dsf", orderBy = "a.tree_sorts, a.folder_name")
public class FilemanagerFolder extends TreeEntity<FilemanagerFolder> {
	private static final long serialVersionUID = 1L;
	private String folderName;
	private String groupType;
	private String officeCode;

	public FilemanagerFolder(String id) {
		super(id);
	}

	public void setParent(FilemanagerFolder parent) {
		this.parent = parent;
	}

	public FilemanagerFolder getParent() {
		return (FilemanagerFolder) this.parent;
	}

	@NotBlank(message = "文件夹名不能为空")
	@Length(min = 0, max = 100, message = "文件夹名长度不能超过 100 个字符")
	public String getFolderName() {
		return this.folderName;
	}

	@NotBlank(message = "文件分组类型不能为空")
	@Length(min = 0, max = 64, message = "文件分组类型长度不能超过 64 个字符")
	public String getGroupType() {
		return this.groupType;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	@Length(min = 0, max = 64, message = "部门编码长度不能超过 64 个字符")
	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public FilemanagerFolder() {
		this(null);
	}
}