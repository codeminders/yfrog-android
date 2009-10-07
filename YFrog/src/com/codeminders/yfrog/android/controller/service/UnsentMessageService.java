/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;
import com.codeminders.yfrog.android.controller.dao.DAOFactory;
import com.codeminders.yfrog.android.controller.dao.UnsentMessageDAO;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class UnsentMessageService {
	private UnsentMessageDAO unsentMessageDAO;

	UnsentMessageService() {
		unsentMessageDAO = DAOFactory.getUnsentMessageDAO();
	}
	
	public UnsentMessage getUnsentMessage(int id) {
		return unsentMessageDAO.getUnsentMessage(id);

	}
	
	public ArrayList<UnsentMessage> getUnsentMessagesForAccount(long accountId) {
		return unsentMessageDAO.getUnsentMessagesForAccount(accountId);

	}

	public UnsentMessage addUnsentMessage(UnsentMessage message) {
		long id = unsentMessageDAO.addUnsentMessage(message);
		return unsentMessageDAO.getUnsentMessage(id);
	}
	
	public void updateUnsentMessage(UnsentMessage message) {
		unsentMessageDAO.updateUnsentMessage(message);
	}
	
	public void deleteUnsentMessage(long id) {
		unsentMessageDAO.deleteUnsentMessage(id);
	}
	
	public int getUnsentMessagesCountForAccount(long accountId) {
		return unsentMessageDAO.getUnsentMessagesCountForAccount(accountId);
	}

}
