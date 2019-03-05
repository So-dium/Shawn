package virnet.experiment.combinedao;

import virnet.experiment.dao.ResultConfigDAO;
import virnet.experiment.entity.ResultConfig;

public class ResultConfigCDAO {
	
	private ResultConfigDAO rcDAO = new ResultConfigDAO();
	
	//返回实验模板配置Id
	public Integer edit(Integer resultTaskId,Integer deviceNum){    
		
		ResultConfig config = (ResultConfig)this.rcDAO.getUniqueByProperty("resultTaskId",resultTaskId);
		
		//找不到该实验模板配置，即未设置过该实验模板对应任务的配置信息，任务号为0指的是初始配置
		if(config==null){
			
			ResultConfig newconfig = new ResultConfig();
			newconfig.setResultTaskId(resultTaskId);
			newconfig.setResultConfigNum(deviceNum);
			
			if(this.rcDAO.add(newconfig)){
				return newconfig.getResultConfigId();
			}
			else
				return 0;
		}
		else{             //已经存在该实验模板对应的任务的配置信息,则修改
			
			config.setResultTaskId(resultTaskId);
			config.setResultConfigNum(deviceNum);
			
			if(this.rcDAO.update(config)){
				return config.getResultConfigId();
			}
			else
				return 0;
		}
	}
}
