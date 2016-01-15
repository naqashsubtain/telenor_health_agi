package com.health.script;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DBHelper;
import com.agiserver.helper.common.AbstractBaseAgiScript;

/**
 * 
 * @author ABDUL MATEEN
 */
public class GetHealthTvContents extends AbstractBaseAgiScript {
	private final static Logger log = Logger
			.getLogger(GetHealthTvContents.class);

	@Override
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		// TODO Auto-generated method stub
		try {

			String cellno = "0" + channel.getVariable("DB_CLI");
			String contentType = channel.getVariable("GET_CONTENT_TYPE");
			String currentContentId = channel.getVariable("CURRENT_CONTENT_ID");

			String context = request.getContext();

			log.debug("From caller Context:   " + request.getContext());
			log.debug("From  contentType:   " + contentType);

			if (contentType.equals("BOOKMARKED")) {
				List<Map<String, Object>> result = DBHelper
						.getInstance()
						.query("SELECT * FROM subscriber_content_history where cellno=?",
								super.getConnection(), new Object[] { cellno });

				if (result != null) {
					int i = 1;
					for (Map<String, Object> map : result) {

						String filename = (String) map.get("content_name");
						Integer duration = (Integer) map.get("duration");
						Integer contentId = (Integer) map.get("content_id");
						channel.setVariable("file" + i, filename);
						channel.setVariable("File_Name" + i, filename);
						channel.setVariable("content_id" + i, "" + contentId);
						channel.setVariable("CONTENT_DUR" + i, "" + duration);

						i++;
					}
					int size = result.size();
					channel.setVariable("TOTAL_FILES", "" + size);
				} else {
					channel.setVariable("TOTAL_FILES", "0");
				}

			} else if (contentType.equals("FILE_START")) {
				// List<Map<String, Object>> result = DBHelper
				// .getInstance()
				// .query("select b.duration,b.content_id,c.content_file,c.id from subscriber_content_history b ,health_tv_content c where b.content_id=c.id  and b.cellno=? order by b.dt desc limit 1",
				// super.getConnection(), new Object[] { cellno });select * from
				// health_tv_content where dt >= DATE_SUB(CURDATE(), INTERVAL
				// DAYOFWEEK(CURDATE())-1 DAY) order by dt",
				List<Map<String, Object>> result = DBHelper
						.getInstance()
						.query("select * from health_tv_content order by dt desc",
								super.getConnection(), new Object[] { cellno });

				if (result != null) {
					int i = 1;
					for (Map<String, Object> map : result) {

						String filename = (String) map.get("content_file");
						Integer contentId = (Integer) map.get("content_id");
						Integer duration = (Integer) map.get("duration");
						channel.setVariable("file" + i, filename);
						channel.setVariable("File_Name" + i, filename);
						channel.setVariable("content_id" + i, "" + contentId);
						channel.setVariable("CONTENT_DUR", "" + 0);

						i++;
					}
					int size = result.size();
					channel.setVariable("TOTAL_FILES", "" + size);
				} else {
					channel.setVariable("TOTAL_FILES", "0");
				}

			} else if (contentType.equals("FROM_START")) {
				List<Map<String, Object>> result = DBHelper
						.getInstance()
						.query("select * from health_tv_content order by dt desc",
								super.getConnection(), new Object[] {});
				// select * from health_tv_content where dt >=
				// DATE_SUB(CURDATE(), INTERVAL DAYOFWEEK(CURDATE())-1 DAY)
				// order by dt

				if (result != null) {
					int i = 1;
					for (Map<String, Object> map : result) {

						String filename = (String) map.get("content_file");
						Integer contentId = (Integer) map.get("id");
						channel.setVariable("file" + i, filename);
						channel.setVariable("File_Name" + i, filename);
						channel.setVariable("content_id" + i, "" + contentId);
						channel.setVariable("CONTENT_DUR", "" + 0);

						i++;
					}
					int size = result.size();
					channel.setVariable("TOTAL_FILES", "" + size);
				} else {
					channel.setVariable("TOTAL_FILES", "0");
				}

			} else if (contentType.equals("TODAY_START")) {
				List<Map<String, Object>> result = DBHelper
						.getInstance()
						.query("select * from health_tv_content order by dt desc",
								super.getConnection(), new Object[] {});

				if (result != null) {
					int i = 1;
					for (Map<String, Object> map : result) {

						String filename = (String) map.get("content_file");
						Integer contentId = (Integer) map.get("id");
						channel.setVariable("file" + i, filename);
						channel.setVariable("File_Name" + i, filename);
						channel.setVariable("content_id" + i, "" + contentId);
						channel.setVariable("CONTENT_DUR", "" + 0);

						i++;
					}
					int size = result.size();
					channel.setVariable("TOTAL_FILES", "" + size);
				} else {
					channel.setVariable("TOTAL_FILES", "0");
				}

			} else if (contentType.equals("YESTERDAY_START")) {
				List<Map<String, Object>> result = DBHelper
						.getInstance()
						.query("select * from health_tv_content order by dt desc",
								super.getConnection(), new Object[] {});

				if (result != null) {
					int i = 1;
					for (Map<String, Object> map : result) {
						if (i == 1) {
							String filename = (String) map.get("content_file");
							Integer contentId = (Integer) map.get("id");
							channel.setVariable("file" + i, filename);
							channel.setVariable("File_Name" + i, filename);
							channel.setVariable("content_id" + i, ""
									+ contentId);
							channel.setVariable("CONTENT_DUR", "" + 0);
						}
						i++;
					}
					int size = result.size();
					channel.setVariable("TOTAL_FILES", "" + size);
				} else {
					channel.setVariable("TOTAL_FILES", "0");
				}

			}

		} catch (Exception e) {
			channel.setVariable("HAVE_CAT", "NO");
			channel.setVariable("TOTAL_FILES", "0");
			channel.verbose(
					"Exception: [" + e.getClass() + "] " + e.getMessage(), 0);
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {

		}

	}

}

