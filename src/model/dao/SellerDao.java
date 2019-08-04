package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Integer id);
	
	//encontrar um vendedor com esse id
	Seller findById(Integer id);
	
	//retornar todos vendedores
	List<Seller> findAll();
	

}
