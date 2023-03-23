package foltan.javaee.test.app.controller;

import foltan.javaee.test.app.controller.NoteViewController;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import foltan.javaee.test.app.bean.UserSessionBean;
import foltan.javaee.test.app.dao.NoteDao;
import foltan.javaee.test.app.model.ModelFactory;
import foltan.javaee.test.app.model.Note;
import foltan.javaee.test.app.model.User;

public class NoteViewControllerTest {

	private NoteViewController noteViewController;
	private NoteDao noteDao;
	private UserSessionBean userSession;
	
	private Note note;
	private User user;
	
	private Long noteId;
	private Long userId;
	
	@Before
	public void setUp() throws InitializationError {
		noteViewController = new NoteViewController();
		noteDao = mock(NoteDao.class);
		noteId = Long.valueOf(10);
		userId = Long.valueOf(7);
		note = ModelFactory.note();
		user = ModelFactory.user();
		note.setUser(user);
		
		userSession = new UserSessionBean();
		userSession.setUserId(userId);
		
		try {
			FieldUtils.writeField(note, "id", noteId, true);
			FieldUtils.writeField(user, "id", userId, true);
			FieldUtils.writeField(noteViewController, "noteDao", noteDao, true);
			FieldUtils.writeField(noteViewController, "userSession", userSession, true);
			FieldUtils.writeField(noteViewController, "noteId", noteId.toString(), true);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
	}

	@Test
	public void testGetNote() {
		when(noteDao.findById(noteId)).thenReturn(note);
		
		assertEquals(note, noteViewController.getNote());
		assertEquals(noteId, noteViewController.getNote().getId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetNoteNoId() throws InitializationError {
		try {
			FieldUtils.writeField(noteViewController, "noteId", null, true);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
		
		noteViewController.getNote();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetNoteBadIdFormat() throws InitializationError {
		try {
			FieldUtils.writeField(noteViewController, "noteId", "hello", true);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
		
		noteViewController.getNote();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetNoteNotOwner() {
		when(noteDao.findById(noteId)).thenReturn(note);
		userSession.setUserId(Long.valueOf(1000));
		
		noteViewController.getNote();
	}

}
