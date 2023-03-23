package foltan.javaee.test.app.controller;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import foltan.javaee.test.app.bean.UserSessionBean;
import foltan.javaee.test.app.dao.NoteDao;
import foltan.javaee.test.app.model.Note;

@Model
public class NoteListController {

	@Inject
	private UserSessionBean userSession;
	
	@Inject
	private NoteDao noteDao;
	
	private List<Note> notes;
	
	public List<Note> getNotes() {
		if(notes == null) {
			notes = noteDao.findByUserId( userSession.getUserId() );
		}
		
		return notes;
	}
	
}
