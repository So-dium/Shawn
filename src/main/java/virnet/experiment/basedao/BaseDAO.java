package virnet.experiment.basedao;  
  
import java.io.Serializable;  
import java.util.List;  

import org.hibernate.Query;  
import org.hibernate.Session;  
import org.hibernate.Transaction;

import virnet.management.util.HibernateSessionFactory;
import virnet.management.util.PageUtil;
  

public class BaseDAO {  
	public boolean add(Object obj) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession(); 
            tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
            return true;

        } catch (Exception e) {
        	System.out.println("error:"+e);
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

   
    public void delete(Object obj) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            tx = session.beginTransaction(); session.delete(obj); tx.commit(); } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    @SuppressWarnings({ "rawtypes", "null" })
	public void deleteById(Class clazz, Serializable id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            session.delete(session.get(clazz, id));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    public boolean update(Object obj) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(obj);
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

   
    @SuppressWarnings("rawtypes")
	public Object get(Class clazz, Serializable id) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            Object obj = session.get(clazz, id);
            return obj;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

   
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getByNProperty(Class clazz, String... strs) {

        if (strs != null && strs.length != 0 && 0 == strs.length % 2) {
            StringBuffer hql = new StringBuffer("select model from "
                    + clazz.getName() + " as model where ");
            for (int i = 0; i < strs.length; i += 2) {
            	if(i == 0){
            		hql.append(" " + strs[i] + " = '" + strs[i + 1] + "'");
            	}
            	else{
            		hql.append(" and " + strs[i] + " = '" + strs[i + 1] + "'");
            	}
            }

            Session session = null;
            try {
                session = HibernateSessionFactory.getSessionFactory().openSession();
                List<Object> objs = session.createQuery(hql.toString()).list();
                if (objs != null && objs.size() != 0) {
                    return objs.get(0);
                } else {
                    return null;
                }
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        } else {
            return null;
        }

    }

   
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getUniqueByProperty(Class clazz, String pName, Object pValue) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            String hql = "select model from " + clazz.getName()
                    + " as model where model." + pName + " = '" + pValue + "'";
            List<Object> objs = session.createQuery(hql).list();
            if (objs != null && objs.size() != 0) {
                return objs.get(0);
            } else {
                return null;
            }

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    @SuppressWarnings("unchecked")
	public Object getUniqueByHql(String hql) {

        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            List<Object> objs = session.createQuery(hql).list();
            if (objs != null && objs.size() != 0) {
                return objs.get(0);
            } else {
                return null;
            }

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getUniqueBySql(String sql, Class clazz) {

        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            Query query = session.createSQLQuery(sql).addEntity(clazz);
            List<Object> objs = query.list();
            if (objs != null && objs.size() != 0) {
                return objs.get(0);
            } else {
                return null;
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // ////////////////////查询单个完毕////////////////

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> getList(Class clazz) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            String hql = "select model from " + clazz.getName() + " as model ";
            List list = session.createQuery(hql).list();
            return list;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> getListByProperty(Class clazz, String pName,
            Object pValue) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession(); // 获得安全session
            String hql = "select model from " + clazz.getName()
                    + " as model where model." + pName + " = '" + pValue + "'";
            return session.createQuery(hql).list();

        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> getListByProperty(Class clazz, String pName, Object pValue, String condition) { Session session = null;

        try {
        	session = HibernateSessionFactory.getSessionFactory().openSession();
            String hql = "select model from " + clazz.getName()
                    + " as model where model." + pName + " " + condition
                    + " '%" + pValue + "%'";
            List<Object> list = session.createQuery(hql).list();
            return list;
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getListByNProperty(Class clazz, String... strs) {

    	if (strs != null && strs.length != 0 && 0 == strs.length % 2) {
            StringBuffer hql = new StringBuffer("select model from "
                    + clazz.getName() + " as model where ");
            for (int i = 0; i < strs.length; i += 2) {
            	if(i == 0){
            		hql.append(" model." + strs[i] + " =  '" + strs[i + 1] + "' ");
            	}
            	else{
            		hql.append("and model." + strs[i] + " =  '" + strs[i + 1] + "' ");
            	}
                
            }

            Session session = null;
            try {
                session = HibernateSessionFactory.getSessionFactory().openSession();
                List<Object> objs = session.createQuery(hql.toString()).list();
                return objs;
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        } else {
            return null;
        }

    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> getListByHql(String hql) {

        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            List list = session.createQuery(hql).list();
            return list;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    @SuppressWarnings("rawtypes")
	public List getListBySql(String sql, Class clazz) {

        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            Query query = session.createSQLQuery(sql).addEntity(clazz);
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

   
    @SuppressWarnings("rawtypes")
	public void getListByPage(Class calzz, PageUtil pageUtil) {	
        String hql = "SELECT model from " + calzz.getName() + " as model";
//        System.out.println("Model:"+hql);
        getListByPage(hql, pageUtil);
    }

   
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void getListByPage(String hql, PageUtil pageUtil) {
        if (null == hql) {
            return;
        }
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();
            Query query = session.createQuery(hql);
            query.setFirstResult(pageUtil.getFirstRec());
            query.setMaxResults(pageUtil.getPageSize());
            pageUtil.setList(query.list());

            String queryString = "";
            if (hql.toUpperCase().indexOf("SELECT") != -1) {
                int i = query.getQueryString().toUpperCase().indexOf("FROM");
                queryString = "Select count(*) "
                        + hql.substring(i, hql.length());
            } else {
                queryString = "Select count(*) " + hql;
            }

            // 去掉ORDER BY 的部分
            int j = queryString.toUpperCase().lastIndexOf("ORDER");
            if (j != -1) {
                queryString = queryString.substring(0, j);
            }
            System.out.println("Query:"+queryString);
            Query cquery = session.createQuery(queryString);
            cquery.setCacheable(true);
            Number recTotal = ((Number)cquery.iterate().next()).intValue();
            pageUtil.setRecTotal((Integer) recTotal);
            
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

