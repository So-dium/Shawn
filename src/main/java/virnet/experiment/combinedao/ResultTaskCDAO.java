package virnet.experiment.combinedao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.experiment.dao.ResultTaskDAO;
import virnet.management.dao.ExpTaskDAO;
import virnet.management.entity.ExpTask;
import virnet.experiment.entity.ResultTask;

public class ResultTaskCDAO {
	
	private ResultTaskDAO rtDAO = new ResultTaskDAO();
	
	//创建实验结果任务表    expCaseId 为实验实例Id  对应entity为resultCaseId
	public boolean init(String expCaseId, Integer expId){
		
		@SuppressWarnings("unchecked")
		List<ResultTask>  rtlist= rtDAO.getListByProperty("resultCaseId", expCaseId);
		
		if(rtlist.size()!=0){
			//删除
			int i=0;
			while(i!=rtlist.size()){
				this.rtDAO.delete(rtlist.get(i));
				i++;
			}	
		}
			
		ExpTaskDAO tDAO = new ExpTaskDAO();
		@SuppressWarnings("unchecked")
		List<ExpTask>  tlist = tDAO.getListByProperty("expId", expId);
			
		boolean success = true;
		for(int i=0;i<tlist.size();i++){
				
			ResultTask resultTask = new ResultTask();
			resultTask.setResultCaseId(expCaseId);
			resultTask.setResultExpId(expId);
			resultTask.setResultTaskOrder(tlist.get(i).getExpTaskOrder());
			resultTask.setResultTaskType(tlist.get(i).getExpTaskType());
			resultTask.setResultTaskContent(tlist.get(i).getExpTaskContent());
		    
			if(!this.rtDAO.add(resultTask))
				success = false;
		}
		System.out.println("resultTask ok !!!!!!!!!!"+ success);
		return success;	
	}
	public Integer getResultTaskId(String expCaseId, String expId ,String expTaskOrder){
		
		String[] para = {"resultCaseId",expCaseId,"resultExpId",expId,"resultTaskOrder",expTaskOrder};
		ResultTask resultTask = (ResultTask)this.rtDAO.getByNProperty(para);
		if(resultTask!=null)
			return resultTask.getResultTaskId();
		else
			return 0;
	}
	public Map<String, Object> getResultTaskScore(String expCaseId, String expId ,String expTaskOrder){
		Map<String, Object> map = new HashMap<String, Object>();
		String[] para = {"resultCaseId",expCaseId,"resultExpId",expId,"resultTaskOrder",expTaskOrder};
		ResultTask resultTask = (ResultTask)this.rtDAO.getByNProperty(para);
		if(resultTask!=null){
			map.put("topoScore", resultTask.getTopoScore());
			map.put("configScore", resultTask.getConfigScore());
			map.put("pingScore", resultTask.getPingScore());
			map.put("groupScore", resultTask.getGroupScore());
		}
		return map;
	}
	public boolean setResultTaskScore(String resultCaseId ,String resultTaskOrder, Integer topoScore, Integer configScore, Integer pingScore, Integer groupScore){
		boolean success = true;
		String[] para = {"resultCaseId",resultCaseId,"resultTaskOrder",resultTaskOrder};
		ResultTask resultTask = (ResultTask)this.rtDAO.getByNProperty(para);
		resultTask.setTopoScore(topoScore);
		resultTask.setConfigScore(configScore);
		resultTask.setPingScore(pingScore);
		resultTask.setGroupScore(groupScore);
		if(!this.rtDAO.update(resultTask)){
			success = false;
		}
		System.out.println("resultTaskScore ok !!!!!!!!!!"+ success);
		return success;	
	}
}

