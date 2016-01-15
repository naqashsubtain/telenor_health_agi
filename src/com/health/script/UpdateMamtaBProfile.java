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

public class UpdateMamtaBProfile extends AbstractBaseAgiScript {

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String prid = channel.getVariable("prid");
			System.out.println(prid);
			int pid = Integer.parseInt(prid);
			System.out.println(pid);

			String type = channel.getVariable("TYPE");
			if (type.equals("1")) {
				DBHelper.getInstance().executeDml(
						"delete from mamta_alerts_profiles where id=?",
						super.getConnection(), new Object[] { pid });

			} else if (type.equals("2")) {
				String gender = channel.getVariable("GENDER");
				DBHelper.getInstance()
						.executeDml(
								"UPDATE mamta_alerts_profiles set  gender=? WHERE id=?",
								super.getConnection(),
								new Object[] { gender, pid });
			} else if (type.equals("3")) {
				String ub_year = channel.getVariable("B_YEAR");
				String ub_month = channel.getVariable("B_MONTH");
				String ub_day = channel.getVariable("B_DAY");
				System.out.println(ub_year);
				System.out.println(ub_month);
				System.out.println(ub_day);
				String ub_date = ub_day + "-" + ub_month + "-" + ub_year;
				System.out.println(ub_date);
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = dateFormat.parse(ub_date);
				long time = date.getTime();
				Timestamp ub_d = new Timestamp(time);
				System.out.println(ub_d.toString());
				System.out.println(ub_d);
				DBHelper.getInstance()
						.executeDml(
								"UPDATE mamta_alerts_profiles set  born_date=? WHERE id=?",
								super.getConnection(),
								new Object[] { ub_d, pid });
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
