package foltan.javaee.test.app.bean;

import foltan.javaee.test.app.bean.UserSessionBean;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserSessionBeanTest {

	private UserSessionBean userSession;
	
	@Before
	public void setUp() {
		userSession = new UserSessionBean();
	}
	
	@Test
	public void testIsLoggedIn() {
		assertFalse(userSession.isLoggedIn());
		
		userSession.setUserId(Long.valueOf(1));
		assertTrue(userSession.isLoggedIn());
	}
	
}
