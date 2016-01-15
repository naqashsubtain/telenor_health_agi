package com.health.script;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.SMSSender;
import com.health.common.Util;

public class saveSubConfirmation extends AbstractBaseAgiScript {
	private static final Logger logger = Logger
			.getLogger(saveSubConfirmation.class);
	SMSSender smsSender = new SMSSender();

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			String lang = channel.getVariable("LANG");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			String shortcode = channel.getVariable("EXTENTED_CODE");

			channel.setVariable("IS_SUB", "NO");
			channel.setVariable("IS_LANG", "NO");
			String sub_from = channel.getVariable("SUB_FROM");
			String su_params = "subscribe?msisdn=" + msisdn;
			// + "&type_of_subscription=new";
			if (sub_from != null && sub_from.equals("OBD")) {
				su_params = "subscribe?msisdn=" + msisdn
						+ "&subscribed_through_obd=true";
			}
			String response = Util.makeWebServiceRequest(su_params);
			logger.debug("subscribe response: " + response);
			if (response != null) {

				String[] params = response.split(",");
				// logger.debug("params: " + params[3]);
				String[] sub_info = params[0].split(":");
				logger.debug("response: " + response);
				logger.debug("sub info: " + sub_info);
				if (params.length > 0) {
					channel.setVariable("IS_SUB",
							sub_info[1].equals("1") ? "YES" : "NO");
					smsSender.sendSMS(msisdn, shortcode,
							"HEALTHY_LIFESTYLE_SUB", lang);
					smsSender.sendSMS(msisdn, shortcode,
							"HEALTHY_LIFESTYLE_INSTRUCTIONS", lang);
				}
			}

			DBHelper.getInstance()
					.executeDml(
							"INSERT INTO subscriber (cellno, sub_dt, lang,is_lang_set, status, last_call_dt) VALUES(?, NOW(), ?, 0, 100, NOW()) ON DUPLICATE KEY UPDATE status=100, last_call_dt=now(),lang=?",
							super.getConnection(),
							new Object[] { cellno, lang, lang });

			channel.verbose("Customer: " + cellno + ", has subscribed ", 0);

			// channel.setVariable("IS_SUB", "YES");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
