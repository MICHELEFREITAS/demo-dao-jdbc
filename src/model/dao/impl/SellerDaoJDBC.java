package model.dao.impl;

import java.io.IOException;
import java.security.KeyStore.ProtectionParameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	//Dao terá uma dependencia com a conexão. Esse obj conn ficará disposição qualquer lugar dessa classe
	private Connection conn;
	
	//construtor
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);//id do novo vendedor inserido
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0 ) {
				ResultSet rs = st.getGeneratedKeys(); 
				
				//if pq inserindo só um dado. Se existir
				if(rs.next()) {
					//pega o valor do id gerado
					int id = rs.getInt(1);
					//obj fique populado com o novo id dele
					obj.setId(id);
				}
				DB.closeResultset(rs);
				
			}else {//caso nenhuma linha foi alterada
				throw new DbException("Unexpected error! No rows affected!");
			}
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ " SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			                 //pego do obj que chegou como argumento do update...
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());//Id do vendedor
			
			st.executeUpdate();
			
		//excecao personalisada caso aconteca erro	
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id " 
					+ "WHERE seller.Id = ? ");
			
			//vai receber id da entrada da função
			st.setInt(1, id);
			//vem o resultado no rs
			rs = st.executeQuery();
			if(rs.next()) {
				
				//chama a função e passa o rs como argumento
				Department dep = instantiateDepartment(rs);
				
				//Instanciando o seller. Chama a função.
				Seller obj = instatiateSeller(rs, dep);
				
				return obj;
				
			}
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}

	}

	//throws SQLException - propagar a excecao
	private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller obj = new Seller();
		//obj seller com todos atributos definidos
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		
		return obj;
	}


	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		//setando dados dele
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		
		return dep;
	}


	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//buscar todos vendedores com nome de departamento, fazendo join e ordenando por nome
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			//chave é o Integer que é o id do departament e o valor de cada obj será do tipo department
			//Map vazio que vai guardar o department que eu instanciar
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				
				//se o dep existe eu pego ele
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				//se não existir o dep
				if(dep == null) {
					//instancia e salva com put dentro do map
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				//com isso instancia todos vendedores sem repetir departamento
				Seller obj = instatiateSeller(rs, dep);
				
				list.add(obj);				
			}
			return list;
					
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}
		
	}


	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " 
					+ "FROM seller INNER JOIN department " 
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			//chave é o Integer que é o id do departament e o valor de cada obj será do tipo department
			//Map vazio que vai guardar o department que eu instanciar
			Map<Integer, Department> map = new HashMap<>();
			
			//Percorrer rs enquanto tiver um próximo
			while(rs.next()) {
				
				//vê se o department existe. Se não existir vai retornar null
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep==null) {
					//vai instanciar depart com o rs
					dep = instantiateDepartment(rs);
					//salvar dep dentro map para que na proxima vez verifica e ve que ele existe
					map.put(rs.getInt("DepartmentId"), dep);
				}
		
				//instancia vendedor apontando para o dep, seja ele existente ou um novo depart.
				Seller obj = instatiateSeller(rs, dep);
				
				//adicionar vendedor na lista
				list.add(obj);
			}
			return list;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultset(rs);
		}
	}

}
