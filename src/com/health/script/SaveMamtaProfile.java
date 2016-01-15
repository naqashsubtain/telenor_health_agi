package com.health.script;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;
import com.health.common.Util;

public class SaveMamtaProfile extends AbstractBaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String lang = channel.getVariable("LANG");
			String born = channel.getVariable("BORN");
			String gender = "MALE";
			String msisdn = "92" + channel.getVariable("DB_CLI");
			System.out.println(cellno);
			String profile_type = "";
			channel.setVariable("MA_SUB", "NO");
			System.out.println("hello");
			String birth_date = null;
			if (born.equals("0")) {
				String ub_year = channel.getVariable("UB_YEAR");
				String ub_month = channel.getVariable("UB_MONTH");
				String ub_day = channel.getVariable("UB_DAY");
				String ub_date = ub_day + "-" + ub_month + "-" + ub_year;
				System.out.println(ub_date);
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = dateFormat.parse(ub_date);
				long time = date.getTime();
				Timestamp ub_d = new Timestamp(time);
				System.out.println(ub_d);
				birth_date = ub_date;
				profile_type = "Unborn";
				DBHelper.getInstance()
						.executeDml(
								"INSERT INTO mamta_alerts_profiles (cellno, is_born, gender,  born_date, status) VALUES(?, ?, ?, ?, 100) ON DUPLICATE KEY UPDATE status=100",
								super.getConnection(),
								new Object[] { cellno, born, gender, ub_d });
			} else if (born.equals("1")) {
				gender = channel.getVariable("GENDER");
				String b_year = channel.getVariable("B_YEAR");
				String b_month = channel.getVariable("B_MONTH");
				String b_day = channel.getVariable("B_DAY");
				String b_date = b_day + "-" + b_month + "-" + b_year;
				System.out.println(b_date);
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = dateFormat.parse(b_date);
				long time = date.getTime();
				Timestamp b_d = new Timestamp(time);
				System.out.println(b_d);
				birth_date = b_date;
				profile_type = "Born";
				DBHelper.getInstance()
						.executeDml(
								"INSERT INTO mamta_alerts_profiles (cellno, is_born, gender,  born_date, status) VALUES(?, ?, ?, ?, 100) ON DUPLICATE KEY UPDATE status=100",
								super.getConnection(),
								new Object[] { cellno, born, gender, b_d });
			}
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = dateFormat.parse(birth_date);

			String response = Util.makeWebServiceRequest("profile?msisdn="
					+ msisdn + "&profile_type=" + profile_type
					+ "&child_gender=" + gender + "&birth_date="
					+ date.toString());

			logger.debug("subscribe response: " + response);
			if (response != null) {

				String[] params = response.split(",");
				logger.debug("response: " + response);
				if (params.length > 0) {
					channel.setVariable("MA_SUB", params[0].equals("1") ? "YES"
							: "NO");
				}

			}
			channel.verbose("Profile: " + cellno + ", has Saved ", 0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
