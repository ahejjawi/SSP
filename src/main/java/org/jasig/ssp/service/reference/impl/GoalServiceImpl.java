package org.jasig.ssp.service.reference.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.ssp.dao.reference.GoalDao;
import org.jasig.ssp.model.reference.Goal;
import org.jasig.ssp.service.reference.GoalService;

@Service
@Transactional
public class GoalServiceImpl extends
		AbstractReferenceService<Goal>
		implements GoalService {

	public GoalServiceImpl() {
		super(Goal.class);
	}

	@Autowired
	transient private GoalDao dao;

	protected void setDao(final GoalDao dao) {
		this.dao = dao;
	}

	@Override
	protected GoalDao getDao() {
		return dao;
	}
}
