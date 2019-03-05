package virnet.management.dao;


import java.io.Serializable;
import java.util.List;

import virnet.management.basedao.BaseDAO;
import virnet.management.entity.Facilities;
import virnet.management.util.PageUtil;

public class FacilitiesDAO extends BaseDAO {
	 	public boolean add(Facilities obj) {
	        return super.add(obj);
	    }
	   
	 	public void delete(Facilities obj) {
	        super.delete(obj);
	    }

		public void deleteById(Serializable id) {
	        super.deleteById(Facilities.class, id);
	    }

	   
	    public boolean update(Facilities obj) {
	    	return super.update(obj);
	    }

	   
	    public Object get(Serializable id) {
	    	Object o = super.get(Facilities.class, id);
			return o;
	    }

	   
		public Object getByNProperty(String... strs) {
	    	Object o = super.getByNProperty(Facilities.class, strs);
			return o;
	    }

		public Object getUniqueByProperty(String pName, Object pValue) {
	    	Object o = super.getUniqueByProperty(Facilities.class, pName, pValue);
			return o;
	    }

	   
	    public Object getUniqueByHql(String hql) {
	    	Object o = super.getUniqueByHql(hql);
	    	return o;
	    }

		public Object getUniqueBySql(String sql) {
	    		Object o = super.getUniqueBySql(sql, Facilities.class);
	    	return o;
	    }

	    // ////////////////////��ѯ�������////////////////

	   
	    @SuppressWarnings({ "rawtypes" })
		public List getList() {
	    	List list = super.getList(Facilities.class);
	    	return list;
	    }

	   
	    @SuppressWarnings({ "rawtypes" })
		public List getListByProperty(String pName,
	            Object pValue) {
	    	List list = super.getListByProperty(Facilities.class, pName, pValue);
	    	return list;	    	
	    }

	   
	    @SuppressWarnings({ "rawtypes" })
		public List getListByProperty(String pName, Object pValue, String condition) {
	    	List list = super.getListByProperty(Facilities.class, pName, pValue, condition);
	    	return list;
	    }

	   
	    @SuppressWarnings({ "rawtypes" })
		public List getListByNProperty(String... strs) {
	    	List list = super.getListByNProperty(Facilities.class, strs);
	    	return list;
	    }

	   
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public List getListByHql(String hql) {
	    	List list = super.getListByHql(hql);
	    	return list;
	    }

	   
	    @SuppressWarnings("rawtypes")
		public List getListBySql(String sql) {
	    	List list = super.getListBySql(sql, Facilities.class);
	    	return list;
	    }

	   
		@SuppressWarnings("rawtypes")
		public void getListByPage(PageUtil pageUtil) {
	        super.getListByPage(Facilities.class, pageUtil);
	    }

		@SuppressWarnings("rawtypes")
		public void getListByPage(String hql, PageUtil pageUtil) {
			super.getListByPage(hql, pageUtil);
	    }
}