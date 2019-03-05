package virnet.management.score.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import virnet.management.score.service.ScoreService;
import virnet.experiment.dao.ResultTaskDAO;
import virnet.management.combinedao.AutoScoreCDAO;
import virnet.management.combinedao.ExpAutoTopoCDAO;
import virnet.management.dao.ExpTaskDAO;
import virnet.management.entity.ExpTask;

public class ScoreAction extends ActionSupport implements ServletRequestAware{
	

	private static final long serialVersionUID = 6703262531506665378L;
	
	private HttpServletRequest request;
	private ExpTaskDAO tDAO = new ExpTaskDAO();
	private ExpTask task;
	private Map<String, Object> result = new HashMap<String, Object>();
	private ScoreService service = new ScoreService();
	private final Integer deviceNum = 10;//设备总数
	private Integer topoSum = 0;
	private Integer configSum = 0;
	private Integer pingSum = 0;
	private ResultTaskDAO rDAO = new ResultTaskDAO();
	
	public String sessionConfigure(){
		
		String	index = this.request.getParameter("index");
		String	str = this.request.getParameter("str");
		String	taskid = this.request.getParameter("taskid");
		String	tasknum = this.request.getParameter("tasknum");
			
		ActionContext.getContext().getSession().put("index", index);
		ActionContext.getContext().getSession().put("str", str);
		ActionContext.getContext().getSession().put("taskid", taskid);
		ActionContext.getContext().getSession().put("tasknum", tasknum);
		
		this.result.put("isSuccess", true);
		
		return SUCCESS;
	}
	public String LoadScorePageInfo(){
		String	caseid = this.request.getParameter("caseid");
		String	taskid = this.request.getParameter("taskid");

		this.result = this.service.LoadScoreInfo(caseid, taskid);
		//ExpAutoScore();
		//AnsAutoScore();
		//AnsAutoConfig();
		return SUCCESS;
	}
	public String LoadConfigureInfo(){
		String	caseid = this.request.getParameter("caseid");
		String	taskid = this.request.getParameter("taskid");
		String	order = this.request.getParameter("order");
		this.result = this.service.ShowConfigure(caseid, taskid, order);
		return SUCCESS;
	}
	public String LoadAnsScorePageInfo(){
		System.out.println("start");
		String	caseid = this.request.getParameter("caseid");
		String	taskid = this.request.getParameter("taskid");
		System.out.println(caseid);
		System.out.println(taskid);
		this.result = this.service.LoadAnsScoreInfo(caseid, taskid);

		return SUCCESS;
	}
	public String LoadAnsConfigureInfo(){
		String	caseid = this.request.getParameter("caseid");
		String	taskid = this.request.getParameter("taskid");
		String	order = this.request.getParameter("order");
		this.result = this.service.ShowAnsConfigure(caseid, taskid, order);
		return SUCCESS;
	}
	
	public String HandinScore(){
		String	caseid = this.request.getParameter("caseid");
		String	taskid = this.request.getParameter("taskid");
		Integer toposcore = 0;
		Integer	pingscore = 0;
		Integer	configscore = 0;
		
		//防止输入错误
		try{
		    toposcore = Integer.parseInt(this.request.getParameter("toposcore"));
		}catch(Exception e){
			toposcore = 0;
		}
		try{
			configscore = Integer.parseInt(this.request.getParameter("configurescore"));
		}catch(Exception e){
			configscore = 0;
		}
		try{
			pingscore = Integer.parseInt(this.request.getParameter("pingscore"));
		}catch(Exception e){
			pingscore = 0;
		}

		Integer groupscore = toposcore+pingscore+configscore;
		System.out.println(toposcore);
		System.out.println(pingscore);
		System.out.println(configscore);
		this.result = this.service.handscore(caseid, taskid, toposcore, configscore, pingscore, groupscore);
		return SUCCESS;
	}
	//网段数zyj
	public int AnsAutoConfig(String caseid, String taskid){
		System.out.println("count network");
		int count = this.service.CountNetworkNum(caseid, taskid);
		System.out.println(count);
		return count;
	}
	//标准答案拓扑图评分点保存
	@SuppressWarnings("unchecked")
	
