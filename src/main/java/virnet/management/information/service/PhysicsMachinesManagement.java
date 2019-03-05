package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.PhysicsMachinesDAO;
import virnet.management.entity.Class;
import virnet.management.entity.PhysicsMachines;
import virnet.management.util.PageUtil;

public class PhysicsMachinesManagement implements InformationQuery{
	private PhysicsMachinesDAO physicsMachinesDAO = new PhysicsMachinesDAO();

	/*
	 * @param
	 * page --- required page in database
	 * @return
	 * map : "data" the query list
	 *       "page" total pages
	 * @see virnet.management.information.service.InformationQuery#query(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "序号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "物理机柜编号");
		head_name.put("class", "");
		head.add(head_name);
		
		Map<String, String> head_jump = new HashMap<String, String>();
		head_jump.put("name", "管理设备");
		head_jump.put("class", "");
		head.add(head_jump);
		
		list.add(head);
		
		PageUtil<PhysicsMachines> pageUtil = new PageUtil<PhysicsMachines>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		List<PhysicsMachines> physicsmachineslist = new ArrayList<PhysicsMachines>();
		int recordSize = physicsMachinesDAO.getList().size();
		System.out.println("记录数："+recordSize);
		if (recordSize > 0) {
			this.physicsMachinesDAO.getListByPage(pageUtil);
			physicsmachineslist = pageUtil.getList();
		}
		int size = physicsmachineslist.size();
		System.out.println("physicsMachinesD list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> physicsmachinesinfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", physicsmachineslist.get(i).getPhysicsMachinesId() + "");
			map_id.put("class", "");
			physicsmachinesinfo.add(map_id);
			
			Map<String, String> map_name = new HashMap<String, String>();
			map_name.put("name", physicsmachineslist.get(i).getPhysicsMachinesName()+ "");
			map_name.put("class", "btn btn-link");
			map_name.put("onclick", "showDetail('" + physicsmachineslist.get(i).getPhysicsMachinesName() + "', 'physicsMachines');");//physicsMachines detail
			physicsmachinesinfo.add(map_name);
			
			Map<String, String> map_jump = new HashMap<String, String>();
			map_jump.put("name", "<i class='icon-arrow-right'></i>");
			map_jump.put("class", "btn btn-new");
			map_jump.put("onclick", "fetchFacilitiesData('facilities-management','" + user + "','0','','" + physicsmachineslist.get(i).getPhysicsMachinesName() + "',false" + ");");
			physicsmachinesinfo.add(map_jump);
			
			System.out.println("index : " + i + ", exp id : " + physicsmachineslist.get(i).getPhysicsMachinesId() + ", exp type : " + physicsmachineslist.get(i).getPhysicsMachinesName());
			
			list.add(physicsmachinesinfo);
		}
		
		int total = this.physicsMachinesDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增机柜");
		button.put("class", "btn button-new");
		button.put("click", "addContent('physicsMachines-management');");
		
		map.put("data", list);
		map.put("page", pageNO);
		map.put("button_new", button);
		
		return map;
	}

}
