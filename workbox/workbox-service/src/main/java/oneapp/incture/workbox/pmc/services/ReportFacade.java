package oneapp.incture.workbox.pmc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.pmc.dto.DownloadReportResponseDto;
import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ReportFormattedDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;
import oneapp.incture.workbox.pmc.reports.DownloadReportFactoryGenerator;
import oneapp.incture.workbox.pmc.reports.File;
import oneapp.incture.workbox.pmc.reports.Report;
import oneapp.incture.workbox.pmc.reports.ReportObjectFactory;
import oneapp.incture.workbox.pmc.reports.ServiceFactory;
import oneapp.incture.workbox.pmc.wsdlconsumers.UMEManagementEngineConsumer;
import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * Session Bean implementation class ReportFacade
 */
/**
 * @author Saurabh
 *
 */
@Service("ReportFacade")
public class ReportFacade implements ReportFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ReportFacade.class);

	/*
	 * @EJB private UserManagementFacadeWsdlConsumerLocal webService;
	 */

	@Autowired
	private UMEManagementEngineConsumer umeConsumer;

	@Autowired
	private ConfigurationFacadeLocal config;
	@Autowired
	private ReportObjectFactory reportObjectFactory;

	@Override
	public DownloadReportResponseDto generateReport(ReportPayload payload) {
		logger.error("[PMC] REPORT - ReportFacade  - generateReport() - Started with ReportPayload - " + payload);
		DownloadReportResponseDto response = null;
		if (!ServicesUtil.isEmpty(payload) && !ServicesUtil.isEmpty(payload.getFileFormate())
				&& !ServicesUtil.isEmpty(payload.getReportName())) {
			logger.error("payload - Report Name =  " + payload.getReportName() + "  File Formate = "
					+ payload.getFileFormate());
			if (!ServicesUtil.isEmpty(payload.getUserGroup())
					&& !payload.getUserGroup().equals(PMCConstant.SEARCH_SMALL_ALL)) {
				payload.setUsersList(umeConsumer.getUsersAssignedInGroup(payload.getUserGroup()));
				logger.error("UserList - " + payload.getUsersList());
			}
			if (!ServicesUtil.isEmpty(payload.getGraphType())
					&& PMCConstant.GRAPH_TREND_MONTH.equalsIgnoreCase(payload.getGraphType())) {
				payload.setReportAgingDtos(config.getAgeingBuckets(PMCConstant.PROCESS_AGING_REPORT));
			}
			if (ServicesUtil.isEmpty(payload.getUserId())) {
				payload.setUserId(umeConsumer.getLoggedInUser().getUserId());
			}

			ServiceFactory reportFactory = DownloadReportFactoryGenerator.getReportFactory();

			Report report = reportFactory.getReport(payload.getReportName());
			PMCReportBaseDto pmcReportBaseDto = report.getData(payload,reportObjectFactory.getReportObject(payload.getReportName()));
			logger.error("[PMC] REPORT - ReportFacade  - generateReport() - pmcReportBaseDto- " + pmcReportBaseDto);

			ServiceFactory downloadFactory = DownloadReportFactoryGenerator.getDownloadFactory();
			File file = downloadFactory.getFile(payload.getFileFormate());
			ReportFormattedDto formattedDto = file.setSheetName(payload.getReportName());
			response = file.pushData(file.exportToFormattedDto(pmcReportBaseDto, formattedDto));
		}
		logger.error("[PMC] REPORT - ReportFacade  - generateReport() - Ended with ReportPayload - " + response);
		return response;
	}
}
