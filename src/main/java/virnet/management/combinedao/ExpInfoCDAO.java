package virnet.management.combinedao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;

import virnet.management.dao.CabinetTempletDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.OrderDAO;
import virnet.management.entity.Exp;
import virnet.management.entity.Order;
import virnet.management.entity.CabinetTemplet;
import virnet.management.util.DateUtil;
import virnet.management.util.ViewUtil;
import virnet.management.combinedao.TaskInfoCDAO;
import virnet.management.combinedao.CabinetTempletDeviceInfoCDAO;

public class ExpInfoCDAO {
	
	private ExpDAO eDAO = new ExpDAO();
	private ViewUtil vutil = new ViewUtil();
	private Integer maxExpStuNum= 3;
	
	@SuppressWarnings("unchecked")
	public String getExpName(int id){
		List<Exp> elist = this.eDAO.getListByProperty("expId", id);
		if(elist.isEmpty() || elist.size() > 1){
			return "no such experiment";
		}
		else{
			return elist.get(0).getExpName();
		}
	}
	
	public Exp getExp(String name){
		return (Exp) this.eDAO.getUniqueByProperty("expName", name);
	}
	
 	public Map<String, Object> showExpDetail(String id, String name, String orderId) throws ParseException{
 		
 		
 		//name 是实验名 id是操作区分符
 		Map<String, Object> map = new HashMap<String, Object>();
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "实验模板 <i class='icon-double-angle-right'></i> " + name);		
		
		String returnid = id;
		
		switch(id){
		case "time-management" :
		case "class-management" :
		case "course-management" :
		case "exp-management" : Map<String, Object> button = new HashMap<String, Object>();
								button.put("content", "修改实验内容及任务");
								button.put("class", "btn button-new");
								button.put("click", "editContent();");
								list = expManagement(name,id, false , orderId); 
								map.put("button", button);
								returnid = "exp-management";
								break;//管理员的实验管理 --- 返回实验模板， 包括进入第二层
		case "exp-arrangement" : list = expArrangement(name); break;//教师的排课 --- 查看到实验模板
		case "my-exp" :list = expManagement(name,id, false,orderId);
		}
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("id", returnid);
		
