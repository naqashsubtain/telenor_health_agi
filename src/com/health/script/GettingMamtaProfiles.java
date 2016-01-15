package com.health.script;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class GettingMamtaProfiles extends AbstractBaseAgiScript {
	private final static Logger log = Logger
			.getLogger(GettingMamtaProfiles.class);

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			String cat = channel.getVariable("BORN");

			String context = request.getContext();

			log.debug("From caller Context:   " + request.getContext());
			log.debug("From caller cat selected   cat: " + cat);

			List<Map<String, Object>> result = DBHelper
					.getInstance()
					.query("select * from mamta_alerts_profiles where is_born=? and cellno=?",
							super.getConnection(), new Object[] { cat, cellno });

			if (result != null) {
				int i = 1;
				for (Map<String, Object> map : result) {

					Timestamp borndate = (Timestamp) map.get("born_date");
					channel.setVariable("born_date" + i, borndate.toGMTString());
					String dob = borndate.toString().substring(0, 11);
					System.out.println(dob);
					channel.setVariable("pid" + i, "" + map.get("id"));
					System.out.println(dob.substring(8, 10));
					System.out.println(dob.substring(5, 7));
					System.out.println(dob.substring(0, 4));
					channel.setVariable("bday" + i, dob.substring(8, 10));
					channel.setVariable("bmonth" + i, dob.substring(5, 7));
					channel.setVariable("byear" + i, dob.substring(0, 4));
					i++;
				}
				int size = result.size();
				channel.setVariable("TOTAL_PROFILES", "" + size);
				channel.setVariable("HAVE_PROFILE", "YES");
			} else {
				channel.setVariable("HAVE_PROFILE", "NO");
				channel.setVariable("TOTAL_PROFILES", "0");
			}

		} catch (Exception e) {
			channel.setVariable("HAVE_PROFILE", "NO");
			channel.setVariable("TOTAL_PROFILES", "0");
			channel.verbose(
					"Exception: [" + e.getClass() + "] " + e.getMessage(), 0);
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {

		}

	}

}