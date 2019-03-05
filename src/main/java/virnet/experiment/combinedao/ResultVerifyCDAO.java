package virnet.experiment.combinedao;

import virnet.experiment.dao.ResultVerifyDAO;
import virnet.experiment.entity.ResultVerify;

public class ResultVerifyCDAO {
	
	private ResultVerifyDAO rvDAO = new ResultVerifyDAO();
	
	//返回实验模板验证Id
	public Integer edit(Integer resultTaskId){    
		
		ResultVerify verify = (ResultVerify)this.rvDAO.getUniqueByProperty("resultTaskId",resultTaskId);
		
		//找不到该实验模板验证，即未设置过该实验模板对应任务的验证信息，任务号为0指的是初始验证
		if(verify==null){
			
			ResultVerify newverify = new ResultVerify();
			newverify.setResultTaskId(resultTaskId);
			
			//未确定验证类型
			newverify.setResultVerifyType(1);;
			
			if(this.rvDAO.add(newverify)){
				return newverify.getResultVerifyId();
			}
			else
				return 0;
		}
		else{             //已经存在该实验模板对应的任务的验证信息,则修改
			
			verify.setResultTaskId(resultTaskId);
			verify.setResultVerifyType(1);
			
			if(this.rvDAO.update(verify)){
				return verify.getResultVerifyId();
			}
			else
				return 0;
		}
	}
}
