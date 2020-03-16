package com.jeesite.modules.filemanager.dao;

import com.jeesite.common.dao.BaseDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.filemanager.entity.Filemanager;
import java.util.List;

@MyBatisDao
public abstract interface FilemanagerDao extends BaseDao
{
  public abstract List<Filemanager> findList(Filemanager paramFilemanager);
  
  public abstract List<Filemanager> findListNew(Filemanager paramFilemanager);
}