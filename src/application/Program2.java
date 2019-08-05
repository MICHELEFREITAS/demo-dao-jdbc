package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentdao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TESTE 1: department findById ==== ");
		
		Department department = departmentdao.findById(2);
		
		System.out.println(department);

	}

}
