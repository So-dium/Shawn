package virnet.management.dao;

import java.io.Serializable;
import java.util.List;

import virnet.management.basedao.BaseDAO;
import virnet.management.entity.UserCharacter;
import virnet.management.util.PageUtil;

public class UserCharacterDAO extends BaseDAO{
	 public boolean add(UserCharacter obj) {
        return super.add(obj);
    }

   
    public void delete(UserCharacter obj) {
        super.delete(obj);
    }

	public void deleteById(Serializable id) {
        super.deleteById(UserCharacter.class, id);
    }

   
    public void update(UserCharacter obj) {
    	super.update(obj);
    }

   
    public Object get(Serializable id) {
    	Object o = super.get(UserCharacter.class, id);
		return o;
    }

   
	public Object getByNProperty(String... strs) {
    	Object o = super.getByNProperty(UserCharacter.class, strs);
		return o;
    }

	public Object getUniqueByProperty(String pName, UserCharacter pValue) {
    	Object o = super.getUniqueByProperty(UserCharacter.class, pName, pValue);
		return o;
    }

   
    public Object getUniqueByHql(String hql) {
    	Object o = super.getUniqueByHql(hql);
    	return o;
    }

	public Object getUniqueBySql(String sql) {
    		Object o = super.getUniqueBySql(sql, UserCharacter.class);
    	return o;
    }

    // ////////////////////查询单个完毕////////////////

   
    @SuppressWarnings({ "rawtypes" })
	public List getList() {
    	List list = super.getList(UserCharacter.class);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName,
            Object pValue) {
    	List list = super.getListByProperty(UserCharacter.class, pName, pValue);
    	return list;	    	
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByProperty(String pName, UserCharacter pValue, String condition) {
    	List list = super.getListByProperty(UserCharacter.class, pName, pValue, condition);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes" })
	public List getListByNProperty(String... strs) {
    	List list = super.getList(UserCharacter.class);
    	return list;
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getListByHql(String hql) {
    	List list = super.getListByHql(hql);
    	return list;
    }

   
    @SuppressWarnings("rawtypes")
	public List getListBySql(String sql) {
    	List list = super.getListBySql(sql, UserCharacter.class);
    	return list;
    }

   
	@SuppressWarnings("rawtypes")
	public void getListByPage(PageUtil pageUtil) {
        super.getListByPage(UserCharacter.class, pageUtil);
    }

	@SuppressWarnings("rawtypes")
	public void getListByPage(String hql, PageUtil pageUtil) {
		super.getListByPage(hql, pageUtil);
    }
}

