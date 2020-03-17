package com.jeesite.modules.wght.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.base.entity.BdCustomer;
import com.jeesite.modules.bd.entity.BdProject;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.wght.entity.WgContract;
import com.jeesite.modules.wght.entity.WgContractCust;
import com.jeesite.modules.wght.entity.WgContractHouse;
import com.jeesite.modules.wght.entity.WgContractMzq;
import com.jeesite.modules.wght.entity.WgContractRentprice;
import com.jeesite.modules.wght.entity.WgContractSrcf;
import com.jeesite.modules.wght.entity.WgContractWyf;
import com.jeesite.modules.wght.entity.WgContractWyfcf;
import com.jeesite.modules.wght.entity.WgContractWytype;
import com.jeesite.modules.wght.entity.WgContractYwcf;
import com.jeesite.modules.wght.entity.WgContractZltype;
import com.jeesite.modules.wght.entity.WgContractZqfy;
import com.jeesite.modules.wght.entity.WgContractZqfycf;
import com.jeesite.modules.wght.entity.WgContractZzq;
import com.jeesite.modules.zl.entity.ZlBuildingfile;
import com.jeesite.modules.zl.entity.ZlHousesource;

public class CalculateRentUtils {
	
	private static OfficeService officeService = SpringUtils.getBean(OfficeService.class);
	private static List<Map<String, Object>> paymap=new ArrayList<Map<String,Object>>();//存放根据条件拆分
	
