package foltan.javaee.test.app.bean.startup;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;

import foltan.javaee.test.app.dao.NoteDao;
import foltan.javaee.test.app.dao.UserDao;
import foltan.javaee.test.app.model.ModelFactory;
import foltan.javaee.test.app.model.Note;
import foltan.javaee.test.app.model.User;

@Singleton
@Startup
public class StartupBean {

	@Inject
	private UserDao userDao;
	
	@Inject
	private NoteDao noteDao;
	
	private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm";
	
    /**
     *
     * @throws Exception
     */
        @PostConstruct
	@Transactional
	public void init() throws Exception {
		User user1 = user("andrej@gmail.com", "andrej");
		User user2 = user("foltan@gmail.com", "foltan");
		
		userDao.save(user1);
		userDao.save(user2);
		noteDao.save( note(
					"Paperworks", 
					"- Call Social Security\n- Hand tax form\n- Pay bills", 
					DateUtils.parseDate("20/03/2023 11:15", DATE_PATTERN), 
					user1) );
		
		noteDao.save( note(
				"Favorite quotes", 
				"A good laugh is sunshine in the house\nIn love the paradox occurs that two beings become one and yet remain two", 
				DateUtils.parseDate("20/03/2023 13:20", DATE_PATTERN), 
				user1) );
		
		noteDao.save( note(
				"Bash Cheatsheet", 
				"mv <a> <b>\ncp <a> <b>\ncat <file>\nmkdir <path>", 
				DateUtils.parseDate("21/03/2023 09:34", DATE_PATTERN), 
				user2) );
		
		
	}
	
	private User user(String email, String password) {
		User user = ModelFactory.user();
		user.setEmail(email);
		user.setPassword(password);
		
		return user;
	}
	
	private Note note(String title, String body, Date creationDate, User author) {
		Note note = ModelFactory.note();
		note.setTitle(title);
		note.setBody(body);
		note.setCreationDate(creationDate);
		note.setUser(author);
		
		return note;
	}
}
