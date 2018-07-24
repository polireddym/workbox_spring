package oneapp.incture.workbox.pmc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.ProcessConfigDto;
import oneapp.incture.workbox.pmc.dto.ProcessListDto;
import oneapp.incture.workbox.pmc.dto.responses.ProcessConfigResponse;
import oneapp.incture.workbox.pmc.dto.responses.WorkloadRangeResponse;
import oneapp.incture.workbox.pmc.services.ConfigurationFacadeLocal;




@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/config", produces = "application/json")
public class ConfigurationRest {

	@Autowired
	ConfigurationFacadeLocal config;

	
	@RequestMapping(value = "/processnames", method = RequestMethod.GET)
	public ProcessListDto getAllProcessNames() {
		return config.getAllProcessNames();
	}

	
	@RequestMapping(value = "/labels", method = RequestMethod.GET)
	public ProcessConfigResponse getUserBusinessLabels() {
		return config.getUserBusinessLabels();
	}
	
	@RequestMapping(value = "/adminLabels", method = RequestMethod.GET)
	public ProcessConfigResponse getAllBusinessLabels() {
		return config.getAllBusinessLabels();
	}
	
	
	@RequestMapping(value = "/{processName}", method = RequestMethod.POST)
	public ProcessConfigDto getBusinessLabelByProcessName(@PathVariable("processName") String processName) {
		return config.getBusinessLabelByProcessName(processName);
	}


	@RequestMapping(value = "/workloadrange", method = RequestMethod.GET)
	public WorkloadRangeResponse getWorkLoadRange() {
		return config.getWorkLoadRange();
	}

}
