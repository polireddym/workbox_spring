package oneapp.incture.workbox.pmc.services;

import oneapp.incture.workbox.pmc.dto.DownloadReportResponseDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;

public interface ReportFacadeLocal {

	DownloadReportResponseDto generateReport(ReportPayload payload);

}
