package com.health.script;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class SaveSurveyResponse extends AbstractBaseAgiScript {
	private static final Logger logger = Logger
			.getLogger(SaveSurveyResponse.class);

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {

			String cellno = "0" + channel.getVariable("DB_CELL");
			String question = channel.getVariable("QUESTION");
			String answer = channel.getVariable("ANSWER");
			if (answer == null || answer.trim().length() <= 0) {
				answer = "NR";
			}
			// String lang = channel.getVariable("LANG");
			// String msisdn = "92" + channel.getVariable("DB_CLI");
			// String shortcode = channel.getVariable("EXTENTED_CODE");

			DBHelper.getInstance()
					.executeDml(
							"INSERT INTO survey_response (cellno, question, answer, dt) VALUES(?, ?, ?, NOW()) ON DUPLICATE KEY UPDATE dt=now()",
							super.getConnection(),
							new Object[] { cellno, question, answer });

			channel.verbose("Customer: " + cellno + "...Respons saved ", 0);

			// channel.setVariable("IS_SUB", "YES");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
