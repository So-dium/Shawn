package virnet.management.combinedao;

import java.util.HashMap;
import java.util.Map;

import virnet.management.dao.AutoScoreDAO;
import virnet.management.entity.AutoScore;

public class AutoScoreCDAO {
	
	private AutoScoreDAO aDAO = new AutoScoreDAO();

	public boolean addAutoScore(String caseId, Integer taskId, Integer topo, Integer config, Integer ping, Integer grade){
		boolean flag = true;
		AutoScore autoScore = new AutoScore();
		autoScore.setCaseId(caseId);
		autoScore.setTaskId(taskId);
		autoScore.setTopo(topo);
		autoScore.setConfig(config);
		autoScore.setPing(ping);
		autoScore.setGrade(grade);
		if(!this.aDAO.add(autoScore)){
			flag = false;
		}
		return flag;	
	}
	
	public Map<String, Object> selectAutoScore(String caseId, String taskId){
		Map<String, Object> map = new HashMap<String, Object>();
		AutoScore autoScore = (AutoScore)this.aDAO.getByNProperty("caseid", caseId, "taskid", taskId);
		if(autoScore != null){
			map.put("topo", autoScore.getTopo());
			map.put("config", autoScore.getConfig());
			map.put("ping", autoScore.getPing());
			map.put("grade", autoScore.getGrade());
		}
		return map;
	}
}
