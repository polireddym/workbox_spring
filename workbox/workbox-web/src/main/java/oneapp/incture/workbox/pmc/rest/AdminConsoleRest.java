package oneapp.incture.workbox.pmc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.AdminControlDto;
import oneapp.incture.workbox.pmc.services.AdminControlFacadeLocal;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/admin", produces = "application/json")
public class AdminConsoleRest {

	@Autowired
	private AdminControlFacadeLocal adminControlFacadeLocal;

	@RequestMapping(value = "/configurations", method = RequestMethod.GET)
	public AdminControlDto getAdminConfigurationData() {
		return adminControlFacadeLocal.getAdminConfigurationData();
	}

	@RequestMapping(value = "/delete/{processName}", method = RequestMethod.POST)
	public ResponseMessage deleteProcessConfig(@PathVariable("processName") String processName) {
		return adminControlFacadeLocal.deleteProcessConfig(processName);
	}

	@RequestMapping(value = "/configurations", method = RequestMethod.POST)
	public ResponseMessage createUpdateDataAdmin(AdminControlDto adminControlDto) {
		return adminControlFacadeLocal.createUpdateDataAdmin(adminControlDto);
	}

}
