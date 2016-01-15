package com.health.script;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.DatabaseException;
import com.agiserver.helper.common.AbstractBaseAgiScript;

public class GetLifeStyleFiles extends AbstractBaseAgiScript {
	private final static Logger logger = Logger
			.getLogger(GetLifeStyleFiles.class);

	public GetLifeStyleFiles() throws DatabaseException {
		super(true);
	}

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {
			String cellno = "0" + channel.getVariable("DB_CLI");
			logger.debug("Cellno = " + cellno);
			channel.setVariable("TOTAL_RESULT", "" + 0);
			List<Map<String, Object>> result = DBHelper.getInstance().query(
					"SELECT * FROM life_style", super.getConnection(),
					new Object[] {});
			if (result != null && !result.isEmpty()) {
				int i = 1;
				for (Map<String, Object> map : result) {
					channel.setVariable("ID_" + i, map.get("id").toString());
					channel.setVariable("CONTENT_NAME_" + i, map.get("hs_name")
							.toString());
					channel.setVariable("FILE_NAME_" + i, map.get("hs_file")
							.toString());
					i++;
				}
				channel.setVariable("TOTAL_RESULT", "" + result.size());
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
