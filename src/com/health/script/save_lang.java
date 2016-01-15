package com.health.script;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class save_lang extends AbstractBaseAgiScript {
	private static final Logger logger = Logger.getLogger(save_lang.class);

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			String lang = channel.getVariable("LANG");
			// Integer rs = DBHelper
			// .getInstance()
			// .executeDml(
			// "INSERT INTO subscriber (cellno, sub_dt, lang, status, last_call_dt) VALUES(?, NOW(), ?, 100, NOW()) ON DUPLICATE KEY UPDATE status=100, last_call_dt=now(),lang=?",
			// super.getConnection(),
			// new Object[] { cellno, lang, lang });
			Integer rs = DBHelper
					.getInstance()
					.executeDml(
							"UPDATE subscriber SET last_call_dt = NOW(), lang=?, is_lang_set=1  WHERE cellno = ?",
							super.getConnection(),
							new Object[] { lang, cellno });
			channel.setVariable("IS_LANG", "YES");
			if (rs.intValue() > 0) {
				logger.info(cellno + " language save successfully ");
			} else {
				logger.error(cellno + " save language failed ");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
