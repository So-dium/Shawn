package virnet.management.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import virnet.management.basedao.BaseDAO;
import virnet.management.entity.Order;
//import virnet.management.util.PageUtil;
import virnet.management.util.HibernateSessionFactory;

public class OrderDAO extends BaseDAO{
	 public Integer add(Order obj) {
		 Session session = null;
	        Transaction tx = null;
	        try {
	            session = HibernateSessionFactory.getSessionFactory().openSession(); 
	            tx = session.beginTransaction();
	            session.save(obj);
	            tx.commit();
	            System.out.println("good");
	            return (Integer)session.getIdentifier(obj);

	        } catch (Exception e) {
	        	System.out.println("error:"+e);
	            if (tx != null) {
	                tx.rollback();
	            }
	            return 0;
	        } finally {
	            if (session != null) {
	                session.close();
	            }
	        }
    }  

   
    public void delete(Order obj) {
        super.delete(obj);
    }

	public void deleteById(Serializable id) {
        super.deleteById(Order.class, id);
    }

   
    public boolean update(Order obj) {
    	return super.update(obj);
    }

   
    public Object get(Serializable id) {
    	Object o = super.get(Order.class, id);
		return o;
    }

   
	public Object getByNProperty(String... strs) {
    	Object o = super.getByNProperty(Order.class, strs);
		return o;
    }

	public Object getUniqueByProperty(String pName, Object pValue) {
    	Object o = super.getUniqueByProperty(Order.class, pName, pValue);
		return o;
    }

   
    public Object getUniqueByHql(String hql) {
    	Object o = super.getUniqueByHql(hql);
    	return o;
    }

	public Object getUniqueBySql(String sql) {
    		Object o = super.getUniqueBySql(sql, Order.class);
    	return o;
    }

    // ////////////////////查询单个完毕////////////////

   
    @SuppressWarnings({ "rawtypes" })
	public List getList() {
    	List list = super.getList(Order.class);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName,
            Object pValue) {
    	List list = super.getListByProperty(Order.class, pName, pValue);
    	return list;	    	
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName, Object pValue, String condition) {
    	List list = super.getListByProperty(Order.class, pName, pValue, condition);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByNProperty(String... strs) {
    	List list = super.getListByNProperty(Order.class, strs);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getListByHql(String hql) {
    	List list = super.getListByHql(hql);
    	return list;
    }

   
    @SuppressWarnings("rawtypes")
	public List getListBySql(String sql) {
    	List list = super.getListBySql(sql, Order.class);
    	return list;
    }

   
//	@SuppressWarnings("rawtypes")
//	public void getListByPage(PageUtil pageUtil) {
//        super.getListByPage(Exp.class, pageUtil);
//    }
//
//	@SuppressWarnings("rawtypes")
//	public void getListByPage(String hql, PageUtil pageUtil) {
//		super.getListByPage(hql, pageUtil);
//    }
}

