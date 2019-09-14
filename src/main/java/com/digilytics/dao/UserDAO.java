package com.digilytics.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.digilytics.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class UserDAO implements IUserDAO {
	@PersistenceContext	
	private EntityManager entityManager;

	@Override
	public void addUser(User user) {
		entityManager.persist(user);
	}

	@Override
	public List getAllRoles() {
		return entityManager.createQuery("Select name from Role").getResultList();
	}

}
