package com.health.script;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.common.AbstractBaseAgiScript;

public class ValidateProfiles extends AbstractBaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String v_type = channel.getVariable("V_TYPE");
			String msisdn = "92" + channel.getVariable("DB_CLI");
			System.out.println(cellno);
			Date cur_date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.format(cur_date);
			int cur_day = Integer.parseInt(dateFormat.format(cur_date)
					.substring(0, 2));
			int cur_month = Integer.parseInt(dateFormat.format(cur_date)
					.substring(3, 5));
			int cur_year = Integer.parseInt(dateFormat.format(cur_date)
					.substring(6, 10));
			if (v_type.equals("B_YEAR")) {
				String b_year = channel.getVariable("B_YEAR");
				System.out.println(cur_year);
				channel.setVariable("B_YEAR_C", "NO");
				if (b_year.length() > 0 && b_year.length() == 4) {
					System.out.println("1");
					int int_year = Integer.parseInt(b_year);
					System.out.println(int_year);
					if (int_year != 0000) {

						if (int_year <= cur_year && int_year >= (cur_year - 2)) {
							System.out.println("2");
							channel.setVariable("B_YEAR_C", "YES");
						}
					}
				}

			} else if (v_type.equals("B_MONTH")) {
				String b_month = channel.getVariable("B_MONTH");
				channel.setVariable("B_MONTH_C", "NO");
				if (b_month.length() > 0 && b_month.length() <= 2) {
					int int_month = Integer.parseInt(b_month);
					if (int_month != 00 && int_month != 0) {
						if (int_month > 0 && int_month <= 12) {
							if (b_month.length() == 1)
								channel.setVariable("B_MONTH", "0" + b_month);
							channel.setVariable("B_MONTH_C", "YES");
						}
					}
				}
			} else if (v_type.equals("B_DAY")) {
				String b_month = channel.getVariable("B_MONTH");
				String b_day = channel.getVariable("B_DAY");
				channel.setVariable("B_DAY_C", "NO");
				boolean valid = true;
				if (b_day.length() > 0 && b_day.length() <= 2) {

					int int_day = Integer.parseInt(b_day);
					if (int_day != 00 && int_day != 0) {

						if (int_day > 0 && int_day <= 31) {
							if (b_month.equals("04") || b_month.equals("06")
									|| b_month.equals("09")
									|| b_month.equals("11")) {
								if (int_day > 30)
									valid = false;
							} else if (b_month.equals("02")) {
								if (int_day > 29)
									valid = false;
							}

							if (valid) {
								if (b_day.length() == 1)
									channel.setVariable("B_DAY", "0" + b_day);
								channel.setVariable("B_DAY_C", "YES");
							}
						}
					}
				}
			} else if (v_type.equals("UB_YEAR")) {
				String ub_year = channel.getVariable("UB_YEAR");
				System.out.println(cur_year);
				channel.setVariable("UB_YEAR_C", "NO");
				if (ub_year.length() > 0 && ub_year.length() == 4) {
					System.out.println("1");
					int int_year = Integer.parseInt(ub_year);
					if (int_year != 0000) {
						System.out.println(int_year);
						if (int_year >= cur_year && int_year <= (cur_year + 1)) {
							System.out.println("2");
							channel.setVariable("UB_YEAR_C", "YES");
						}
					}
				}
			} else if (v_type.equals("UB_MONTH")) {
				String ub_month = channel.getVariable("UB_MONTH");
				System.out.println(cur_year);
				channel.setVariable("UB_YEAR_C", "NO");
				if (ub_month.length() > 0 && ub_month.length() <= 2) {
					System.out.println("1");
					int int_month = Integer.parseInt(ub_month);
					if (int_month != 00 && int_month != 0) {
						System.out.println(int_month);
						if (int_month > 0 && int_month <= 12) {
							if (ub_month.length() == 1)
								channel.setVariable("UB_MONTH", "0" + ub_month);
							System.out.println("2");
							channel.setVariable("UB_MONTH_C", "YES");
						}
					}
				}
			} else if (v_type.equals("UB_DAY")) {
				String ub_month = channel.getVariable("UB_MONTH");
				String ub_day = channel.getVariable("UB_DAY");
				System.out.println(cur_year);
				boolean valid = true;
				channel.setVariable("UB_DAY_C", "NO");
				if (ub_day.length() > 0 && ub_day.length() <= 2) {
					int int_day = Integer.parseInt(ub_day);
					if (int_day != 00 && int_day != 0) {
						if (int_day > 0 && int_day <= 31) {
							if (ub_month.equals("04") || ub_month.equals("06")
									|| ub_month.equals("09")
									|| ub_month.equals("11")) {
								if (int_day > 30)
									valid = false;
							} else if (ub_month.equals("02")) {
								if (int_day > 29)
									valid = false;
							}

							if (valid) {
								if (ub_day.length() == 1)
									channel.setVariable("UB_DAY", "0" + ub_day);
								channel.setVariable("UB_DAY_C", "YES");
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
