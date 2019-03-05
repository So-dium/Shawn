package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ExpConnectDAO;
import virnet.experiment.dao.ExpTopoDAO;
import virnet.experiment.entity.ExpTopo;

public class ExpTopoCDAO {
	
	private ExpTopoDAO tDAO = new ExpTopoDAO();
	private ExpConnectCDAO ecDAO = new ExpConnectCDAO();
	private ExpTopoPositionCDAO etpDAO = new ExpTopoPositionCDAO();
	private ExpTopoDAO eTopoDAO = new ExpTopoDAO();
	
	//返回实验模板拓扑Id,device是一个以#分割的字符串，可以得出连接数
	public Integer edit(String expId, String expTaskOrder ,String device){    
		
		String str[]=device.split("##");
		Integer connectNum = str.length;    //根据#分割数判断连接数
		
		String para[]={"expId",expId,"expTaskOrder",expTaskOrder};
		ExpTopo topo = (ExpTopo)this.tDAO.getByNProperty(para);
		
		//找不到该实验模板拓扑，即未设置过该实验模板对应任务的拓扑信息，任务号为0指的是初始拓扑
		if(topo==null){
			
			ExpTopo newtopo = new ExpTopo();
			newtopo.setExpTaskOrder(Integer.parseInt(expTaskOrder));
			newtopo.setExpId(Integer.parseInt(expId));
			newtopo.setExpTopoConnectNum(connectNum);
			
			if(this.tDAO.add(newtopo)){
				return newtopo.getExpTopoId();
			}
			else
				return 0;
		}
		else{             //已经存在该实验模板对应的任务的拓扑信息,则修改
			
			topo.setExpTaskOrder(Integer.parseInt(expTaskOrder));
			topo.setExpId(Integer.parseInt(expId));
			topo.setExpTopoConnectNum(connectNum);
			
			if(this.tDAO.update(topo)){
				return topo.getExpTopoId();
			}
			else
				return 0;
		}
	}
	//删除某个任务拓扑信息，包括（拓扑表、连接表、位置表）
	public void deleteTopoInfo(Integer expId,Integer expTaskOrder){
		System.out.println("删除拓扑连接信息失败");
		//连接	
		this.ecDAO.deleteAllConnectInfo(expId,expTaskOrder);
		
		//位置
		this.etpDAO.deleteAllPositionInfo(expId,expTaskOrder);
		
		//拓扑表
		String[] para = {"expId", ""+expId,"expTaskOrder",""+expTaskOrder};
		ExpTopo expTopo = (ExpTopo) eTopoDAO.getByNProperty(para);
		if(expTopo == null)
			return;
		eTopoDAO.delete(expTopo);
	}
}
