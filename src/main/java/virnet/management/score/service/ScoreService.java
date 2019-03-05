package virnet.management.score.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.experiment.combinedao.ExpConnectCDAO;
import virnet.experiment.combinedao.ExpTopoPositionCDAO;
import virnet.experiment.combinedao.ResultConnectCDAO;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.combinedao.ResultTopoPositionCDAO;
import virnet.experiment.dao.ExpTopoDAO;
import virnet.experiment.dao.ResultTaskDAO;
import virnet.experiment.dao.ResultTopoDAO;
import virnet.experiment.entity.ExpTopo;
import virnet.experiment.entity.ResultTask;
import virnet.experiment.entity.ResultTopo;
import virnet.management.combinedao.CabinetTempletDeviceInfoCDAO;
import virnet.management.dao.CaseDAO;
import virnet.management.dao.ExpTaskDAO;
import virnet.management.entity.Case;

public class ScoreService {
	private ResultTaskDAO rDAO = new ResultTaskDAO();
	private ResultTopoDAO rtDAO = new ResultTopoDAO();
	private ResultConnectCDAO connectDAO = new ResultConnectCDAO();
	private ResultTopoPositionCDAO positionDAO = new ResultTopoPositionCDAO();
	private ExpTaskDAO eDAO = new ExpTaskDAO();
	private ExpTopoDAO etDAO = new ExpTopoDAO();
	private ExpConnectCDAO econnectDAO = new ExpConnectCDAO();
	private ExpTopoPositionCDAO epositionDAO = new ExpTopoPositionCDAO();
	private CabinetTempletDeviceInfoCDAO deviceDAO = new CabinetTempletDeviceInfoCDAO(); 
	private CaseDAO caseDAO = new CaseDAO();
	
	
	//读取学生实验信息，包括拓扑图，ping验证，各项分数
	public Map<String, Object> LoadScoreInfo(String caseid, String taskid){
		System.out.println(caseid);
		System.out.println(taskid);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> tlist = new ArrayList<Object>();
		List<Object> olist = new ArrayList<Object>();	
		List pslist = new ArrayList();
		List pdlist = new ArrayList();
		List plist = new ArrayList();
		List<Object> explist = new ArrayList<Object>();
		List<Object> tasklist = new ArrayList<Object>();
		List ttslist = new ArrayList();
		List tcslist = new ArrayList();
		List tpslist = new ArrayList();
		String expname="",task="",connect="",position="",equipmentname="",equipmentorder="",equipmentport="";
		String hql1="select t4.deviceType "
				+ "from ResultTask as t1, Exp as t2, CabinetTemplet as t3, CabinetTempletDevice as t4 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expCabinetTempletId = t3.cabinetTempletId and "
				+ "t3.cabinetTempletId = t4.cabinetTempletId and "
				+ "t1.resultCaseId = " + caseid + "and "
				+ "t1.resultTaskOrder = " + taskid;
		tlist = rDAO.getListByHql(hql1);
		String hql2="select t4.deviceOrder "
				+ "from ResultTask as t1, Exp as t2, CabinetTemplet as t3, CabinetTempletDevice as t4 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expCabinetTempletId = t3.cabinetTempletId and "
				+ "t3.cabinetTempletId = t4.cabinetTempletId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid;
		olist = rDAO.getListByHql(hql2);
		String hql3="select t3.sourcePCOrder "
				+ "from ResultTask as t1, ResultVerify as t2, ResultVerifyPing as t3 "
				+ "where t1.resultTaskId = t2.resultTaskId and "
				+ "t2.resultVerifyId = t3.resultVerifyId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid;
		pslist = rDAO.getListByHql(hql3);
		String hql4="select t3.destPCOrder "
				+ "from ResultTask as t1, ResultVerify as t2, ResultVerifyPing as t3 "
				+ "where t1.resultTaskId = t2.resultTaskId and "
				+ "t2.resultVerifyId = t3.resultVerifyId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid;
		pdlist = rDAO.getListByHql(hql4);
		String hql5="select t3.successFlag "
				+ "from ResultTask as t1, ResultVerify as t2, ResultVerifyPing as t3 "
				+ "where t1.resultTaskId = t2.resultTaskId and "
				+ "t2.resultVerifyId = t3.resultVerifyId and "
				+ "t1.resultCaseId = " + caseid + "and "
				+ "t1.resultTaskOrder = " + taskid;
		plist = rDAO.getListByHql(hql5);
		try {
			String hql6="select t2.expName "
					+ "from ResultTask as t1, Exp as t2 "
					+ "where t1.resultExpId = t2.expId and "
					+ "t1.resultCaseId = " + caseid + " and "
					+ "t1.resultTaskOrder = " + taskid;
			explist = rDAO.getListByHql(hql6);
			expname = (String)explist.get(0);
			String hql7="select t1.resultTaskContent "
					+ "from ResultTask as t1 "
					+ "where t1.resultCaseId = " + caseid + " and "
					+ "t1.resultTaskOrder = " + taskid;
			tasklist = rDAO.getListByHql(hql7);
			task = (String)tasklist.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String hql8="select t2.expTaskTopoScore "
				+ "from ResultTask as t1, ExpTask as t2 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid + " and "
				+ "t2.expTaskId = " + taskid;
		ttslist = eDAO.getListByHql(hql8);
		String hql9="select t2.expTaskConfigScore "
				+ "from ResultTask as t1, ExpTask as t2 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid + " and "
				+ "t2.expTaskId = " + taskid;
		tcslist = eDAO.getListByHql(hql9);
		String hql10="select t2.expTaskPingScore "
				+ "from ResultTask as t1, ExpTask as t2 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid + " and "
				+ "t2.expTaskId = " + taskid;
		tpslist = eDAO.getListByHql(hql10);
		
		
		ResultTask rtask = new ResultTask();
		String[] strs ={"resultCaseId",caseid,"resultTaskOrder",taskid};
		rtask = (ResultTask)rDAO.getByNProperty(strs);
		equipmentname = deviceDAO.equipment(rtask.getResultExpId().toString());
		
		ResultTopo rtopo = new ResultTopo();
		try {
			rtopo = (ResultTopo)rtDAO.getUniqueByProperty("resultTaskId", rtask.getResultTaskId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connect = connectDAO.connectInfo(rtopo.getResultTopoId());
			position = positionDAO.position(rtopo.getResultTopoId());
			
			equipmentorder = olist.get(0).toString();
			if(tlist.get(0).toString().equals("1")){
				equipmentport = "0@1"; 
			}
			if(tlist.get(0).toString().equals("2")){
				equipmentport = "1@2@3@4@5@6@7@8"; 
			}
			if(tlist.get(0).toString().equals("3")){
				equipmentport = "1@2@3@4@5@6"; 
			}
			if(tlist.get(0).toString().equals("4")){
				equipmentport = "1"; 
			}
			for(int i = 1; i < olist.size(); i++){
				equipmentorder += "##" + olist.get(i).toString();
				if(tlist.get(i).toString().equals("1")){
					equipmentport += "##0@1"; 
				}
				if(tlist.get(i).toString().equals("2")){
					equipmentport += "##1@2@3@4@5@6@7@8"; 
				}
				if(tlist.get(i).toString().equals("3")){
					equipmentport += "##1@2@3@4@5@6"; 
				}
				if(tlist.get(i).toString().equals("4")){
					equipmentport += "##1"; 
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("devicetype", tlist);
		map.put("deviceorder", olist);
		map.put("pingsourse", pslist);
		map.put("pingdest", pdlist);
		map.put("pingstate", plist);
		map.put("expname", expname);
		map.put("task", task);
		map.put("connect", connect);
		map.put("position", position);
		map.put("equipmentname", equipmentname);
		map.put("equipmentorder", equipmentorder);
		map.put("equipmentport", equipmentport);
		map.put("tasktoposcore", ttslist);
		map.put("taskconfigscore", tcslist);
		map.put("taskpingscore", tpslist);
		return map;
	}
	
	
	//读取学生实验配置文件
	public Map<String, Object> ShowConfigure(String caseid, String taskid, String order){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> flist = new ArrayList<Object>();	
		try {
			String hql="select t3.configFile "
					+ "from ResultTask as t1, ResultConfig as t2, ResultDeviceConfig as t3 "
					+ "where t1.resultTaskId = t2.resultTaskId and "
					+ "t2.resultConfigId = t3.resultConfigId and "
					+ "t3.DeviceOrder = " + order + " and "
					+ "t1.resultCaseId = " + caseid + " and "
					+ "t1.resultTaskOrder = " + taskid;
			flist = rDAO.getListByHql(hql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(flist.size());
		map.put("configfile", flist);
		
		return map;
	}

	
	//读取实验模版实验信息，包括拓扑图，ping验证
	public Map<String, Object> LoadAnsScoreInfo(String caseid, String taskid){
		System.out.println("answer");
		System.out.println(caseid);
		System.out.println(taskid);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> tlist = new ArrayList<Object>();
		List<Object> olist = new ArrayList<Object>();	
		List pslist = new ArrayList();
		List pdlist = new ArrayList();
		List plist = new ArrayList();
		List<Object> explist = new ArrayList<Object>();
		List<Object> tasklist = new ArrayList<Object>();
		List<Object> tplist = new ArrayList<Object>();
		
		String expname="",task="",connect="",position="",equipmentname="",equipmentorder="",equipmentport="";
		String hql1="select t4.deviceType "
				+ "from ResultTask as t1, Exp as t2, CabinetTemplet as t3, CabinetTempletDevice as t4 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expCabinetTempletId = t3.cabinetTempletId and "
				+ "t3.cabinetTempletId = t4.cabinetTempletId and "
				+ "t1.resultCaseId = " + caseid + "and "
				+ "t1.resultTaskOrder = " + taskid;
		tlist = rDAO.getListByHql(hql1);
		String hql2="select t4.deviceOrder "
				+ "from ResultTask as t1, Exp as t2, CabinetTemplet as t3, CabinetTempletDevice as t4 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expCabinetTempletId = t3.cabinetTempletId and "
				+ "t3.cabinetTempletId = t4.cabinetTempletId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid;
		olist = rDAO.getListByHql(hql2);
		try {
			String hql3="select t3.sourcePCOrder "
					+ "from ResultTask as t1, ExpVerify as t2, ExpVerifyPing as t3 "
					+ "where t1.resultExpId = t2.expId and "
					+ "t2.expVerifyId = t3.expVerifyId and "
					+ "t1.resultCaseId = " + caseid + " and "
					+ "t2.expTaskOrder = " + taskid;
			pslist = rDAO.getListByHql(hql3);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String hql4="select t3.destPCOrder "
				+ "from ResultTask as t1, ExpVerify as t2, ExpVerifyPing as t3 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expVerifyId = t3.expVerifyId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t2.expTaskOrder = " + taskid;
		pdlist = rDAO.getListByHql(hql4);
		String hql5="select t3.successFlag "
				+ "from ResultTask as t1, ExpVerify as t2, ExpVerifyPing as t3 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expVerifyId = t3.expVerifyId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t2.expTaskOrder = " + taskid;
		plist = rDAO.getListByHql(hql5);
		String hql6="select t2.expTaskPing "
				+ "from ResultTask as t1, ExpTask as t2 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t1.resultTaskOrder = " + taskid + " and "
				+ "t2.expTaskId = " + taskid;
		tplist = eDAO.getListByHql(hql6);
		
		
		ResultTask rtask = new ResultTask();	
		String[] strs ={"resultCaseId",caseid,"resultTaskOrder",taskid};
		rtask = (ResultTask)rDAO.getByNProperty(strs);
		equipmentname = deviceDAO.equipment(rtask.getResultExpId().toString());
		
		ExpTopo etopo = new ExpTopo();
		try {
			String[] strs1 = {"expId", rtask.getResultExpId().toString(), "expTaskOrder", taskid}; 
			etopo = (ExpTopo)etDAO.getByNProperty(strs1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connect = econnectDAO.connectInfo(etopo.getExpTopoId());
			position = epositionDAO.position(etopo.getExpTopoId());
			
			equipmentorder = olist.get(0).toString();
			if(tlist.get(0).toString().equals("1")){
				equipmentport = "0@1"; 
			}
			if(tlist.get(0).toString().equals("2")){
				equipmentport = "1@2@3@4@5@6@7@8"; 
			}
			if(tlist.get(0).toString().equals("3")){
				equipmentport = "1@2@3@4@5@6"; 
			}
			if(tlist.get(0).toString().equals("4")){
				equipmentport = "1"; 
			}
			for(int i = 1; i < olist.size(); i++){
				equipmentorder += "##" + olist.get(i).toString();
				if(tlist.get(i).toString().equals("1")){
					equipmentport += "##0@1"; 
				}
				if(tlist.get(i).toString().equals("2")){
					equipmentport += "##1@2@3@4@5@6@7@8"; 
				}
				if(tlist.get(i).toString().equals("3")){
					equipmentport += "##1@2@3@4@5@6"; 
				}
				if(tlist.get(i).toString().equals("4")){
					equipmentport += "##1"; 
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("标准答案拓扑连线：" + connect);
		map.put("devicetype", tlist);
		map.put("deviceorder", olist);
		map.put("pingsourse", pslist);
		map.put("pingdest", pdlist);
		map.put("pingstate", plist);
		map.put("expname", expname);
		map.put("task", task);
		map.put("connect", connect);
		map.put("position", position);
		map.put("equipmentname", equipmentname);
		map.put("equipmentorder", equipmentorder);
		map.put("equipmentport", equipmentport);
		map.put("taskping", tplist);
		
		return map;
	}
	
	//读取实验模版配置文件
	public Map<String, Object> ShowAnsConfigure(String caseid, String taskid, String order){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> flist = new ArrayList<Object>();	
		String hql="select t3.configFile "
				+ "from ResultTask as t1, ExpConfig as t2, ExpDeviceConfig as t3 "
				+ "where t1.resultExpId = t2.expId and "
				+ "t2.expConfigId = t3.expConfigId and "
				+ "t3.expDeviceOrder = " + order + " and "
				+ "t1.resultCaseId = " + caseid + " and "
				+ "t2.expTaskOrder = " + taskid;
		flist = rDAO.getListByHql(hql);
		System.out.println(flist);
		map.put("configfile", flist);
		
		return map;
	}
	
	
	//更新成绩
    public Map<String, Object> handscore(String caseid, String taskid, Integer toposcore,
    		                             Integer configscore, Integer pingscore, Integer groupscore){
		Map<String, Object> map = new HashMap<String, Object>();
		ResultTaskCDAO rtCDAO = new ResultTaskCDAO();
		if(rtCDAO.setResultTaskScore(caseid, taskid, toposcore, configscore, pingscore, groupscore)){
			map.put("value", 1);
		}
		else{
			map.put("value", 0);
		}
		
		return map;
	}
	public boolean UpdateTopoInfo(String caseid, Integer score, Integer[] expScore){//保存相同类型不同设备连线信息
		
		return true;
	}
	public String getIpResult(String configFile){
		Integer[] res = new Integer[4];
		String Res = new String();
		String[] Config = configFile.split("\n");
		String[] IP = Config[0].split("：");
		String[] Mask = Config[1].split("：");
		String[]  SplitIP = IP[1].split("\\.");
		String[]  SplitMask = Mask[1].split("\\.");
		for(int j=0;j<4;j++){	
			res[j] = Integer.parseInt(SplitIP[j])&Integer.parseInt(SplitMask[j]);
			Res += res[j].toString();
		}
		return Res;
	}
	public int CountNetworkNum(String caseid, String taskid){
		int stucount=1;
		int anscount=1;
		String[] StuRes = new String[4];
		String[] AnsRes = new String[4];
		int ConfigNum=0;
		List<Object> olist = new ArrayList<Object>();
		List<Object> stuflist = new ArrayList<Object>();
		List<Object> ansflist = new ArrayList<Object>();
		Map<String, Object> stumap = new HashMap<String, Object>();
		Map<String, Object> ansmap = new HashMap<String, Object>();
		try {
			String hql="select t2.resultConfigNum "
					+ "from ResultTask as t1, ResultConfig as t2 "
					+ "where t1.resultTaskId = t2.resultTaskId and "
					+ "t1.resultCaseId = " + caseid + " and "
					+ "t1.resultTaskOrder = " + taskid;
			olist = rDAO.getListByHql(hql);
			ConfigNum = (int)olist.get(0);
			System.out.println("num is"+ConfigNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=ConfigNum-3; i<=ConfigNum; i++){
			Integer[] stures = new Integer[4];
			Integer[] ansres = new Integer[4];
			try {
				stumap = this.ShowConfigure(caseid, taskid, i+"");
				ansmap = this.ShowAnsConfigure(caseid, taskid, i+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stuflist = (List<Object>) stumap.get("configfile");
			ansflist = (List<Object>) ansmap.get("configfile");
			String stuconfig = (String) stuflist.get(0);
			String ansconfig = (String) ansflist.get(0);
//			String[] StuConfig = stuconfig.split("\n");
//			String[] AnsConfig = ansconfig.split("\n");
//			String[] StuIP = StuConfig[0].split("：");
//			String[] AnsIP = AnsConfig[0].split("：");
//			String[] StuMask = StuConfig[1].split("：");
//			String[] AnsMask = AnsConfig[1].split("：");
//			String[]  StuSplitIP = StuIP[1].split("\\.");
//			String[]  AnsSplitIP = AnsIP[1].split("\\.");
//			String[]  StuSplitMask = StuMask[1].split("\\.");
//			String[]  AnsSplitMask = AnsMask[1].split("\\.");
//			for(int j=0;j<4;j++){
//				stures[j] = Integer.parseInt(StuSplitIP[j])&Integer.parseInt(StuSplitMask[j]);
//				ansres[j] = Integer.parseInt(AnsSplitIP[j])&Integer.parseInt(AnsSplitMask[j]);
//				StuRes[i-ConfigNum+3]+= stures[j].toString();
//				AnsRes[i-ConfigNum+3]+= ansres[j].toString();
//			}
			StuRes[i-ConfigNum+3] += getIpResult(stuconfig);
			AnsRes[i-ConfigNum+3] += getIpResult(ansconfig);
		}
		for(int i=1;i<4;i++){
			for(int j=0;j<i;j++){
				if(StuRes[i].equals(StuRes[j])){
					break;
				}
				else{
					if(j<i-1)
						continue;
					if(j==i-1)
						stucount++;
				}
			}
		}
		for(int i=1;i<4;i++){
			for(int j=0;j<i;j++){
				if(AnsRes[i].equals(AnsRes[j])){
					break;
				}
				else{
					if(j<i-1)
						continue;
					if(j==i-1)
						anscount++;
				}
			}
		}
		System.out.println(stucount);
		System.out.println(anscount);
		if(stucount == anscount)
			return 30;
		else
			return 0;
	}
	public String[] getPcIp(String caseId, String taskId){
		List<Object> olist = new ArrayList<Object>();
		List<Object> ansflist = new ArrayList<Object>();
		Map<String, Object> stumap = new HashMap<String, Object>();
		Map<String, Object> ansmap = new HashMap<String, Object>();
		String[] AnsRes = new String[4];
		int ConfigNum=0;
		try {
			String hql="select t2.resultConfigNum "
					+ "from ResultTask as t1, ResultConfig as t2 "
					+ "where t1.resultTaskId = t2.resultTaskId and "
					+ "t1.resultCaseId = " + caseId + " and "
					+ "t1.resultTaskOrder = " + taskId;
			olist = rDAO.getListByHql(hql);
			ConfigNum = (int)olist.get(0);
			System.out.println("num is"+ConfigNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=ConfigNum-3; i<=ConfigNum; i++){
			Integer[] stures = new Integer[4];
			Integer[] ansres = new Integer[4];
			try {
				ansmap = this.ShowAnsConfigure(caseId, taskId, i+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ansflist = (List<Object>) ansmap.get("configfile");
			String ansconfig = (String) ansflist.get(0);
			AnsRes[i-ConfigNum+3] += getIpResult(ansconfig);
		}
		return AnsRes;
	}
}
