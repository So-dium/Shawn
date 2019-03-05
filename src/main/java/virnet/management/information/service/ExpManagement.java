package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ExpDAO;
import virnet.management.entity.Exp;
import virnet.management.util.PageUtil;

public class ExpManagement implements InformationQuery{
	private ExpDAO expDAO = new ExpDAO();

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
		head_id.put("name", "实验编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "实验名称");
		head_name.put("class", "");
		head.add(head_name);		
		
		list.add(head);
		
		PageUtil<Exp> pageUtil = new PageUtil<Exp>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		List<Exp> explist = new ArrayList<Exp>();
		int recordSize = expDAO.getList().size();
		System.out.println("记录数："+recordSize);
		if (recordSize > 0) {
			this.expDAO.getListByPage(pageUtil);
			explist = pageUtil.getList();
		}
		
		int size = explist.size();
		System.out.println("exp list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> expInfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", explist.get(i).getExpId() + "");
			map_id.put("class", "");
			expInfo.add(map_id);
			
			Map<String, String> map_name = new HashMap<String, String>();
			map_name.put("name", explist.get(i).getExpName());
			map_name.put("class", "btn btn-link");
			map_name.put("onclick", "showDetail('" + explist.get(i).getExpName() + "', 'exp');");//experiment detail
			expInfo.add(map_name);
			
			Map<String, String> map_delete = new HashMap<String, String>();
			map_delete.put("name", "删除");
			map_delete.put("class", "btn btn-new hide deleteButton");
			map_delete.put("onclick", "deleteExp( '"+ explist.get(i).getExpName() + "');");
			expInfo.add(map_delete);
			
			System.out.println("index : " + i + ", exp id : " + explist.get(i).getExpId() + ", exp name : " + explist.get(i).getExpName());
			
			list.add(expInfo);
		}
		
		int total = this.expDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新建实验");
		button.put("class", "btn button-new");
		button.put("click", "addContent('exp-management');");
		
		Map<Object, Object> button_delete = new HashMap<Object, Object>();
		button_delete.put("content", "- 删除实验");
		button_delete.put("class", "btn button-new");
		button_delete.put("click", "showDeleteExpButton();");
		button_delete.put("id", "showDeleteExpButton");
		
		map.put("data", list);
		map.put("page", pageNO);
		
		map.put("button_delete", button_delete);
		map.put("button_new", button);
		
		return map;
	}

}
