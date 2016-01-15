package com.health.script;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.Util;

public class checkingIsSub extends AbstractBaseAgiScript {
	private static final Logger logger = Logger.getLogger(checkingIsSub.class);
	private boolean isRoomActive = false;

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			logger.info("Customer: " + cellno + " checking Subscription");
			//channel.setVariable("IS_SUB", "NO");
			//////////////////subscribe_cheat/////////////////
			channel.setVariable("IS_SUB", "YES");
			///////////////////////////////////////////////
			channel.setVariable("IS_LANG", "NO");
			channel.setVariable("IS_MOD_MENU", "NO");
			channel.setVariable("IS_MOD", "FALSE");
				
			// service call to subscibe
//			String response = Util
//					.makeWebServiceRequest("subscriber_detail?msisdn=" + msisdn);
//			logger.debug("subscriber_detail response: " + response);
//
//			if (response != null) {
//				String[] params = response.split(",");
//				logger.debug("params: " + params[3]);
//				String[] sub_info = params[3].split(":");
//				if (params.length > 0) {
//					logger.debug("subscriber_detail subscription response: "
//							+ sub_info[1]);
//					channel.setVariable("IS_SUB",
//							sub_info[1].equals("true") ? "YES" : "NO");
//				}
//				String[] exp_info = params[3].split(":");
//				if (params.length > 1) {
//					channel.setVariable("IS_EXPIRED",
//							exp_info[1].equals("true") ? "YES" : "NO");
//				}
//			}

			Map<String, Object> result = DBHelper
					.getInstance()
					.firstRow(
							"select * from subscriber where cellno = ? AND status = 100",
							super.getConnection(), new Object[] { cellno });

			if (result != null) {

				boolean isMod = ((Boolean) result.get("is_mod")).booleanValue();
				if (isMod) {
					channel.setVariable("IS_MOD", "TRUE");
					String dml = "";
					Statement stmt = null;
					ResultSet rs = null;
					try {
						dml = "select * from conf_room where now() between conf_room_start_time and conf_room_end_time";

						stmt = super.getConnection().createStatement();
						rs = stmt.executeQuery(dml);
						if (rs.next()) {
							this.isRoomActive = true;
							channel.setVariable("IS_MOD_MENU", "YES");
						}
					} catch (SQLException e) {
						logger.error(dml, e);

						throw new AgiException("Error setting user vars: "
								+ dml, e);
					} finally {
						if (rs != null) {
							try {
								rs.close();
							} catch (SQLException localSQLException1) {
							}
						}
						if (stmt != null) {
							try {
								stmt.close();
							} catch (SQLException localSQLException2) {
							}
						}
					}
					channel.setVariable("IS_MOD", "TRUE");
					channel.verbose("Customer in local db: " + cellno
							+ ", is a Moderator", 0);
					logger.info("Cellno in colcal" + cellno
							+ " is valid Moderator");
				} else {
					channel.setVariable("IS_MOD", "FALSE");
				}

				switch (result.get("is_lang_set").toString()) {
				case "0":
					channel.setVariable("IS_LANG", "NO");
					break;
				case "1":
					channel.setVariable("IS_LANG", "YES");
					break;
				}
				System.out.println(result.get("is_lang_set"));
				DBHelper.getInstance()
						.executeDml(
								"UPDATE subscriber SET last_call_dt = NOW() WHERE cellno = ?",
								super.getConnection(), new Object[] { cellno });
				channel.verbose("Customer in local db: " + cellno
						+ ", is a sunscriber", 0);
				// channel.setVariable("IS_SUB", "YES");
				channel.setVariable("LANG", result.get("lang").toString());
				logger.info("Cellno in colcal" + cellno + " is valid subsciber");
				return;
			}

			// channel.verbose("Customer: " + cellno + ", is not a sunscriber",
			// 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
