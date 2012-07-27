package org.jasig.ssp.factory.impl;

import java.util.HashSet;

import org.jasig.ssp.dao.PersonDao;
import org.jasig.ssp.factory.AbstractAuditableTOFactory;
import org.jasig.ssp.factory.PersonReferralSourceTOFactory;
import org.jasig.ssp.factory.PersonServiceReasonTOFactory;
import org.jasig.ssp.factory.PersonSpecialServiceGroupTOFactory;
import org.jasig.ssp.factory.PersonTOFactory;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.PersonProgramStatus;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.reference.StudentTypeService;
import org.jasig.ssp.transferobject.PersonTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Person transfer object factory implementation
 */
@Service
@Transactional(readOnly = true)
public class PersonTOFactoryImpl extends
		AbstractAuditableTOFactory<PersonTO, Person>
		implements PersonTOFactory {

	public PersonTOFactoryImpl() {
		super(PersonTO.class, Person.class);
	}

	@Autowired
	private transient PersonDao dao;

	@Autowired
	private transient PersonService personService;

	@Autowired
	private transient StudentTypeService studentTypeService;

	@Autowired
	private transient PersonSpecialServiceGroupTOFactory personSpecialServiceGroupTOFactory;

	@Autowired
	private transient PersonReferralSourceTOFactory personReferralSourceTOFactory;

	@Autowired
	private transient PersonServiceReasonTOFactory personServiceReasonTOFactory;

	@Override
	protected PersonDao getDao() {
		return dao;
	}

	@Override
	public Person from(final PersonTO tObject)
			throws ObjectNotFoundException {
		final Person model = super.from(tObject);

		model.setFirstName(tObject.getFirstName());
		model.setMiddleName(tObject.getMiddleName());
		model.setLastName(tObject.getLastName());
		model.setBirthDate(tObject.getBirthDate());
		model.setPrimaryEmailAddress(tObject.getPrimaryEmailAddress());
		model.setSecondaryEmailAddress(tObject.getSecondaryEmailAddress());
		model.setUsername(tObject.getUsername());
		model.setHomePhone(tObject.getHomePhone());
		model.setWorkPhone(tObject.getWorkPhone());
		model.setCellPhone(tObject.getCellPhone());
		model.setAddressLine1(tObject.getAddressLine1());
		model.setAddressLine2(tObject.getAddressLine2());
		model.setCity(tObject.getCity());
		model.setState(tObject.getState());
		model.setZipCode(tObject.getZipCode());
		model.setPhotoUrl(tObject.getPhotoUrl());
		model.setSchoolId(tObject.getSchoolId());
		model.setEnabled(tObject.getEnabled());
		model.setStrengths(tObject.getStrengths());
		model.setAbilityToBenefit(tObject.getAbilityToBenefit());
		model.setAnticipatedStartTerm(tObject.getAnticipatedStartTerm());
		model.setAnticipatedStartYear(tObject.getAnticipatedStartYear());
		model.setActualStartTerm(tObject.getActualStartTerm());
		model.setActualStartYear(tObject.getActualStartYear());
		model.setStudentIntakeRequestDate(tObject.getStudentIntakeRequestDate());
		model.setStudentType((tObject.getStudentType() == null)
				|| (tObject.getStudentType().getId() == null) ? null // NOPMD
				: studentTypeService.get(tObject.getStudentType().getId()));

		model.setCoach((tObject.getCoach() == null)
				|| (tObject.getCoach().getId() == null) ? null : personService // NOPMD
				.get(tObject.getCoach().getId()));

		personSpecialServiceGroupTOFactory.updateSetFromLites(
				model.getSpecialServiceGroups(),
				tObject.getSpecialServiceGroups(), model);

		personReferralSourceTOFactory
				.updateSetFromLites(model.getReferralSources(),
						tObject.getReferralSources(), model);

		personServiceReasonTOFactory.updateSetFromLites(
				model.getServiceReasons(), tObject.getServiceReasons(), model);

		// program statuses can not be overwritten here. always reload from db
		if (tObject.getId() == null) {
			model.setProgramStatuses(new HashSet<PersonProgramStatus>(0));
		} else {
			final Person person = personService.get(tObject.getId());
			model.setProgramStatuses(person.getProgramStatuses());
		}

		return model;
	}
}