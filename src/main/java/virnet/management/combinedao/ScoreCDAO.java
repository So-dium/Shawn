package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.experiment.dao.ResultTaskDAO;
import virnet.experiment.entity.ResultTask;
import virnet.management.dao.CaseDAO;
import virnet.management.dao.CaseMemberDAO;
import virnet.management.dao.ClassDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.CourseexpDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.dao.UserDAO;
import virnet.management.entity.CaseMember;
import virnet.management.entity.Class;
import virnet.management.entity.Course;
import virnet.management.entity.Courseexp;
import virnet.management.entity.Exp;
import virnet.management.entity.Case;
import virnet.management.util.DateUtil;
import virnet.management.util.ViewUtil;

public class ScoreCDAO {
	private static String EditTittle = "实验信息";
	private ClassInfoCDAO cDAO = new ClassInfoCDAO();
	private CourseInfoCDAO coDAO = new CourseInfoCDAO();
	private PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
	private CaseDAO caseDAO = new CaseDAO();
	private CaseMemberDAO casememberDAO = new CaseMemberDAO();
	private UserDAO userDAO = new UserDAO();
	private CourseDAO courseDAO = new CourseDAO();
	@SuppressWarnings("unused")
	private ClassDAO classDAO = new ClassDAO();
	@SuppressWarnings("unused")
	private ExpDAO expDAO = new ExpDAO();
	private ResultTaskDAO resulttaskDAO = new ResultTaskDAO();
	private ViewUtil vutil = new ViewUtil();
	private DateUtil dateutil = new DateUtil();
		
