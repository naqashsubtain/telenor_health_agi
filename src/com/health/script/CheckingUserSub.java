package com.health.script;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.Util;

public class CheckingUserSub extends AbstractBaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			logger.info("Customer: " + cellno + " checking Subscription");
			channel.setVariable("IS_ANY_SUB", "NO");
			// service call to subscibe
			String response = Util
					.makeWebServiceRequest("subscriber_detail?msisdn=" + msisdn);
			logger.debug("subscriber_detail response: " + response);

			if (response != null) {
				String[] params = response.split(",");
				logger.debug("params: " + params[3]);
				String[] sub_info = params[3].split(":");
				if (params.length > 0) {
					logger.debug("subscriber_detail subscription response: "
							+ sub_info[1]);
					channel.setVariable("IS_ANY_SUB",
							sub_info[1].equals("true") ? "YES" : "NO");
				}
				String[] exp_info = params[3].split(":");
				if (params.length > 1) {
					channel.setVariable("IS_EXPIRED",
							exp_info[1].equals("true") ? "YES" : "NO");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(), ex);
		}

	}

}
