package com.ezetap.contacts.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ezetap.contacts.entity.Contacts;

@Repository
public class BaseContactsRepository implements ContactsRepository {

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Contacts> getContacts(String name, String email, Integer offset, Integer limit) {
		StringBuilder queryBuilder = new StringBuilder("select c from Contacts c");
		if (name != null || email != null) {
			queryBuilder.append(" where");
			if (name != null) {
				queryBuilder.append(" c.name='" + name + "'");
			}
			if (name != null && email != null)
				queryBuilder.append(" and");
			if (email != null) {
				queryBuilder.append(" c.email='" + email + "'");
			}
		}
		return this.entityManager.createQuery(queryBuilder.toString()).setFirstResult(offset).setMaxResults(limit)
				.getResultList();
	}

	@Override
	public Integer getCount() {
		StringBuilder queryBuilder = new StringBuilder("select count(c) from Contacts c");

		return ((Long) this.entityManager.createQuery(queryBuilder.toString()).getSingleResult()).intValue();
	}

	@Override
	@Transactional
	public void saveContact(Contacts contact) {
		this.entityManager.persist(contact);
	}

	@Override
	@Transactional
	public void updateContact(Contacts contact) {
		this.entityManager.merge(contact);
	}

	@Override
	@Transactional
	public void deleteContact(Contacts contact) {
		this.entityManager.remove(contact);
	}

}
