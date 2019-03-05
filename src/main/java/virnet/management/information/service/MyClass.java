package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.ClassInfoCDAO;
import virnet.management.combinedao.StudentInfoCDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.StuClassDAO;
import virnet.management.entity.Class;
import virnet.management.entity.StuClass;
import virnet.management.util.UserInfoProcessUtil;

public class MyClass implements InformationQuery {

	@SuppressWarnings("unused")
	private StudentInfoCDAO sDAO = new StudentInfoCDAO();
	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	private UserInfoProcessUtil usercheck = new UserInfoProcessUtil();
	private StuClassDAO scDAO = new StuClassDAO();
	private ClassDAO classDAO = new ClassDAO();

	@SuppressWarnings("unchecked")
	@Override
	/*
	 * @Documented( get the student user id, then find out the classes he/she
	 * belong to)
	 * 
	 * @see
	 * virnet.management.information.service.InformationQuery#query(java.lang.
	 * String, int, java.lang.String)
	 */
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub

		Map<String, Object> map = new HashMap<String, Object>();

		int stuid = this.usercheck.checkUsername(user);

		// 返回该学生所在的班级列表
		@SuppressWarnings("unchecked")
		List<StuClass> sclist = this.scDAO.getListByProperty("stuClassUserId", stuid);

		List<Class> clist = new ArrayList<Class>();
		int scSize = sclist.size();
		for (int i = 0; i < scSize; i++) {
			clist.add(this.getClass(sclist.get(i).getStuClassClassId()));
		}

		int size = clist.size();
		List<Object> selectlist = new ArrayList<Object>();
		for (int i = 0; i < size; i++) {
			Map<String, Object> cmap = new HashMap<String, Object>();

			int classid = clist.get(i).getClassId();
			cmap.put("id", classid);

			cmap.put("class", this.cDAO.getClassName(classid));
			selectlist.add(cmap);
		}

		// get the request class id
		int s = selectlist.size();
		int classid;
		if (s == 0) {
			// list is null
			classid = -1;
		} else {
			classid = (int) ((Map<String, Object>) selectlist.get(0)).get("id");
		}

		for (int i = 0; i < s; i++) {
			if (select.equals(((Map<String, Object>) selectlist.get(i)).get("class"))) {
				classid = (int) ((Map<String, Object>) selectlist.get(i)).get("id");
			}
		}

		// detail
		//老师看到的班级详情内容和学生看到的我的班级内容是一样的，因此这里复用了classOfTeacher方法
		List<List<Map<String, Object>>> detaillist = this.cDAO.ClassOfTeacher(this.cDAO.getClassName(classid));

		map.put("detail", detaillist);
		map.put("select", selectlist);
		map.put("page", 1);

		return map;
	}

	/**
	 * 根据班级编号得到班级实体类
	 * 
	 * @param classid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class getClass(int classid) {
		List<Class> clist = this.classDAO.getListByProperty("classId", classid);

		if (clist.isEmpty() || clist.size() > 1) {
			return null;
		} else {
			return clist.get(0);
		}
	}

}
