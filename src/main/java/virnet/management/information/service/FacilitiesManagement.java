package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.FacilitiesDAO;
import virnet.management.entity.Facilities;

public class FacilitiesManagement implements FacilitiesInformationQuery{
	private FacilitiesDAO facilitiesDAO = new FacilitiesDAO();

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
	public Map<String, Object> Facilitiesquery(String user, int page, String select, String physicsMachinesName) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "设备序号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "设备类型");
		head_name.put("class", "");
		head.add(head_name);		
		
		list.add(head);

		String sql="select * from facilities where facilities_belong_physics_machines= '"+physicsMachinesName + "'";
		
		List<Facilities> facilitieslist = new ArrayList<Facilities>();
		facilitieslist=this.facilitiesDAO.getListBySql(sql);
		int size = facilitieslist.size();
		System.out.println("facilities list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> facilitiesInfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_order = new HashMap<String, String>();
			map_order.put("name", facilitieslist.get(i).getFacilitiesOrder() + "");
			map_order.put("class", "");
			facilitiesInfo.add(map_order);
			
			Map<String, String> map_name = new HashMap<String, String>();
			Integer type=facilitieslist.get(i).getFacilitiesType();
			System.out.println(type);
			switch(type){
				case 1: map_name.put("name", "路由器");break;
				case 2: map_name.put("name", "二层交换机");break;
				case 3: map_name.put("name", "三层交换机");break;
				case 4: map_name.put("name", "PC");break;
				case 5: map_name.put("name", "拓扑连接器");break;
				default : map_name.put("name", "未知设备");break;
			}
			map_name.put("class", "btn btn-link");
			map_name.put("onclick", "showDetail('" + facilitieslist.get(i).getFacilitiesId() + "', 'facilities');");//facilities detail
			facilitiesInfo.add(map_name);
			
			list.add(facilitiesInfo);
		}
		
		int pageNO=1;
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增设备");
		button.put("class", "btn button-new");
		button.put("click", "addContent('facilities-management');");
		
		map.put("data", list);
		map.put("page", pageNO);
		map.put("button_new", button);
		
		return map;
	}

}
