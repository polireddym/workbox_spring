package oneapp.incture.workbox.pmc.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.util.PMCConstant;
import oneapp.incture.workbox.util.ServicesUtil;

/**
 * @author Saurabh
 *
 */
@Component("FileFactory")
public class FileFactory implements ServiceFactory {

	private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

	@Override
	public File getFile(String fileFormate) {
		logger.error("[PMC] REPORT - FileFactory  - getFile() - Started with FileFormate - " + fileFormate);
		if (!ServicesUtil.isEmpty(fileFormate)) {
			if (PMCConstant.REPORT_EXCEL.equalsIgnoreCase(fileFormate.trim())) {
				logger.error("Excel Formate");
				return new Excel();
			} else if (PMCConstant.REPORT_PDF.equalsIgnoreCase(fileFormate.trim())) {
				logger.error("PDF Formate");
				return new PDF();
			}
		}
		return null;
	}

	@Override
	public Report getReport(String reportName) {
		logger.error("NO IMPLEMENTATION -- Get Implementation in ReportFactory class");
		return null;
	}

}
