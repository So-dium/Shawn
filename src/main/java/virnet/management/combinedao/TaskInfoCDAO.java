package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ExpTaskDAO;
import virnet.experiment.combinedao.ExpConfigCDAO;
import virnet.experiment.combinedao.ExpTopoCDAO;
import virnet.experiment.combinedao.ExpVerifyCDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.entity.Exp;
import virnet.management.entity.ExpTask;

public class TaskInfoCDAO {
	
	private ExpTaskDAO tDAO = new ExpTaskDAO();
//	private ExpTopoCDAO etcDAO = new ExpTopoCDAO();
//	private ExpConfigCDAO eccDAO = new ExpConfigCDAO();
//	private ExpVerifyCDAO evcDAO = new ExpVerifyCDAO();


	//通过实验名称查找所有任务信息
	public List<List<Map<String, Object>>> showTaskDetail(List<List<Map<String, Object>>> list,String expName,boolean isEdit){

		ExpDAO eDAO = new ExpDAO();

		@SuppressWarnings("unchecked")
		List<Exp> elist = eDAO.getListByProperty("expName", expName);
		//获得实验Id,以此查询该实验任务
		Integer expId=elist.get(0).getExpId();
		
		//获得该实验的所有任务信息
		@SuppressWarnings("unchecked")
		List<ExpTask> tlist=tDAO.getListByProperty("expId", expId);
		
		Integer i=0;
		while(i<tlist.size()){
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "任务序号");
		
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", tlist.get(i).getExpTaskOrder());
			map12.put("value", "expTaskOrder"+"#"+tlist.get(i).getExpTaskOrder());
			
			Map<String, Object> map13 = new HashMap<String, Object>();
			if(isEdit){
				map13.put("name", "删除");
				map13.put("class", "btn btn-new");
				map13.put("onclick", "deleteTaskOrder(" + expId + "," + tlist.get(i).getExpTaskOrder() + ",'"+ expName + "')" );
			}

			list1.add(map11);
			list1.add(map12);
			list1.add(map13);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "任务类型");
		
			//在value值中附上任务的次序，以区分不同任务的信息，格式为“数据类型#任务次序”
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", tlist.get(i).getExpTaskType());
			if(isEdit){
				map22.put("class", "btn btn-link edit");
				map22.put("onclick", "editable(this);");
				map22.put("value", "expTaskType"+"#"+tlist.get(i).getExpTaskOrder());
			}
			Map<String, Object> map23 = new HashMap<String, Object>();
			map23.put("name", "");
			
