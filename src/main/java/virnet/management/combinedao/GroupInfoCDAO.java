package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.GroupmemberDAO;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Groupmember;

public class GroupInfoCDAO {
	private ClassgroupDAO gDAO = new ClassgroupDAO();
	private GroupmemberDAO mDAO = new GroupmemberDAO();
	private UserInfoCDAO uDAO = new UserInfoCDAO();
	
	@SuppressWarnings("unchecked")
	public List<Classgroup> getGroup(int classid){
		List<Classgroup> glist = this.gDAO.getListByProperty("classgroupClassId", classid);
		
		return glist;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isUserInGroup(int userid, int groupid){		
		List<Groupmember> mlist = this.mDAO.getListByNProperty("classgroupmemberUserId", userid + "", "classgroupmemberGroupId", groupid + "");
		
		if(mlist.isEmpty() || mlist.size() > 1){
			return false;
		}
		else{
			return true;
		}
	}
	
	public Classgroup getGroupOfStuInClass(int userid, int classid){
		List<Classgroup> glist = this.getGroup(classid);
		
		List<Classgroup> gl = new ArrayList<Classgroup>();
		int size = glist.size();
		for(int i = 0; i < size; i++){
			if(this.isUserInGroup(userid, glist.get(i).getClassgroupId())){
				gl.add(glist.get(i));
			}
		}
		
		if(gl.isEmpty() || gl.size() > 1){
			//database error : one user in no class/two or more classes
			return null;
		}
		else{
			return gl.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getGroupMember(int groupid){
		List<Groupmember> mlist = this.mDAO.getListByProperty("classgroupmemberGroupId", groupid);
		List<Object> map = new ArrayList<Object>();
		
		int size = mlist.size();
		for(int i = 0; i < size; i++){
			String username = this.uDAO.getUserName(mlist.get(i).getClassgroupmemberUserId());
			System.out.println(username);
			if(username != null){
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("name", username);
				m.put("class", "btn btn-link");
				m.put("onclick", "showDetail('" + username + "', 'user');");
				map.add(m);
			}
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<Classgroup> getClassGroup(int classid){
		List<Classgroup> glist = new ArrayList<Classgroup>();
		
		glist = this.gDAO.getListByProperty("classgroupClassId", classid);
		
		return glist;
	}
	
	public Map<String, Object> getGroupInfo(int classid, int groupid){
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		
		return map;
	}
}
