package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;

import java.awt.print.Pageable;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //to run the test on real database
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userHtn = new User("hoangnguyenhtng@gmail.com", "htn17012003", "Nguyen", "Hoang");
		userHtn.addRole(roleAdmin);
		
		entityManager.clear();
		User savedUser = repo.save(userHtn);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		User userRavi = new User("ravi@gmail.com" , "ravi2020", "Ravi" ,"David");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);
		
		entityManager.clear();
		User savedUser = repo.save(userRavi);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	
	
	}
	
	@Test
	public void testGetUserById() {
		User userNguyen = repo.findById(1).get();
		System.out.println(userNguyen);
		assertThat(userNguyen).isNotNull()	;
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userNguyen = repo.findById(1).get();
		userNguyen.setEnabled(true);
		userNguyen.setEmail("nguyencotdien@gmail.com");
		
		repo.save(userNguyen);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userRavi = repo.findById(3).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(4);
		
		userRavi.getRoles().remove(roleEditor);	
		userRavi.addRole(roleSalesperson);
		
		repo.save(userRavi);
		}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 3;
		repo.deleteById(userId);
		
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "ravi@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 100;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
	
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "nguyen";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
