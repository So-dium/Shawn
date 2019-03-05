package virnet.experiment.assistantapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import virnet.experiment.combinedao.ExpConnectCDAO;
import virnet.experiment.combinedao.ExpTopoCDAO;
import virnet.experiment.combinedao.ExpTopoPositionCDAO;
import virnet.experiment.combinedao.ResultConnectCDAO;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.combinedao.ResultTopoCDAO;
import virnet.experiment.combinedao.ResultTopoPositionCDAO;
import virnet.experiment.operationapi.FacilityConfigure;
import virnet.experiment.operationapi.NTCEdit;
import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.resourceapi.ResourceAllocate;

public class TopoSave {
													
	public String expId; 
	public String expTaskOrder;
	public String expCaseId;
	public String expRole;
	
	public TopoSave() {
		
	}
	public boolean save(String groupId,
						   ConcurrentHashMap<String, String> MapTopo,
				           ConcurrentHashMap<String, Integer> MapTaskOrder,
				           ConcurrentHashMap<String, String> MapExpId,
				           String expRole,
						   String expCaseId) {// 保存为任务标准拓扑，任务号为0时为初始拓扑
		
		boolean success = true;
		
		String[] equiporder = MapTopo.get(groupId).split("@");
		String position =  equiporder[0];
		String leftNUM_Str = equiporder[1]; // 左端设备序号串，“##”隔开
		String rightNUM_Str = equiporder[2]; // 右端设备序号串，“##”隔开
		String leftport_Str = equiporder[3]; // 左端设备端口序号串，“##”隔开
		String rightport_Str = equiporder[4]; // 右端设备端口序号串，“##”隔开
		
		expId = MapExpId.get(groupId);
		expTaskOrder = MapTaskOrder.get(groupId).toString();
		this.expRole = expRole;

		// 学生实验保存到结果表
		if (expRole.equals("stu")) {

			// 修改实验结果拓扑表
			ResultTaskCDAO taskcDAO = new ResultTaskCDAO();
			Integer resultTaskId = taskcDAO.getResultTaskId(expCaseId, expId, expTaskOrder);

			if (resultTaskId != 0) {
				ResultTopoCDAO rtcDAO = new ResultTopoCDAO();
				Integer resultTopoId = rtcDAO.edit(resultTaskId, leftNUM_Str);

				if (resultTopoId != 0) {

					// 修改实验结果连接表
					ResultConnectCDAO rcCDAO = new ResultConnectCDAO();
					boolean connectSuccess = rcCDAO.edit(resultTopoId, leftNUM_Str, rightNUM_Str, leftport_Str,
							rightport_Str);

					// 修改实验结果位置表
					ResultTopoPositionCDAO rtpcDAO = new ResultTopoPositionCDAO();
					boolean positionSuccess = rtpcDAO.edit(resultTopoId, position);

					if (connectSuccess && positionSuccess)
						success = true;
					else
						success = false;
				} else
					success = false;
			} else
				success = false;
		}

		// 管理员保存到模板表
		else if (expRole.equals("GM")) {

			// 修改实验模板拓扑表
			ExpTopoCDAO tcDAO = new ExpTopoCDAO();
			Integer ExpTopoId = tcDAO.edit(expId, expTaskOrder, leftNUM_Str);
			if (ExpTopoId != 0) {
				// 修改实验模板连接表
				ExpConnectCDAO cCDAO = new ExpConnectCDAO();
				boolean connectSuccess = cCDAO.edit(ExpTopoId, leftNUM_Str, rightNUM_Str, leftport_Str,
						rightport_Str);

				// 修改实验模板拓扑位置表
				ExpTopoPositionCDAO tpCDAO = new ExpTopoPositionCDAO();
				boolean positionSuccess = tpCDAO.edit(ExpTopoId, position);

				if (connectSuccess && positionSuccess)
					success = true;
				else
					success = false;
			} else {
				System.out.println("false");
				success = false;
			}
		} else
			success = false;
		return  success;
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
	public String getExpCaseId() {
		return expCaseId;
	}
	public void setExpCaseId(String expCaseId) {
		this.expCaseId = expCaseId;
	}
	public String getExpRole() {
		return expRole;
	}
	public void setExpRole(String expRole) {
		this.expRole = expRole;
	}
	
}