		return map;
	}
	
 	/**
 	 * 
 	 * 在管理员的权限中查看实验内容，并且进入实验桌面，修改配置
 	 * @param name the name of experiment want to query
 	 * @param isEdit check whether the list is for edit page
 	 * 
 	 * @return a list contains experiment name, standard time, profile and instruction, enter experiment 
 	 *  in the format of map
 	 * @throws ParseException 
 	 */
	public boolean time_compare(List<Object> time) throws ParseException
	{	
		DateUtil dateutil = new DateUtil();
		String[] timeString = dateutil.TimeFormat(time).split("~");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date begin_date = df.parse( timeString[0] + ":00");
		Date end_date = df.parse( timeString[1] + ":00");
		System.out.println(begin_date);
		System.out.println(end_date);
		if(begin_date.before(new Date())&&end_date.after(new Date()))
		{
			return true;
		}
		else{return false;}
	}
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> expManagement(String name,String id, boolean isEdit ,String orderId) throws ParseException{
		
		Map<String,Object> session = ActionContext.getContext().getSession();
		Integer user = Integer.parseInt((String)session.get("User"));
		
		//session 赋上实验Id
		Exp exp = (Exp)this.eDAO.getUniqueByProperty("expName", name);
		Integer expId = exp.getExpId();
		if(expId == null){
			System.out.println("expId查询为空");
		}
		ActionContext.getContext().getSession().put("expId", expId);
		
		//学生做实验
		if(id.equals("my-exp")){
			
			//session 赋上实验角色    实验平台是利用预约Id进行排队的
			ActionContext.getContext().getSession().put("expRole", "stu");
			ActionContext.getContext().getSession().put("workgroup", orderId);
			System.out.println("排队依据是预约号："+ orderId);
		}
		
		//管理员制作实验模板
		else if(id.equals("exp-management")){
			// session赋上实验角色 组号 实验实例号(无用)
			ActionContext.getContext().getSession().put("expRole", "GM");
			ActionContext.getContext().getSession().put("workgroup", 0);
		}
		else{
			System.out.println("illeage role");
		}
		
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		//实验不能重名
		List<Exp> elist = this.eDAO.getListByProperty("expName", name);  
		
		if(elist.isEmpty() || elist.size() > 1){
			//error
			list.clear();
			
			return list;
		}
		else{
			//get the experiment template
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "实验名称");
			
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", elist.get(0).getExpName());
			if(isEdit){
				map12.put("class", "btn btn-link edit");
				map12.put("onclick", "editable(this);");
				map12.put("value", "expName");
			}
			Map<String, Object> map13 = new HashMap<String, Object>();
			map13.put("name", "");
			
			
			list1.add(map11);
			list1.add(map12);
			list1.add(map13);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "标准实验时间");
			
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", elist.get(0).getExpStanTime());
			if(isEdit){
				map22.put("class", "btn btn-link edit");
				map22.put("onclick", "editable(this);");
				map22.put("value", "expStanTime");
			}
			Map<String, Object> map23 = new HashMap<String, Object>();
			map23.put("name", "");
			
			list2.add(map21);
			list2.add(map22);
			list2.add(map23);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "实验简介");
			
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", elist.get(0).getExpProfile());
			if(isEdit){
				map32.put("class", "btn btn-link edit");
				map32.put("onclick", "editable(this);");
				map32.put("value", "expProfile");
			}
			Map<String, Object> map33 = new HashMap<String, Object>();
			map33.put("name", "");
			
			list3.add(map31);
			list3.add(map32);
			list3.add(map33);
			
			List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map41 = new HashMap<String, Object>();
			map41.put("name", "实验指导");
			
			Map<String, Object> map42 = new HashMap<String, Object>();
			map42.put("name", elist.get(0).getExpInstruct());
			if(isEdit){
				map42.put("class", "btn btn-link a edit");
				map42.put("onclick", "editable(this);");
				map42.put("value", "expInstruct");
			}
			Map<String, Object> map43 = new HashMap<String, Object>();
			map43.put("name", "");
			
			list4.add(map41);
			list4.add(map42);
			list4.add(map43);
			
			List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map51 = new HashMap<String, Object>();
			map51.put("name", "实验类型编码");
			
			Map<String, Object> map52 = new HashMap<String, Object>();
			map52.put("name", elist.get(0).getExpType());
			if(isEdit){
				map52.put("class", "btn btn-link edit");
				map52.put("onclick", "editable(this);");
				map52.put("value", "expType");
			}
			Map<String, Object> map53 = new HashMap<String, Object>();
			map53.put("name", "");
			
			list5.add(map51);
			list5.add(map52);
			list5.add(map53);
			
			list.add(list1);
			list.add(list2);
			list.add(list3);
			list.add(list4);
			list.add(list5);
			
			//插入设备信息
			CabinetTempletDeviceInfoCDAO ctdDAO =new CabinetTempletDeviceInfoCDAO();
			List<List<Map<String, Object>>> devicelist=ctdDAO.showTaskDetail(list, name,isEdit);
			//插入设备信息完毕
			
			//插入实验机柜模板信息
			CabinetTempletInfoCDAO ctDAO =new CabinetTempletInfoCDAO();
			List<List<Map<String, Object>>> cabinetTempletlist= ctDAO.showTaskDetail(devicelist, name,isEdit);
			//插入实验机柜模板信息
			
			//插入任务的信息
			TaskInfoCDAO tDAO = new TaskInfoCDAO();

			List<List<Map<String, Object>>> tasklist=tDAO.showTaskDetail(cabinetTempletlist, name,isEdit);
			//插入任务信息完毕
			
			if(!isEdit){
				List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
			
				Map<String, Object> map61 = new HashMap<String, Object>();
				map61.put("name", "进入实验桌面");
				
				if(id.equals("my-exp")){
					
					//获取进入实验的允许时间
					String hql = "select t2.periodarrangeStartDate, t2.periodarrangeStartTime , t2.periodarrangeEndDate , t2.periodarrangeEndTime "
							+ " from Periodarrange as t2 , Classarrange as t3 "
							+ "where t2.periodarrangeId = t3.classarrangePeriodArrangeId "
							+ " and t3.classarrangeExpArrangeId = "
								+ "( select t4.orderExpArrangeId "
								+ "  from Order as t4 "
								+ "  where t4.orderId = " + orderId + ")";
					
					System.out.println(hql);
					
					OrderDAO oDAO = new OrderDAO();
					List<Object>  order = oDAO.getListByHql(hql);
					
					Order o = (Order) oDAO.getUniqueByProperty("orderId", orderId);
					String status = o.getOrderStatus();
					
					if(id.equals("my-exp") && 
						((time_compare(order) == false) || 
						 status.equals("实验结束")
						)
					){//
					
						Map<String, Object> map62 = new HashMap<String, Object>();
						map62.put("name", "<i class='icon-arrow-right'></i>");
						map62.put("onclick", "");
						map62.put("class", "btn btn-false");
					
						Map<String, Object> map63 = new HashMap<String, Object>();
						map63.put("name", "");
					
						list6.add(map61);
						list6.add(map62);
						list6.add(map63);
					}
					else{
							Map<String, Object> map62 = new HashMap<String, Object>();
							map62.put("name", "<i class='icon-arrow-right'></i>");
							map62.put("onclick", "JumpToExpPlatform();");
							map62.put("class", "btn btn-new");
								
							Map<String, Object> map63 = new HashMap<String, Object>();
							map63.put("name", "");
								
							list6.add(map61);
							list6.add(map62);
							list6.add(map63);
					}
					List<Map<String, Object>> selectlist = new ArrayList<Map<String, Object>>();
					
					for(int i=1;i<=maxExpStuNum;i++){
						Map<String, Object> option = new HashMap<String, Object>();
						option.put("name", i);
						option.put("value", i);
						selectlist.add(option);
					}
					
					List<Map<String, Object>> Rtlist = this.vutil.createList("本次实验人数", "", "", selectlist, "singleselect", "", "realExpStuNum",maxExpStuNum);
					tasklist.add(Rtlist);
				}
				else if(id.equals("exp-management"))
				{
					Map<String, Object> map62 = new HashMap<String, Object>();
					map62.put("name", "<i class='icon-arrow-right'></i>");
					map62.put("onclick", "JumpToExpPlatform();");
					map62.put("class", "btn btn-new");
					
					Map<String, Object> map63 = new HashMap<String, Object>();
					map63.put("name", "");
					
					list6.add(map61);
					list6.add(map62);
					list6.add(map63);
					
                    List<Map<String, Object>> selectlist = new ArrayList<Map<String, Object>>();
					
					for(int i=1;i<=maxExpStuNum;i++){
						Map<String, Object> option = new HashMap<String, Object>();
						option.put("name", i);
						option.put("value", i);
						selectlist.add(option);
					}
					
					List<Map<String, Object>> Rtlist = this.vutil.createList("本次实验人数", "", "", selectlist, "singleselect", "", "realExpStuNum",maxExpStuNum);
					tasklist.add(Rtlist);
				}
				
			
				tasklist.add(list6);
			}
			
			return tasklist;
		}
	}
	
	/**
	 * 在教师的课时排实验时，查看实验内容
	 * 
	 * @param name the name of experiment want to query
	 * 
	 * @return a list contains experiment name, standard time, profile and instruction 
 	 *  in the format of map
 	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> expArrangement(String name){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		//实验不能重名
		List<Exp> elist = this.eDAO.getListByProperty("expName", name);
		
		if(elist.isEmpty() || elist.size() > 1){
			//error
			list.clear();
		}
		else{
			//get the experiment template
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "实验名称");
			
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", elist.get(0).getExpName());
			
			list1.add(map11);
			list1.add(map12);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "标准实验时间");
			
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", elist.get(0).getExpStanTime());
			
			list2.add(map21);
			list2.add(map22);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "实验简介");
			
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", elist.get(0).getExpProfile());
			
			list3.add(map31);
			list3.add(map32);
			
			List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map41 = new HashMap<String, Object>();
			map41.put("name", "实验指导");
			
			Map<String, Object> map42 = new HashMap<String, Object>();
			map42.put("name", elist.get(0).getExpInstruct());
			
			list4.add(map41);
			list4.add(map42);
			
			List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map51 = new HashMap<String, Object>();
			map51.put("name", "实验类型编码");
			
			Map<String, Object> map52 = new HashMap<String, Object>();
			map52.put("name", elist.get(0).getExpInstruct());
			
			list5.add(map51);
			list5.add(map52);
			
			list.add(list1);
			list.add(list2);
			list.add(list3);
			list.add(list4);
			list.add(list5);
		}
		
		return list;
	}

	/**
	 * 生成编辑实验界面的数据
	 * @param name the name of experiment want to query
	 * @return the data show on the page
	 * @throws ParseException 
	 */
	public Map<String, Object> Edit(String name) throws ParseException{
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "实验模板 <i class='icon-double-angle-right'></i> " + name);
		
		list = expManagement(name,"exp-management", true ," ");
		
		Map<Object, Object> button_newTask = new HashMap<Object, Object>();
		button_newTask.put("content", "新增任务");
		button_newTask.put("class", "btn button-new");
		button_newTask.put("click", "addtask()");
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
	    
		map.put("button_newTask", button_newTask);
		map.put("button", button);
		map.put("tittle", tittle);
		map.put("data", list);
		
		return map;
	}
	
	public Map<String, Object> Add(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "实验模板 <i class='icon-double-angle-right'></i> 新增实验");
		
		List<Map<String, Object>> ename = this.vutil.createList("实验名称", "", "", "", "btn btn-link edit", "editable(this);", "expName",0);
		List<Map<String, Object>> etime = this.vutil.createList("实验标准时间", "", "", "", "btn btn-link edit", "editable(this);", "expStanTime",0);
		List<Map<String, Object>> eprofile = this.vutil.createList("实验简介", "", "", "", "btn btn-link edit", "editable(this);", "expProfile",0);
		List<Map<String, Object>> einstruct = this.vutil.createList("实验指导", "", "", "", "btn btn-link edit", "editable(this);", "expInstruct",0);
		List<Map<String, Object>> etype = this.vutil.createList("标准实验类型", "", "", "", "btn btn-link edit", "editable(this);", "expType",0);
		
		List<Map<String, Object>> selectlist = new ArrayList<Map<String, Object>>();
		for(int i=0;i<3;i++){
			Map<String, Object> option = new HashMap<String, Object>();
			option.put("name", i);
			option.put("value", i);
			selectlist.add(option);
		}
		
		List<Map<String, Object>> Rt = this.vutil.createList("路由器数量", "", "", selectlist, "singleselect", "", "Rt",0);
		List<Map<String, Object>> Sw2 = this.vutil.createList("三层交换机数量",  "", "", selectlist, "singleselect", "",  "Sw3",0);
		List<Map<String, Object>> Sw3 = this.vutil.createList("二层交换机数量",  "", "", selectlist, "singleselect", "",  "Sw2",0);
		
		List<Map<String, Object>> Limit = this.vutil.createList("约束条件", "", "", "", "btn btn-link edit", "editable(this);", "Limit",0);
		List<Map<String, Object>> Remark = this.vutil.createList("备注", "", "", "", "btn btn-link edit", "editable(this);", "Remark",0);
		
		list.add(ename);
		list.add(etime);
		list.add(eprofile);
		list.add(einstruct);
		list.add(etype);
		list.add(Rt);
		list.add(Sw2);
		list.add(Sw3);
		list.add(Limit);
		list.add(Remark);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "保存实验");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}
	
	public Map<String, Object> save(String name, Map<String, Object> map){
		Map<String, Object> r = new HashMap<String, Object>();
		
		//处理实验机柜信息
		CabinetTempletDAO ctDAO =new CabinetTempletDAO();
		Exp exp;
		CabinetTemplet cabinetTemplet;
		String expName = (String) map.get("expName");
		if(name.equals("")){
			
			//实验名称不能重复
			Exp  duplicate = (Exp)this.eDAO.getUniqueByProperty("expName", expName);
			if(duplicate != null){
				r.put("isSuccess", false);
				r.put("returndata", "实验名已存在");
				return r;
			}
			
			exp = new Exp();                        //新建一个实验模板
			cabinetTemplet = new CabinetTemplet();	//新建一个实验机柜模板
		}
		else{
			
			Exp  duplicate = (Exp)this.eDAO.getUniqueByProperty("expName", expName);
			if(duplicate != null && !expName.equals(name)){
				r.put("isSuccess", false);
				r.put("returndata", "实验名已存在");
				return r;
			}
			
			
			exp = (Exp) this.eDAO.getUniqueByProperty("expName", name);
			cabinetTemplet = (CabinetTemplet) ctDAO.getUniqueByProperty("cabinetTempletName", name);
		}
		
		//处理设备信息
		CabinetTempletDeviceInfoCDAO ctdDAO = new CabinetTempletDeviceInfoCDAO();
		Map<String, Object> deviceMap = new HashMap<String, Object>();
		
		//处理任务信息
		TaskInfoCDAO tDAO = new TaskInfoCDAO();    
		
		Set<String> key = map.keySet();
		Iterator<String> keylist = key.iterator();
		boolean success=true;
		while(keylist.hasNext()){
			String k = keylist.next();
			System.out.println(k);
			boolean flag=true;			
			switch(k){
			case "expName" : String Name=(String) map.get(k);
							 exp.setExpName(Name); 
							 cabinetTemplet.setCabinetTempletName(Name);
							 break;
			case "expStanTime" : exp.setExpStanTime((String) map.get(k)); break;
			case "expProfile" : exp.setExpProfile((String) map.get(k)); break;
			case "expInstruct" : exp.setExpInstruct((String) map.get(k)); break;
			case "expType" : exp.setExpType((String) map.get(k)); break;
			//将每一个设备的数量记录下来，在下面在统计更改实验机柜设备表
			case "Rt" :  System.out.println(map.get(k).getClass());deviceMap.put("Rt",Integer.parseInt(((String[])map.get(k))[0]));break;
			case "Sw3" : deviceMap.put("Sw3",Integer.parseInt(((String[])map.get(k))[0]));break;
			case "Sw2" : deviceMap.put("Sw2",Integer.parseInt(((String[])map.get(k))[0]));break;
			//
			case "Limit" :cabinetTemplet.setCabinetTempletLimit((String) map.get(k)); break;
			case "Remark" :cabinetTemplet.setCabinetTempletRemark((String) map.get(k)); break;
			//若上述均不能匹配，说明为任务信息，应存到任务表
			default :  flag=tDAO.save(exp.getExpId(), k, map);break;
			}
			if(flag==false)
				success=false;
		}
		
		if(name.equals("")){
			//实验模板表和实验机柜模板表都新建完成，将机柜模板Id回填到实验模板表，并更改实验机柜设备表
			if(this.eDAO.add(exp)&&ctDAO.add(cabinetTemplet)&&success){   

				Integer cabinetTempletId = cabinetTemplet.getCabinetTempletId();
				exp.setExpCabinetTempletId(cabinetTempletId);    //回填
				
				boolean deviceUpdate = ctdDAO.save(deviceMap,cabinetTempletId);
				
				if(this.eDAO.update(exp)&&deviceUpdate){
					r.put("isSuccess", true);
					r.put("name", map.get("expName"));
					r.put("key", "exp");
				}
				else{
					r.put("isSuccess", false);
					r.put("returndata", "设备模板新增失败");
				}		
			}
			else{
				r.put("isSuccess", false);
				r.put("returndata", "实验模板新增失败");
			}
		}
		else{
			if(this.eDAO.update(exp)&&ctDAO.update(cabinetTemplet)&&success){
				
				boolean deviceUpdate = ctdDAO.save(deviceMap,cabinetTemplet.getCabinetTempletId());
				
				if(deviceUpdate){
					r.put("isSuccess", true);
					r.put("name", map.get("expName"));
					r.put("key", "exp");
				}
				else
					r.put("isSuccess", false);
					r.put("returndata", "实验设备模板更新失败");
				
			}
			else{
				r.put("isSuccess", false);
				r.put("returndata", "实验模板更新失败");
			}
		}
		
		return r;
	}
	
	public Map<String, Object> addtask(String name) throws ParseException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Exp exp;

		if(name.equals("")){
			exp = new Exp();
		}
		else{
			exp = (Exp) this.eDAO.getUniqueByProperty("expName", name);
		}

		TaskInfoCDAO tDAO = new TaskInfoCDAO();
		boolean flag=tDAO.addtask(exp.getExpId());
		
		if(flag==true){
			
			map=Edit(name);
			map.put("isSuccess", true);
		}
		else{
			map=Edit(name);
			map.put("isSuccess", false);
		}

		return map;
	
	}
	
	public Map<String, Object> deleteExp(String expName){
		
		Exp exp = (Exp) this.eDAO.getUniqueByProperty("expName", expName);
		if(exp == null)
			return null;
		Integer CabinetTempletId  = exp.getExpCabinetTempletId();
		
		//删除实验模板设备表
		CabinetTempletDeviceInfoCDAO ctdDAO = new CabinetTempletDeviceInfoCDAO();
		ctdDAO.deleteEquipment(CabinetTempletId);
		
		//删除实验模板表
		CabinetTempletDAO ctDAO =new CabinetTempletDAO();
		CabinetTemplet cabinetTemplet = (CabinetTemplet) ctDAO.getUniqueByProperty("cabinetTempletId", CabinetTempletId);
		ctDAO.delete(cabinetTemplet);
		
		//删除任务表（将该任务下的拓扑、配置、ping信息都一起删除了）
		TaskInfoCDAO tDAO = new TaskInfoCDAO(); 
		tDAO.deleteAllTask(exp.getExpId());
		
		this.eDAO.delete(exp);
	
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Exp> getAllExp(){
		List<Exp> elist = this.eDAO.getList();
		
		return elist;
	}
	
}
