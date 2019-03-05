package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.CabinetTempletDAO;
import virnet.management.entity.CabinetTemplet;

public class CabinetTempletInfoCDAO {
	
	private CabinetTempletDAO ctdDAO = new CabinetTempletDAO();
	
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> showTaskDetail(List<List<Map<String, Object>>> list,String expName,boolean isEdit){
		
		//模板名与实验名相同
		List<CabinetTemplet> ctlist = ctdDAO.getListByProperty("cabinetTempletName", expName);
	
		List<Map<String, Object>> Limitlist = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map11 = new HashMap<String, Object>();
		map11.put("name", "约束条件");
		
		Map<String, Object> map12 = new HashMap<String, Object>();
		map12.put("name", ctlist.get(0).getCabinetTempletLimit());
		if(isEdit){
			map12.put("class", "btn btn-link a edit");
			map12.put("onclick", "editable(this);");
			map12.put("value", "Limit");
		}
			
		Map<String, Object> map13 = new HashMap<String, Object>();
		map13.put("name", "");

		Limitlist.add(map11);
		Limitlist.add(map12);
		Limitlist.add(map13);
			
		List<Map<String, Object>> Remarklist = new ArrayList<Map<String, Object>>();
				
		Map<String, Object> map21 = new HashMap<String, Object>();
		map21.put("name", "备注");
				
		Map<String, Object> map22 = new HashMap<String, Object>();
		map22.put("name",ctlist.get(0).getCabinetTempletRemark() );
		if(isEdit){
			map22.put("class", "btn btn-link a edit");
			map22.put("onclick", "editable(this);");
			map22.put("value", "Remark");
		}
		
					
		Map<String, Object> map23 = new HashMap<String, Object>();
		map23.put("name", "");

		Remarklist.add(map21);
		Remarklist.add(map22);
		Remarklist.add(map23);
		
		list.add(Limitlist);
		list.add(Remarklist);	

		return list;
	}
}
