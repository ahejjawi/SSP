/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api.external;

import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.factory.external.ExternalFacultyCourseRosterTOFactory;
import org.jasig.ssp.factory.external.ExternalTOFactory;
import org.jasig.ssp.factory.external.FacultyCourseTOFactory;
import org.jasig.ssp.model.external.ExternalFacultyCourseRoster;
import org.jasig.ssp.model.external.FacultyCourse;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.external.FacultyCourseService;
import org.jasig.ssp.transferobject.PagedResponse;
import org.jasig.ssp.transferobject.external.ExternalFacultyCourseRosterTO;
import org.jasig.ssp.transferobject.external.FacultyCourseTO;
import org.jasig.ssp.transferobject.external.SearchFacultyCourseTO;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequestMapping("/1/person/{facultySchoolId}/instruction")
public class FacultyCourseController extends AbstractExternalController<FacultyCourseTO, FacultyCourse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FacultyCourseController.class);

	@Autowired
	protected transient FacultyCourseService service;

	@Autowired
	protected transient FacultyCourseTOFactory factory;

	@Autowired
	protected transient ExternalFacultyCourseRosterTOFactory externalFacultyCourseRosterTOFactory;

	@Override
	protected FacultyCourseService getService() {
		return service;
	}

	@Override
	protected ExternalTOFactory<FacultyCourseTO, FacultyCourse> getFactory() {
		return factory;
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}


	protected FacultyCourseController() {
		super(FacultyCourseTO.class, FacultyCourse.class);
	}


	/**
	 * Gets all courses for the specified faculty.
	 * 
	 * @param facultySchoolId
	 *            The faculty school id to use to lookup the associated data.
	 * @return The specified courses if any were found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If that specified data is not invalid.
	 */
	@RequestMapping(value = "/course", method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_PERSON_INSTRUCTION_READ)
	public @ResponseBody
	PagedResponse<FacultyCourseTO> getAllCoursesForFaculty(final @PathVariable String facultySchoolId)
			throws ObjectNotFoundException, ValidationException {

		final List<FacultyCourse> list = getService().getAllCoursesForFacultySortedByTerm(facultySchoolId);

		return new PagedResponse<FacultyCourseTO>(true, Long.valueOf(list.size()), factory.asTOList(list));
	}

	/**
	 * Gets the course roster for the specified faculty's course.
	 * 
	 * @param facultySchoolId
	 *            The faculty school id to use to lookup the associated data.
	 * @param formattedCourse
	 *            the course
	 * @param termCode
	 *            term code filter (optional)
	 * @return The specified courses if any were found.
	 * @throws ObjectNotFoundException
	 *             If specified object could not be found.
	 * @throws ValidationException
	 *             If that specified data is not invalid.
	 */
	@RequestMapping(value = "/course/{formattedCourse}/roster", method = RequestMethod.GET)
	@PreAuthorize(Permission.SECURITY_PERSON_INSTRUCTION_READ)
	public @ResponseBody
	PagedResponse<ExternalFacultyCourseRosterTO> getRoster(
			final @PathVariable String facultySchoolId,
			final @PathVariable String formattedCourse,
			final @RequestParam(required = false) String sectionCode,
			final @RequestParam(required = false) String termCode)
			    throws ObjectNotFoundException, ValidationException {

		final String scrubbedTermCode = StringUtils.trimToNull(termCode);
		final List<ExternalFacultyCourseRoster> list = getService().getFacultyCourseRoster(
		        new SearchFacultyCourseTO( facultySchoolId, termCode, sectionCode, formattedCourse));

		return new PagedResponse<ExternalFacultyCourseRosterTO>(true, Long.valueOf(list.size()),
                externalFacultyCourseRosterTOFactory.asTOList(list));
	}
}