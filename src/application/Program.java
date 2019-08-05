package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		//dessa forma prog  não conhece a implementação, só conhece a interface
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TESTE 1: seller findById ==== ");
		
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
	}

}
