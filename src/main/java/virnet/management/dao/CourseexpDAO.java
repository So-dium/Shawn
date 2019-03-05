package virnet.management.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import virnet.management.basedao.BaseDAO;
import virnet.management.entity.Courseexp;
import virnet.management.util.HibernateSessionFactory;
import virnet.management.util.PageUtil;

public class CourseexpDAO extends BaseDAO{
	 public void add(Courseexp obj) {
        super.add(obj);
    }

   
    public void delete(Courseexp obj) {
        super.delete(obj);
    }

	public void deleteById(Serializable id) {
        super.deleteById(Courseexp.class, id);
    }

   
    public boolean update(Courseexp obj) {
    	return super.update(obj);
    }
    
    public boolean update(List<Courseexp> dlist, List<Courseexp> ilist){
    	Session session = null;
        Transaction tx = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            tx = session.beginTransaction();
            int size = dlist.size();
            for(int i = 0; i < size; i++){
            	session.delete(dlist.get(i));
            }
            size = ilist.size();
            for(int i = 0; i < size; i++){
            	session.save(ilist.get(i));
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    public Object get(Serializable id) {
    	Object o = super.get(Courseexp.class, id);
		return o;
    }

   
	public Object getByNProperty(String... strs) {
    	Object o = super.getByNProperty(Courseexp.class, strs);
		return o;
    }

	public Object getUniqueByProperty(String pName, Object pValue) {
    	Object o = super.getUniqueByProperty(Courseexp.class, pName, pValue);
		return o;
    }

   
    public Object getUniqueByHql(String hql) {
    	Object o = super.getUniqueByHql(hql);
    	return o;
    }

	public Object getUniqueBySql(String sql) {
    		Object o = super.getUniqueBySql(sql, Courseexp.class);
    	return o;
    }

    // ////////////////////查询单个完毕////////////////

   
    @SuppressWarnings({ "rawtypes" })
	public List getList() {
    	List list = super.getList(Courseexp.class);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName,
            Object pValue) {
    	List list = super.getListByProperty(Courseexp.class, pName, pValue);
    	return list;	    	
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName, Object pValue, String condition) {
    	List list = super.getListByProperty(Courseexp.class, pName, pValue, condition);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByNProperty(String... strs) {
    	List list = super.getListByNProperty(Courseexp.class, strs);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getListByHql(String hql) {
    	List list = super.getListByHql(hql);
    	return list;
    }

   
    @SuppressWarnings("rawtypes")
	public List getListBySql(String sql) {
    	List list = super.getListBySql(sql, Courseexp.class);
    	return list;
    }

   
	@SuppressWarnings("rawtypes")
	public void getListByPage(PageUtil pageUtil) {
        super.getListByPage(Courseexp.class, pageUtil);
    }

	@SuppressWarnings("rawtypes")
	public void getListByPage(String hql, PageUtil pageUtil) {
		super.getListByPage(hql, pageUtil);
    }
}

