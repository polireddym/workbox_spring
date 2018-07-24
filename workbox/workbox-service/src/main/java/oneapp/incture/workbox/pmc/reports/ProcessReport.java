package oneapp.incture.workbox.pmc.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;
import oneapp.incture.workbox.poadapter.dao.ProcessEventsDao;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */
public class ProcessReport implements Report {

	
	private static final Logger logger = LoggerFactory.getLogger(ProcessReport.class);

	@Override
	public PMCReportBaseDto getData(ReportPayload payload,Object dao) {
		logger.error("[PMC] REPORT - ProcessReport  - getData() - Started with ReportPayload - " + payload);
		ProcessEventsDao processEventsDao=(ProcessEventsDao) dao;
		if (!ServicesUtil.isEmpty(payload)) {
			return processEventsDao.getProcessAgeing(payload.getGraphType(), payload.getProcessName(), payload.getReportAgingDtos());
		}
		return null;
	}

}
