package com.health.script;

import java.util.Map;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class CheckMamtaProfile extends AbstractBaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			logger.info("Customer: " + cellno + " checking Subscription");
			channel.setVariable("IS_MA_SUB", "NO");

			Map<String, Object> result = DBHelper
					.getInstance()
					.firstRow(
							"select * from mamta_alerts_profiles where cellno = ? AND status = 100",
							super.getConnection(), new Object[] { cellno });

			if (result != null) {

				// DBHelper.getInstance()
				// .executeDml(
				// "UPDATE family_first_subscriber SET last_call_dt = NOW() WHERE cellno = ?",
				// super.getConnection(), new Object[] { cellno });
				// channel.verbose("Customer in local db: " + cellno
				// + ", is a sunscriber", 0);
				channel.setVariable("IS_MA_SUB", "YES");
				channel.setVariable("LANG", result.get("lang").toString());
				logger.info("Cellno in colcal" + cellno + " is valid subsciber");
				return;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
