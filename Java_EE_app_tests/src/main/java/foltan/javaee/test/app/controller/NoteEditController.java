package foltan.javaee.test.app.controller;

import java.util.Date;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import foltan.javaee.test.app.bean.UserSessionBean;
import foltan.javaee.test.app.bean.producer.HttpParam;
import foltan.javaee.test.app.dao.NoteDao;
import foltan.javaee.test.app.dao.UserDao;
import foltan.javaee.test.app.model.ModelFactory;
import foltan.javaee.test.app.model.Note;

@Model
public class NoteEditController {

	@Inject
	private NoteDao noteDao;
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private UserSessionBean userSession;
	
	@Inject @HttpParam("id")
	private String noteId;
	
	@Inject @HttpParam("add")
	private String addFlag;
	
	private Note note;
	private Boolean adding;
	
	public Note getNote() {
		if(note == null) {
			initNote();
		}
		return note;
	}
	
	@Transactional
	public String save() {
		noteDao.save(getNote());
		return "home";
	}
	
	public Boolean getAdding() {
		return adding;
	}
	
	
	protected void initNote() {
		if(Boolean.valueOf( addFlag ) ) {
			adding = true;
			note = ModelFactory.note();
			note.setCreationDate( new Date() );
			note.setUser( userDao.findById(userSession.getUserId()) );
			return;
		}
		
		adding = false;
		if(StringUtils.isEmpty(noteId)) {
			throw new IllegalArgumentException("id not found");
		}
		
		try {
			Long id = Long.valueOf(noteId);
			note = noteDao.findById(id);
			
			if(!note.getUser().getId().equals( userSession.getUserId())) {
				throw new IllegalStateException("you are not the owner of the note");
			}
			
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("id not a number");
		}
	}
	
}
