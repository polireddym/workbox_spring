package oneapp.incture.workbox.pmc.services;

import oneapp.incture.workbox.pmc.dto.AdminControlDto;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

public interface AdminControlFacadeLocal {

	
	public ResponseMessage deleteProcessConfig(String processName);

	public AdminControlDto getAdminConfigurationData();

	public ResponseMessage createUpdateDataAdmin(AdminControlDto adminControlDto);

	//public ResponseMessage createReportAging(ReportAgingDto dto);

	//ResponseMessage createProcessConfig(ProcessConfigDto dto);
}
