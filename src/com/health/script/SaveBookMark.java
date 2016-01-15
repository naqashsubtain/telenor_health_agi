package com.health.script;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.DatabaseException;
import com.agiserver.helper.common.AbstractBaseAgiScript;

/**
 * 
 * @author Yasir Ashfaq
 */

public class SaveBookMark extends AbstractBaseAgiScript {
	public SaveBookMark() throws DatabaseException {
		super(true);
	}

	@Override
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			Integer duration = Integer.parseInt(request
					.getParameter("DURATION"));
			Integer contentid = Integer.parseInt(request
					.getParameter("CONTENT_ID"));
			String contentname = String.valueOf(request
					.getParameter("CONTENT_NAME"));
			logger.debug("cellno = " + cellno + " And duration = " + duration);

			if (duration > 0) {
				String query = "INSERT INTO subscriber_content_history (cellno,content_id,content_name,dt,duration)  VALUES (?,?,?,now(),?)  ON DUPLICATE KEY UPDATE dt=now(),duration=?,content_id=?,content_name=?";
				DBHelper.getInstance().executeDml(query, super.getConnection(),
						cellno, contentid, contentname, duration, duration,
						contentid, contentname);
			}
			channel.setVariable("SAVE_BOOKMARK", "YES");
		} catch (DatabaseException databaseException) {
			logger.error(databaseException.getMessage(), databaseException);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

}
