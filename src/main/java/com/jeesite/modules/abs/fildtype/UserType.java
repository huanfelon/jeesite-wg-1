package com.jeesite.modules.abs.fildtype;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.UserService;

import java.util.List;

import org.springframework.core.NamedThreadLocal;

public class UserType
{
  private static ThreadLocal<List<User>> cache = new NamedThreadLocal<List<User>>("UserType");
  private static UserService userService = SpringUtils.getBean(UserService.class);

  public static Object getValue(String val)
  {
    List<User> cacheList = (List<User>)cache.get();
    if (cacheList == null) {
    	//获取所有用户
    	User where = new User();
		where.setStatus(User.STATUS_NORMAL);
    	cacheList = userService.findList(where);
    	cache.set(cacheList);
    }
    for (User e : cacheList) {
      if (StringUtils.trimToEmpty(val).equals(e.getUserName())) {
        return e;
      }
    }
    return null;
  }

  public static String setValue(Object val)
  {
    if ((val != null) && (((User)val).getUserName() != null)) {
      return ((User)val).getUserName();
    }
    return "";
  }

  public static void clearCache()
  {
    cache.remove();
  }
}