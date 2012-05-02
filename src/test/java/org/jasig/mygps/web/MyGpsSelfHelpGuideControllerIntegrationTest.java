package org.jasig.mygps.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.reference.Challenge;
import org.jasig.ssp.service.impl.SecurityServiceInTestEnvironment;
import org.jasig.ssp.transferobject.reference.ChallengeTO;
import org.jasig.ssp.transferobject.reference.SelfHelpGuideDetailTO;
import org.jasig.ssp.transferobject.reference.SelfHelpGuideQuestionTO;
import org.jasig.ssp.web.api.AbstractControllerHttpTestSupport;
import org.jasig.ssp.web.api.reference.ChallengeController;

/**
 * {@link MyGpsSelfHelpGuideController} tests
 * 
 * @author jon.adams
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../../ssp/web/ControllerIntegrationTests-context.xml")
@TransactionConfiguration
@Transactional
public class MyGpsSelfHelpGuideControllerIntegrationTest
		extends
		AbstractControllerHttpTestSupport<ChallengeController, ChallengeTO, Challenge> {

	private static final UUID SELFHELPGUIDE_ID = UUID
			.fromString("4fd534df-e7fe-e555-7c71-0042593b1990");

	private static final String SELFHELPGUIDE_NAME = "Student Success Course";

	@Autowired
	private transient MyGpsSelfHelpGuideController controller;

	@Autowired
	private transient SecurityServiceInTestEnvironment securityService;

	/**
	 * Setup the security service with the administrator user.
	 */
	@Override
	@Before
	public void setUp() {
		super.setUp();
		securityService.setCurrent(new Person(Person.SYSTEM_ADMINISTRATOR_ID));
	}

	/**
	 * Test the {@link MyGpsSelfHelpGuideController#getContentById(UUID)} 
	 * action's collection sorting annotations.
	 * 
	 * @throws Exception
	 *             Thrown if the controller throws any exceptions.
	 */
	@Test
	public void testControllerGetContentById() throws Exception {
		assertNotNull(
				"Controller under test was not initialized by the container correctly.",
				controller);

		final SelfHelpGuideDetailTO obj = controller
				.getContentById(SELFHELPGUIDE_ID);

		assertEquals("Expected success from answer().", SELFHELPGUIDE_NAME,
				obj.getName());
		assertFalse("Questions list should not have been empty.", obj
				.getQuestions().isEmpty());

		for (int i = 0; i < obj.getQuestions().size(); i++) {
			final SelfHelpGuideQuestionTO question = obj.getQuestions().get(i);
			assertEquals("Question number did not match the list order.",
					i + 1,
					question.getQuestionNumber());
			assertNotNull("Challenge should not have been null.",
					question.getChallenge());
			assertNotNull("Challenge.Id should not have been null.", question
					.getChallenge().getId());
		}
	}
}
