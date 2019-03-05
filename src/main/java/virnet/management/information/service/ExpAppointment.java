package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.combinedao.OrderCDAO;
import virnet.management.dao.AppointmentDAO;
import virnet.management.entity.Appointment;
import virnet.management.entity.PhysicsMachines;
import virnet.management.util.PageUtil;


public class ExpAppointment implements InformationQuery{
	private AppointmentDAO appointmentDAO = new AppointmentDAO();
	private OrderCDAO ocDAO = new OrderCDAO();

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "预约编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> exp_case_id = new HashMap<String, String>();
		exp_case_id.put("name", "生成实验编号");
		exp_case_id.put("class", "");
		head.add(exp_case_id);
		
		Map<String, String> head_time_id = new HashMap<String, String>();
		head_time_id.put("name", "预约时间编号");
		head_time_id.put("class", "");
		head.add(head_time_id);
		
		Map<String, String> head_cabinet_id = new HashMap<String, String>();
		head_cabinet_id.put("name", "预约机柜编号");
		head_cabinet_id.put("class", "");
		head.add(head_cabinet_id);
		
		Map<String, String> head_group_id = new HashMap<String, String>();
		head_group_id.put("name", "预约小组编号");
		head_group_id.put("class", "");
		head.add(head_group_id);
		
		Map<String, String> head_create_time_id = new HashMap<String, String>();
		head_create_time_id.put("name", "预约生成时间");
		head_create_time_id.put("class", "");
		head.add(head_create_time_id);
		
		Map<String, String> head_exp_name = new HashMap<String, String>();
		head_exp_name.put("name", "预约实验名称");
		head_exp_name.put("class", "");
		head.add(head_exp_name);
		
		list.add(head);
		
		PageUtil<Appointment> pageUtil = new PageUtil<Appointment>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		List<Appointment> Appointmentlist = new ArrayList<Appointment>();
		int recordSize = appointmentDAO.getList().size();
		System.out.println("记录数："+recordSize);
		if (recordSize > 0) {
			this.appointmentDAO.getListByPage(pageUtil);
			Appointmentlist = pageUtil.getList();
		}

		int size = Appointmentlist.size();
		System.out.println("Course list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> AppointmentInfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", Appointmentlist.get(i).getAppointmentId() + "");//此处getAppointmentTimeId()返回的是Interger类型，通过加上""转换为String类型
			map_id.put("class", "App_Id");
			AppointmentInfo.add(map_id);
			
			Map<String, String> map_case_id = new HashMap<String, String>();
			map_case_id.put("name", Appointmentlist.get(i).getExpCaseId() + "");
			map_case_id.put("class", "App_Exp_Id");
			AppointmentInfo.add(map_case_id);
			
			Map<String, String> map_time_id = new HashMap<String, String>();
			map_time_id.put("name", ocDAO.time_show(Appointmentlist.get(i).getAppointmentTimeId()) + "");
			map_time_id.put("class", "");
			AppointmentInfo.add(map_time_id);
			
			Map<String, String> map_cabinet_id = new HashMap<String, String>();
			map_cabinet_id.put("name", Appointmentlist.get(i).getAppointmentCabinetId() + "");
			map_cabinet_id.put("class", "");
			AppointmentInfo.add(map_cabinet_id);
			
			Map<String, String> map_group_id = new HashMap<String, String>();
			map_group_id.put("name", Appointmentlist.get(i).getAppointmentGroupId() + "");
			map_group_id.put("class", "");
			AppointmentInfo.add(map_group_id);
			
			Map<String, String> map_create_time = new HashMap<String, String>();
			map_create_time.put("name", Appointmentlist.get(i).getAppointmentCreateTime() + "");
			map_create_time.put("class", "");
			AppointmentInfo.add(map_create_time);
			
			Map<String, String> map_exp_name = new HashMap<String, String>();
			map_exp_name.put("name", Appointmentlist.get(i).getAppointmentExpName() + "");
			map_exp_name.put("class", "");
			AppointmentInfo.add(map_exp_name);
			
			System.out.println("index : " + i + ", Appointment time  : " + Appointmentlist.get(i).getAppointmentTimeId() + ", Appointment cabinet : " + Appointmentlist.get(i).getAppointmentCabinetId());
			
			list.add(AppointmentInfo);
		}
		
		int total = this.appointmentDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增预约");
		button.put("class", "btn button-new");
		button.put("click", "addContent('exp-appointment');");
		
		Map<Object, Object> delete_button = new HashMap<Object, Object>();
		delete_button.put("content", "删除预约");
		delete_button.put("class", "btn button-new");
		delete_button.put("click", "create_delete_form('App_Id');");
		
		map.put("button_new", button);
		map.put("button_delete",delete_button);
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}

}
