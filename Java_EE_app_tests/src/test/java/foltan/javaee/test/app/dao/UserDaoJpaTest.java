package foltan.javaee.test.app.dao;

import foltan.javaee.test.app.dao.UserDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import foltan.javaee.test.app.model.ModelFactory;
import foltan.javaee.test.app.model.User;

public class UserDaoJpaTest extends JpaTest {

	private UserDao userDao;
	private User user;
	
	@Override
	protected void init() throws InitializationError {
		user = ModelFactory.user();
		user.setEmail("andrej@example.com");
		user.setPassword("pass321987word");
		
		entityManager.persist(user);
		
		userDao = new UserDao();
		try {
			FieldUtils.writeField(userDao, "entityManager", entityManager, true);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
	}

	@Test
	public void testSave() {
		User anotherUser = ModelFactory.user();
		anotherUser.setEmail("foltan@example.com");
		anotherUser.setPassword("pass654321word");
		
		userDao.save(anotherUser);
		
		assertEquals( anotherUser, entityManager
							.createQuery("from User where uuid =:uuid", User.class)
									.setParameter("uuid", anotherUser.getUuid())
									.getSingleResult());
				
	}
	
	@Test
	public void testSaveUpdate() {
		user = entityManager.find(User.class, user.getId());
		String changedEmail = "new@example.com";
		user.setEmail(changedEmail);
		
		userDao.save(user);
		
		assertEquals( changedEmail, entityManager
							 .createQuery("from User where uuid =:uuid", User.class)
									.setParameter("uuid", user.getUuid())
									.getSingleResult()
									.getEmail());
				
	}
	
	@Test
	public void testFindById() {
		User result = userDao.findById(user.getId());
		
		assertEquals( user.getId(), result.getId() );
		assertEquals( user.getUuid(), result.getUuid() );
		assertEquals( user.getEmail(), result.getEmail() );
		assertEquals( user.getPassword(), result.getPassword() );
	}
	
	@Test
	public void testLogin() {
		User credentials = ModelFactory.user();
		credentials.setEmail("andrej@example.com");
		credentials.setPassword("pass321987word");
		
		assertEquals( user, userDao.login(credentials) );
	}
	
	@Test
	public void testLoginWrongCredentials() {
		User credentials = ModelFactory.user();
		credentials.setEmail("foltan@example.com");
		credentials.setPassword("pass7654321word");
		
		assertNull( userDao.login(credentials) );
	}
	
	
}
