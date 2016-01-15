package com.health.script;

import java.util.Map;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class CheckHealthAssistantSub extends AbstractBaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			logger.info("Customer: " + cellno + " checking Subscription");
			channel.setVariable("IS_HA_SUB", "NO");
			Map<String, Object> result = DBHelper
					.getInstance()
					.firstRow(
							"select * from health_assistant_subscriber where cellno = ? AND status = 100",
							super.getConnection(), new Object[] { cellno });
			if (result != null) {
				channel.setVariable("IS_HA_SUB", "YES");
				channel.setVariable("LANG", result.get("lang").toString());
				logger.info("Cellno in colcal" + cellno + " is valid subsciber");
				return;
			}
			// String response = Util
			// .makeWebServiceRequest("subscriber_detail?msisdn=" + msisdn);
			// logger.debug("subscriber_detail response: " + response);
			//
			// if (response != null) {
			// String[] params = response.split(",");
			// logger.debug("params: " + params);
			// if (params.length > 0) {
			// channel.setVariable("IS_HA_SUB",
			// params[3].equals("true") ? "YES" : "NO");
			// }
			//
			// if (params.length > 1) {
			// channel.setVariable("IS_EXPIRED",
			// params[4].equals("true") ? "YES" : "NO");
			// }
			// }

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
