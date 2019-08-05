package application;

import java.util.Date;
import java.util.List;

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
		
		System.out.println("\n=== TESTE 2: seller findByDepartment ==== ");
		
		
		Department department = new Department(2, null);
		
		//o department é passado na função buscar por department e os dados da busca vão para lista.
		List<Seller> list = sellerDao.findByDepartment(department);
		
		//Para cada vendedor obj na minha lista list vai imprimir esse obj
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		
		System.out.println("\n=== TESTE 3: seller findALL ====");
		//reaproveitando variável list de cima, só que buscando todos vendedores
		list = sellerDao.findAll();
		for(Seller obj : list) {
			System.out.println(obj);
		}
	}

}
