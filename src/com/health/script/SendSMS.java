package com.health.script;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.Util;

public class SendSMS extends AbstractBaseAgiScript {

	public void service(AgiRequest arg0, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String lang = channel.getVariable("LANG");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			String shortcode = channel.getVariable("EXTENTED_CODE");
			
			String message_key = channel.getVariable("SMS_KEY");
			System.out.println(shortcode);
			channel.setVariable("SMS_SEND", "NO");
			String response = Util.makeWebServiceRequest("send_message?msisdn="
					+ msisdn + "&shortcode=" + shortcode + "&message_key="
					+ message_key + "&language=" + lang);

			logger.debug("subscribe response: " + response);
			if (response != null) {

				String[] params = response.split(",");
				logger.debug("response: " + response);
				if (params.length > 0) {
					channel.setVariable("SMS_SEND",
							params[0].equals("1") ? "YES" : "NO");
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
