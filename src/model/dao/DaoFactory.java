package model.dao;

import model.dao.impl.SellerDaoJDBC;

//classe vai ter opera��es est�ticas para instanciar os DAOs
public class DaoFactory {
	
	//o createSellerDao retorna um SellerDao
	public static SellerDao createSellerDao() {
		
		//instanciar a implementa��o
		return new SellerDaoJDBC();
	}

}
