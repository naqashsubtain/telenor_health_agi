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

public class GettingTvCategory extends AbstractBaseAgiScript {
	private final static Logger logger = Logger
			.getLogger(GettingTvCategory.class);

	public GettingTvCategory() throws DatabaseException {
		super(true);
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			logger.debug("Cellno = " + cellno);

			List<Map<String, Object>> result = DBHelper.getInstance().query(
					"SELECT * FROM tv_category", super.getConnection());
			if (result != null && !result.isEmpty()) {
				int i = 1;
				for (Map<String, Object> map : result) {
					channel.setVariable("CAT_ID_" + i, map.get("id").toString());
					channel.setVariable("CAT_NAME_" + i, map.get("cat_name")
							.toString());
					channel.setVariable("FILE_NAME_" + i, map.get("cat_file")
							.toString());
					System.out.println(map.get("cat_file").toString());
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
