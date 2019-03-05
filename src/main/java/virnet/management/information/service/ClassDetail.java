package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.ClassInfoCDAO;
import virnet.management.dao.StuClassDAO;
import virnet.management.entity.Class;
import virnet.management.entity.StuClass;
import virnet.management.util.UserInfoProcessUtil;

public class ClassDetail implements InformationQuery {

	private ClassInfoCDAO cDAO = new ClassInfoCDAO();

	private UserInfoProcessUtil usercheck = new UserInfoProcessUtil();	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			int userid = this.usercheck.checkUsername(user);
			
			List<Class> clist = this.cDAO.getMyClass(userid);
			
			int size = clist.size();
			List<Object> selectlist = new ArrayList<Object>();
			for(int i = 0; i < size; i++){
				Map<String, Object> cmap = new HashMap<String, Object>();
				
				int classid = clist.get(i).getClassId();
				cmap.put("id", classid);
				
				cmap.put("class", this.cDAO.getClassName(classid));
				selectlist.add(cmap);
			}
			
			//get the request class id
			int s = selectlist.size();
			int classid;
			if(s == 0){
				//list is null
				classid = -1;
			}
			else{
				classid = (int) ((Map<String, Object>) selectlist.get(0)).get("id");
			}
			
			for(int i = 0; i < s; i++){
				if(select.equals(((Map<String, Object>) selectlist.get(i)).get("class"))){
					classid = (int) ((Map<String, Object>) selectlist.get(i)).get("id");
				}
			}

			//detail
			List<List<Map<String, Object>>> detaillist = this.cDAO.ClassOfTeacher(this.cDAO.getClassName(classid));
			
			map.put("detail", detaillist);
			map.put("select", selectlist);
			map.put("page", 1);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public boolean isThisClassStu(Integer classId,Integer StudentId){
		
		StuClassDAO scDAO = new StuClassDAO();
		String[] para = {"stuClassUserId",StudentId+"","stuClassClassId",classId+""};
		StuClass result = (StuClass) scDAO.getByNProperty(para);
		if(result != null)
			return true;
		else
			return false;
	}

}
