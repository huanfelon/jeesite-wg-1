package com.jeesite.modules.filemanager.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.filemanager.entity.FilemanagerFolder;
import com.jeesite.modules.filemanager.service.FilemanagerFolderService;
import com.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping({ "${adminPath}/filemanager/filemanagerFolder" })
public class FilemanagerFolderController extends BaseController {

	@Autowired
	private FilemanagerFolderService filemanagerFolderService;

	//@RequiresPermissions({ "filemanager:filemanagerFolder:view" })
	@RequestMapping({ "treeData" })
	@ResponseBody
	public List<Map<String, Object>> treeData(String groupType, String vbillno,String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();

		FilemanagerFolder fold = new FilemanagerFolder();
		if (StringUtils.isBlank(groupType)) {
			groupType = "global";
		}
		fold.setGroupType(groupType);
		if(StringUtils.isNotBlank(vbillno)){
			fold.setTreeNames(vbillno);
			fold.getSqlMap().getDataScope().addFilter("dsf", "a.folder_name ='"+vbillno+"'");
		}
		List<FilemanagerFolder> list = filemanagerFolderService.findList(fold);

		for (int i = 0; i < list.size(); i++) {
			FilemanagerFolder e = list.get(i);
			// 过滤非正常的数据
			if (!FilemanagerFolder.STATUS_NORMAL.equals(e.getStatus())) {
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)) {
				String[] excodes=excludeCode.split(",");
				boolean flag=false;
				for(String excode:excodes){
					if (e.getId().equals(excode)) {
						flag=true;
					}
					if (e.getParentCodes().contains("," + excode + ",")) {
						flag=true;
					}
				}
				if(flag){
					continue ;
				}
			}
			
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", e.getTreeName_());
			map.put("group", e.getGroupType());
			mapList.add(map);
		}
		return mapList;
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:edit" })
	@RequestMapping({ "delete" })
	@ResponseBody
	public String delete(FilemanagerFolder filemanagerFolder) {
		filemanagerFolderService.delete(filemanagerFolder);
		return renderResult(Global.TRUE, text("删除文件目录成功！"));
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:edit" })
	@RequestMapping({ "fixTreeData" })
	@ResponseBody
	public String fixTreeData(FilemanagerFolder filemanagerFolder) {
		if (!UserUtils.getUser().isAdmin()) {
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		filemanagerFolderService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:view" })
	@RequestMapping({ "form" })
	public String form(FilemanagerFolder filemanagerFolder, Model model) {
		filemanagerFolder = createNextNode(filemanagerFolder);
		model.addAttribute("filemanagerFolder", filemanagerFolder);
		return "modules/filemanager/filemanagerFolderForm";
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:view" })
	@RequestMapping({ "listData" })
	@ResponseBody
	public List<FilemanagerFolder> listData(FilemanagerFolder filemanagerFolder) {
		
		if (StringUtils.isBlank(filemanagerFolder.getParentCode())) {
			filemanagerFolder.setParentCode("0");
		}

		if (StringUtils.isNotBlank(filemanagerFolder.getFolderName())) {
			filemanagerFolder.setParentCode(null);
		}

		if (StringUtils.isNotBlank(filemanagerFolder.getGroupType()))
			filemanagerFolder.setParentCode(null);
		if (StringUtils.isNotBlank(filemanagerFolder.getOfficeCode())) {
			filemanagerFolder.setParentCode(null);
		}
		if (StringUtils.isNotBlank(filemanagerFolder.getRemarks())) {
			filemanagerFolder.setParentCode(null);
		}

		return this.filemanagerFolderService.findList(filemanagerFolder);
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:edit" })
	@PostMapping({ "save" })
	@ResponseBody
	public String save(@Validated FilemanagerFolder filemanagerFolder) {
		this.filemanagerFolderService.save(filemanagerFolder);
		return renderResult(Global.TRUE, text("保存文件目录成功！"));
	}

	@ModelAttribute
	public FilemanagerFolder get(String id, boolean isNewRecord) {
		return (FilemanagerFolder) this.filemanagerFolderService.get(id, isNewRecord);
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:edit" })
	@RequestMapping({ "createNextNode" })
	@ResponseBody
	public FilemanagerFolder createNextNode(FilemanagerFolder filemanagerFolder) {
		if (StringUtils.isNotBlank(filemanagerFolder.getParentCode())) {
			filemanagerFolder.setParent(filemanagerFolderService.get(filemanagerFolder.getParentCode()));
		}
		if (filemanagerFolder.getIsNewRecord()) {
			FilemanagerFolder where = new FilemanagerFolder();
			where.setParentCode(filemanagerFolder.getParentCode());
			FilemanagerFolder last = filemanagerFolderService.getLastByParentCode(where);
			if (last != null) {
				filemanagerFolder.setTreeSort(Integer.valueOf(last.getTreeSort().intValue() + 30));
			}

		}

		if (filemanagerFolder.getTreeSort() == null) {
			filemanagerFolder.setTreeSort(Integer.valueOf(30));
		}

		return filemanagerFolder;
	}

	//@RequiresPermissions({ "filemanager:filemanagerFolder:view" })
	@RequestMapping({ "list", "" })
	public String list(FilemanagerFolder filemanagerFolder, Model model) {
		if (StringUtils.isBlank(filemanagerFolder.getGroupType()))
			filemanagerFolder.setGroupType("global");

		model.addAttribute("filemanagerFolder", filemanagerFolder);
		return "modules/filemanager/filemanagerFolderList";
	}
}