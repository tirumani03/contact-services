package com.ezetap.contacts.repository;

import java.util.List;

import com.ezetap.contacts.entity.Contacts;

public interface ContactsRepository {

	public List<Contacts> getContacts(String name, String email, Integer offset,Integer limit);

	public void saveContact(Contacts contact);

	public void updateContact(Contacts contact);

	public void deleteContact(Contacts contact);

	public Integer getCount();

}
