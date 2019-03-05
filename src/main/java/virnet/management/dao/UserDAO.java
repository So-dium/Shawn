package virnet.management.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import virnet.management.basedao.BaseDAO;
import virnet.management.entity.User;
import virnet.management.entity.Character;
import virnet.management.entity.UserCharacter;

public class UserDAO extends BaseDAO{
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
	
	public List getListByProperty(String pName,
            Object pValue) {
    	List list = super.getListByProperty(User.class, pName, pValue);
    	return list;	    	
    }
	
	public User getUniqueByProperty(String pName, Object pValue){
		log.debug("get unique instance by property " + pName + " : " + pValue);
		
		Object o = super.getUniqueByProperty(User.class, pName, pValue);
		return (User) o;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getCharacterGroup(User user){
		log.debug("get character list by user instance with user id : " + user.getUserId());

		Session session = null;
		
		try{
	        session = HibernateSessionFactory.getSessionFactory().openSession();
	        String hql = "select model from " + UserCharacter.class.getName()
	                + " as model where model.userCharacterUserId" + " = '" + user.getUserId() + "'";
	        List objs = session.createQuery(hql).list();
	        
	        if(objs != null && objs.size() != 0){
	        	int size = objs.size();
	        	
	        	List clist = new ArrayList<Object>();
	        	for(int i = 0; i < size; i++){
	        		String hql2 = "select model from " + Character.class.getName()
	        				+ " as model where model.characterId" + " = '" + ((UserCharacter) objs.get(i)).getUserCharacterId() + "'";
	        		clist.addAll(session.createQuery(hql2).list());
	        	}
	        	
	        	if(clist != null && clist.size() != 0){
	        		return clist;
	        	}
	        	else{
	        		return null;
	        	}
	        }
	        else{
	        	return null;
	        }
		} catch (RuntimeException re) {
            log.error("get character list failed", re);
            throw re;
        }finally{
        	if(session != null){
        		session.close();
        	}
        }
	}
}