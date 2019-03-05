package virnet.management.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUtil {

	public List<Map<String, Object>> createList(String tittleName, String tittleClass, String tittleClick, 
			Object content, String contentClass, String contentClick, String contentValue, Integer initValue){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("name", tittleName);
		tittle.put("class", tittleClass);
		tittle.put("onclick", tittleClick);
		
		Map<String, Object> ctnt = new HashMap<String, Object>();
		ctnt.put("name", content);
		ctnt.put("class", contentClass);
		ctnt.put("onclick", contentClick);
		ctnt.put("value", contentValue);
		ctnt.put("initValue", initValue);
		
		list.add(tittle);
		list.add(ctnt);
		
		return list;
	}
	
}
