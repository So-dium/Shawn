package virnet.experiment.combinedao;

import virnet.experiment.dao.ExpConfigDAO;
import virnet.experiment.combinedao.ExpDeviceConfigCDAO;
import virnet.experiment.entity.ExpConfig;
import virnet.experiment.entity.ExpTopo;

public class ExpConfigCDAO {
	
	private ExpConfigDAO cDAO = new ExpConfigDAO();
	private ExpDeviceConfigCDAO edccDAO = new ExpDeviceConfigCDAO();
	
	//返回实验模板配置Id
	public Integer edit(String expId, String expTaskOrder ,Integer deviceNum){    
		
		String para[]={"expId",expId,"expTaskOrder",expTaskOrder};
		ExpConfig config = (ExpConfig)this.cDAO.getByNProperty(para);
		
		//找不到该实验模板配置，即未设置过该实验模板对应任务的配置信息，任务号为0指的是初始配置
		if(config==null){
			
			ExpConfig newconfig = new ExpConfig();
			newconfig.setExpTaskOrder(Integer.parseInt(expTaskOrder));
			newconfig.setExpId(Integer.parseInt(expId));
			newconfig.setExpConfigNum(deviceNum);
			
			if(this.cDAO.add(newconfig)){
				return newconfig.getExpConfigId();
			}
			else
				return 0;
		}
		else{             //已经存在该实验模板对应的任务的配置信息,则修改
			
			config.setExpTaskOrder(Integer.parseInt(expTaskOrder));
			config.setExpId(Integer.parseInt(expId));
			config.setExpConfigNum(deviceNum);
			
			if(this.cDAO.update(config)){
				return config.getExpConfigId();
			}
			else
				return 0;
		}
	}

	// 删除某个任务配置信息，包括（配置表，设备配置表）
	public void deleteConfigInfo(Integer expId, Integer expTaskOrder) {

		// 设备配置
		this.edccDAO.deleteAllConfigInfo(expId, expTaskOrder);

		// 配置表
		String[] para = { "expId", "" + expId, "expTaskOrder", "" + expTaskOrder };
		ExpConfig expconfig = (ExpConfig) cDAO.getByNProperty(para);
		if(expconfig == null)
			return;
		cDAO.delete(expconfig);
	}
}
