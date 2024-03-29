package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentdao = DaoFactory.createDepartmentDao();
		
		//buscar departamento por Id		
		System.out.println("=== TESTE 1: department findById ==== ");
		
		Department department = departmentdao.findById(2);
		
		System.out.println(department);
		
		//Exibir todos departamentos
		System.out.println("\n=== TESTE 2: department findAll ==== ");
		
		List<Department> list = departmentdao.findAll();
		
		for(Department dep : list) {
			System.out.println(dep);
		}
		
		//Inserir um novo departamento
		System.out.println("\n=== TESTE 3: department insert ==== ");
		
		Department depnew = new Department(null, "Music");
		
		departmentdao.insert(depnew);
		
		System.out.println("Inserted! New id: " + depnew.getId());
		
		//Alterar um departamento de acordo com o id escolhido do BD
		System.out.println("\n TESTE 4: department update ==== ");
		
		Department dep2 = departmentdao.findById(1);
		dep2.setName("Food");
		departmentdao.update(dep2);
		System.out.println("Update completed!");
		
		//Deletar um departamento de acrodo com o id informado
		System.out.println("\n TESTE 5: department delete ==== ");
		
		System.out.println("Enter id for delete test");
		int id = sc.nextInt();
		
		departmentdao.deleteById(id);
		
		System.out.println("Delete completed!");
		
		sc.close();
	}

}
