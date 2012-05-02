package org.jasig.ssp.service.reference;

import java.util.List;
import java.util.UUID;

import org.jasig.ssp.model.reference.SelfHelpGuide;
import org.jasig.ssp.model.reference.SelfHelpGuideGroup;
import org.jasig.ssp.service.AuditableCrudService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortingAndPaging;

public interface SelfHelpGuideService extends
		AuditableCrudService<SelfHelpGuide> {

	@Override
	PagingWrapper<SelfHelpGuide> getAll(SortingAndPaging sAndP);

	@Override
	SelfHelpGuide get(UUID id) throws ObjectNotFoundException;

	@Override
	SelfHelpGuide create(SelfHelpGuide obj);

	@Override
	SelfHelpGuide save(SelfHelpGuide obj) throws ObjectNotFoundException;

	@Override
	void delete(UUID id) throws ObjectNotFoundException;

	List<SelfHelpGuide> getBySelfHelpGuideGroup(
			SelfHelpGuideGroup selfHelpGuideGroup);
}
