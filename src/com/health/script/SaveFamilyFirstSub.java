package com.health.script;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.SMSSender;

public class SaveFamilyFirstSub extends AbstractBaseAgiScript {
	private static final Logger logger = Logger
			.getLogger(saveSubConfirmation.class);
	SMSSender smsSender = new SMSSender();

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String lang = channel.getVariable("LANG");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			String shortcode = channel.getVariable("EXTENTED_CODE");
			channel.setVariable("FF_SUB", "NO");
			// String response = Util.makeWebServiceRequest("subscribe?msisdn="
			// + msisdn);
			//
			// // String response =
			// Util.makeWebServiceRequest("subscribe?msisdn="
			// // + msisdn + "&subscribed_through_obd=true");
			//
			// logger.debug("subscribe response: " + response);
			// if (response != null) {
			//
			// String[] params = response.split(",");
			// logger.debug("response: " + response);
			// if (params.length > 0) {
			// channel.setVariable("FF_SUB", params[0].equals("1") ? "YES"
			// : "NO");
			// smsSender.sendSMS(msisdn, shortcode, "FAMILY_FIRST_SUB",
			// lang);
			// smsSender.sendSMS(msisdn, shortcode,
			// "FAMILY_FIRST_INSTRUCTIONS", lang);
			// }
			//
			// }

			DBHelper.getInstance()
					.executeDml(
							"INSERT INTO family_first_subscriber (cellno, sub_date, lang, status) VALUES(?, NOW(), ?, 100) ON DUPLICATE KEY UPDATE status=100,lang=?",
							super.getConnection(),
							new Object[] { cellno, lang, lang });
			channel.setVariable("FF_SUB", "YES");
			channel.verbose("Customer: " + cellno + ", has subscribed ", 0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
