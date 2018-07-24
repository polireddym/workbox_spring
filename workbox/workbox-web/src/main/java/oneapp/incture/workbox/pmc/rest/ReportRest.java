package oneapp.incture.workbox.pmc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.DownloadReportResponseDto;
import oneapp.incture.workbox.pmc.dto.ReportPayload;
import oneapp.incture.workbox.pmc.services.ReportFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/report", produces = "application/json")
public class ReportRest {

	@Autowired
	private ReportFacadeLocal reportLocal;

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public DownloadReportResponseDto generateReport(@RequestBody ReportPayload payload) {
		return reportLocal.generateReport(payload);
	}
}
