package virnet.experiment.resourceapi;

import java.util.List;
import java.util.Random;

import virnet.management.dao.PhysicsMachinesDAO;
import virnet.management.entity.PhysicsMachines;

public class CabinetResource {
	
	private PhysicsMachinesDAO pDAO = new PhysicsMachinesDAO();
	
	public synchronized String allocate(){

		try {
			@SuppressWarnings("unchecked")
			List<PhysicsMachines> freeCabinet = pDAO.getListByProperty("status", "0");
			
			if(freeCabinet.size() == 0)
				return null;
			
			PhysicsMachines cabinet = freeCabinet.get(0);
			String ip = cabinet.getphysicsMachinesIp();
			
			System.out.println("机柜ip："+ip);
			cabinet.setStatus(1);
			if(pDAO.update(cabinet)){
				return ip;
			}
			else
				return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		//return "172.17.0.64"; //"";//0604： 202.112.113.135
	}
	
	public boolean release(String CabinetIp){

		PhysicsMachines Cabinet = (PhysicsMachines) pDAO.getUniqueByProperty("physicsMachinesIp", CabinetIp);
		Cabinet.setStatus(0);
		if(pDAO.update(Cabinet)){
			return true;
		}
		else
			return false;
		
	}
	
	
}
