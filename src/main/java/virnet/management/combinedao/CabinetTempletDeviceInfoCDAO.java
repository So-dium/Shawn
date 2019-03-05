package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.CabinetTempletDeviceDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.entity.Exp;
import virnet.management.entity.CabinetTempletDevice;
import virnet.management.util.ViewUtil;

public class CabinetTempletDeviceInfoCDAO {
	
	private CabinetTempletDeviceDAO ctdDAO = new CabinetTempletDeviceDAO();
	private ViewUtil vutil = new ViewUtil();
	
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> showTaskDetail(List<List<Map<String, Object>>> list,String expName,boolean isEdit){
		
		ExpDAO eDAO = new ExpDAO();
		Integer initValue = null;	
		List<Exp> elist = eDAO.getListByProperty("expName", expName);
		//获得实验Id,以此查询该实验模板ID
		Integer cabinetTempletId=elist.get(0).getExpCabinetTempletId();
		
		//获得该实验所需的路由器数量,以list.size记录
		String[] para1={"cabinetTempletId",""+cabinetTempletId,"deviceType","1"};
		List<CabinetTempletDevice> result1 =  this.ctdDAO.getListByNProperty(para1);
		
		//非编辑状态
		if(!isEdit){
			List<Map<String, Object>> Rtlist = new ArrayList<Map<String, Object>>();
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "路由器数量");
			
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", result1.size());
				
			Map<String, Object> map13 = new HashMap<String, Object>();
			map13.put("name", "");
	
			Rtlist.add(map11);
			Rtlist.add(map12);
			Rtlist.add(map13);
			
			list.add(Rtlist);
		}
		//编辑状态应为下拉框
		else{
			List<Map<String, Object>> selectlist = new ArrayList<Map<String, Object>>();
			
			for(int i=0;i<3;i++){
				Map<String, Object> option = new HashMap<String, Object>();
				option.put("name", i);
				option.put("value", i);
				selectlist.add(option);
				if( i == result1.size())
					initValue = i;
				
			}
			
			List<Map<String, Object>> Rtlist = this.vutil.createList("路由器数量", "", "", selectlist, "singleselect", "", "Rt",initValue);
			
			list.add(Rtlist);
		}
		
		//获得该实验所需的三层交换机数量,以list.size记录
		String[] para3={"cabinetTempletId",""+cabinetTempletId,"deviceType","2"};
		List<CabinetTempletDevice> result3 =  this.ctdDAO.getListByNProperty(para3);
					
		//非编辑状态
		if(!isEdit){
			List<Map<String, Object>> sw3list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "三层交换机数量");
					
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", result3.size());
				
			Map<String, Object> map33 = new HashMap<String, Object>();
			map33.put("name", "");
			
			sw3list.add(map31);
			sw3list.add(map32);
			sw3list.add(map33);
					
