package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ExpConfigDAO;
import virnet.experiment.dao.ExpDeviceConfigDAO;
import virnet.experiment.dao.ExpTopoDAO;
import virnet.experiment.entity.ExpConfig;
import virnet.experiment.entity.ExpConnect;
import virnet.experiment.entity.ExpDeviceConfig;
import virnet.experiment.entity.ExpTopo;

public class ExpDeviceConfigCDAO {
	
	private ExpDeviceConfigDAO dcDAO = new ExpDeviceConfigDAO();
	
	public void delete(Integer expConfigId){
		
		//删除原记录,如果原来没有记录，则list长度为0
		@SuppressWarnings("unchecked")
		List<ExpDeviceConfig> dclist = this.dcDAO.getListByProperty("expConfigId", expConfigId);
		int i=0;
		while(i!=dclist.size()){
			this.dcDAO.delete(dclist.get(i));
			i++;
		}
	}
	
	//逐个设备重写
	public boolean edit(Integer expConfigId,Integer deviceOrder,String configFile){
		
		ExpDeviceConfig newDeviceConfig = new ExpDeviceConfig();
		newDeviceConfig.setExpConfigId(expConfigId);
		newDeviceConfig.setExpDeviceOrder(deviceOrder);
		newDeviceConfig.setConfigFile(configFile);
		
		if(this.dcDAO.add(newDeviceConfig))
			return true;
		else
			return false;
	}

	// 删除一个任务的所有设备配置信息
	public void deleteAllConfigInfo(Integer expId, Integer expTaskOrder) {

		ExpConfigDAO ConfigDAO = new ExpConfigDAO();
		String[] para = { "expId", "" + expId, "expTaskOrder", "" + expTaskOrder };
		ExpConfig config = (ExpConfig) ConfigDAO.getByNProperty(para);
		
		if(config == null)
			return;

		Integer configId = config.getExpConfigId();
		List<ExpDeviceConfig> dclist = this.dcDAO.getListByProperty("expConfigId", configId);
		try {
			int i = 0;
			while (i != dclist.size()) {
				this.dcDAO.delete(dclist.get(i));
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("删除设备配置信息失败");
		}
	}

}
