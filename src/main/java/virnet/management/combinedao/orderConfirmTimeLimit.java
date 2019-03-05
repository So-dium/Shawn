package virnet.management.combinedao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import virnet.management.dao.OrderDAO;
import virnet.management.dao.OrdermemberDAO;
import virnet.management.entity.Order;
import virnet.management.entity.Ordermember;

public class orderConfirmTimeLimit extends Thread {
	
	private Integer orderId;
	private OrdermemberDAO omDAO = new OrdermemberDAO();
	private OrderDAO oDAO = new OrderDAO();
	private Integer delay = 5;
	
	public orderConfirmTimeLimit(Integer orderId){ 
			this.orderId = orderId;
	}
	@Override
	public void run() {
		
		long start = System.currentTimeMillis();

		try {
			Thread.sleep(this.delay*60*1000 + 5*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Order order = (Order) this.oDAO.getUniqueByProperty("orderId", orderId);
		String date = order.getOrderSetUpDate();
		String time = order.getOrderSetUpTime();
		
		if(time.equals(null))
			return;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date previousTime = null;
		try {
			System.out.println(date + " " + time);
			previousTime = sdf.parse(date + " " + time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date currentTime = new Date();
		
		if ((currentTime.getTime() - previousTime.getTime())/(1000*60) >= this.delay) {
			String hql = "select t1.ordermemberId " + " from Ordermember as t1 " + " where t1.ordermemberOrderId = "
					+ this.orderId + " and t1.ordermemberConfirmStatus = -1";
			List<Object> result = this.omDAO.getListByHql(hql);
			if (result.size() != 0) {
				//有成员超时未确认，即认为放弃
				List<Ordermember> omlist = this.omDAO.getListByProperty("ordermemberOrderId", this.orderId);
				for (Ordermember member : omlist) {
					member.setOrdermemberUserId(0);
					member.setOrdermemberConfirmStatus(0);
					this.omDAO.update(member);
				}
				OrderDAO oDAO = new OrderDAO();
				Order o = (Order) oDAO.getUniqueByProperty("orderId", this.orderId);
				o.setOrderSetUpDate("无");
				o.setOrderSetUpTime("无");
				o.setOrderSetUpUserId(0);
				o.setOrderStatus("无");
				oDAO.update(o);
			} 
		}
		long end = System.currentTimeMillis();
		System.out.println("线程等待：" + (end - start) + "ms");
	}
	
}