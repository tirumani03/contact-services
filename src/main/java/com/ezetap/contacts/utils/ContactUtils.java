package com.ezetap.contacts.utils;

import java.util.function.Function;

import com.ezetap.contacts.entity.Contacts;
import com.ezetap.contacts.web.api.model.ContactModel;

public class ContactUtils {

	public static Function<Contacts, ContactModel> entityToModel = new Function<Contacts, ContactModel>() {

		public ContactModel apply(Contacts t) {
			ContactModel contactModel = new ContactModel();
			contactModel.setKeyId(t.getKeyId());
			contactModel.setName(t.getName());
			contactModel.setAddressLine1(t.getAddressLine1());
			contactModel.setEmail(t.getEmail());
			contactModel.setAddressLine2(t.getAddressLine2());
			contactModel.setAddressType(t.getAddressType());
			contactModel.setCity(t.getCity());
			contactModel.setState(t.getState());
			contactModel.setDesignation(t.getDesignation());
			contactModel.setCountry(t.getCountry());
			contactModel.setZipcode(t.getZipcode());
			contactModel.setTelephone(t.getTelephone());
			return contactModel;
		}
	};

	public static Function<ContactModel, Contacts> modelToEntity = new Function<ContactModel, Contacts>() {

		public Contacts apply(ContactModel t) {
			Contacts contacts = new Contacts();
			if (t.getKeyId() != null)
				contacts.setKeyId(t.getKeyId());
			contacts.setName(t.getName());
			contacts.setAddressLine1(t.getAddressLine1());
			contacts.setEmail(t.getEmail());
			contacts.setAddressLine2(t.getAddressLine2());
			contacts.setAddressType(t.getAddressType());
			contacts.setCity(t.getCity());
			contacts.setState(t.getState());
			contacts.setDesignation(t.getDesignation());
			contacts.setCountry(t.getCountry());
			contacts.setZipcode(t.getZipcode());
			contacts.setTelephone(t.getTelephone());
			return contacts;
		}
	};

}
