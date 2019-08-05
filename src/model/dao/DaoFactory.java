package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

//classe vai ter operações estáticas para instanciar os DAOs
public class DaoFactory {
	
	//o createSellerDao retorna um SellerDao
	public static SellerDao createSellerDao() {
		
		//instanciar a implementação
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		
		return new DepartmentDaoJDBC(DB.getConnection());
	}

}
