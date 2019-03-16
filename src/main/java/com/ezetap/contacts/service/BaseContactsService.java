package com.ezetap.contacts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezetap.contacts.entity.Contacts;
import com.ezetap.contacts.repository.ContactsRepository;
import com.ezetap.contacts.utils.ContactUtils;
import com.ezetap.contacts.web.api.model.ContactModel;

@Service
public class BaseContactsService implements ContactsService {

	@Autowired
	private ContactsRepository contactsRepository;

	@Override
	public List<ContactModel> getContacts(String name, String email,Integer offset,Integer limit) {
		return contactsRepository.getContacts(name, email,offset,limit).stream().map(ContactUtils.entityToModel)
				.collect(Collectors.toList());
	}

	@Override
	public Integer getCount() {
		return contactsRepository.getCount();
	}

	@Override
	@Transactional
	public String saveContact(ContactModel contactModel) {
		Contacts contact = ContactUtils.modelToEntity.apply(contactModel);
		contactsRepository.saveContact(contact);
		return contact.getKeyId();
	}

	@Override
	@Transactional
	public void updateContact(ContactModel contactModel) {
		Contacts contact = ContactUtils.modelToEntity.apply(contactModel);
		contactsRepository.updateContact(contact);
	}

	@Override
	@Transactional
	public void deleteContact(ContactModel contactModel) {
		Contacts contact = ContactUtils.modelToEntity.apply(contactModel);
		contactsRepository.deleteContact(contact);
	}
}
