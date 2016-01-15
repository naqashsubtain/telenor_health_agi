package com.health.script;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.DatabaseException;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class CheckSessionStattus extends AbstractBaseAgiScript {
	private final static Logger log = Logger
			.getLogger(CheckSessionStattus.class);

	@Override
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		String scellno = "0" + channel.getVariable("DB_CLI");
		String msisdn = "92" + channel.getVariable("DB_CLI");
		String cellNumber = formatCellNumber(channel.getVariable("CALLER_NUM"));
		String cellno = "0" + cellNumber.substring(2);
		String curChannel = channel.getVariable("CHANNEL");
		String ismod = channel.getVariable("IS_ACTIVE");
		String event = request.getParameter("event");
		String user = request.getParameter("user");
		String unique_id = channel.getVariable("UNIQUEID");
		StringBuilder query = new StringBuilder(100);
		logger.debug("cellNumber: " + cellNumber + "user" + user + "event:"
				+ event);
		if (channel.getVariable("IS_MOD_MENU").equals("YES")) {
			channel.setVariable("IS_MOD_MENU", "YES");
			return;
		}
		boolean isRoomActive = false, isHighlightActive = false, isSessionActive = false;
		// //////////////start check room status/////////////////

		String dml = "";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			dml = "select * from conf_room where now() between conf_room_start_time and conf_room_end_time";

			// Fetch User from DB by Cell Number
			stmt = super.getConnection().createStatement();
			rs = stmt.executeQuery(dml);
			if (rs.next()) {
				isRoomActive = true;
				channel.setVariable("IS_CONF_ACTIVE", "YES");
				channel.setVariable("CONF_ROOM_TYPE",
						rs.getString("conf_room_type"));
				channel.setVariable("CONF_WELCOME_FILE",
						rs.getString("welcome_file_name"));
				channel.setVariable("CONF_CONNECT_FILE",
						rs.getString("connect_file_name"));
				channel.setVariable("CONF_WAIT_FILE",
						rs.getString("wait_file_name"));
				channel.setVariable("CONF_ROOM_NAME",
						rs.getString("conf_room_name"));
				channel.setVariable("IS_ANNOUNCEMENT_ACTIVE", "NO");
				channel.setVariable("IS_NOCELEB_ACTIVE", "NO");
				channel.setVariable("IS_CELEBBUSY_ACTIVE", "NO");
				channel.setVariable("IS_WAIT_ACTIVE", "NO");

			} else {
				channel.setVariable("IS_CONF_ACTIVE", "NO");
			}
		} catch (SQLException e) {
			logger.error(dml, e);
			// TODO Do error message logic
			throw new AgiException("Error setting user vars: " + dml, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ignore) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException ignore) {
				}
			}
		}

		// //////////end check room status//////////////

		// ////////start checking moderator status/////////////////
		if (isRoomActive) {
			try {
				List<Map<String, Object>> ListCATa = DBHelper.getInstance()
						.query("select * FROM subscriber where is_mod=?",
								super.getConnection(), new Object[] { 1 });
				if (ListCATa != null) {
					int j = 1;

					for (Map<String, Object> mapa : ListCATa) {

						String scellNo = mapa.get("cellno").toString();
						scellNo = "92" + scellNo.substring(1);
						List<Map<String, Object>> ListCAT = DBHelper
								.getInstance()
								.query("SELECT * FROM live_session where user_cellno = ?",
										super.getConnection(), scellNo);
						if (ListCAT != null) {
							int i = 1;
							for (Map<String, Object> map : ListCAT) {
								System.out.println(map.get("is_online"));
								if (map.get("is_online").toString()
										.equals("true")) {
									channel.setVariable("IS_ACTIVE", "TRUE");
									isSessionActive = true;
									channel.verbose("Customer in local db: "
											+ cellno + ", is a Moderator", 0);
									logger.info("Cellno in colcal" + cellno
											+ " is valid Moderator");
									log.info("is_online:"
											+ i
											+ map.get("is_online").toString()
													.trim());
								}
								i++;
							}
						}
						j++;
					}
				} else {
					channel.setVariable("IS_ACTIVE", "FALSE");
				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		} else {
			channel.setVariable("IS_ACTIVE", "FALSE");
		}

		System.out.println(isSessionActive);
		if (isSessionActive) {
			channel.setVariable("IS_MOD_MENU", "YES");
		} else {
			channel.setVariable("IS_MOD_MENU", "NO");
		}
		// ///////end setting the menu to play////////////
	}
}
