package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ResultDeviceConfigDAO;
import virnet.experiment.entity.ResultDeviceConfig;

public class ResultDeviceConfigCDAO {
	
	private ResultDeviceConfigDAO rdcDAO = new ResultDeviceConfigDAO();
	
	public void delete(Integer resultConfigId){
		
		//删除原记录,如果原来没有记录，则list长度为0
		@SuppressWarnings("unchecked")
		List<ResultDeviceConfig> rdclist = this.rdcDAO.getListByProperty("resultConfigId", resultConfigId);
		int i=0;
		while(i!=rdclist.size()){
			this.rdcDAO.delete(rdclist.get(i));
			i++;
		}
	}
	
	//逐个设备重写
	public boolean edit(Integer resultConfigId,Integer deviceOrder,String configFile){
		
		ResultDeviceConfig newDeviceConfig = new ResultDeviceConfig();
		newDeviceConfig.setResultConfigId(resultConfigId);
		newDeviceConfig.setDeviceOrder(deviceOrder);
		newDeviceConfig.setConfigFile(configFile);
		
		if(this.rdcDAO.add(newDeviceConfig))
		    return true;
		else
			return false;
	}
		
}
