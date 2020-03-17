package com.jeesite.modules.base.datascope;


public class CommonDataScopeUtils {/*
	
	private static BdPsndocService psnService = SpringUtils.getBean(BdPsndocService.class);
	
	private static String strwherekey="dsf";
	
	private static String deptkey="a.pk_dept";
	
	private static String officekey="a.pk_office";
	
	*//**
	 *数据查询权限控制类
	 *entity 实体类
	 *strWhereKey where别名
	 *deptKey 部门字段
	 *officeKey 组织字段
	 *//*
	@SuppressWarnings("rawtypes")
	public static void addDataScope(DataEntity entity,String strWhereKey,String deptKey,String officeKey){
		
		if(strWhereKey!=null&&!"".equals(strWhereKey)){
			strwherekey=strWhereKey;
		}
		if(deptKey!=null&&!"".equals(deptKey)){
			deptkey=deptKey;
		}
		if(officeKey!=null&&!"".equals(officeKey)){
			officekey=officeKey;
		}
		
		//1.获取当前登录用户
		User user=UserUtils.getUser();
		if(user.isSuperAdmin()){
			entity.getSqlMap().getDataScope().addFilter(strwherekey, "1=1");//超级管理员查询所有数据
			return ;
		}
		
		//2.根据用户查询人员身份
		BdPsndoc psnvo=psnService.getPsndocByUserCode(user.getUserCode());
		if(psnvo==null){
			entity.getSqlMap().getDataScope().addFilter(strwherekey, "1=2");//未关联人员的不允许查询
			return ;
		}
		
		BdPsndoc vo=new BdPsndoc();
		vo.setId(psnvo.getId());
		psnvo=psnService.get(vo);
		
		String datactrl=psnvo.getDatacontrol();
		
		if(datactrl.equals(AbsEnumType.SCOPE_DEPT)){//本部门权限
			entity.getSqlMap().getDataScope().addFilter(strwherekey, deptkey+"='"+psnvo.getPkDept().getId()+"'");
		}else if(datactrl.equals(AbsEnumType.SCOPE_CORP)){//本公司权限
			entity.getSqlMap().getDataScope().addFilter(strwherekey, officekey+"='"+psnvo.getPkOrg().getId()+"'");
		}else if(datactrl.equals(AbsEnumType.SCOPE_ALL)){//所有，即查询该用户有权限的公司数据
			
			String cond="";
			//1.从js_sys_role_data_scope抓取
			List<Role> roleList=user.getRoleList();
			if(roleList==null||roleList.size()<=0){
				cond +=" 1=2 ";
			}else{
				String rolestr="";
				for(Role ro:roleList){
					rolestr="'"+ro.getRoleCode()+"',";
				}
				rolestr=rolestr.substring(0, rolestr.lastIndexOf(","));
				
				cond +=" exists(select 1 from js_sys_role_data_scope e where e.ctrl_permi = '1' AND e.ctrl_type = 'Office' "
						+ "AND e.role_code IN ("+rolestr+") and e.ctrl_data ="+officekey+") ";
			}
			
			//2.从js_sys_user_data_scope抓取
			String usercode=user.getUserCode();
			
			cond += " or exists(select 1 from js_sys_user_data_scope e where e.ctrl_permi = '1' AND e.ctrl_type = 'Office' "
						+ "AND e.user_code ='"+usercode+"' and e.ctrl_data ="+officekey+") ";
			
			cond = "(" + cond +")";
			entity.getSqlMap().getDataScope().addFilter(strwherekey, cond);
		}else{
			entity.getSqlMap().getDataScope().addFilter(strwherekey, "1=2");
		}
		
	}

*/}