			list2.add(map21);
			list2.add(map22);
			list2.add(map23);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "任务内容");
		
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", tlist.get(i).getExpTaskContent());
			if(isEdit){
				map32.put("class", "btn btn-link edit");
				map32.put("onclick", "editable(this);");
				map32.put("value", "expTaskContent"+"#"+tlist.get(i).getExpTaskOrder());
			}
			Map<String, Object> map33 = new HashMap<String, Object>();
			map33.put("name", "");
			
			list3.add(map31);
			list3.add(map32);
			list3.add(map33);
			
			List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map41 = new HashMap<String, Object>();
			map41.put("name", "ping监测点");
			Map<String, Object> map42 = new HashMap<String, Object>();
			map42.put("name", tlist.get(i).getExpTaskPing());
			if(isEdit){
				map42.put("class", "btn btn-link edit");
				map42.put("onclick", "editable(this);");
				map42.put("value", "expTaskPing" + "#" + tlist.get(i).getExpTaskOrder());
				//System.out.println("expTaskPing!");
				//System.out.println(tlist.get(i).getExpTaskPing());
			}
			Map<String, Object> map43 = new HashMap<String, Object>();
			map43.put("name", "");
			list4.add(map41);
			list4.add(map42);
			list4.add(map43);
			
			List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map51 = new HashMap<String, Object>();
			map51.put("name", "拓扑图占分比");
			Map<String, Object> map52 = new HashMap<String, Object>();
			map52.put("name", tlist.get(i).getExpTaskTopoScore());
			if(isEdit){
				map52.put("class", "btn btn-link edit");
				map52.put("onclick", "editable(this);");
				map52.put("value", "expTaskTopoScore" + "#" + tlist.get(i).getExpTaskOrder());
				//System.out.println("expTaskPing!");
				//System.out.println(tlist.get(i).getExpTaskPing());
			}
			Map<String, Object> map53 = new HashMap<String, Object>();
			map53.put("name", "");
			list5.add(map51);
			list5.add(map52);
			list5.add(map53);
			
			List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map61 = new HashMap<String, Object>();
			map61.put("name", "PC配置占分比");
			Map<String, Object> map62 = new HashMap<String, Object>();
			map62.put("name", tlist.get(i).getExpTaskConfigScore());
			if(isEdit){
				map62.put("class", "btn btn-link edit");
				map62.put("onclick", "editable(this);");
				map62.put("value", "expTaskConfigScore" + "#" + tlist.get(i).getExpTaskOrder());
				//System.out.println("expTaskPing!");
				//System.out.println(tlist.get(i).getExpTaskPing());
			}
			Map<String, Object> map63 = new HashMap<String, Object>();
			map63.put("name", "");
			list6.add(map61);
			list6.add(map62);
			list6.add(map63);
			
			List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map71 = new HashMap<String, Object>();
			map71.put("name", "Ping检测占分比");
			Map<String, Object> map72 = new HashMap<String, Object>();
			map72.put("name", tlist.get(i).getExpTaskPingScore());
			if(isEdit){
				map72.put("class", "btn btn-link edit");
				map72.put("onclick", "editable(this);");
				map72.put("value", "expTaskPingScore" + "#" + tlist.get(i).getExpTaskOrder());
				//System.out.println("expTaskPing!");
				//System.out.println(tlist.get(i).getExpTaskPing());
			}
			Map<String, Object> map73 = new HashMap<String, Object>();
			map73.put("name", "");
			list7.add(map71);
			list7.add(map72);
			list7.add(map73);
			
			list.add(list1);
			list.add(list2);
			list.add(list3);
			list.add(list4);
			list.add(list5);
			list.add(list6);
			list.add(list7);
			
			i++;
		}
		return list;
	}
	
	public boolean save(Integer expId,String Key,Map<String, Object> map){
		//首先解析字符串Key，以#分割,k[0]为数据类型，k[1]为任务序号
		String[] k=Key.split("#");
		ExpTask task;		
		switch(k[0]){
			case "expTaskType" ://找到任务表中的对应行	
				String[] para1={"expId",""+expId,"expTaskOrder",k[1]};
				task = (ExpTask) this.tDAO.getByNProperty(para1); 
				task.setExpTaskType(Integer.parseInt((String) map.get(Key))); break;
			case "expTaskContent" : //找到任务表中的对应行	
				String[] para2={"expId",""+expId,"expTaskOrder",k[1]};
				task = (ExpTask) this.tDAO.getByNProperty(para2);
				task.setExpTaskContent((String)map.get(Key)); break;
			case "expTaskPing" : //找到任务表中的对应行	
				String[] para3={"expId",""+expId,"expTaskOrder",k[1]};
				task = (ExpTask) this.tDAO.getByNProperty(para3);
				task.setExpTaskPing((String)map.get(Key)); break;
			case "expTaskTopoScore" : //找到任务表中的对应行	
				String[] para4={"expId",""+expId,"expTaskOrder",k[1]};
				task = (ExpTask) this.tDAO.getByNProperty(para4);
				task.setExpTaskTopoScore(Integer.parseInt((String)map.get(Key))); break;	
			case "expTaskConfigScore" : //找到任务表中的对应行	
				String[] para5={"expId",""+expId,"expTaskOrder",k[1]};
				task = (ExpTask) this.tDAO.getByNProperty(para5);
				task.setExpTaskConfigScore(Integer.parseInt((String)map.get(Key))); break;
			case "expTaskPingScore" : //找到任务表中的对应行	
				String[] para6={"expId",""+expId,"expTaskOrder",k[1]};
				task = (ExpTask) this.tDAO.getByNProperty(para6);
				task.setExpTaskPingScore(Integer.parseInt((String)map.get(Key))); break;
			default : return true;
		}
		
		if(this.tDAO.update(task))
			return true;
		else
			return false;
	}
	public boolean addtask(Integer expId){
		//从1开始搜索未使用的任务号
		Integer i=1,taskOrder=1;
		while(i!=0){
			String[] para={"expId",""+expId,"expTaskOrder",""+i};
			ExpTask task = (ExpTask) this.tDAO.getByNProperty(para);
			//找不到该任务，即此任务次序号未占用
			if(task==null){
				taskOrder=i;
				i=-1;
			}
			i++;
		}
		ExpTask expTask;
		expTask = new ExpTask();
		expTask.setExpId(expId);
		expTask.setExpTaskOrder(taskOrder);
		if(this.tDAO.add(expTask))
			return true;
		else
			return false;
		
	}
	//任务个数
	public Integer taskNum(String expId){
		Integer EXPID = Integer.parseInt(expId);
		@SuppressWarnings("unchecked")
		List<ExpTask> tlist = this.tDAO.getListByProperty("expId", EXPID);
		System.out.println("任务个数是"+tlist.size());
		return tlist.size();
	}
	
	public void deleteAllTask(Integer expId){
		Integer i=1;
		while(true){
			String[] para={"expId",""+expId,"expTaskOrder",""+i};
			ExpTask task = (ExpTask) this.tDAO.getByNProperty(para);
			//找不到该任务，即此任务次序号未占用
			if(task==null){
				return;
			}else{
				this.deleteTask(expId, i);
			}
			i++;
		}
	}
	
	public void deleteTask(Integer expId,Integer expTaskOrder){
		System.out.println("aaa");
		String[] para={"expId",""+expId,"expTaskOrder",""+expTaskOrder};
		ExpTask task = (ExpTask) this.tDAO.getByNProperty(para);
		if(task == null){
			System.out.println("实验：" + expId + "任务：" + expTaskOrder + "该任务不存在");
		}
		else{
			try {
				ExpTopoCDAO etcDAO = new ExpTopoCDAO();
				ExpConfigCDAO eccDAO = new ExpConfigCDAO();
				ExpVerifyCDAO evcDAO = new ExpVerifyCDAO();
				etcDAO.deleteTopoInfo(expId, expTaskOrder);
				eccDAO.deleteConfigInfo(expId, expTaskOrder);
				evcDAO.deleteVerifyInfo(expId, expTaskOrder);
				this.tDAO.delete(task);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print("删除任务失败");
			}
		}	
	}
}
