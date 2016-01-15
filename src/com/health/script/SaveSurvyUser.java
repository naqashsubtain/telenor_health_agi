package com.health.script;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class SaveSurvyUser extends AbstractBaseAgiScript {
	private static final Logger logger = Logger.getLogger(SaveSurvyUser.class);

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			// String lang = channel.getVariable("LANG");
			// String msisdn = "92" + channel.getVariable("DB_CLI");
			// String shortcode = channel.getVariable("EXTENTED_CODE");

			DBHelper.getInstance()
					.executeDml(
							"INSERT INTO subscriber_survey_received (cellno, dt) VALUES(?, NOW()) ON DUPLICATE KEY UPDATE dt=now()",
							super.getConnection(), new Object[] { cellno });

			channel.verbose("Customer: " + cellno + "...Respons saved ", 0);

			// channel.setVariable("IS_SUB", "YES");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
