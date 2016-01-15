package com.health.script;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.SMSSender;
import com.health.common.Util;

public class UnSubscribed extends AbstractBaseAgiScript {
	private static final Logger logger = Logger.getLogger(UnSubscribed.class);
	SMSSender smsSender = new SMSSender();

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String cellno = "0" + channel.getVariable("DB_CLI");
		String shortcode = channel.getVariable("EXTENTED_CODE");
		String msisdn = "92" + channel.getVariable("DB_CLI");
		String lang = channel.getVariable("LANG");
		channel.setVariable("IS_UNSUB", "NO");

		String response = null;
		try {
			response = Util.makeWebServiceRequest("unsubscribe?msisdn="
					+ msisdn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("{error} " + e.getMessage());
			e.printStackTrace();
		}
		logger.debug("unsubscribe response: " + response);
		if (response != null) {

			String[] params = response.split(",");
			logger.debug("response: " + response);
			if (params.length > 0) {
				smsSender
						.sendSMS(msisdn, shortcode, "FAMILY_FIRST_UNSUB", lang);
				channel.setVariable("IS_UNSUB", params[0].equals("1") ? "YES"
						: "NO");
			}
		}

		logger.info("Customer: " + cellno + " checking UnSubscribed");
		try {
			DBHelper.getInstance()
					.executeDml(
							"Update subscriber set status=-100, , is_lang_set=0 where cellno=?",
							super.getConnection(), new Object[] { cellno });
			// channel.setVariable("IS_UNSUB", "YES");
			logger.info(cellno + " unsub successfully ");

			//

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
