package virnet.management.groupArrange.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import virnet.management.dao.ClassDAO;
import virnet.management.entity.Class;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.ClassgroupDAO;
import virnet.management.dao.GroupmemberDAO;
import virnet.management.dao.UserDAO;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Groupmember;
import virnet.management.util.ViewUtil;


public class groupArrangeAction extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 8688894248196843633L;
	
	private HttpServletRequest request;
	private Map<String, Object> result = new HashMap<String, Object>();
	private ViewUtil vutil = new ViewUtil();
	
	//生成选择学生分组界面
	public String groupArrange(){
		
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		Integer ClassId = Integer.parseInt((String)this.request.getParameter("ClassId"));
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "分组安排 <i class='icon-double-angle-right'></i>(仅显示未分组学生) ");
		
		//根据操作人的id 筛选出属于这个老师的课程 的下拉框
		
		String hql = "SELECT model from " + ClassTeacher.class.getName() + " as model "
				+ "where model.classTeacherTeacherId ='" + user + "'";
		List<ClassTeacher> ctlist = new ArrayList<ClassTeacher>();
		ClassTeacherDAO ctDAO = new ClassTeacherDAO();
		ctlist = ctDAO.getListByHql(hql);
		
		int size = ctlist.size();
		System.out.println("Class list size : " + size);
		
		//结构：  班级名
		List<Map<String, Object>> cclist = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < size; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			
			int classId = ctlist.get(i).getClassTeacherClassId();
			
			ClassDAO classDAO= new ClassDAO();
			Class queryResult = (Class) classDAO.getUniqueByProperty("classId", classId);
			
			cmap.put("name",  queryResult.getClassName());
			cmap.put("value", classId);
			
			cclist.add(cmap);
		}
		
		List<Map<String, Object>> cName = this.vutil.createList("", "", "", cclist, "singleselect", "", "inputClassName",0);
		
		
		//学生信息列表
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> stuId = new HashMap<String, Object>();
		stuId.put("name", "学号");
		stuId.put("class", "");
		head.add(stuId);
		
		Map<String, Object> stuName= new HashMap<String, Object>();
		stuName.put("name", "姓名");
		stuName.put("class", "");
		head.add(stuName);
		
		Map<String, Object> choose= new HashMap<String, Object>();
		choose.put("name", "选择");
		choose.put("class", "");
		head.add(choose);
		
		list.add(head);
		
		if(ClassId == 0)
			ClassId = ctlist.get(0).getClassTeacherClassId(); 
		
		//查询未分组的学生
		hql = "select t4.userId , t4.userNickname "
			+ " from StuClass as t1, User as t4 "
			+ " where t1.stuClassClassId = " + ClassId
			+ " and t1.stuClassUserId = t4.userId "
			+ " and t1.stuClassUserId not in ( "
			+ " select t3.classgroupmemberUserId"
			+ " from Classgroup as t2, Groupmember as t3 "
			+ " where t2.classgroupClassId = " + ClassId
			+ " and t2.classgroupId = t3.classgroupmemberGroupId)"; 
		
		UserDAO uDAO= new UserDAO();
		List<Object> stuInfo = uDAO.getListByHql(hql);
		for(Object stu:stuInfo){
			
			List<Map<String, Object>> groupArrangeInfo = new ArrayList<Map<String, Object>>();
			
			Object[] stuInfomation = (Object[])stu;
			
			Map<String, Object> map_stuId = new HashMap<String, Object>();
			map_stuId.put("name", stuInfomation[0]);
			map_stuId.put("class", "stuId");
			groupArrangeInfo.add(map_stuId);
			
			Map<String, Object> map_stuName = new HashMap<String, Object>();
			map_stuName.put("name", stuInfomation[1]);
			map_stuName.put("class", "");
			groupArrangeInfo.add(map_stuName);
			
			Map<String, Object> map_choose = new HashMap<String, Object>();
			map_choose.put("name", "未选");
			map_choose.put("class", "btn btn-fade");
			map_choose.put("onclick", "choose(this);");
			map_choose.put("value", "0");
			groupArrangeInfo.add(map_choose);
			
			list.add(groupArrangeInfo);
		}
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "确认");
		button.put("class", "btn button-new");
		button.put("click", "submitGroupArrange()");

		r.put("tittle", tittle);
		r.put("select", cName);
		r.put("data", list);
		r.put("button_new", button);
		
		this.setResult(r);
		return SUCCESS;
	}
	
	//分组信息提交确认界面
	public String submitGroupArrange(){
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		String  stuIdList = this.request.getParameter("stuId");
		System.out.println(this.request.getParameter("classId"));
		Integer ClassId = Integer.parseInt((String)this.request.getParameter("classId"));
		
		System.out.println(stuIdList);
		String[] studentId = stuIdList.split("##");
		
		//新增班级小组  class_group
		Classgroup classGroup = new Classgroup();
		classGroup.setClassgroupClassId(ClassId);
		classGroup.setClassgroupName(studentId[0]+"的小组");
		classGroup.setClassgroupTotal(3);//20180901蒋家盛修改预约组队人数为3人
		classGroup.setClassgroupType("教师指定");
		ClassgroupDAO cgDAO = new ClassgroupDAO();
		if(!cgDAO.add(classGroup)){
			r.put("isSuccess", false);
			r.put("data", "修改小组表失败");
			this.setResult(r);
			return SUCCESS;
		}
		//Integer groupId = classGroup.getClassgroupId();
		Integer groupId = ((Classgroup)cgDAO.getUniqueByProperty("classgroupName",studentId[0]+"的小组")).getClassgroupId();
		
		//修改小组成员表  group_membe
		GroupmemberDAO gDAO = new GroupmemberDAO();
		for(int i=0;i<studentId.length;i++){
			Groupmember groupmember = new Groupmember();
			groupmember.setClassgroupmemberGroupId(groupId);
			groupmember.setClassgroupmemberUserId(Integer.parseInt((String)studentId[i]));
			if(i==0)
				groupmember.setClassgroupmemberLeaderFlag(1);
			else
				groupmember.setClassgroupmemberLeaderFlag(0);
			if(!gDAO.add(groupmember)){
				r.put("isSuccess", false);
				r.put("data", "修改小组成员表失败");
				this.setResult(r);
				return SUCCESS;
			}
		}
		r.put("isSuccess", true);
		r.put("data", "分组成功");
		this.setResult(r);
		return SUCCESS;
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
}
