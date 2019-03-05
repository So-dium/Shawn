package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ResultTopoDAO;
import virnet.experiment.entity.ResultTopo;

public class ResultTopoCDAO {
	
	private ResultTopoDAO rtDAO = new ResultTopoDAO();
	
	//返回实验模板拓扑Id,device是一个以#分割的字符串，可以得出连接数
	public Integer edit(Integer resultTaskId,String device){    
		
		String str[]=device.split("##");
		Integer connectNum = str.length;    //根据#分割数判断连接数
		
		ResultTopo topo = (ResultTopo)this.rtDAO.getUniqueByProperty("resultTaskId",resultTaskId);
		
		//找不到该实验结果拓扑，即未设置过该实验实例对应任务的拓扑信息
		if(topo==null){
			
			ResultTopo newtopo = new ResultTopo();
			newtopo.setResultTaskId(resultTaskId);
			newtopo.setresultTopoConnetNum(connectNum);
			
			if(this.rtDAO.add(newtopo)){
				return newtopo.getResultTopoId();
			}
			else
				return 0;
		}
		else{             //已经存在该实验实例对应的任务的拓扑信息,则修改
			
			topo.setResultTaskId(resultTaskId);
			topo.setresultTopoConnetNum(connectNum);
			
			if(this.rtDAO.update(topo)){
				return topo.getResultTopoId();
			}
			else
				return 0;
		}
	}
}