	public boolean ExpAutoPing(String caseId, String taskId){
		Map<String, Object> expResult = new HashMap<String, Object>();
		expResult = this.service.LoadAnsScoreInfo(caseId, taskId);
		String expConnect = String.valueOf(expResult.get("taskping")) ;
		String[] pingPairList = expConnect.split("&");
		List<Integer> pingAnsList = (List<Integer>)expResult.get("pingstate");
		ArrayList<ArrayList<Object>> ans = new ArrayList<ArrayList<Object>>();
		String[] pc_ips = this.service.getPcIp(caseId, taskId);
		for(int i=0;i<pingPairList.length;i++){
			ArrayList<Object> ans_list = new ArrayList<Object>();
			ans_list.add(pingPairList[i].split("@")[0]);
			ans_list.add(pingPairList[i].split("@")[1]);
			Boolean is_same_part = false;
			String part1 = pc_ips[pingPairList[i].split("@")[0].toCharArray()[0]-'a'];
			String part2 = pc_ips[pingPairList[i].split("@")[1].toCharArray()[0]-'a'];
			if(part1.equals(part2)) is_same_part = true;
			ans_list.add(is_same_part);
			ans_list.add(pingAnsList.get(i));
			ans.add(ans_list);
		}
		return true;
	}
	
	public boolean ExpAutoTopo(String caseId, String taskId){
		Map<String, Object> expResult = new HashMap<String, Object>();
		expResult = this.service.LoadAnsScoreInfo(caseId, taskId);
		String expConnect = String.valueOf(expResult.get("connect")) ;
		List<Integer> expType = (List<Integer>)expResult.get("devicetype") ;
		String[] expInfo = expConnect.split(",");
		String[] expLeftDevice = expInfo[0].split("##");
		String[] expRightDevice = expInfo[1].split("##");
		Integer[] expNum = new Integer[expType.size()];//各类型不同设备的连线数
		Arrays.fill(expNum, 0);
		for(int i = 0; i < expLeftDevice.length; i++){
			expNum[Integer.parseInt(expLeftDevice[i])-1]++;
			expNum[Integer.parseInt(expRightDevice[i])-1]++;
		}
		for(int i = 0; i < expType.size(); i++){//升序排列
			for(int j = i; j < expType.size() - 1; j++){
				if(expType.get(j) == expType.get(j + 1)){
					if(expNum[j] > expNum[j + 1]){
						Integer temp = expNum[j];
						expNum[j] = expNum[j + 1];
						expNum[j + 1] = temp;
					}
				}
			}
		}
		Integer[] expScore = new Integer[deviceNum];//与数据表对应
		Arrays.fill(expScore, 0);
		for(int i = 0; i < expType.size(); i++){
			int count = 2*expType.get(i) - 2;
			while(true){
				if( expScore[count]== 0){
					expScore[count] = expNum[i];
					break;
				}
				count++;
			}
		}
		ExpAutoTopoCDAO eCDAO = new ExpAutoTopoCDAO();
		if(eCDAO.addExpAutoTopo(caseId, Integer.parseInt(taskId), expScore)){
			System.out.println("标准答案拓扑图评分已保存");
			return true;
		}
		//将expScore保存到数据库
//		System.out.println("拓扑自动评分");
//		for(int i = 0; i < deviceNum; i++){
//			System.out.println(expScore[i]);
//		}
//		for(int i = 0; i < expLeftService.length; i++){
//			System.out.println(expLeftService[i]);
//			System.out.println(expRightService[i]);
//			System.out.println(ansLeftService[i]);
//			System.out.println(ansRightService[i]);
//		}
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public Integer AnsAutoTopo(String caseId, String taskId){
		boolean flag = true;
		Map<String, Object> ansResult = new HashMap<String, Object>();
		ansResult = this.service.LoadScoreInfo(caseId, taskId);
		topoSum = ((List<Integer>)ansResult.get("tasktoposcore")).get(0);
		System.out.println(topoSum);
		String ansConnect = String.valueOf(ansResult.get("connect")) ;
		List<Integer> ansType = (List<Integer>)ansResult.get("devicetype") ;
		String[] ansInfo = ansConnect.split(",");
		String[] ansLeftDevice = ansInfo[0].split("##");
		String[] ansRightDevice = ansInfo[1].split("##");
		Integer[] ansNum = new Integer[ansType.size()];//各类型不同设备的连线数
		Arrays.fill(ansNum, 0);
		for(int i = 0; i < ansLeftDevice.length; i++){
			ansNum[Integer.parseInt(ansLeftDevice[i])-1]++;
			ansNum[Integer.parseInt(ansRightDevice[i])-1]++;
		}
		for(int i = 0; i < ansType.size(); i++){//升序排列
			for(int j = i; j < ansType.size() - 1; j++){
				if(ansType.get(j) == ansType.get(j + 1)){
					if(ansNum[j] > ansNum[j + 1]){
						Integer temp = ansNum[j];
						ansNum[j] = ansNum[j + 1];
						ansNum[j + 1] = temp;
					}
				}
			}
		}
		Integer[] ansScore = new Integer[deviceNum];//与数据表对应
		Arrays.fill(ansScore, 0);
		for(int i = 0; i < ansType.size(); i++){
			int count = 2*ansType.get(i) - 2;
			while(true){
				if( ansScore[count]== 0){
					ansScore[count] = ansNum[i];
					break;
				}
				count++;
			}
		}
		ExpAutoTopoCDAO eCDAO = new ExpAutoTopoCDAO();
		Integer[] expScore = new Integer[deviceNum];
		expScore = eCDAO.selectExpAutoTopo(caseId, taskId);
		for(int i = 0; i < deviceNum; i++){
			if(expScore[i] != ansScore[i]){
				flag = false;
			}
		}
		System.out.println("学生结果拓扑评分已完成");
		if(flag){
			return topoSum;
		}
		else{
			return 0;
		}
	}
	
	public boolean ExpAutoScore(){//标准答案评分信息保存，在保存标准答案的同时保存
		boolean flag = true;
		String	caseId = this.request.getParameter("caseid");
		String	taskId = this.request.getParameter("taskid");
		if(!ExpAutoTopo(caseId, taskId)){
			flag = false;
		}
//		if(!ExpAutoConfig()){
//			flag = false;
//		}
//		if(!ExpAutoPing()){
//			flag = false;
//		}
		System.out.println("标准答案自动评分信息已保存");
		return flag;
	}
	
	public boolean AnsAutoScore(){//学生答案自动评分，在学生实验时间结束之后评分
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		String	caseId = this.request.getParameter("caseid");
		String	taskId = this.request.getParameter("taskid");
		Integer topo = 0;
		Integer config = 0;
		Integer ping = 0;
		Integer grade = 0; 
		
		topo = AnsAutoTopo(caseId, taskId);
//		config = AnsAutoConfig(caseId, taskId);
//		ping = AnsAutoPing(caseId, taskId));
		grade = topo + config + ping;
		AutoScoreCDAO aCDAO = new AutoScoreCDAO();
		flag =  aCDAO.addAutoScore(caseId, Integer.parseInt(taskId), topo, config, ping, grade);//调试时如果已经保存过可能会主键冲突
		System.out.println("学生答案自动评分分数已保存");
		map = aCDAO.selectAutoScore(caseId, taskId);
	    System.out.println("拓扑自动评分结果为：" + map.get("topo"));
	    System.out.println("PC配置自动评分结果为：" + map.get("config"));
	    System.out.println("ping自动评分结果为：" + map.get("ping"));
	    System.out.println("自动评分总结果为：" + map.get("grade"));
	    return flag;
	}
	
	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }
	public Integer getConfigSum() {
		return configSum;
	}
	public void setConfigSum(Integer configSum) {
		this.configSum = configSum;
	}
	public Integer getPingSum() {
		return pingSum;
	}
	public void setPingSum(Integer pingSum) {
		this.pingSum = pingSum;
	}
	public Integer getDeviceNum() {
		return deviceNum;
	}
	
}
