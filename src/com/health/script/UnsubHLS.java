package com.health.script;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.SMSSender;

public class UnsubHLS extends AbstractBaseAgiScript {

	private static final Logger logger = Logger.getLogger(UnSubFF.class);
	SMSSender smsSender = new SMSSender();

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String cellno = "0" + channel.getVariable("DB_CLI");
		String shortcode = channel.getVariable("EXTENTED_CODE");
		String msisdn = "92" + channel.getVariable("DB_CLI");
		String lang = channel.getVariable("LANG");
		channel.setVariable("IS_HLS_UNSUB", "NO");
		logger.info("Customer: " + cellno + " checking UnSubscribed");
		try {
			// if(code.contains("Code=0")){
			//
			// String response = null;
			// try {
			// response = Util.makeWebServiceRequest("unsubscribe?msisdn="
			// + msisdn);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// logger.debug("{error} " + e.getMessage());
			// e.printStackTrace();
			// }
			// logger.debug("unsubscribe response: " + response);
			// if (response != null) {
			//
			// String[] params = response.split(",");
			// logger.debug("response: " + response);
			// if (params.length > 0) {
			// smsSender.sendSMS(msisdn, shortcode,
			// "HEALTHY_LIFESTYLE_UNSUB", lang);
			// channel.setVariable("IS_HLS_UNSUB",
			// params[0].equals("1") ? "YES" : "NO");
			// }
			// }

			DBHelper.getInstance()
					.executeDml(
							"Update healthy_lifestyle_sub set status=-100 where cellno=?",
							super.getConnection(), new Object[] { cellno });
			channel.setVariable("IS_HLS_UNSUB", "YES");
			logger.info(cellno + " unsub successfully ");

			//

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	// public void service(AgiRequest request, AgiChannel channel)
	// throws AgiException {
	//
	// String cellno = "0" + channel.getVariable("DB_CLI");
	//
	// String msisdn = "92" + channel.getVariable("DB_CLI");
	// logger.info("Customer: " + cellno + " checking UnSubscribed");
	// unsub(cellno);
	//
	// }
	//
	// public void unsub(String cellno) {
	//
	// System.out.println("unsub:" + cellno);
	// try {
	// // if(code.contains("Code=0")){
	//
	// DBHelper.getInstance()
	// .executeDml(
	// "Update healthy_lifestyle_sub set status=-100 where cellno=?",
	// super.getConnection(), new Object[] { cellno });
	// logger.info(cellno + " unsub successfully ");
	//
	// //
	//
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// }
	//
	// }
}
