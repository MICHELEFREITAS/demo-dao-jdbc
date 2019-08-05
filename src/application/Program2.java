package application;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao departmentdao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== TESTE 1: department findById ==== ");
		
		Department department = departmentdao.findById(2);
		
		System.out.println(department);
		
		
		System.out.println("\n=== TESTE 2: department findAll ==== ");
		
		List<Department> list = departmentdao.findAll();
		
		for(Department dep : list) {
			System.out.println(dep);
		}

	}

}
