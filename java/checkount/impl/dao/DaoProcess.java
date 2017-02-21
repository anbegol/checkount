package checkount.impl.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DaoProcess {
	
	/*El encargado de proveer la funcionalidad para manejar la sesión de Hibernate*/
	SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory(){
		if(sessionFactory == null) {
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();			
		}
		return sessionFactory;
	}
}