			list.add(sw3list);
		}
		//编辑状态应为下拉框
		else{
			List<Map<String, Object>> selectlist = new ArrayList<Map<String, Object>>();
					
			for(int i=0;i<3;i++){
				Map<String, Object> option = new HashMap<String, Object>();
				option.put("name", i);
				option.put("value", i);
				if( i == result3.size())
					initValue = i;
				selectlist.add(option);
			}
					
			List<Map<String, Object>> sw3list = this.vutil.createList("三层交换机数量", "", "", selectlist, "singleselect", "", "Sw3",initValue);
					
			list.add(sw3list);
		}
		
		//获得该实验所需的二层交换机数量,以list.size记录
		String[] para2={"cabinetTempletId",""+cabinetTempletId,"deviceType","3"};
		List<CabinetTempletDevice> result2 =  this.ctdDAO.getListByNProperty(para2);
					
		//非编辑状态
		if(!isEdit){
			List<Map<String, Object>> sw2list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "二层交换机数量");
						
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", result2.size());
						
			Map<String, Object> map23 = new HashMap<String, Object>();
			map23.put("name", "");
					
			sw2list.add(map21);
			sw2list.add(map22);
			sw2list.add(map23);
							
			list.add(sw2list);
		}
		//编辑状态应为下拉框
		else{
			List<Map<String, Object>> selectlist = new ArrayList<Map<String, Object>>();
					
			for(int i=0;i<3;i++){
				Map<String, Object> option = new HashMap<String, Object>();
				option.put("name", i);
				option.put("value", i);
				selectlist.add(option);
				if( i == result2.size())
					initValue = i;
			}
							
			List<Map<String, Object>> sw2list = this.vutil.createList("二层交换机数量", "", "", selectlist, "singleselect", "", "Sw2",initValue);
							
			list.add(sw2list);
		}
		
		return list;
	}
	@SuppressWarnings("unchecked")
	public boolean save(Map<String, Object> deviceMap, Integer cabinetTempletId) {
		
		boolean flag=true;
		boolean success=true;
		//清空本表中此实验机柜模板表的该实验设备，而后重写
		List<CabinetTempletDevice> list = this.ctdDAO.getListByProperty("cabinetTempletId", cabinetTempletId);
		int i=0;
		while(i!=list.size()){
			this.ctdDAO.delete(list.get(i));
			i++;
		}
		int Order = 1;
		int count = 1;
		//重写路由器
		while(count<=(Integer)deviceMap.get("Rt")){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(1);
			ctd.setLanPortNum(8);    //赋予默认值为8,日后再改
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		count = 1;
		//重写三层交换机
		while(count<=(Integer)deviceMap.get("Sw3")){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(2);
			ctd.setLanPortNum(8);
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		count = 1;
		//重写二层交换机
		while(count<=(Integer)deviceMap.get("Sw2")){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(3);
			ctd.setLanPortNum(8);
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		count = 1;
		//重写PC，默认4个
		while(count<=4){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(4);
			ctd.setLanPortNum(8);
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		return success;
	}

	public boolean deleteEquipment(Integer cabinetTempletId) {

		// 清空本表中此实验机柜模板表的该实验设备
		try {
			List<CabinetTempletDevice> list = this.ctdDAO.getListByProperty("cabinetTempletId", cabinetTempletId);
			int i = 0;
			while (i != list.size()) {
				this.ctdDAO.delete(list.get(i));
				i++;
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	//返回本实验所需要的设备数量
	public Integer equipmentNumber(String expId){
		System.out.println("equipmentNumber");
		//获得实验机柜模板Id
		Integer EXPID = -1;
		try {
			EXPID = Integer.parseInt(expId);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("EXPID:" + EXPID);
		ExpDAO eDAO = new ExpDAO();
		Exp exp = (Exp) eDAO.getUniqueByProperty("expId",EXPID );
		Integer cabinetTempletId = exp.getExpCabinetTempletId();
		System.out.println("cabinetTempletId" + cabinetTempletId);
		//获得设备表
		@SuppressWarnings("unchecked")
		List<CabinetTempletDevice> ctdlist = this.ctdDAO.getListByProperty("cabinetTempletId", cabinetTempletId);
		System.out.println("ctdlist.size()" + ctdlist.size());
		return ctdlist.size();
	}
	//生成设备名称字符串
	public String equipment(String expId){
		String name_str = "";
		
		//获得实验机柜模板Id
		Integer EXPID = Integer.parseInt(expId);
		System.out.println("equipment expId:" + expId);
		ExpDAO eDAO = new ExpDAO();
		Exp exp = (Exp) eDAO.getUniqueByProperty("expId",EXPID );
		Integer cabinetTempletId = exp.getExpCabinetTempletId();
		System.out.println("equipment cabinetTempletId:" + cabinetTempletId);
		
		@SuppressWarnings("unchecked")
		List<CabinetTempletDevice> ctdlist = this.ctdDAO.getListByProperty("cabinetTempletId", cabinetTempletId);
		int deviceOrder=1;
		System.out.println("#####"+ctdlist.size());
		while(deviceOrder <= ctdlist.size()){
			System.out.print("%%%%"+deviceOrder);
			//按序号取设备
			String para[]={"cabinetTempletId",""+cabinetTempletId,"deviceOrder",""+deviceOrder};
			CabinetTempletDevice ctd = (CabinetTempletDevice)this.ctdDAO.getByNProperty(para);
			Integer deviceType = ctd.getDeviceType();
			switch(deviceType){
				case 1 : name_str = name_str + "RT##";break;
				case 2 : name_str = name_str + "SW3##";break;
				case 3 : name_str = name_str + "SW2##";break;
				case 4 : name_str = name_str + "PC##";break;
			}
			deviceOrder++;
		}
		String result = name_str.substring(0, name_str.length()-2);
		System.out.println(result);
		return result;
	}
}
