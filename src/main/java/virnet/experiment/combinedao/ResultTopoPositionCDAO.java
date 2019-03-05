package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ResultTopoPositionDAO;
import virnet.experiment.entity.ResultTopoPosition;

public class ResultTopoPositionCDAO {
	
	private ResultTopoPositionDAO rtpDAO = new ResultTopoPositionDAO();
	
	public boolean edit(Integer resultTopoId,String position){
		//position字符串 设备间用逗号相隔    X，Y以空格相隔
		
		String grid[]=position.split(" |,");
		
		@SuppressWarnings("unchecked")
		List<ResultTopoPosition> plist = this.rtpDAO.getListByProperty("resultTopoId", resultTopoId); 
		//删除以往记录
		int i = 0;
		while(i!=plist.size()){
			this.rtpDAO.delete(plist.get(i));
			i++;
		}
		//重写
		i = 0;
		Integer deviceOrder = 1;
		boolean flag = true;
		boolean success = true;
		while(i<grid.length){
			
			ResultTopoPosition newposition = new ResultTopoPosition();
			newposition.setDeviceOrder(deviceOrder);
			newposition.setResultTopoId(resultTopoId);
			newposition.setGridX(Double.parseDouble(grid[i]));
			i++;
			newposition.setGridY(Double.parseDouble(grid[i]));
			i++;
			deviceOrder++;
			flag = this.rtpDAO.add(newposition);

			if(flag == false)
				success = false;
		}
		return success;
	}
//	public String defaultPosition(String name_Str){
//		String position = "";
//		String initial[] ={"14.814612985170868 67.11075821883657," , //RT
//						   "112.81461298517087 65.11075821883657," ,
//						   "228.54588190987232 68.24818194020466," , //SW3 
//						   "365.75548701569164 66.85362696124423," ,
//						   "496.54729338446475 64.66479299282383," , //SW2
//						   "613.1831313425671 65.22381485223474," ,
//						   "700.4325365461277 65.19924324582155," ,  //PC
//						   "745.3032420498927 65.0481252822043," ,
//						   "782.2896885512944 64.68780022070018," ,  
//						   "820.7405419568145 65.17354324294729,"   };
//		String device[] = name_Str.split("##");
//		int deviceNum[]={0,0,0,0};       //记录已分配位置的设备类型及其数量
//		int i = 0;
//		while(i < device.length ){
//			switch(device[i]){
//			case "RT" :position = position + initial[deviceNum[0]];
//					   deviceNum[0]++;
//					   break;
//			case "SW3" :position = position + initial[2+deviceNum[1]];
//			   			deviceNum[1]++;
//			   			break;
//			case "SW2" :position = position + initial[4+deviceNum[2]];
//			   			deviceNum[2]++;
//			   			break;
//			case "PC" :position = position + initial[6+deviceNum[3]];
//			   		   deviceNum[3]++;
//			   		   break;
//			}
//			i++;
//		}
//		//将末尾的逗号去除
//		return position.substring(0, position.length()-1);
//	}
	
	
	//从初始拓扑模板中取出位置信息/，返回各个设备的位置，参数为实验模板拓扑Id
	public String position(Integer resultTopoId){
		String position = "";
		int deviceOrder=1;
		while(true){
			String para[] ={"resultTopoId",""+resultTopoId,"deviceOrder",""+deviceOrder}; 
			ResultTopoPosition rtp = (ResultTopoPosition) this.rtpDAO.getByNProperty(para);
			//如果查询结果不存在，即遍历了所有设备
			if(rtp == null){
				if(deviceOrder == 1)
					//拓扑为空
					return position;
				else
					//将末尾的逗号去除
					return position.substring(0, position.length()-1);
			}
			position = position + rtp.getGridX() + " " + rtp.getGridY() + ",";
			deviceOrder++;
		}
		
	}
	
	
}
