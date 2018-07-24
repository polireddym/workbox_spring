package oneapp.incture.workbox.pmc.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.incture.pmc.poadapter.services.UserDetailsDto;
import com.incture.pmc.poadapter.services.UserDto;

import oneapp.incture.workbox.pmc.dto.RoleOfPmcDto;
import oneapp.incture.workbox.pmc.dto.UserWorkloadDetailsDto;
import oneapp.incture.workbox.pmc.dto.responses.GroupInfoDtoResponse;
import oneapp.incture.workbox.pmc.dto.responses.RoleInfoResponse;
import oneapp.incture.workbox.pmc.dto.responses.UserDetailsResponse;
import oneapp.incture.workbox.pmc.dto.responses.UserDtoResponse;
import oneapp.incture.workbox.pmc.dto.responses.UserGroupDtoResponse;
import oneapp.incture.workbox.pmc.wsdlconsumers.UMEManagementEngineConsumer;
import oneapp.incture.workbox.poadapter.dto.ResponseMessage;


public class UserManagementRest {

	@Autowired
	private UMEManagementEngineConsumer umeConsumer;

	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	public UserGroupDtoResponse getAllGroups() {
		return umeConsumer.getAllUserGroup();
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public RoleInfoResponse getAllRoles() {
		return umeConsumer.getAllUserRole();
	}

	@RequestMapping(value = "/users/{userSearch}", method = RequestMethod.GET)
	public UserDtoResponse getAllUsers(@PathVariable("userSearch") String userSearch) {
		return umeConsumer.getAllUsers(userSearch);
	}

	@RequestMapping(value = "/pmcUserRole", method = RequestMethod.GET)
	public RoleOfPmcDto getParticularUserRoleByUserId() {
		return umeConsumer.getPmcUserRolesByLoggedInUser();
	}

	@RequestMapping(value = "/info/{userId}", method = RequestMethod.GET)
	public UserWorkloadDetailsDto getUserInformation(@PathVariable("userId") String userId) {
		return umeConsumer.getUserInformation(userId);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public UserDetailsDto getLoggedInUser() {
		return umeConsumer.getLoggedInUser();
	}

	@RequestMapping(value = "/userGroupById/{userId}", method = RequestMethod.GET)
	public GroupInfoDtoResponse getUserGroupsById(@PathVariable("userId") String userId) {
		return umeConsumer.getUserGroupByuserId(userId);
	}

	@RequestMapping(value = "/usersByRole/{roleUniqueName}", method = RequestMethod.GET)
	public UserDetailsResponse getUsersByRoleUniqueName(@PathVariable("roleUniqueName") String roleUniqueName) {
		return umeConsumer.getUsersByRole(roleUniqueName);
	}

	@RequestMapping(value = "/userRoleById/{userId}", method = RequestMethod.GET)
	public RoleInfoResponse getUserRoleByUserId(@PathVariable("userId") String userId) {
		return umeConsumer.getUserRoleByuserId(userId);

	}

	@RequestMapping(value = "/getUsersAssignedInGroup/{groupId}", method = RequestMethod.GET)
	public UserDtoResponse getUserDetailsAssignedInGroup(@PathVariable("groupId") String groupId) {
		return umeConsumer.getUserDetailsAssignedInGroup(groupId);
	}

	@RequestMapping(value = "/getUserIdsAssignedInGroup/{groupId}", method = RequestMethod.GET)
	public UserDtoResponse getUsersAssignedInGroup(@PathVariable("groupId") String groupId) {
		UserDtoResponse userDtoResponse = new UserDtoResponse();
		ResponseMessage responseMessage = new ResponseMessage();
		List<String> stringList = umeConsumer.getUsersAssignedInGroup(groupId);
		List<UserDto> userDtoList = new ArrayList<UserDto>();
		for (String st : stringList) {
			UserDto dto = new UserDto();
			dto.setLoginId(st);
			userDtoList.add(dto);
		}
		responseMessage.setMessage("Users Fetched Sucessfully");
		responseMessage.setStatus("SUCCESS");
		responseMessage.setStatusCode("1");
		userDtoResponse.setResponseMessage(responseMessage);
		userDtoResponse.setUserDtos(userDtoList);
		return userDtoResponse;
	}
}
