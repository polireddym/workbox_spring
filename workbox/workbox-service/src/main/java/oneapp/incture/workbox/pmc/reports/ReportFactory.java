package oneapp.incture.workbox.pmc.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */
public class ReportFactory implements ServiceFactory {

	private static final Logger logger = LoggerFactory.getLogger(ReportFactory.class);	

	@Override
	public Report getReport(String reportName) {
		logger.error("[PMC] REPORT - ReportFactory  - getReport() - Started with reportName - " + reportName);
		if (!ServicesUtil.isEmpty(reportName)) {
			if (PMCConstant.USER_TASK_REPORT.equalsIgnoreCase(reportName.trim())) {
				logger.error(PMCConstant.USER_TASK_REPORT);
				return new UserWorkloadReport();
			} else if (PMCConstant.PROCESS_TRACKER.equalsIgnoreCase(reportName.trim())) {
				logger.error(PMCConstant.PROCESS_TRACKER);
				return new ProcessReport();
			} else if (PMCConstant.TASK_AEGING.equalsIgnoreCase(reportName.trim())) {
				logger.error(PMCConstant.TASK_AEGING);
				return new TaskAegingReport();
			} else if (PMCConstant.PROCESS_BY_DURATION.equalsIgnoreCase(reportName.trim())) {
				logger.error(PMCConstant.PROCESS_BY_DURATION);
				return new ProcessByDurationReport();
			} else if (PMCConstant.TASK_MANAGER.equalsIgnoreCase(reportName.trim())) {
				logger.error(PMCConstant.TASK_MANAGER);
				return new TaskManagerReport();
			}
		}
		logger.error("No Report Found");
		return null;
	}

	@Override
	public File getFile(String fileFormate) {
		logger.error("NO IMPLEMENTATION -- Get Implementation in FileFactory class");
		return null;
	}

}
