package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.List;

import virnet.management.dao.StuClassDAO;
import virnet.management.entity.Class;
import virnet.management.entity.StuClass;

public class StudentInfoCDAO {
	private StuClassDAO scDAO= new StuClassDAO();
	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	
	@SuppressWarnings("unchecked")
	public List<Class> getMyClass(int userid){
		List<Class> clist = new ArrayList<Class>();
		
		List<StuClass> sclist = this.scDAO.getListByProperty("stuClassUserId", userid);
		
		int s = sclist.size();
		for(int i = 0; i < s; i++){
			Class c = this.cDAO.getClass(sclist.get(i).getStuClassClassId());
			
			if(c != null){
				clist.add(c);
			}
		}
		
		return clist;
	}
}
