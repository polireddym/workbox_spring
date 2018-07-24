package oneapp.incture.workbox.pmc.reports;

import org.springframework.stereotype.Component;

import oneapp.incture.workbox.pmc.dto.DownloadReportResponseDto;
import oneapp.incture.workbox.pmc.dto.ReportFormattedDto;

/**
 * @author Saurabh
 *
 */
@Component("PDF")
public class PDF extends File{

	@Override
	public DownloadReportResponseDto pushData(ReportFormattedDto reportFormattedDto) {
		return null;
	}
}
