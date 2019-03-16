package com.ezetap.contacts.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezetap.contacts.service.ContactsService;
import com.ezetap.contacts.web.api.ContactsApi;
import com.ezetap.contacts.web.api.model.ContactCollectionModel;
import com.ezetap.contacts.web.api.model.ContactModel;
import com.ezetap.contacts.web.api.model.Links;
import com.ezetap.contacts.web.api.model.PagingDto;
import com.ezetap.contacts.web.api.model.ResponseModel;

import io.swagger.annotations.ApiParam;

@CrossOrigin(origins = "*")
@RestController
public class ContactsController implements ContactsApi {

	@Autowired
	private ContactsService contactsService;

	private String baseUri = "/web/v1/contacts";

	@Override
	public ResponseEntity<ResponseModel> addContacts(
			@ApiParam(value = "Request Body that contains data required for creating new Contact", required = true) @Valid @RequestBody ContactModel contactModel) {
		ResponseModel responseModel = new ResponseModel();
		ResponseEntity<ResponseModel> responseEntity = null;
		HttpStatus statusCode = null;
		String id = contactsService.saveContact(contactModel);
		if (id != null) {
			responseModel.setMessage("Record Saved Successfully");
			Link selfLink = ControllerLinkBuilder.linkTo(ContactsController.class).slash(baseUri).slash(id)
					.withSelfRel();
			statusCode = HttpStatus.CREATED;
			responseModel.setData(new ResourceSupport());
			responseModel.getData().add(selfLink);
		} else {
			responseModel.setMessage("Record Failed to Save");
			statusCode = HttpStatus.BAD_REQUEST;
		}
		responseEntity = new ResponseEntity<>(responseModel, statusCode);
		return responseEntity;
	}

	@Override
	public ResponseEntity<ResponseModel> updateContact(
			@ApiParam(value = "Id of the Contact that needs to be updated", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Request body that contains Contact Model that needs to be updated", required = true) @Valid @RequestBody ContactModel contactModel) {
		// To avoid inserting duplicate record
		contactModel.setKeyId(id);

		ResponseModel responseModel = new ResponseModel();
		ResponseEntity<ResponseModel> responseEntity = null;
		HttpStatus statusCode = null;
		contactsService.updateContact(contactModel);
		responseModel.setMessage("Record Updated Successfully");
		Link selfLink = ControllerLinkBuilder.linkTo(ContactsController.class).slash(baseUri).slash(id).withSelfRel();
		statusCode = HttpStatus.ACCEPTED;
		responseModel.setData(new ResourceSupport());
		responseModel.getData().add(selfLink);
		responseEntity = new ResponseEntity<>(responseModel, statusCode);
		return responseEntity;
	}

	@Override
	public ResponseEntity<ResponseModel> deleteContact(
			@ApiParam(value = "Id of the Contact that needs to be updated", required = true) @PathVariable("id") String id,
			@ApiParam(value = "Request body that contains Contact Model that needs to be updated", required = true) @Valid @RequestBody ContactModel contactModel) {
		// To avoid deleting different record
		contactModel.setKeyId(id);

		ResponseModel responseModel = new ResponseModel();
		ResponseEntity<ResponseModel> responseEntity = null;
		HttpStatus statusCode = null;
		contactsService.deleteContact(contactModel);
		responseModel.setMessage("Record Deleted Successfully");
		Link selfLink = ControllerLinkBuilder.linkTo(ContactsController.class).slash(baseUri).slash(id).withSelfRel();
		statusCode = HttpStatus.ACCEPTED;
		responseModel.setData(new ResourceSupport());
		responseModel.getData().add(selfLink);
		responseEntity = new ResponseEntity<>(responseModel, statusCode);
		return responseEntity;
	}

	public ResponseEntity<ContactCollectionModel> getContacts(
			@ApiParam(value = "Query based on name") @Valid @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "Query based on Email") @Valid @RequestParam(value = "email", required = false) String email,
			@ApiParam(value = "Query based on offset") @Valid @RequestParam(value = "offset", required = false) Integer offset,
			@ApiParam(value = "Query based on limit") @Valid @RequestParam(value = "limit", required = false) Integer limit) {

		ContactCollectionModel contactCollectionModel = new ContactCollectionModel();
		List<ContactModel> contactModelList = new ArrayList<>();
		List<ContactModel> contactModelListWithLink = new ArrayList<>();
		Integer initialOffset = offset;
		Integer initialLimit = limit;
		Integer nRecords = 10;
		if (null == offset && null == limit) {
			offset = 0;
			limit = nRecords;
		}
		contactModelList.addAll(contactsService.getContacts(name, email, offset, limit));

		// Adding self Links - HATEOS
		contactModelListWithLink = contactModelList.stream().map(n -> {
			Link selfLink = ControllerLinkBuilder.linkTo(ContactsController.class).slash(baseUri).slash(n.getKeyId())
					.withSelfRel();
			n.add(selfLink);
			return n;
		}).collect(Collectors.toList());

		// Adding Paging Information
		PagingDto pagination = new PagingDto();
		Links links = new Links();
		pagination.setLinks(links);
		contactCollectionModel.data(contactModelListWithLink);
		int totalCount = 0;
		totalCount = contactsService.getCount();
		pagination.setTotalResults(totalCount);
		int nextoff = 0, nextlim = 0, preoff = 0, prelim = 0, pagesize = nRecords;
		nextoff = offset + pagesize;
		nextlim = pagesize;
		if (offset != 0) {
			preoff = (offset - nRecords) < 0 ? 0 : offset - nRecords;
			prelim = limit;
		}
		if (initialOffset != null) {
			nextoff = initialOffset + initialLimit;
			if (offset != 0) {
				preoff = (initialOffset - initialLimit) < 0 ? 0 : initialOffset - initialLimit;
			}
		}
		if (initialLimit != null) {
			nextlim = initialLimit;
			prelim = initialLimit;
		}
		String baseUrinext = "contacts?" + "offset=" + nextoff + "&limit=" + nextlim;
		String baseUriprev = "contacts?" + "offset=" + preoff + "&limit=" + prelim;
		pagination.getLinks()
				.setNext(ControllerLinkBuilder.linkTo(ContactsController.class).slash(baseUrinext).withRel("next"));
		pagination.getLinks()
				.setPrev(ControllerLinkBuilder.linkTo(ContactsController.class).slash(baseUriprev).withRel("prev"));
		if (offset == 0 && limit == 0) {
			pagination.getLinks().setPrev(null);
			pagination.getLinks().setNext(null);
		} else if (offset == 0) {
			baseUriprev = null;
			pagination.getLinks().setPrev(null);
		}
		if (contactModelList.isEmpty() || nextoff == totalCount || contactModelList.size() < limit) {
			baseUrinext = null;
			pagination.getLinks().setNext(null);
		}
		contactCollectionModel.paging(pagination);
		return ResponseEntity.ok(contactCollectionModel);
	}

}
