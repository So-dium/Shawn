package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.management.dao.FacilitiesDAO;
import virnet.management.entity.Facilities;
import virnet.management.util.ViewUtil;

public class FacilitiesInfoCDAO {
	
	private FacilitiesDAO fDAO = new FacilitiesDAO();
	private ViewUtil vutil = new ViewUtil();
	
 	public Map<String, Object> showFacilitiesDetail(String id, String name){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		String sql="select * from facilities where facilities_id="+name;
		
		List<Facilities> facilitieslist = new ArrayList<Facilities>();
		facilitieslist=this.fDAO.getListBySql(sql);
		String physicsMachinesName = facilitieslist.get(0).getFacilitiesBelongPhysicsMachines();
		Integer facilitiesOrder = facilitieslist.get(0).getFacilitiesOrder();
	
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "物理机柜  "+physicsMachinesName+"<i class='icon-double-angle-right'></i>"+" 设备 " + facilitiesOrder);		
		
		String returnid = id;

		Integer pri_key=Integer.parseInt(name);
		
		switch(id){
		case "time-management" :
		case "class-management" :
		case "course-management" :
		case "exp-management" : 
		case "facilities-management" : Map<String, Object> button = new HashMap<String, Object>();
									 button.put("content", "修改设备详情");
									 button.put("class", "btn button-new");
									 button.put("click", "editContent();");
									 list = FacilitiesManagement(pri_key, false); 
									 map.put("button", button);
									 returnid = "facilities-management";
									 break;//管理员的设备管理 --- 返回实验模板， 包括进入第二层
		}
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("id", returnid);
		
