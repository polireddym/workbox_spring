package oneapp.incture.workbox.pmc.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.pmc.dto.RuleManagementDto;
import oneapp.incture.workbox.pmc.dto.responses.RuleManagementResponseDto;
import oneapp.incture.workbox.pmc.services.RuleManagementFacadeLocal;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/rules", produces = "application/json")
public class RulesRest {

	@Autowired
	private RuleManagementFacadeLocal rule;

	@RequestMapping(value = "/details/{processName}", method = RequestMethod.GET)
	public RuleManagementResponseDto getRules(@PathVariable("processName") String processName) {
		return rule.getRules(processName);
	}

	@RequestMapping(value = "/updateRules", method = RequestMethod.POST)
	public ResponseMessage submit(@RequestBody List<RuleManagementDto> dtoList) {
		return rule.onSubmit(dtoList);
	}

}
