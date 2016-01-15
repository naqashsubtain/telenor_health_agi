package com.health.script;

import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class CheckHistStatus extends AbstractBaseAgiScript {
	private static final Logger logger = Logger
			.getLogger(CheckHistStatus.class);

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			logger.info("Customer: " + cellno + " checking Subscription");
			channel.setVariable("IS_HIST", "NO");

			Map<String, Object> result = DBHelper
					.getInstance()
					.firstRow(
							"select * from subscriber_content_history where cellno = ?",
							super.getConnection(), new Object[] { cellno });
			if (result != null) {
				channel.setVariable("IS_HIST", "YES");
				// channel.setVariable("ID_" + 1, result.get("content_id")
				// .toString());
				// channel.setVariable("FILE_NAME_" + 1, result
				// .get("content_name").toString());
				// channel.setVariable("CONTENT_DUR_" + 1,
				// result.get("duration")
				// .toString());
				System.out.println(result.get("content_name").toString());
				channel.verbose("Customer in local db: " + cellno
						+ ", is a sunscriber", 0);

				logger.info("Cellno in colcal" + cellno + " have book marks");
				return;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