		return map;
	}
	
 	/**
 	 * 
 	 * 在管理员的权限中查看实验内容，并且进入实验桌面，修改配置
 	 * @param name the name of experiment want to query
 	 * @param isEdit check whether the list is for edit page
 	 * 
 	 * @return a list contains experiment name, standard time, profile and instruction, enter experiment 
 	 *  in the format of map                    //待定
 	 */
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> FacilitiesManagement(Integer id, boolean isEdit){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		//实验不能重名
		List<Facilities> flist = this.fDAO.getListByProperty("facilitiesId", id);   //待定

		if(flist.isEmpty() || flist.size() > 1){
			//error
			list.clear();
		}
		else{
			//get the experiment template   //待定
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "设备所属物理机柜");
			
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", flist.get(0).getFacilitiesBelongPhysicsMachines());
			if(isEdit){
				map12.put("class", "btn btn-link edit");
				map12.put("onclick", "editable(this);");
				map12.put("value", "facilitiesBelongPhysicsMachines");
			}
			
			list1.add(map11);
			list1.add(map12);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "设备序号");
			
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", flist.get(0).getFacilitiesOrder());
			if(isEdit){
				map22.put("class", "btn btn-link edit");
				map22.put("onclick", "editable(this);");
				map22.put("value", "facilitiesOrder");
			}
			
			list2.add(map21);
			list2.add(map22);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "设备类型");
			
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", flist.get(0).getFacilitiesType());
			if(isEdit){
				map32.put("class", "btn btn-link edit");
				map32.put("onclick", "editable(this);");
				map32.put("value", "facilitiesType");
			}
			
			list3.add(map31);
			list3.add(map32);
			
			List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map41 = new HashMap<String, Object>();
			map41.put("name", "设备端口数");
			
			Map<String, Object> map42 = new HashMap<String, Object>();
			map42.put("name", flist.get(0).getFacilitiesNumberUsePort());
			if(isEdit){
				map42.put("class", "btn btn-link edit");
				map42.put("onclick", "editable(this);");
				map42.put("value", "facilitiesNumberUsePort");
			}
			
			list4.add(map41);
			list4.add(map42);
			
			List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map51 = new HashMap<String, Object>();
			map51.put("name", "设备原有端口数");
			
			Map<String, Object> map52 = new HashMap<String, Object>();
			map52.put("name", flist.get(0).getFacilitiesNumberPort());
			if(isEdit){
				map52.put("class", "btn btn-link edit");
				map52.put("onclick", "editable(this);");
				map52.put("value", "facilitiesNumberPort");
			}
			
			list5.add(map51);
			list5.add(map52);
			
			List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map61 = new HashMap<String, Object>();
			map61.put("name", "厂家信息");
			
			Map<String, Object> map62 = new HashMap<String, Object>();
			map62.put("name", flist.get(0).getFacilitiesFactory());
			if(isEdit){
				map62.put("class", "btn btn-link edit");
				map62.put("onclick", "editable(this);");
				map62.put("value", "facilitiesFactory");
			}
			
			list6.add(map61);
			list6.add(map62);
			
			List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map71 = new HashMap<String, Object>();
			map71.put("name", "设备ip");
			
			Map<String, Object> map72 = new HashMap<String, Object>();
			map72.put("name", flist.get(0).getFacilitiesIp());
			if(isEdit){
				map72.put("class", "btn btn-link edit");
				map72.put("onclick", "editable(this);");
				map72.put("value", "facilitiesIp");
			}
			
			list7.add(map71);
			list7.add(map72);
			

			list.add(list1);
			list.add(list2);
			list.add(list3);
			list.add(list4);
			list.add(list5);
			list.add(list6);
			list.add(list7);
		}
		
		return list;
	}
	
	/**
	 * 老师查看设备信息
	 * 
	 * @param name the name of experiment want to query
	 * 
	 * @return a list contains experiment name, standard time, profile and instruction 
 	 *  in the format of map
 	 *                                           //待定
	 */


	/**
	 * 生成编辑设备界面的数据
	 * @param name the name of experiment want to query
	 * @return the data show on the page
	 */
	public Map<String, Object> Edit(String name){

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Integer pri_key=Integer.parseInt(name);
		
		String sql="select * from facilities where facilities_id="+name;
		
		List<Facilities> facilitieslist = new ArrayList<Facilities>();
		facilitieslist=this.fDAO.getListBySql(sql);
		String physicsMachinesName = facilitieslist.get(0).getFacilitiesBelongPhysicsMachines();
		Integer facilitiesOrder = facilitieslist.get(0).getFacilitiesOrder();
	
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "物理机柜"+physicsMachinesName+"<i class='icon-double-angle-right'></i>"+"设备" + facilitiesOrder);
		
		list = FacilitiesManagement(pri_key, true);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}
	
	public Map<String, Object> Add(){

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "新增设备<i class='icon-double-angle-right'></i>");
		
		List<Map<String, Object>> fbelongphysicsmachines = this.vutil.createList("设备所属物理机柜", "", "", "", "btn btn-link edit", "editable(this);", "facilitiesBelongPhysicsMachines",0);
		List<Map<String, Object>> forder = this.vutil.createList("设备序号", "", "", "", "btn btn-link edit", "editable(this);", "facilitiesOrder",0);
		List<Map<String, Object>> ftype = this.vutil.createList("设备类型", "", "", "", "btn btn-link edit", "editable(this);", "facilitiesType",0);
		List<Map<String, Object>> fnumberusport = this.vutil.createList("设备端口数", "", "", "", "btn btn-link edit", "editable(this);", "facilitiesNumberUsePort",0);
		List<Map<String, Object>> fnumberport = this.vutil.createList("设备原有端口数", "", "", "", "btn btn-link edit", "editable(this);", "facilitiesNumberPort",0);
		List<Map<String, Object>> ffactory = this.vutil.createList("厂家信息"    , "", "", "", "btn btn-link edit", "editable(this);", "facilitiesFactory",0);
		List<Map<String, Object>> fip = this.vutil.createList("设备ip"    , "", "", "", "btn btn-link edit", "editable(this);", "facilitiesIp",0);
		
		list.add(fbelongphysicsmachines);
		list.add(forder);
		list.add(ftype);
		list.add(fnumberusport);
		list.add(fnumberport);
		list.add(ffactory);
		list.add(fip);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "保存信息");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);	
		return map;
	}
	
	public Map<String, Object> save(String name, Map<String, Object> map){
		Map<String, Object> r = new HashMap<String, Object>();
		
		Facilities facilities;

		if(name.equals("")){
			facilities = new Facilities();
		}
		else{
			Integer pri_key=Integer.parseInt(name);
			facilities = (Facilities) this.fDAO.getUniqueByProperty("facilitiesId", pri_key);
		}
		
		Set<String> key = map.keySet();
		Iterator<String> keylist = key.iterator();
		while(keylist.hasNext()){
			String k = keylist.next();
			switch(k){
			case "facilitiesBelongPhysicsMachines" : facilities.setFacilitiesBelongPhysicsMachines((String) map.get(k)); break;
			case "facilitiesOrder" : facilities.setFacilitiesOrder(Integer.parseInt((String) map.get(k))); break;
			case "facilitiesType" : facilities.setFacilitiesType(Integer.parseInt((String) map.get(k))); break;
			case "facilitiesNumberUsePort" : facilities.setFacilitiesNumberUsePort(Integer.parseInt((String) map.get(k))); break;
			case "facilitiesNumberPort" : facilities.setFacilitiesNumberPort(Integer.parseInt((String) map.get(k))); break;
			case "facilitiesFactory" : facilities.setFacilitiesFactory((String) map.get(k)); break;
			case "facilitiesIp" : facilities.setFacilitiesIp((String) map.get(k)); break;
			}
		}


		if(name.equals("")){
			facilities.setStatus(1);
			if(this.fDAO.add(facilities)){
				r.put("isSuccess", true);
				r.put("id", map.get("facilitiesId"));
				r.put("key", "facilities");
			}
			else{
				r.put("isSuccess", false);
			}
		}
		else{
			if(this.fDAO.update(facilities)){
				r.put("isSuccess", true);
				r.put("id", map.get("facilitiesId"));
				r.put("key", "facilities");
			}
			else{
				r.put("isSuccess", false);
			}
		}
		
		return r;
	}

}