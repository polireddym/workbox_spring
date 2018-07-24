package oneapp.incture.workbox.pmc.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ProcessDetailsDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;
import oneapp.incture.workbox.poadapter.dao.ProcessEventsDao;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */

public class ProcessByDurationReport implements Report {

	private static final Logger logger = LoggerFactory.getLogger(ProcessByDurationReport.class);


	@Override
	public PMCReportBaseDto getData(ReportPayload payload,Object dao) {
		logger.error("[PMC] REPORT - ProcessByDurationReport  - getData() - Started with ReportPayload - " + payload);
		ProcessEventsDao processEventsDao=(ProcessEventsDao) dao;
		if (!ServicesUtil.isEmpty(payload)) {
			return processEventsDao.getProcessByDuration(new ProcessDetailsDto(payload.getProcessName(), payload.getStartDayFrom(),
					payload.getStartDayTo(), payload.getPage()));
		}
		return null;
	}

}
