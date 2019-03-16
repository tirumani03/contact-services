package com.ezetap.contacts.service;

import java.util.List;

import com.ezetap.contacts.web.api.model.ContactModel;

public interface ContactsService {

	public List<ContactModel> getContacts(String name, String email, Integer offset, Integer limit);

	public Integer getCount();

	public String saveContact(ContactModel contactModel);

	public void updateContact(ContactModel contactModel);

	public void deleteContact(ContactModel contactModel);

}
