package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userBamBi = new User("bambi@bambi.com", "bambi", "BamBi", "Cao");
		userBamBi.addRole(roleAdmin);
		
		User savedUser = repo.save(userBamBi);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userLuffy = new User("luffy@onepiece.com", "luffy", "Luffy", "Monkey D");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userLuffy.addRole(roleEditor);
		userLuffy.addRole(roleAssistant);
		
		User savedUser = repo.save(userLuffy);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userBamBi = repo.findById(1).get();
		System.out.println(userBamBi);
		assertThat(userBamBi).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userBamBi = repo.findById(1).get();
		userBamBi.setEnabled(true);
		repo.save(userBamBi);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userLuffy = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		userLuffy.getRoles().remove(roleEditor);
		userLuffy.addRole(roleSalesperson);
		repo.save(userLuffy);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
}
