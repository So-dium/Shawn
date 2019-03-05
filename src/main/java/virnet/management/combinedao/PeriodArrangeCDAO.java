package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.entity.Periodarrange;
import virnet.management.util.DateUtil;

public class PeriodArrangeCDAO {
	private PeriodarrangeDAO paDAO = new PeriodarrangeDAO();
	
	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	private DateUtil dateutil = new DateUtil();
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDefaultWeekInfo(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		//get the id of each class who is in period table
		List<Periodarrange> plist = this.paDAO.getList();
		
		int j = 0;
		for(Periodarrange p : plist){
			System.out.println("*********count : " + j + ", list size : " + list.size());
			int classid = p.getPeriodarrangeClassId();
			
			boolean judge = false;
			
			int size = list.size();
			for(int i = 0; i < size; i++){
				Map<String, Object> m = list.get(i);
				if((Integer)m.get("classid") == classid){
					//add to list
					List<Object> timelist = (List<Object>) m.get("timelist");
					timelist.add(this.processDate(p));
					
					judge = true;
				}
				else{
					//create a new class map
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("classid", classid);
					map.put("class", this.cDAO.getClassName(classid));
					
					System.out.println("\n*********************************************");
					System.out.println("class name : " + this.cDAO.getClassName(classid));
					System.out.println();
					
					List<Object> timelist = new ArrayList<Object>();
					
					timelist.add(processDate(p));
					
					map.put("timelist",  timelist);
					
					list.add(map);
					judge = true;
				}
			}
			
			if(!judge){
				//create a new class map
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("classid", classid);
				map.put("class", this.cDAO.getClassName(classid));
				
				System.out.println("*********************************************");
				System.out.println("class name : " + this.cDAO.getClassName(classid));
				
				List<Object> timelist = new ArrayList<Object>();
				
				timelist.add(processDate(p));
				
				map.put("timelist",  timelist);
				
				list.add(map);
				judge = true;
			}
		}
		
		return list;
	}
	
	private Map<String, Object> processDate(Periodarrange p){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, Object> ms = this.dateutil.CalDate(this.dateutil.dateToString(p.getPeriodarrangeStartDate()));
		Map<String, Object> me = this.dateutil.CalDate(this.dateutil.dateToString(p.getPeriodarrangeEndDate()));
		
		//suppose that start and end are in the same day
		//no judge
		
		List<String> timelist = this.dateutil.processDateToCTable((int)ms.get("weekday"), p.getPeriodarrangeStartTime(), (int)me.get("weekday"), p.getPeriodarrangeEndTime());
		
		map.put("weekNO", ms.get("weekNO"));
		map.put("timelist", timelist);
		
		
		System.out.println("*********************************************");
		System.out.println("weekNO : " + ms.get("weekNO"));
		int l = timelist.size();
		
		System.out.print("timelist : ");
		for(int i = 0; i < l; i++){
			System.out.print(timelist.get(i) + " ");
		}
		System.out.println(";");
		
		return map;
	}
}