// public class GetHealthTvContents extends AbstractBaseAgiScript {
// private final static Logger logger = Logger
// .getLogger(GetHealthTvContents.class);
//
// public GetHealthTvContents() throws DatabaseException {
// super(true);
// }
//
// public void service(AgiRequest request, AgiChannel channel)
// throws AgiException {
// try {
// String contentType = channel.getVariable("GET_CONTENT_TYPE");
// String cellno = "0" + channel.getVariable("DB_CLI");
// channel.setVariable("TOTAL_RESULT", "" + 0);
// logger.debug("Cellno = " + cellno);
// if (contentType.equals("WEEK_CONTENT")) {
// List<Map<String, Object>> result = DBHelper
// .getInstance()
// .query("select * from health_tv_content where dt >= DATE_SUB(CURDATE(), INTERVAL DAYOFWEEK(CURDATE())-1 DAY)",
// super.getConnection(), new Object[] {});
// WHERE dt >= curdate() - INTERVAL DAYOFWEEK(curdate())+6 DAY
// AND dt < curdate() - INTERVAL DAYOFWEEK(curdate())-1 DAY
// if (result != null && !result.isEmpty()) {
// int i = 1;
// for (Map<String, Object> map : result) {
// channel.setVariable("ID_" + i, map.get("id").toString());
// channel.setVariable("CONTENT_NAME_" + i,
// map.get("content_name").toString());
// channel.setVariable("FILE_NAME_" + i,
// map.get("content_file").toString());
// channel.setVariable("CONTENT_DUR_" + i, "" + 0);
// System.out.println(map.get("content_file").toString());
// i++;
// }
// channel.setVariable("TOTAL_RESULT", "" + result.size());
// return;
// }
// } else if (contentType.equals("YESTERDAY")) {
// List<Map<String, Object>> result = DBHelper
// .getInstance()
// .query("select * from health_tv_content where dt >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND dt < CURDATE()",
// super.getConnection(), new Object[] {});
// if (result != null && !result.isEmpty()) {
// int i = 1;
// for (Map<String, Object> map : result) {
// channel.setVariable("ID_" + i, map.get("id").toString());
// channel.setVariable("CONTENT_NAME_" + i,
// map.get("content_name").toString());
// channel.setVariable("FILE_NAME_" + i,
// map.get("content_file").toString());
// channel.setVariable("CONTENT_DUR_" + i, "" + 0);
// System.out.println(map.get("content_file").toString());
// i++;
// }
// channel.setVariable("TOTAL_RESULT", "" + result.size());
// return;
// }
// } else if (contentType.equals("TODAY")) {
// List<Map<String, Object>> result = DBHelper
// .getInstance()
// .query("select * from health_tv_content WHERE dt >= CURDATE() AND dt <= DATE_SUB(CURDATE(), INTERVAL -1 DAY)",
// super.getConnection(), new Object[] {});
// if (result != null && !result.isEmpty()) {
// int i = 1;
// for (Map<String, Object> map : result) {
// channel.setVariable("ID_" + i, map.get("id").toString());
// channel.setVariable("CONTENT_NAME_" + i,
// map.get("content_name").toString());
// channel.setVariable("FILE_NAME_" + i,
// map.get("content_file").toString());
// channel.setVariable("CONTENT_DUR_" + i, "" + 0);
// System.out.println(map.get("content_file").toString());
// i++;
// }
// channel.setVariable("TOTAL_RESULT", "" + result.size());
// return;
// }
// } else {
//
// }
// } catch (DatabaseException databaseException) {
// logger.error(databaseException.getMessage(), databaseException);
// } catch (Exception ex) {
// logger.error(ex.getMessage(), ex);
// }
//
// channel.setVariable("TOTAL_RESULT", "0");
//
// }
// }
