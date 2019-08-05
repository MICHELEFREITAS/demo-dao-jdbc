package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	//operação responsável por inserir no BD o obj de padrão de entrada
	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	
	//retornar department. Consultar no BD obj com esse id
	Department findById(Integer id);
	
	//retornar todos departamentos
	List<Department> findAll();
	
	
}
