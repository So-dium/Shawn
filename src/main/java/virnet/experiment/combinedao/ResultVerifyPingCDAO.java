package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ResultVerifyPingDAO;
import virnet.experiment.entity.ResultVerifyPing;

public class ResultVerifyPingCDAO {
	
	private ResultVerifyPingDAO rvpDAO = new ResultVerifyPingDAO();
	
	public void delete(Integer resultVerifyId){
		
		try {
			//删除原记录,如果原来没有记录，则list长度为0
			@SuppressWarnings("unchecked")
			List<ResultVerifyPing> Rvplist = this.rvpDAO.getListByProperty("resultVerifyId", resultVerifyId);
			int i=0;
			while(i!=Rvplist.size()){
				this.rvpDAO.delete(Rvplist.get(i));
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//逐个设备重写
	public boolean edit(Integer resultVerifyId,String equipmentNumber, String[] ipAddress , String[] verifyResult,
						String leftNUM_Str, String rightNUM_Str,String leftport_Str,String rightport_Str){
		
		//第一个PC序号
		Integer PCNumber =  Integer.parseInt(equipmentNumber) - 3;
		
		String leftNUM[] = leftNUM_Str.split("##");
		String rightNUM[] = rightNUM_Str.split("##");
		String leftport[] = leftport_Str.split("##");
		String rightport[] = rightport_Str.split("##");
		
		//获取PC设备连接的设备序号及端口序号,分别对应4个PC的连接
		Integer[] device =  new Integer[4];
		Integer[] port = new Integer[4];
		
		int i = 0;
		while(i < leftNUM.length){
			
			Integer left = Integer.parseInt(leftNUM[i]);
			Integer right = Integer.parseInt(rightNUM[i]);
			
			if( left >= PCNumber){             //左端是PC			
				device[left-PCNumber] = right;
				port[left-PCNumber] = Integer.parseInt(rightport[i]);
			}
			
			else if(right >= PCNumber){         //右侧是PC
				device[right-PCNumber] = left;
				port[right-PCNumber] = Integer.parseInt(leftport[i]);
			}
			i++;
		}
		
		boolean success = true;
		//verifyResult 中为  1-2 1-3 1-4 2-3 2-4 3-4  的顺序
		int j = 0, ptr = 1;
		i = 1;
		while(i<4){
			j=i+1;
			while(j<=4){
				
				ResultVerifyPing rvp= new ResultVerifyPing();
				
				rvp.setResultVerifyId(resultVerifyId);
				rvp.setSourcePCOrder(PCNumber + i -1);
				rvp.setSourcePCIp(ipAddress[i]);
				rvp.setSourcePCDeviceOrder(device[i - 1]);
				rvp.setSourcePCPortOrder(port[i - 1]);
				rvp.setDestPCOrder(PCNumber + j - 1);
				rvp.setDestPCIp(ipAddress[j]);
				rvp.setDestPCDeviceOrder(device[j - 1]);
				rvp.setDestPCPortOrder(port[j-1]);
				if(verifyResult[ptr].equals("connected"))
					rvp.setSuccessFlag(1);
				else if(verifyResult[ptr].equals("disconnected"))
					rvp.setSuccessFlag(0);
				else
					return false;
				
				if(this.rvpDAO.add(rvp) == false)
					success = false;
				j++;
				ptr++;
		   }
		   i++;
		}
		return success;
	}	
}

