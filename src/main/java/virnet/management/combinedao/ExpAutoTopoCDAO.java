package virnet.management.combinedao;

import java.util.List;

import virnet.management.dao.ExpAutoTopoDAO;
import virnet.management.entity.ExpAutoTopo;

public class ExpAutoTopoCDAO {
	
	private ExpAutoTopoDAO eDAO = new ExpAutoTopoDAO();
	private final Integer deviceNum= 10;
//	private ExpTopoCDAO etcDAO = new ExpTopoCDAO();
//	private ExpConfigCDAO eccDAO = new ExpConfigCDAO();
//	private ExpVerifyCDAO evcDAO = new ExpVerifyCDAO();

	public boolean addExpAutoTopo(String caseId, Integer taskId, Integer[] expScore){
//		//从1开始搜索未使用的id号
//		Integer i=1,id=1;
//		while(i!=0){
//			String[] para={"id","" + i};
//			ExpAutoTopo auto = (ExpAutoTopo) this.eDAO.getByNProperty(para);
//			//找不到该任务，即此任务次序号未占用
//			if(auto==null){
//				id=i;
//				i=-1;
//			}
//			i++;
//		}
		boolean flag = true;
		ExpAutoTopo expAutoTopo = new ExpAutoTopo();
		for(int i = 0; i < expScore.length; i++){
			expAutoTopo.setId(i + 1);
			expAutoTopo.setCaseId(caseId);
			expAutoTopo.setTaskId(taskId);
			expAutoTopo.setFacilitiesOrder(i + 1);
			expAutoTopo.setConnect(expScore[i]);
			if(!this.eDAO.add(expAutoTopo)){
				flag = false;
			}
		}
		return flag;
		
	}
	
	@SuppressWarnings("unchecked")
	public Integer[] selectExpAutoTopo(String caseId, String taskId){
		Integer[] expScore = new Integer[deviceNum];
		for(int i= 0; i < deviceNum; i++){
			ExpAutoTopo expAutoTopo = (ExpAutoTopo)this.eDAO.getByNProperty("id", (i + 1)+"", "caseid", caseId, "taskid", taskId);
			expScore[i] = expAutoTopo.getConnect();	
		}
		return expScore;
	}
}
