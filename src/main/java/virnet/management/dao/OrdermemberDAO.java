package virnet.management.dao;

import java.io.Serializable;
import java.util.List;

import virnet.management.basedao.BaseDAO;
import virnet.management.entity.Ordermember;
//import virnet.management.util.PageUtil;

public class OrdermemberDAO extends BaseDAO{
	 public boolean add(Ordermember obj) {
        return super.add(obj);
    }  

   
    public void delete(Ordermember obj) {
        super.delete(obj);
    }

	public void deleteById(Serializable id) {
        super.deleteById(Ordermember.class, id);
    }

   
    public boolean update(Ordermember obj) {
    	return super.update(obj);
    }

   
    public Object get(Serializable id) {
    	Object o = super.get(Ordermember.class, id);
		return o;
    }

   
	public Object getByNProperty(String... strs) {
    	Object o = super.getByNProperty(Ordermember.class, strs);
		return o;
    }

	public Object getUniqueByProperty(String pName, Object pValue) {
    	Object o = super.getUniqueByProperty(Ordermember.class, pName, pValue);
		return o;
    }

   
    public Object getUniqueByHql(String hql) {
    	Object o = super.getUniqueByHql(hql);
    	return o;
    }

	public Object getUniqueBySql(String sql) {
    		Object o = super.getUniqueBySql(sql, Ordermember.class);
    	return o;
    }

    // ////////////////////查询单个完毕////////////////

   
    @SuppressWarnings({ "rawtypes" })
	public List getList() {
    	List list = super.getList(Ordermember.class);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName,
            Object pValue) {
    	List list = super.getListByProperty(Ordermember.class, pName, pValue);
    	return list;	    	
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName, Object pValue, String condition) {
    	List list = super.getListByProperty(Ordermember.class, pName, pValue, condition);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByNProperty(String... strs) {
    	List list = super.getListByNProperty(Ordermember.class, strs);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getListByHql(String hql) {
    	List list = super.getListByHql(hql);
    	return list;
    }

   
    @SuppressWarnings("rawtypes")
	public List getListBySql(String sql) {
    	List list = super.getListBySql(sql, Ordermember.class);
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