	public Map<String, Object> showScoreDetail(String id, String ArrangeId){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String,List<Map<String, Object>>> Selectmap = new HashMap<String,List<Map<String, Object>>>();
		Map<String, Object> tittle = new HashMap<String, Object>();
		//String[] expinfo=expname.split(" ");
		//tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> "+expinfo[0]+" <i class='icon-double-angle-right'></i> "+expinfo[1]+" <i class='icon-double-angle-right'></i> "+expinfo[2]);
		/*List<Course> clist = this.courseDAO.getList();
		List<Map<String, Object>> courselist = new ArrayList<Map<String, Object>>();
		Map<String, Object> all_course_map = new HashMap<String, Object>();
		all_course_map.put("name", "全部");
		all_course_map.put("value", -1);
		
		courselist.add(all_course_map);
		int cs = clist.size();
		for(int i = 0; i < cs; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			cmap.put("name", clist.get(i).getCourseName());
			cmap.put("value", clist.get(i).getCourseId());
			
			courselist.add(cmap);
		}
		
		List<Map<String, Object>> belongs_course = this.vutil.createList("所属课程", "", "", courselist, "mutiselect", "", "BelongsToCourseID");

		List<Class> clalist = this.classDAO.getList();
		List<Map<String, Object>> classlist = new ArrayList<Map<String, Object>>();
		Map<String, Object> all_class_map = new HashMap<String, Object>();
		all_class_map.put("name", "全部");
		all_class_map.put("value", -1);
		
		classlist.add(all_class_map);
		int clas = clalist.size();
		for(int i = 0; i < clas; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			cmap.put("name", clalist.get(i).getClassName());
			cmap.put("value", clalist.get(i).getClassId());
			
			classlist.add(cmap);
		}
		
		List<Map<String, Object>> belongs_class = this.vutil.createList("所属班级", "", "", classlist, "mutiselect", "", "BelongsToClassID");
*/		
		String titlehql = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
						+ " from Periodarrange as t2 , Classarrange as t3 "
						+ "where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
						+ " and t3.classarrangeExpArrangeId = " + ArrangeId;
		List<Object> periodList = pDAO.getListByHql(titlehql);
		
		Map<String,String> title = new HashMap<String,String>();
		title.put("data",dateutil.TimeFormat(periodList));

		List<Object> enamelist = this.expDAO.getList();
		List<Object> eidlist = this.expDAO.getList();
		try {
			String hql1 = "select distinct t3.expName "
					+ "from Case as t1, ResultTask as t2, Exp as t3 "
					+ "where t1.caseId = t2.resultCaseId and "
					+ "t2.resultExpId = t3.expId and "
					+ "t1.caseExpArrangeId = " + ArrangeId;
			enamelist = caseDAO.getListByHql(hql1);
			String hql2 = "select distinct t3.expId "
					+ "from Case as t1, ResultTask as t2, Exp as t3 "
					+ "where t1.caseId = t2.resultCaseId and "
					+ "t2.resultExpId = t3.expId and "
					+ "t1.caseExpArrangeId = " + ArrangeId;
			eidlist = caseDAO.getListByHql(hql2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Map<String, Object>> explist = new ArrayList<Map<String, Object>>();
		Map<String, Object> all_exp_map = new HashMap<String, Object>();
		all_exp_map.put("name", "全部");
		all_exp_map.put("value", -1);
		
		explist.add(all_exp_map);
		int es = enamelist.size();
		for(int i = 0; i < es; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			cmap.put("name", enamelist.get(i));
			cmap.put("value",eidlist.get(i));
			
			explist.add(cmap);
		}
		
		List<Map<String, Object>> belongs_exp = this.vutil.createList("所属实验", "", "", explist, "mutiselect", "", "BelongsToExpID",0);
		
		
		List<String> CaseIdList = null;
		try {
			String hql = "select t1.caseId from Case as t1 where t1.caseExpArrangeId = " + ArrangeId;
			CaseIdList = new ArrayList<String>();
			CaseIdList = caseDAO.getListByHql(hql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int casesize = CaseIdList.size();
		
		List<ResultTask> resultlist = this.resulttaskDAO.getList();
		int resultsize = resultlist.size();
		List<Map<String, Object>> resulttasklist = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < casesize; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			
			
			//小组成员
			String hql = "select t2.userNickname "
					   + "from CaseMember as t1, User as t2 "
					   + "where t1.caseMemberCaseId = " + CaseIdList.get(i)
					   + " and t1.caseMemberStuId = t2.userId ";
			List<String>groupMemberList =caseDAO.getListByHql(hql);
			List<Map<String, Object>> memberlist = new ArrayList<Map<String, Object>>();
			for(int j = 0; j < groupMemberList.size(); j++){
				
			    Map<String, Object> gmap = new HashMap<String, Object>();
				gmap.put("name", groupMemberList.get(j));
				memberlist.add(gmap);

			}
			cmap.put("groupmember",memberlist);
			
			
			//实验实例id
			cmap.put("caseid", CaseIdList.get(i));
			
			
			//实验名称
			String hql3 = "select distinct t3.expName "
					+ "from ResultTask as t2, Exp as t3 "
					+ "where t2.resultExpId = t3.expId and "
					+ "t2.resultCaseId = " + CaseIdList.get(i);
			String expname = (String) resulttaskDAO.getListByHql(hql3).get(0);
			cmap.put("expname", expname);
			
//			//任务
//			List<Map<String, Object>> tasklist = new ArrayList<Map<String, Object>>();
//			for(int j = 0; j < resultsize; j++){
//				Map<String, Object> tmap = new HashMap<String, Object>();
//				if(resultlist.get(j).getResultCaseId().equals(CaseIdList.get(i))){
//					tmap.put("name", resultlist.get(j).getResultTaskContent());
//					tmap.put("value", resultlist.get(j).getResultTaskOrder());
//					tasklist.add(tmap);
//				}
//			}
//			cmap.put("tasklist", tasklist);
			
			
			//得分
			String hqlscore = "select t1.resultTaskOrder, t1.groupScore "
					+ "from ResultTask as t1 "
					+ "where t1.resultCaseId = " + CaseIdList.get(i);
			@SuppressWarnings("unchecked")
			List<Object>groupScoreList =caseDAO.getListByHql(hqlscore);
			List<Map<String, Object>> scoreList = new ArrayList<Map<String, Object>>();
			for(Integer j = 0; j < groupScoreList.size(); j++){	
				
				Object[] groupScore =  (Object[]) groupScoreList.get(j);
				
				Map<String, Object> smap = new HashMap<String, Object>();
				if(groupScore[1] != null){
					smap.put("name",  "任务" + groupScore[0] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;得分：" +groupScore[1]);
				 }else{
				    smap.put("name",  "任务" + groupScore[0] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;得分：" +"无");
				 }
					
				scoreList.add(smap);

			}
			cmap.put("scoreList",scoreList);
			
			resulttasklist.add(cmap);
			
		}
		
		Selectmap.put("Exp",belongs_exp);
		map.put("tittle", title);
		map.put("multiSelect", Selectmap);
		map.put("ResultTask", resulttasklist);
		
		return map;
	}
	
	
}
