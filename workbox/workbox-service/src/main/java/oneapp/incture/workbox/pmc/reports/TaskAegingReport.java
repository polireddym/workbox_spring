package oneapp.incture.workbox.pmc.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;
import oneapp.incture.workbox.poadapter.dao.TaskOwnersDao;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */
public class TaskAegingReport implements Report {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskAegingReport.class);	

	@Override
	public PMCReportBaseDto getData(ReportPayload payload,Object dao) {
		logger.error("[PMC] REPORT - TaskAegingReport  - getData() - Started with ReportPayload - " + payload);
		TaskOwnersDao taskOwnersDao=(TaskOwnersDao) dao;
		if (!ServicesUtil.isEmpty(payload)) {
			return taskOwnersDao.getTaskAgeing(payload.getProcessName(), payload.getUsersList(), payload.getStatus(),payload.getRequestId(), payload.getLabelValue());
		}
		return null;
	}
}
