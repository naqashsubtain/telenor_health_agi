package com.health.script;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.DatabaseException;
import com.agiserver.helper.common.AbstractBaseAgiScript;

/**
 * 
 * @author ABDUL MATEEN
 */

public class GettingTvContents extends AbstractBaseAgiScript {
	private final static Logger logger = Logger
			.getLogger(GettingTvContents.class);

	public GettingTvContents() throws DatabaseException {
		super(true);
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			logger.debug("Cellno = " + cellno);

			int cat_id = Integer.parseInt(request.getParameter("cid"));

			List<Map<String, Object>> result = DBHelper.getInstance().query(
					"SELECT * FROM tv_contents where cat_id=?",
					super.getConnection(), new Object[] { cat_id });
			if (result != null && !result.isEmpty()) {
				int i = 1;
				for (Map<String, Object> map : result) {
					channel.setVariable("ID_" + i, map.get("id").toString());
					channel.setVariable("CONTENT_NAME_" + i,
							map.get("content_name").toString());
					channel.setVariable("FILE_NAME_" + i,
							map.get("content_file").toString());
					System.out.println(map.get("content_file").toString());
					i++;
				}
				channel.setVariable("TOTAL_RESULT", "" + result.size());
				return;
			}
		} catch (DatabaseException databaseException) {
			logger.error(databaseException.getMessage(), databaseException);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		channel.setVariable("TOTAL_RESULT", "0");

	}
}