	/**
	 * 重算年租金明细（含免租增长）
	 * @param wgContract
	 */
	public static WgContract recalRent2(WgContract wgContract) {
		List<WgContractRentprice> listrent=new ArrayList<WgContractRentprice>();
		List<WgContractHouse> listhouse=wgContract.getWgContractHouseList();
		List<WgContractMzq> listmz=wgContract.getWgContractMzqList();//免租期
		
		ZlHousesource houses=new ZlHousesource();
		Integer rowcount=listhouse.size();
		if(rowcount>=1){
			String pkhouses="";
			for(int i=0;i<rowcount;i++){
				if(!pkhouses.contains(listhouse.get(i).getPkHouse().getPkHousesource())){
					pkhouses+=listhouse.get(i).getPkHouse().getPkHousesource()+",";
				}
			}
			houses.setPkHousesource(pkhouses.substring(0,pkhouses.length()-1));
		}
		//查找合同表头租赁方式及租金
		Date ud_fist=wgContract.getDstartdate();
		Date ud_last=wgContract.getDenddate();
		Date start=CalendarUtls.getDateNoTime(ud_fist);
		while(start.compareTo(ud_last)<=0){
			Date start1=start;
			//计算年区间开始免租
			if(listmz!=null&&listmz.size()>0){
				for(int j=0;j<listmz.size();j++){
					Date mzstart=listmz.get(j).getDstartdate();
					Date mzend=listmz.get(j).getDenddate();
					if(mzstart.compareTo(start)==0){//免租开始日期等于年区间开始日期
						start=CalendarUtls.getAfterFirstDay(mzend);
					}
				}
			}
			Date end=CalendarUtls.getNextMonthDay(start, 12);
			Date end2=CalendarUtls.getBeforeFirstDay(end);
			if(end2.compareTo(ud_last)>=0){//结束日期之后
				end2=ud_last;
			}else{
				//加入年区间中间免租
				if(listmz!=null&&listmz.size()>0){
					for(int j=0;j<listmz.size();j++){
						Date mzstart=listmz.get(j).getDstartdate();
						Date mzend=listmz.get(j).getDenddate();
						if(mzstart.compareTo(start)>0&&mzstart.compareTo(end2)<0){//免租开始日期在年区间之间
							Integer months=CalendarUtls.getBetweenMonths(mzstart, mzend);//免租包含月数
							Integer days=0;
							if(CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(mzstart, months)).compareTo(mzend)!=0){
								days=CalendarUtls.getBetweenTwoDays(CalendarUtls.getNextMonthDay(mzstart, months),mzend);//不满整月天数
							}
							end2=CalendarUtls.getAfterNDay(CalendarUtls.getNextMonthDay(end2, months), days);
						}
					}
				}
			}
			Double nprice=getDbobj(wgContract.getNprice());
			Integer yeardays=CalendarUtls.getBetweenTwoDays(start,end2);
			Double nyear=new Double(0);
			Double monm=new Double(0);
			Double daym=new Double(0);
			if(yeardays!=0){
				nyear=wgContract.getNmonthprice()*12;
				monm=wgContract.getNmonthprice();
				daym=getDbobjBy2w(nyear/yeardays);
			}
			WgContractRentprice rent=new WgContractRentprice();
			rent.setDstartdate(start1);
			rent.setDenddate(end2);
			rent.setPkHouse(houses);
			rent.setNprice(nprice);
			rent.setNdaymny(daym);
			rent.setNmonthmny(monm);
			rent.setNyearmny(nyear);
			listrent.add(rent);
			start=CalendarUtls.getAfterFirstDay(end2);//交替起始日期
		}
		wgContract.setWgContractRentpriceList(listrent);
		return wgContract;
	}
	
	/**
	 * 计算业务拆分（含免租增长）
	 * @param wgContract
	 * @return
	 */
	public static WgContract recalYwcf2(WgContract wgContract){
		List<WgContractYwcf> listywcf=new ArrayList<WgContractYwcf>();
		List<WgContractHouse> list_h=wgContract.getWgContractHouseList();//房产页签
		List<WgContractZltype> list_zlt=wgContract.getWgContractZltypeList();//租赁支付方式页签
		if(list_h==null||list_h.size()==0){//无房产不拆分
			return wgContract;
		}
		
		//付款方式拆分
		List<Map<String, Object>> paymap = recalPaystyle(wgContract);
		//对每个付款方式进行业务拆分
		List<Map<String, Object>> ywmap = recalYwcfByPaystyle(wgContract,paymap);
		
		BdProject costp=list_zlt.get(0).getPkYsproject();//预算项目（表体租赁支付方式第一行为默认）
		
		//按月拆分 处理逻辑
		Double nallysmny=new Double(0);
		Double ntaxmny=new Double(0);
		Double nnotaxmny=new Double(0);
		for(Map<String, Object> map:ywmap){
			try {
				Date start=(Date) map.get("start");
				Date end=(Date) map.get("end");
				Double recmny=getDbobj(map.get("nysmny"));
				
				WgContractYwcf ywcf = new WgContractYwcf();
				ywcf.setDstartdate(start);
				ywcf.setDenddate(end);
				ywcf.setPkCostproject(costp);
				ywcf.setPkCustomer(wgContract.getWgContractCustList().get(0).getPkCustomer());
				ZlBuildingfile build=new ZlBuildingfile();
				ZlHousesource house=new ZlHousesource();
				if(wgContract.getWgContractHouseList()!=null&&wgContract.getWgContractHouseList().size()>0){
					String builds="";
					String houses="";
					for (WgContractHouse wghouse : wgContract.getWgContractHouseList()) {
						if(!builds.contains(wghouse.getPkBuild().getPkBuildingfile())){
							builds+=wghouse.getPkBuild().getPkBuildingfile()+",";
						}
						if(!houses.contains(wghouse.getPkHouse().getPkHousesource())){
							houses+=wghouse.getPkHouse().getPkHousesource()+",";
						}
					}
					if(!builds.equals("")){
						build.setPkBuildingfile(builds.substring(0,builds.length()-1));
					}
					if(!houses.equals("")){
						house.setPkHousesource(houses.substring(0,houses.length()-1));
					}
				}
				ywcf.setPkBuild(build);
				ywcf.setPkHouse(house);
				ywcf.setNtaxrate(new Double(wgContract.getNtaxrate()));
				ywcf.setDr(0);
				ywcf.setNrecmny(recmny);
				nallysmny += ywcf.getNrecmny();
				if(end.compareTo(wgContract.getDenddate())==0){//合同最后一笔
					Double nallnotaxmny=getDbobjBy2w(nallysmny/(1+ywcf.getNtaxrate()/100));//合同总金额直接计算无税金额
					Double nalltaxmny=nallysmny-nallnotaxmny;//合同总金额直接计算税额
					ywcf.setNnotaxmny(nallnotaxmny-nnotaxmny);
					ywcf.setNtaxmny(nalltaxmny-ntaxmny);
				}else{
					ywcf.setNnotaxmny(getDbobjBy2w(recmny/(1+ywcf.getNtaxrate()/100)));
					ywcf.setNtaxmny(recmny-ywcf.getNnotaxmny());
					nnotaxmny += ywcf.getNnotaxmny();
					ntaxmny += ywcf.getNtaxmny();
				}
				ywcf.setNrealmny(new Double(0));
				listywcf.add(ywcf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		wgContract.setWgContractYwcfList(listywcf);
		return wgContract;
	}
	
	/**
	 * 计算物业费拆分（含免租）
	 * @param wgContract
	 * @return
	 */
	public static WgContract recalWyfcf2(WgContract wgContract){
		List<WgContractWyfcf> listwyfcf=new ArrayList<WgContractWyfcf>();
		List<WgContractWyf> list_wy=wgContract.getWgContractWyfList();//物业费页签
		List<WgContractWytype> list_wyt=wgContract.getWgContractWytypeList();//物业支付方式页签
		List<WgContractCust> list_cust=wgContract.getWgContractCustList();//客户信息页签
		if(list_wy==null||list_wy.size()==0){//无物业费不拆分
			return wgContract;
		}
		BdCustomer cust=list_cust.get(0).getPkCustomer();
		//物业费支付方式拆分
		List<Map<String, Object>> paymap = recalPaystyleByWy(wgContract);
		BdProject costp=list_wyt.get(0).getPkYsproject();//预算项目（表体租赁支付方式第一行为默认）
		
		if(paymap!=null&&paymap.size()>0){
			for (Map<String, Object> map : paymap) {
				Date ud_first=(Date) map.get("start");
				Date ud_last=(Date) map.get("end");
				Integer paymonth=getIntObj(map.get("paymonth"));
				Integer paydays=getIntObj(map.get("paydays"));
				
				Date start=ud_first;
				while(start.compareTo(ud_last)<=0){
					Date start1=start;
					Date end=CalendarUtls.getAfterNDay(CalendarUtls.getNextMonthDay(start, paymonth), paydays);
					Date end2=CalendarUtls.getBeforeFirstDay(end);
					
					if(end2.compareTo(ud_last)>=0){//结束日期之后
						end2=ud_last;
					}
					Double nmonmny=new Double(0);
					ZlBuildingfile build=new ZlBuildingfile();
					ZlHousesource house=new ZlHousesource();
					String builds="";
					String houses="";
					for (WgContractWyf wyf : list_wy) {
						nmonmny+=wyf.getNfeemny();
						if(!builds.contains(wyf.getPkBuild().getPkBuildingfile())){
							builds+=wyf.getPkBuild().getPkBuildingfile()+",";
						}
						if(!houses.contains(wyf.getPkHouse().getPkHousesource())){
							houses+=wyf.getPkHouse().getPkHousesource()+",";
						}
					}
					if(builds!=""){
						build.setPkBuildingfile(builds.substring(0,builds.length()-1));
					}
					if(houses!=""){
						house.setPkHousesource(houses.substring(0,houses.length()-1));
					}
					Integer month=CalendarUtls.getBetweenMonths(start, end2);
					Integer day=CalendarUtls.getLeftDays(start, end2);
					Double ndaymny=new Double(0);
					if(day!=0){//余天
						Date d1=CalendarUtls.getNextMonthDay(start, month);
						Date d2=CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(d1));
						Integer mday=CalendarUtls.getBetweenTwoDays(d1, d2);
						ndaymny=nmonmny/mday;
					}
					WgContractWyfcf wyfcf = new WgContractWyfcf();
					wyfcf.setDstartdate(start1);
					wyfcf.setDenddate(end2);
					wyfcf.setPkCostproject(costp);
					wyfcf.setPkCustomer(cust);
					wyfcf.setPkBuild(build);
					wyfcf.setPkHouse(house);
					wyfcf.setNtaxrate(new Double(list_wy.get(0).getNtaxrate()));
					wyfcf.setDr(0);
					wyfcf.setNmonthmny(nmonmny);
					wyfcf.setNrecmny(nmonmny*month+ndaymny*day);
					wyfcf.setNnotaxmny(getDbobjBy2w(wyfcf.getNrecmny()/(1+wyfcf.getNtaxrate()/100)));
					wyfcf.setNtaxmny(wyfcf.getNrecmny()-wyfcf.getNnotaxmny());
					wyfcf.setNrealmny(new Double(0));
					listwyfcf.add(wyfcf);
					
					start=CalendarUtls.getAfterFirstDay(end2);//交替起始日期
				}
			}
		}
		wgContract.setWgContractWyfcfList(listwyfcf);
		return wgContract;
	}
	
	/**
	 * 计算其他周期费用拆分
	 * @param wgContract
	 * @return
	 */
	public static WgContract recalZqfycf(WgContract wgContract){
		List<WgContractZqfycf> listzqcf=new ArrayList<WgContractZqfycf>();
		List<WgContractZqfy> list_zq=wgContract.getWgContractZqfyList();//物业费页签
		if(list_zq==null||list_zq.size()==0){//无其他周期费用不拆分
			return wgContract;
		}
		Date ht_first=CalendarUtls.getDateNoTime(wgContract.getDstartdate());
		Date ht_last=CalendarUtls.getDateNoTime(wgContract.getDenddate());
		Double allmny=new Double(0);
		
		for (int i=0;i<list_zq.size();i++) {
			WgContractZqfy zq=list_zq.get(i);
			//按月拆分 处理逻辑
			Date start=ht_first;
			while(start.compareTo(ht_last)<=0){
				Date end=CalendarUtls.getNextMonthDay(start,zq.getDsfzq());
				Date end2=CalendarUtls.getBeforeFirstDay(end);
				if(end2.compareTo(ht_last)>=0){
					end2=ht_last;
				}
				Integer month=CalendarUtls.getBetweenMonths(start, end2);
				Integer day=CalendarUtls.getLeftDays(start, end2);
				Double ndaymny=new Double(0);
				if(day!=0){//余天
					Date d1=CalendarUtls.getNextMonthDay(start, month);
					Date d2=CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(d1));
					Integer mday=CalendarUtls.getBetweenTwoDays(d1, d2);
					ndaymny=zq.getNfeemny()/mday;
				}
				Double recmny=zq.getNfeemny()*month+ndaymny*day;
				WgContractZqfycf zqcf = new WgContractZqfycf();
				zqcf.setDstartdate(start);
				zqcf.setDenddate(end2);
				zqcf.setPkCostproject(zq.getPkYsproject());
				zqcf.setPkCustomer(zq.getPkCustomer());
				zqcf.setNtaxrate(new Double(zq.getNtaxrate()));
				zqcf.setDr(0);
				zqcf.setNmonthmny(zq.getNfeemny());
				zqcf.setNrecmny(recmny);
				zqcf.setNnotaxmny(getDbobjBy2w(zqcf.getNrecmny()/(1+zqcf.getNtaxrate()/100)));
				zqcf.setNtaxmny(zqcf.getNrecmny()-zqcf.getNnotaxmny());
				zqcf.setNrealmny(new Double(0));
				listzqcf.add(zqcf);
				allmny+=recmny;
				start = end;// 交替起始日期
			}
		}
		wgContract.setWgContractZqfycfList(listzqcf);
		return wgContract;
	}
	/**
	 * 计算收入拆分（含免租增长）
	 * @param wgContract
	 * @return
	 */
	public static WgContract recalSrcf(WgContract wgContract){
		Office pkOrg=officeService.get(wgContract.getPkOrg());
		Long ismonth=pkOrg.getExtend().getExtendI1();//是否按月拆分，否则与业务拆分一致
		List<WgContractSrcf> list_srcf=new ArrayList<WgContractSrcf>();
		List<WgContractYwcf> list_ywcf=wgContract.getWgContractYwcfList();//业务拆分
		if(list_ywcf.size()>0){//有数据拆分，无数据跳过
			if(ismonth==1){
				//获取合同总金额
				Double nysmny=new Double(0);
				Double nnotaxmny=new Double(0);
				Double ntaxmny=new Double(0);
				for (WgContractYwcf ywcf : wgContract.getWgContractYwcfList()) {
					nysmny+=ywcf.getNrecmny();
					nnotaxmny+=ywcf.getNnotaxmny();
					ntaxmny+=ywcf.getNtaxmny();
				}
				Integer htalldays=CalendarUtls.getBetweenTwoDays(wgContract.getDstartdate(), wgContract.getDenddate());//合同总天数
				Double ndaytz=getDbobjBy2w(nnotaxmny/htalldays);//每天摊销，合同总金额无税金额/合同天数
				// 处理逻辑
				Date zjstart = wgContract.getDstartdate();
				Date zjend = wgContract.getDenddate();
				WgContractYwcf ywcf=wgContract.getWgContractYwcfList().get(0);
				Double ysmny = new Double(0);
				Double notaxmny=new Double(0);
				Double taxmny=new Double(0);
				Date start=zjstart;
				while (start.compareTo(zjend)<=0) {
					WgContractSrcf csvo = new WgContractSrcf();
					Date end = CalendarUtls.getMaxMonthDay(start);
					Integer days=CalendarUtls.getBetweenTwoDays(start, end);
					Double yqrnotaxmny=ndaytz*days;//无税金额
					Double mny1=getDbobjBy2w(yqrnotaxmny*(ywcf.getNtaxrate()/100+1));//应确认
					Double mny2=mny1-yqrnotaxmny;//税额
					
					if (end.compareTo(zjend)>=0) {// 结束日期之后(拆分的最后一行)
						end = zjend;
						csvo.setNrecmny(nysmny-ysmny);
						csvo.setNnotaxmny(nnotaxmny-notaxmny);
						csvo.setNtaxmny(ntaxmny-taxmny);
					} else {
						csvo.setNrecmny(mny1);
						csvo.setNnotaxmny(yqrnotaxmny);
						csvo.setNtaxmny(mny2);
					}
					csvo.setPkCostproject(ywcf.getPkCostproject());
					csvo.setPkCustomer(ywcf.getPkCustomer());
					csvo.setPkBuild(ywcf.getPkBuild());
					csvo.setPkHouse(ywcf.getPkHouse());
					csvo.setDstartdate(start);
					csvo.setDenddate(end);
					csvo.setNtaxrate(ywcf.getNtaxrate());
					csvo.setNrealmny(new Double(0));
					csvo.setVsrctype("房租");
					list_srcf.add(csvo);
					ysmny += mny1;
					notaxmny += yqrnotaxmny;
					taxmny += mny2;
					start = CalendarUtls.getNextMonthFirstDay(start);// 交替起始日期
				}
			}else{
				// 处理逻辑
				for (WgContractYwcf ywcf : list_ywcf) {
					WgContractSrcf csvo = new WgContractSrcf();
					csvo.setPkCostproject(ywcf.getPkCostproject());
					csvo.setPkCustomer(ywcf.getPkCustomer());
					csvo.setPkBuild(ywcf.getPkBuild());
					csvo.setPkHouse(ywcf.getPkHouse());
					csvo.setDstartdate(ywcf.getDstartdate());
					csvo.setDenddate(ywcf.getDenddate());
					csvo.setNtaxrate(ywcf.getNtaxrate());
					csvo.setNrecmny(ywcf.getNrecmny());
					csvo.setNnotaxmny(ywcf.getNnotaxmny());
					csvo.setNtaxmny(ywcf.getNtaxmny());
					csvo.setNrealmny(new Double(0));
					csvo.setVsrctype("房租");
					list_srcf.add(csvo);
				}
			}
			
		}
		
		List<WgContractWyfcf> list_wycf=wgContract.getWgContractWyfcfList();//物业费拆分
		if(list_wycf.size()>0){//有数据拆分，无数据跳过
			if(ismonth==1){
				for(WgContractWyfcf wycf : list_wycf){
					Date zjstart = wycf.getDstartdate();
					Date zjend = wycf.getDenddate();
					Double nsfmny = wycf.getNmonthmny();
					Double nysmny = wycf.getNrecmny();
					Double nnotaxmny = wycf.getNnotaxmny();
					Double ntaxmny = wycf.getNtaxmny();
					Double ysmny = new Double(0);
					Double notaxmny=new Double(0);
					Double taxmny=new Double(0);
					
					Date start=zjstart;
					while (start.compareTo(zjend)<=0) {
						Date end = CalendarUtls.getMaxMonthDay(start);
						Integer days1=CalendarUtls.getBetweenTwoDays(start, CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(start)))+1;
						Double yqrmny=new Double(0);
						Double mny1=new Double(0);//无税金额
						Double mny2=new Double(0);//税额
						if (end.compareTo(zjend)>=0) {// 结束日期之后(拆分的最后一行)
							end = zjend;
							yqrmny=nysmny-ysmny;
							mny1=nnotaxmny-notaxmny;
							mny2=ntaxmny-taxmny;
						} else {
							Integer months=CalendarUtls.getBetweenMonths(start, end);
							Date date=CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(start, months));
							Integer days=CalendarUtls.getBetweenTwoDays(date, end);
							yqrmny=getDbobjBy2w(nsfmny*months+(nsfmny/days1*days));
							
							mny1=getDbobjBy2w(yqrmny/(wycf.getNtaxrate()/100+1));//无税金额
							mny2=yqrmny-mny1;//税额
						}
						WgContractSrcf csvo = new WgContractSrcf();
						csvo.setPkCostproject(wycf.getPkCostproject());
						csvo.setPkCustomer(wycf.getPkCustomer());
						csvo.setPkBuild(wycf.getPkBuild());
						csvo.setPkHouse(wycf.getPkHouse());
						csvo.setDstartdate(start);
						csvo.setDenddate(end);
						csvo.setNtaxrate(wycf.getNtaxrate());
						csvo.setNrecmny(yqrmny);
						csvo.setNnotaxmny(mny1);
						csvo.setNtaxmny(mny2);
						csvo.setNrealmny(new Double(0));
						csvo.setVsrcid(wycf.getPkContractWyfcf());
						csvo.setVsrctype("物业");
						list_srcf.add(csvo);
						
						ysmny = ysmny+yqrmny;
						notaxmny=notaxmny+mny1;
						taxmny=taxmny+mny2;
						Date nextfistday = CalendarUtls.getNextMonthFirstDay(start);
						start = nextfistday;// 交替起始日期
					}
				}
			}else{
				for(WgContractWyfcf wycf : list_wycf){
					WgContractSrcf csvo = new WgContractSrcf();
					csvo.setPkCostproject(wycf.getPkCostproject());
					csvo.setPkCustomer(wycf.getPkCustomer());
					csvo.setPkBuild(wycf.getPkBuild());
					csvo.setPkHouse(wycf.getPkHouse());
					csvo.setDstartdate(wycf.getDstartdate());
					csvo.setDenddate(wycf.getDenddate());
					csvo.setNtaxrate(wycf.getNtaxrate());
					csvo.setNrecmny(wycf.getNrecmny());
					csvo.setNnotaxmny(wycf.getNnotaxmny());
					csvo.setNtaxmny(wycf.getNtaxmny());
					csvo.setNrealmny(new Double(0));
					csvo.setVsrcid(wycf.getPkContractWyfcf());
					csvo.setVsrctype("物业");
					list_srcf.add(csvo);
				}
			}
		}
		
		List<WgContractZqfycf> list_zqcf=wgContract.getWgContractZqfycfList();//其他周期费用拆分
		if(list_zqcf.size()>0){//有数据拆分，无数据跳过
			if(ismonth==1){
				for(WgContractZqfycf zqcf : list_zqcf){
					Date zjstart = zqcf.getDstartdate();
					Date zjend = zqcf.getDenddate();
					Double nsfmny = zqcf.getNmonthmny();
					Double nysmny = zqcf.getNrecmny();
					Double nnotaxmny = zqcf.getNnotaxmny();
					Double ntaxmny = zqcf.getNtaxmny();
					Double ysmny = new Double(0);
					Double notaxmny=new Double(0);
					Double taxmny=new Double(0);
					
					Date start=zjstart;
					while (start.compareTo(zjend)<=0) {
						Date end = CalendarUtls.getMaxMonthDay(start);
						Integer days1=CalendarUtls.getBetweenTwoDays(start, CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(start)))+1;
						Double yqrmny=new Double(0);
						Double mny1=new Double(0);//无税金额
						Double mny2=new Double(0);//税额
						if (end.compareTo(zjend)>=0) {// 结束日期之后(拆分的最后一行)
							end = zjend;
							yqrmny=nysmny-ysmny;
							mny1=nnotaxmny-notaxmny;
							mny2=ntaxmny-taxmny;
						} else {
							Integer months=CalendarUtls.getBetweenMonths(start, end);
							Date date=CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(start, months));
							Integer days=CalendarUtls.getBetweenTwoDays(date, end);
							yqrmny=getDbobjBy2w(nsfmny*months+(nsfmny/days1*days));
							
							mny1=getDbobjBy2w(yqrmny/(zqcf.getNtaxrate()/100+1));//无税金额
							mny2=yqrmny-mny1;//税额
						}
						WgContractSrcf csvo = new WgContractSrcf();
						csvo.setPkCostproject(zqcf.getPkCostproject());
						csvo.setPkCustomer(zqcf.getPkCustomer());
						csvo.setPkBuild(zqcf.getPkBuild());
						csvo.setPkHouse(zqcf.getPkHouse());
						csvo.setDstartdate(start);
						csvo.setDenddate(end);
						csvo.setNtaxrate(zqcf.getNtaxrate());
						csvo.setNrecmny(yqrmny);
						csvo.setNnotaxmny(mny1);
						csvo.setNtaxmny(mny2);
						csvo.setNrealmny(new Double(0));
						csvo.setVsrcid(zqcf.getPkContractZqfycf());
						csvo.setVsrctype("其他");
						list_srcf.add(csvo);
						
						ysmny = ysmny+yqrmny;
						notaxmny=notaxmny+mny1;
						taxmny=taxmny+mny2;
						Date nextfistday = CalendarUtls.getNextMonthFirstDay(start);
						start = nextfistday;// 交替起始日期
					}
				}
			}else{
				for(WgContractZqfycf zqcf : list_zqcf){
					WgContractSrcf csvo = new WgContractSrcf();
					csvo.setPkCostproject(zqcf.getPkCostproject());
					csvo.setPkCustomer(zqcf.getPkCustomer());
					csvo.setPkBuild(zqcf.getPkBuild());
					csvo.setPkHouse(zqcf.getPkHouse());
					csvo.setDstartdate(zqcf.getDstartdate());
					csvo.setDenddate(zqcf.getDenddate());
					csvo.setNtaxrate(zqcf.getNtaxrate());
					csvo.setNrecmny(zqcf.getNrecmny());
					csvo.setNnotaxmny(zqcf.getNnotaxmny());
					csvo.setNtaxmny(zqcf.getNtaxmny());
					csvo.setNrealmny(new Double(0));
					csvo.setVsrcid(zqcf.getPkContractZqfycf());
					csvo.setVsrctype("其他");
					list_srcf.add(csvo);
				}
			}
		}
		wgContract.setWgContractSrcfList(list_srcf);
		return wgContract;
	}
	
	/**
	 * 按租赁支付方式拆分日期
	 * @param panel
	 * @return
	 * @throws BusinessException
	 */
	private static List<Map<String, Object>> recalPaystyle(WgContract wgContract) {
		
		paymap.clear();
		List<WgContractZltype> listzltype=wgContract.getWgContractZltypeList();
		
		Date pay_fist=wgContract.getDstartdate();
		Date ud_last=wgContract.getDenddate();
		List<Map<String, Object>> listmap=new ArrayList<Map<String,Object>>();
		//拆分日期(付款方式)
		if(listzltype!=null&&listzltype.size()>0){
			for (int j = 0; j < listzltype.size(); j++) {
				Date zlend=ud_last;//租赁支付方式结束日期
				if(j<listzltype.size()-1){//不是最后一行
					zlend=CalendarUtls.getBeforeFirstDay(listzltype.get(j+1).getDstartdate());
				}
				Integer paymonth=listzltype.get(j).getPayyear()*12+listzltype.get(j).getPaymonth();
				Integer paydays=listzltype.get(j).getPayday();
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("start", pay_fist);
				map.put("end", zlend);
				map.put("paymonth", paymonth);
				map.put("paydays", paydays);
				listmap.add(map);
				pay_fist=CalendarUtls.getAfterFirstDay(zlend);//交替日期
			}
		}
		paymap=listmap;
		return listmap;
	}
	/**
	 * 按租赁支付方式周期拆分日期
	 * @param panel
	 * @return
	 * @throws BusinessException
	 */
	private static List<Map<String, Object>> recalYwcfByPaystyle(WgContract wgContract,List<Map<String, Object>> paymap) {
		
		List<Map<String, Object>> ywmap=new ArrayList<Map<String,Object>>();
		List<WgContractMzq> listmz=wgContract.getWgContractMzqList();//免租期
		
		if(paymap!=null&&paymap.size()>0){
			for(Map<String, Object> map : paymap){
				Date ud_first=(Date) map.get("start");
				Date ud_last=(Date) map.get("end");
				Integer paymonth=getIntObj(map.get("paymonth"));
				Integer paydays=getIntObj(map.get("paydays"));
				
				Date start=ud_first;
				while(start.compareTo(ud_last)<=0){
					Map<String, Object> map2=new HashMap<String, Object>();
					Date start1=start;
					//计算年区间开始免租
					if(listmz!=null&&listmz.size()>0){
						for(int j=0;j<listmz.size();j++){
							Date mzstart=listmz.get(j).getDstartdate();
							Date mzend=listmz.get(j).getDenddate();
							if(mzstart.compareTo(start)==0){//免租开始日期等于年区间开始日期
								start=CalendarUtls.getAfterFirstDay(mzend);
							}
						}
					}
					//如果不是当月第一天，则取前一天往后推
					Date end2=CalendarUtls.getAfterNDay(CalendarUtls.getNextMonthDay(CalendarUtls.getBeforeFirstDay(start), paymonth), paydays);
					Boolean tag=CalendarUtls.isFirstDayOfMonth(start);
					if(tag){//如果是当月第一天，则往后推再取前一天
						end2=CalendarUtls.getBeforeFirstDay(CalendarUtls.getAfterNDay(CalendarUtls.getNextMonthDay(start, paymonth), paydays));
					}
					
					Double nmny=new Double(0);
					map2.put("start", start1);
					if(end2.compareTo(ud_last)>=0){//结束日期之后
						end2=ud_last;
						nmny=getDbobjBy2w(calculateMnyBetweenDate(wgContract, start, end2));
					}else{
						//加入年区间中间免租
						if(listmz!=null&&listmz.size()>0){
							for(int j=0;j<listmz.size();j++){
								Date mzstart=listmz.get(j).getDstartdate();
								Date mzend=listmz.get(j).getDenddate();
								if(mzstart.compareTo(start)>0&&mzstart.compareTo(end2)<0){//免租开始日期在年区间之间
									Integer months=CalendarUtls.getBetweenMonths(mzstart, mzend);//免租包含月数
									Integer days=0;
									if(CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(mzstart, months)).compareTo(mzend)!=0){
										days=CalendarUtls.getBetweenTwoDays(CalendarUtls.getNextMonthDay(mzstart, months),mzend);//不满整月天数
									}
									end2=CalendarUtls.getAfterNDay(CalendarUtls.getNextMonthDay(end2, months), days);
								}
							}
						}
						nmny=getDbobjBy2w(calculateMnyBetweenDate(wgContract, start, end2));
					}
					map2.put("end", end2);
					map2.put("nysmny", nmny);
					ywmap.add(map2);
					
					start=CalendarUtls.getAfterFirstDay(end2);//交替起始日期
				}
			}
		}
		return ywmap;
	}
	
	/**
	 * 按物业支付方式拆分日期
	 * @param panel
	 * @return
	 * @throws BusinessException
	 */
	private static List<Map<String, Object>> recalPaystyleByWy(WgContract wgContract) {
		
		List<Map<String, Object>> listmap=new ArrayList<Map<String,Object>>();
		//按付款方式拆分
		List<WgContractWytype> listwytype=wgContract.getWgContractWytypeList();
		
		Date pay_fist=wgContract.getWgContractWyfList().get(0).getDstartdate();//费用开始日期
		Date ud_last=wgContract.getDenddate();//合同结束日期
		
		//拆分日期(付款方式)
		if(listwytype!=null&&listwytype.size()>0){
			for (int j = 0; j < listwytype.size(); j++) {
				Date wyend=ud_last;//物业支付方式结束日期
				if(j< listwytype.size()-1){//不是最后一行
					wyend=CalendarUtls.getBeforeFirstDay( listwytype.get(j+1).getDstartdate());
				}
				Integer paymonth= listwytype.get(j).getPayyear()*12+ listwytype.get(j).getPaymonth();
				Integer paydays= listwytype.get(j).getPayday();
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("start", pay_fist);
				map.put("end", wyend);
				map.put("paymonth", paymonth);
				map.put("paydays", paydays);
				listmap.add(map);
				pay_fist=CalendarUtls.getAfterFirstDay(wyend);//交替日期
			}
		}
		
		return listmap;
	}
	
	/**
	 * 计算两个时间段内的合同金额含免租（按月不足月部分按天）
	 * @param panel
	 * @throws BusinessException
	 */
	private static Double calculateMnyBetweenDate(WgContract wgContract,Date ud_fist,Date ud_last) {
		
		//增长期页签
		List<WgContractZzq> listzz=wgContract.getWgContractZzqList();
		List<WgContractMzq> listmz=wgContract.getWgContractMzqList();
		
		Date ud_fist1=ud_fist;
		Date ud_last1=ud_last;
		
		List<Map<String, Object>> listmap=new ArrayList<Map<String,Object>>();
		Double monthmny=wgContract.getNyearprice()/12;
		Integer ydays=CalendarUtls.getBetweenTwoDays(wgContract.getDstartdate(),CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(wgContract.getDstartdate(), 12)));//第一年天数
		Double daymny=new BigDecimal(wgContract.getNyearprice()/ydays).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		//加入增长
		List<Map<String, Object>> zzmap=new ArrayList<Map<String,Object>>();//增长根据合同起始截止日期拆分
		if(listzz!=null&&listzz.size()>0){
			for(int k=0;k<listzz.size();k++){
				WgContractZzq zzq=listzz.get(k);
				if(k==0){//第一行
					Date dend=CalendarUtls.getBeforeFirstDay(zzq.getDstartdate());
					for(WgContractRentprice rent : wgContract.getWgContractRentpriceList()){
						if(rent.getDstartdate().compareTo(zzq.getDstartdate())<0){//增长期日期之前的按年正常拆分
							Integer ydays2=CalendarUtls.getBetweenTwoDays(rent.getDstartdate(), CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(rent.getDstartdate(), 12)));//年区间天数
							if(ydays.compareTo(ydays2)==0){//年天数相同，金额不变
								
							}else{
								Double nyearmny=new BigDecimal(daymny*ydays2).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
								monthmny=new BigDecimal(nyearmny/12).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
							}
							Date dend1=dend;
							if(rent.getDenddate().compareTo(dend)<0){
								dend1=rent.getDenddate();
							}
							Boolean tag=true;
							if(zzmap.size()>0){
								//获取存储的上一个map
								Map<String, Object> premap=zzmap.get(zzmap.size()-1);
								if(getDbobj(premap.get("nmonthmny")).compareTo(monthmny)==0
										&&getDbobj(premap.get("ndaymny")).compareTo(daymny)==0){//存储金额相同，直接改变上一个的结束日期
									zzmap.get(zzmap.size()-1).put("dend", dend1);
									tag=false;
								}
							}
							if(tag){
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("dstart", rent.getDstartdate());
								map.put("dend", dend1);
								map.put("nmonthmny", monthmny);
								map.put("ndaymny", daymny);
								zzmap.add(map);
							}
						}
					}
				}
				Date dend=wgContract.getDenddate();
				if(k!=listzz.size()-1){//不是最后一行
					dend=CalendarUtls.getBeforeFirstDay(listzz.get(k+1).getDstartdate());
				}
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("dstart", zzq.getDstartdate());
				map.put("dend", dend);
				map.put("nmonthmny", zzq.getNzzyearptzh()/12);
				map.put("ndaymny", zzq.getNzzdayp());
				zzmap.add(map);
			}
			for (Map<String, Object> map : zzmap) {
				Date zzstart = (Date) map.get("dstart");//增长开始日期
				Date zzend = (Date) map.get("dend");//增长结束日期
				Double zzmonthmny = getDbobj(map.get("nmonthmny"));//增长后月租金
				Double zzdaymny = getDbobj(map.get("ndaymny"));//增长后日租金
				
				if(zzstart.compareTo(ud_last)>0){//开始日期在区间结束日期之后
					continue ;
				}else{//开始日期在区间结束日期之前
					if(zzend.compareTo(ud_fist1)<0){//结束日期在区间开始日期之前
						continue;
					}else{//结束日期在区间开始日期之后
						if(zzend.compareTo(ud_last)<0){//结束日期在区间结束日期之前
							ud_last1=zzend;
						}else{//结束日期在区间结束日期之后
							ud_last1=ud_last;
						}
					}
				}
				Integer mzmonths=0;
				Integer mzdays=0;
				for(WgContractMzq mzq : listmz){
					Date mzstart=mzq.getDstartdate();
					Date mzend=mzq.getDenddate();
					if(mzstart.compareTo(ud_fist1)<0){//免租开始在区间开始日期之前
						if(mzend.compareTo(ud_fist1)<0){//免租结束在区间开始之前
							continue;
						}else{//免租结束在区间开始之后
							if(mzend.compareTo(ud_last1)<0){//免租结束日期在区间结束日期之前
								mzmonths=CalendarUtls.getBetweenMonths(ud_fist1, mzend);
								mzdays=CalendarUtls.getLeftDays(ud_fist1, mzend);
							}else{//免租结束日期在区间结束日期之后
								mzmonths=CalendarUtls.getBetweenMonths(ud_fist1, ud_last1);
								mzdays=CalendarUtls.getLeftDays(ud_fist1, ud_last1);
							}
						}
					}else{//免租开始在区间开始日期之后
						if(mzstart.compareTo(ud_last1)>0){//免租开始在区间结束之后
							continue;
						}else{//免租开始在区间结束之前
							if(mzend.compareTo(ud_last1)<0){//免租结束在区间结束之前
								mzmonths=CalendarUtls.getBetweenMonths(mzstart, mzend);
								mzdays=CalendarUtls.getLeftDays(mzstart, mzend);
							}else{//免租结束在区间结束之后
								mzmonths=CalendarUtls.getBetweenMonths(mzstart, ud_last1);
								mzdays=CalendarUtls.getLeftDays(mzstart, ud_last1);
							}
						}
					}
				}
				Map<String, Object> mapzj=new HashMap<String, Object>();
				mapzj.put("nmonthmny", zzmonthmny);
				Integer months=CalendarUtls.getBetweenMonths(ud_fist1, ud_last1);
				Integer days=CalendarUtls.getLeftDays(ud_fist1, ud_last1);
				mapzj.put("months", months-mzmonths);
				mapzj.put("ndaymny", zzdaymny);
				mapzj.put("days", days-mzdays);
				listmap.add(mapzj);
				ud_fist1=CalendarUtls.getAfterFirstDay(ud_last1);//交替日期
			}
		}else{//不含增长期，直接计算
			for(WgContractRentprice rent : wgContract.getWgContractRentpriceList()){
				Integer ydays2=CalendarUtls.getBetweenTwoDays(rent.getDstartdate(), CalendarUtls.getBeforeFirstDay(CalendarUtls.getNextMonthDay(rent.getDstartdate(), 12)));//年区间天数
				if(ydays.compareTo(ydays2)==0){//年天数相同，金额不变
					
				}else{
					Double nyearmny=new BigDecimal(daymny*ydays2).setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
					monthmny=new BigDecimal(nyearmny/12).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				Boolean tag=true;
				if(zzmap.size()>0){
					//获取存储的上一个map
					Map<String, Object> premap=zzmap.get(zzmap.size()-1);
					if(getDbobj(premap.get("nmonthmny")).compareTo(monthmny)==0
							&&getDbobj(premap.get("ndaymny")).compareTo(daymny)==0){//存储金额相同，直接改变上一个的结束日期
						zzmap.get(zzmap.size()-1).put("dend", rent.getDenddate());
						tag=false;
					}
				}
				if(tag){
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("dstart", rent.getDstartdate());
					map.put("dend", rent.getDenddate());
					map.put("nmonthmny", monthmny);
					map.put("ndaymny", daymny);
					zzmap.add(map);
				}
			}
			for (Map<String, Object> map : zzmap) {
				Date dstart = (Date) map.get("dstart");//开始日期
				Date dend = (Date) map.get("dend");//结束日期
				Double nmonthmny = getDbobj(map.get("nmonthmny"));//月租金
				Double ndaymny = getDbobj(map.get("ndaymny"));//日租金
				Integer months=0;
				Integer days=0;
				if(dstart.compareTo(ud_last1)>0){//年租金开始日期在区间结束日期之后
					continue;
				}
				if(dend.compareTo(ud_fist1)<0){//年租金结束日期在区间开始日期之前
					continue;
				}
				if(dstart.compareTo(ud_fist1)>=0){//年租金开始日期在区间开始日期之后
					if(dend.compareTo(ud_last1)<=0){//年租金结束日期在区间结束日期之前
						months=CalendarUtls.getBetweenMonths(dstart, dend);
						days=CalendarUtls.getLeftDays(dstart, dend);
					}else{
						months=CalendarUtls.getBetweenMonths(dstart, ud_last1);
						days=CalendarUtls.getLeftDays(dstart, ud_last1);
					}
				}else if(dend.compareTo(ud_last1)<=0){//年租金结束日期在区间结束日期之前
					months=CalendarUtls.getBetweenMonths(ud_fist1, dend);
					days=CalendarUtls.getLeftDays(ud_fist1, dend);
				}else{
					months=CalendarUtls.getBetweenMonths(ud_fist1, ud_last1);
					days=CalendarUtls.getLeftDays(ud_fist1, ud_last1);
				}
				Integer mzmonths=0;
				Integer mzdays=0;
				for(WgContractMzq mzq : listmz){
					Date mzstart=mzq.getDstartdate();
					Date mzend=mzq.getDenddate();
					if(mzstart.compareTo(ud_fist1)<0){//免租开始在区间开始日期之前
						if(mzend.compareTo(ud_fist1)<0){//免租结束在区间开始之前
							continue;
						}else{//免租结束在区间开始之后
							if(mzend.compareTo(ud_last1)<0){//免租结束日期在区间结束日期之前
								mzmonths=CalendarUtls.getBetweenMonths(ud_fist1, mzend);
								mzdays=CalendarUtls.getLeftDays(ud_fist1, mzend);
							}else{//免租结束日期在区间结束日期之后
								mzmonths=CalendarUtls.getBetweenMonths(ud_fist1, ud_last1);
								mzdays=CalendarUtls.getLeftDays(ud_fist1, ud_last1);
							}
						}
					}else{//免租开始在区间开始日期之后
						if(mzstart.compareTo(ud_last1)>0){//免租开始在区间结束之后
							continue;
						}else{//免租开始在区间结束之前
							if(mzend.compareTo(ud_last1)<0){//免租结束在区间结束之前
								mzmonths=CalendarUtls.getBetweenMonths(mzstart, mzend);
								mzdays=CalendarUtls.getLeftDays(mzstart, mzend);
							}else{//免租结束在区间结束之后
								mzmonths=CalendarUtls.getBetweenMonths(mzstart, ud_last1);
								mzdays=CalendarUtls.getLeftDays(mzstart, ud_last1);
							}
						}
					}
				}
				Map<String, Object> mapzj=new HashMap<String, Object>();
				mapzj.put("nmonthmny", nmonthmny);
				mapzj.put("months", months-mzmonths);
				mapzj.put("ndaymny", ndaymny);
				mapzj.put("days", days-mzdays);
				listmap.add(mapzj);
			}
		}
		
		Double udrow=new Double(0);
		//计算总金额
		for(Map<String, Object> maps:listmap){
			Double nmonthmny=getDbobj(maps.get("nmonthmny"));
			Double ndaymny=getDbobj(maps.get("ndaymny"));
			Integer month=getIntObj(maps.get("months"));
			Integer day=getIntObj(maps.get("days"));
			udrow+=nmonthmny*month+ndaymny*day;
		}
		return udrow;
	}
	
	private static Double getDbobj(Object obj){
		return obj==null?new Double(0):new Double(obj.toString());
	}
	private static Integer getIntObj(Object obj){
		return obj==null?0:new Integer(obj.toString());
	}
	private static Double getDbobjBy2w(Double ud){
		return new BigDecimal(ud).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}
