package virnet.experiment.assistantapi;

import virnet.experiment.combinedao.ExpVerifyCDAO;
import virnet.experiment.combinedao.ExpVerifyPingCDAO;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.combinedao.ResultVerifyCDAO;
import virnet.experiment.combinedao.ResultVerifyPingCDAO;
import net.sf.json.JSONObject;

public class savePingResultToDatabase {

	private String[] verifyResult;
	private String cabinet_num;
	private String expId;
	private String expTaskOrder;
	private String equipmentNumber;
	private String leftNUM_Str; // 左端设备序号串，“##”隔开
	private String rightNUM_Str; // 右端设备序号串，“##”隔开
	private String leftport_Str; // 左端设备端口序号串，“##”隔开
	private String rightport_Str; // 右端设备端口序号串，“##”隔开
	private String expRole;
	private String expCaseId;

	public savePingResultToDatabase(String[] verifyResult, String cabinet_num, String expId, String expTaskOrder,
			String equipmentNumber, String leftNUM_Str, String rightNUM_Str, String leftport_Str, String rightport_Str,
			String expRole, String expCaseId) {
		this.verifyResult = verifyResult;
		this.cabinet_num = cabinet_num;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
		this.equipmentNumber = equipmentNumber;
		this.leftNUM_Str = leftNUM_Str;
		this.rightNUM_Str = rightNUM_Str;
		this.leftport_Str = leftport_Str;
		this.rightport_Str = rightport_Str;
		this.expRole = expRole;
		this.expCaseId = expCaseId;
	}

	public boolean save(String[] pcip) {
		boolean success = true;
		if (verifyResult[0].equals("fail"))
			// 验证失败
			success = false;
		else {

			Integer VerifyId = 0;

			if (expRole.equals("GM")) { // 实验模板
				// 修改实验模板验证表
				ExpVerifyCDAO vcDAO = new ExpVerifyCDAO();
				VerifyId = vcDAO.edit(expId, expTaskOrder);

				if (VerifyId == 0)
					// 修改验证表失败
					success = false;
				else {
					// 删除相应的实验模板ping验证表
					ExpVerifyPingCDAO vpCDAO = new ExpVerifyPingCDAO();
					vpCDAO.delete(VerifyId);

					boolean write = vpCDAO.edit(VerifyId, equipmentNumber, pcip, verifyResult, leftNUM_Str,
							rightNUM_Str, leftport_Str, rightport_Str);
					if (write)
						success = true;
					else
						success = false;
				}
			}

			else if (expRole.equals("stu")) { // 学生实验结果

				// 获取实验结果任务ID
				ResultTaskCDAO taskcDAO = new ResultTaskCDAO();
				Integer resultTaskId = taskcDAO.getResultTaskId(this.expCaseId, expId, expTaskOrder);

				// 修改实验结果验证表
				ResultVerifyCDAO rvcDAO = new ResultVerifyCDAO();
				VerifyId = rvcDAO.edit(resultTaskId);

				if (VerifyId == 0)
					// 修改验证表失败
					success = false;
				else {
					// 删除相应的实验模板ping验证表
					ResultVerifyPingCDAO rvpCDAO = new ResultVerifyPingCDAO();
					rvpCDAO.delete(VerifyId);

					boolean write = rvpCDAO.edit(VerifyId, equipmentNumber, pcip, verifyResult, leftNUM_Str,
							rightNUM_Str, leftport_Str, rightport_Str);
					if (write)
						success = true;
					else
						success = false;
				}
			} else
				success = false;

		}

		return success;
	}
	

	public String[] getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(String[] verifyResult) {
		this.verifyResult = verifyResult;
	}

	public String getCabinet_num() {
		return cabinet_num;
	}

	public void setCabinet_num(String cabinet_num) {
		this.cabinet_num = cabinet_num;
	}

	public String getExpId() {
		return expId;
	}

	public void setExpId(String expId) {
		this.expId = expId;
	}

	public String getExpTaskOrder() {
		return expTaskOrder;
	}

	public void setExpTaskOrder(String expTaskOrder) {
		this.expTaskOrder = expTaskOrder;
	}

	public String getEquipmentNumber() {
		return equipmentNumber;
	}

	public void setEquipmentNumber(String equipmentNumber) {
		this.equipmentNumber = equipmentNumber;
	}

	public String getLeftNUM_Str() {
		return leftNUM_Str;
	}

	public void setLeftNUM_Str(String leftNUM_Str) {
		this.leftNUM_Str = leftNUM_Str;
	}

	public String getRightNUM_Str() {
		return rightNUM_Str;
	}

	public void setRightNUM_Str(String rightNUM_Str) {
		this.rightNUM_Str = rightNUM_Str;
	}

	public String getLeftport_Str() {
		return leftport_Str;
	}

	public void setLeftport_Str(String leftport_Str) {
		this.leftport_Str = leftport_Str;
	}

	public String getRightport_Str() {
		return rightport_Str;
	}

	public void setRightport_Str(String rightport_Str) {
		this.rightport_Str = rightport_Str;
	}

	public String getExpRole() {
		return expRole;
	}

	public void setExpRole(String expRole) {
		this.expRole = expRole;
	}
}
