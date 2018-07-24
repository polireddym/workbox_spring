package oneapp.incture.workbox.pmc.reports;

import oneapp.incture.workbox.pmc.dto.PMCReportBaseDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;

/**
 * @author Saurabh
 *
 */
public interface Report {

	public PMCReportBaseDto getData(ReportPayload payload,Object dao);

}
