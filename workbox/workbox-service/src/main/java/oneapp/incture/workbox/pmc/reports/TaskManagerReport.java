package oneapp.incture.workbox.pmc.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oneapp.incture.workbox.pmc.dto.ManageTasksRequestDto;
import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;
import oneapp.incture.workbox.poadapter.dao.TaskEventsDao;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */
public class TaskManagerReport implements Report {


	private static final Logger logger=LoggerFactory.getLogger(TaskManagerReport.class);

	@Override
	public PMCReportBaseDto getData(ReportPayload payload,Object dao) {
		logger.error("[PMC] REPORT - TaskManagerReport  - getData() - Started with ReportPayload - " + payload);

		TaskEventsDao taskEventsDao=(TaskEventsDao) dao;
		if (!ServicesUtil.isEmpty(payload)) {
			return taskEventsDao.getTasksByUserAndDuration(new ManageTasksRequestDto(payload.getUserId(), payload.getStatus(), payload.getProcessName(), payload.getStartDayFrom(), payload.getStartDayTo(),
					payload.getPage(), payload.getRequestId(), payload.getLabelValue()));
		}
		return null;
	}

}
