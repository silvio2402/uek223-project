package com.ourspace.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourspace.backend.domain.mylistentry.dto.MyListEntryDTO;
import com.ourspace.backend.domain.mylistentry.dto.PostMyListEntryDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EndpointIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@LocalServerPort
	private int port;

	private String jwtUserToken;
	private String jwtAdminToken;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() throws Exception {
		jwtUserToken = obtainJwtToken("user@example.com");
		jwtAdminToken = obtainJwtToken("admin@example.com");
	}

	private String obtainJwtToken(String email) throws Exception {
		Map<String, String> requestMap = Map.of(
				"email", email,
				"password", "1234");
		String requestBody = objectMapper.writeValueAsString(requestMap);

		ResultActions result = mockMvc.perform(post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody));

		String responseBody = result.andReturn().getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);
		return responseMap.get("accessToken");
	}

	@Test
	public void testSetupIsWorking() throws Exception {
		mockMvc.perform(get("/mylistentry")
				.header("Authorization", "Bearer " + jwtUserToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testCreateMyListEntry() throws Exception {
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		ResultActions result = mockMvc.perform(post("/mylistentry")
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated());

		// Validate the response content
		String responseBody = result.andReturn().getResponse().getContentAsString();
		MyListEntryDTO createdEntry = objectMapper.readValue(responseBody, MyListEntryDTO.class);

		assertNotNull(createdEntry.getId());
		assertEquals(myListEntryDTO.getTitle(), createdEntry.getTitle());
		assertEquals(myListEntryDTO.getText(), createdEntry.getText());
		assertEquals(myListEntryDTO.getImportance(), createdEntry.getImportance());
		assertNotNull(createdEntry.getUser_id());
		assertNotNull(createdEntry.getCreation_date());
	}

	@Test
	public void testRetrieveMyListEntryById() throws Exception {
		// Create a MyListEntry first
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		ResultActions createResult = mockMvc.perform(post("/mylistentry")
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody));

		String responseBody = createResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO createdMyListEntry = objectMapper.readValue(responseBody, MyListEntryDTO.class);
		UUID id = createdMyListEntry.getId();

		// Retrieve the created entry by ID
		ResultActions getResult = mockMvc.perform(get("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtUserToken));

		getResult.andExpect(status().isOk());

		// Validate the retrieved entry matches what was created
		String getResponseBody = getResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO retrievedEntry = objectMapper.readValue(getResponseBody, MyListEntryDTO.class);

		assertEquals(createdMyListEntry.getId(), retrievedEntry.getId());
		assertEquals(createdMyListEntry.getTitle(), retrievedEntry.getTitle());
		assertEquals(createdMyListEntry.getText(), retrievedEntry.getText());
		assertEquals(createdMyListEntry.getImportance(), retrievedEntry.getImportance());
		assertEquals(createdMyListEntry.getUser_id(), retrievedEntry.getUser_id());
	}

	@Test
	public void testUpdateMyListEntry() throws Exception {
		// Create a MyListEntry first
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		ResultActions createResult = mockMvc.perform(post("/mylistentry")
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody));

		String responseBody = createResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO createdMyListEntry = objectMapper.readValue(responseBody, MyListEntryDTO.class);
		UUID id = createdMyListEntry.getId();
		UUID user_id = createdMyListEntry.getUser_id();

		// Update the created entry
		MyListEntryDTO updateMyListEntryDTO = new MyListEntryDTO();
		updateMyListEntryDTO.setId(id);
		updateMyListEntryDTO.setTitle("Updated Test Entry");
		updateMyListEntryDTO.setText("Updated Test Text");
		updateMyListEntryDTO.setImportance(2);
		updateMyListEntryDTO.setUser_id(user_id);
		String updateRequestBody = objectMapper.writeValueAsString(updateMyListEntryDTO);

		ResultActions updateResult = mockMvc.perform(put("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateRequestBody));

		updateResult.andExpect(status().isOk());

		// Validate the updated entry
		String updateResponseBody = updateResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO updatedEntry = objectMapper.readValue(updateResponseBody, MyListEntryDTO.class);

		assertEquals(id, updatedEntry.getId());
		assertEquals("Updated Test Entry", updatedEntry.getTitle());
		assertEquals("Updated Test Text", updatedEntry.getText());
		assertEquals(Integer.valueOf(2), updatedEntry.getImportance());
		assertEquals(user_id, updatedEntry.getUser_id());

		// Verify that the update persisted by retrieving it again
		ResultActions verifyResult = mockMvc.perform(get("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtUserToken))
				.andExpect(status().isOk());

		String verifyResponseBody = verifyResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO verifiedEntry = objectMapper.readValue(verifyResponseBody, MyListEntryDTO.class);

		assertEquals("Updated Test Entry", verifiedEntry.getTitle());
	}

	@Test
	public void testDeleteMyListEntry() throws Exception {
		// Create a MyListEntry first
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		ResultActions createResult = mockMvc.perform(post("/mylistentry")
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody));

		String responseBody = createResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO createdMyListEntry = objectMapper.readValue(responseBody, MyListEntryDTO.class);
		UUID id = createdMyListEntry.getId();

		// Delete the created entry
		ResultActions deleteResult = mockMvc
				.perform(delete("/mylistentry/" + id)
						.header("Authorization", "Bearer " + jwtUserToken));

		deleteResult.andExpect(status().isNoContent());

		// Verify the entry is actually deleted
		mockMvc.perform(get("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtUserToken))
				.andExpect(status().isNotFound());
	}

	private PostMyListEntryDTO createSamplePostMyListEntryDTO() {
		PostMyListEntryDTO myListEntryDTO = new PostMyListEntryDTO();
		myListEntryDTO.setTitle("Test Entry");
		myListEntryDTO.setText("Test Text");
		myListEntryDTO.setImportance(1);
		return myListEntryDTO;
	}

	@Test
	public void testAuthenticateSuccess() throws Exception {
		String requestBody = objectMapper.writeValueAsString(Map.of(
				"email", "user@example.com",
				"password", "1234"));

		ResultActions result = mockMvc.perform(post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk());

		// Validate JWT token is returned and valid
		String responseBody = result.andReturn().getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);

		assertNotNull(responseMap.get("accessToken"));
		assertTrue(responseMap.get("accessToken").length() > 0);

		// Verify the token works by making an authorized request
		mockMvc.perform(get("/mylistentry")
				.header("Authorization", "Bearer " + responseMap.get("accessToken")))
				.andExpect(status().isOk());
	}

	@Test
	public void testAuthenticateInvalidCredentials() throws Exception {
		Map<String, String> requestMap = Map.of(
				"email", "user@example.com",
				"password", "invalid_password");
		String requestBody = objectMapper.writeValueAsString(requestMap);

		mockMvc.perform(post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testRefreshTokenSuccess() throws Exception {
		// Get initial valid JWT token
		String refreshToken = obtainJwtToken("user@example.com");

		ResultActions result = mockMvc.perform(post("/auth/refresh")
				.header("Authorization", "Bearer " + refreshToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the response contains a new token
		String responseBody = result.andReturn().getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);

		assertNotNull(responseMap.get("accessToken"));
		assertTrue(responseMap.get("accessToken").length() > 0);

		// Verify the new token is different from the original
		assertNotEquals(refreshToken, responseMap.get("accessToken"));

		// Verify the new token works by making an authorized request
		mockMvc.perform(get("/mylistentry")
				.header("Authorization", "Bearer " + responseMap.get("accessToken")))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateMyListEntryByRegularUserUnauthorized() throws Exception {
		// Create a MyListEntry as an admin
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		ResultActions createResult = mockMvc.perform(post("/mylistentry")
				.header("Authorization", "Bearer " + jwtAdminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody));

		String responseBody = createResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO createdMyListEntry = objectMapper.readValue(responseBody, MyListEntryDTO.class);
		UUID id = createdMyListEntry.getId();
		UUID user_id = createdMyListEntry.getUser_id();

		// Verify the entry was created successfully
		assertNotNull(id);
		assertNotNull(user_id);
		assertEquals(myListEntryDTO.getTitle(), createdMyListEntry.getTitle());

		// Attempt to update the entry as a regular user
		MyListEntryDTO updateMyListEntryDTO = new MyListEntryDTO();
		updateMyListEntryDTO.setId(id);
		updateMyListEntryDTO.setTitle("Updated Test Entry");
		updateMyListEntryDTO.setText("Updated Test Text");
		updateMyListEntryDTO.setImportance(2);
		updateMyListEntryDTO.setUser_id(user_id);
		String updateRequestBody = objectMapper.writeValueAsString(updateMyListEntryDTO);

		ResultActions updateResult = mockMvc.perform(put("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateRequestBody))
				.andExpect(status().isForbidden());

		// Verify the entry wasn't updated by checking it's still the same
		ResultActions getResult = mockMvc.perform(get("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());

		String getResponseBody = getResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO retrievedEntry = objectMapper.readValue(getResponseBody, MyListEntryDTO.class);

		assertEquals(createdMyListEntry.getTitle(), retrievedEntry.getTitle());
		assertEquals(createdMyListEntry.getText(), retrievedEntry.getText());
	}

	@Test
	public void testDeleteMyListEntryByRegularUserUnauthorized() throws Exception {
		// Create a MyListEntry as an admin
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		ResultActions createResult = mockMvc.perform(post("/mylistentry")
				.header("Authorization", "Bearer " + jwtAdminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody));

		String responseBody = createResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO createdMyListEntry = objectMapper.readValue(responseBody, MyListEntryDTO.class);
		UUID id = createdMyListEntry.getId();

		// Verify the entry was created successfully
		assertNotNull(id);
		assertEquals(myListEntryDTO.getTitle(), createdMyListEntry.getTitle());

		// Attempt to delete the entry as a regular user
		ResultActions deleteResult = mockMvc
				.perform(delete("/mylistentry/" + id)
						.header("Authorization", "Bearer " + jwtUserToken));

		deleteResult.andExpect(status().isForbidden());

		// Verify the entry still exists
		ResultActions getResult = mockMvc.perform(get("/mylistentry/" + id)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());

		String getResponseBody = getResult.andReturn().getResponse().getContentAsString();
		MyListEntryDTO retrievedEntry = objectMapper.readValue(getResponseBody, MyListEntryDTO.class);

		assertEquals(id, retrievedEntry.getId());
	}

	@Test
	public void testRetrieveAllMyListEntriesUnauthenticated() throws Exception {
		mockMvc.perform(get("/mylistentry"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testRetrieveMyListEntryByIdUnauthenticated() throws Exception {
		mockMvc.perform(get("/mylistentry/" + UUID.randomUUID()))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testCreateMyListEntryUnauthenticated() throws Exception {
		PostMyListEntryDTO myListEntryDTO = createSamplePostMyListEntryDTO();
		String requestBody = objectMapper.writeValueAsString(myListEntryDTO);

		mockMvc.perform(post("/mylistentry")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testUpdateMyListEntryUnauthenticated() throws Exception {
		MyListEntryDTO updateMyListEntryDTO = new MyListEntryDTO();
		updateMyListEntryDTO.setId(UUID.randomUUID());
		updateMyListEntryDTO.setTitle("Updated Test Entry");
		updateMyListEntryDTO.setText("Updated Test Text");
		updateMyListEntryDTO.setImportance(2);
		updateMyListEntryDTO.setUser_id(UUID.randomUUID());
		String updateRequestBody = objectMapper.writeValueAsString(updateMyListEntryDTO);

		mockMvc.perform(put("/mylistentry/" + UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateRequestBody))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testDeleteMyListEntryUnauthenticated() throws Exception {
		mockMvc.perform(delete("/mylistentry/" + UUID.randomUUID()))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testRetrieveMyListEntryByIdInvalidId() throws Exception {
		mockMvc.perform(get("/mylistentry/invalid-uuid")
				.header("Authorization", "Bearer " + jwtUserToken))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdateMyListEntryInvalidId() throws Exception {
		MyListEntryDTO updateMyListEntryDTO = new MyListEntryDTO();
		updateMyListEntryDTO.setId(UUID.randomUUID());
		updateMyListEntryDTO.setTitle("Updated Test Entry");
		updateMyListEntryDTO.setText("Updated Test Text");
		updateMyListEntryDTO.setImportance(2);
		updateMyListEntryDTO.setUser_id(UUID.randomUUID());
		String updateRequestBody = objectMapper.writeValueAsString(updateMyListEntryDTO);

		mockMvc.perform(put("/mylistentry/invalid-uuid")
				.header("Authorization", "Bearer " + jwtUserToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateRequestBody))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteMyListEntryInvalidId() throws Exception {
		mockMvc.perform(delete("/mylistentry/invalid-uuid")
				.header("Authorization", "Bearer " + jwtUserToken))
				.andExpect(status().isBadRequest());

		// Create a user first
		String createRequestBody = objectMapper.writeValueAsString(Map.of(
				"email", "retrieve@example.com",
				"password", "password",
				"firstName", "Retrieve",
				"lastName", "User"));

		ResultActions createResult = mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequestBody))
				.andExpect(status().isCreated());

		String createResponseBody = createResult.andReturn().getResponse().getContentAsString();
		Map<String, Object> createdUser = objectMapper.readValue(createResponseBody, Map.class);
		UUID userId = UUID.fromString((String) createdUser.get("id"));

		// Retrieve the created user by ID
		mockMvc.perform(get("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testRetrieveAllUsers() throws Exception {
		ResultActions result = mockMvc.perform(get("/user")
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());

		// Validate the response contains a list of users
		String responseBody = result.andReturn().getResponse().getContentAsString();
		List<Map<String, Object>> users = objectMapper.readValue(responseBody, List.class);

		assertNotNull(users);
		assertFalse(users.isEmpty());

		// Check that each user has the required fields
		for (Map<String, Object> user : users) {
			assertNotNull(user.get("id"));
			assertNotNull(user.get("email"));
		}
	}

	@Test
	public void testRetrieveAllUsersUnauthenticated() throws Exception {
		mockMvc.perform(get("/user"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testRegisterUser() throws Exception {
		String email = "newuser@example.com";
		String firstName = "New";
		String lastName = "User";

		String requestBody = objectMapper.writeValueAsString(Map.of(
				"email", email,
				"password", "password",
				"firstName", firstName,
				"lastName", lastName));

		ResultActions result = mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated());

		// Validate the response contains the created user with correct data
		String responseBody = result.andReturn().getResponse().getContentAsString();
		Map<String, Object> createdUser = objectMapper.readValue(responseBody, Map.class);

		assertNotNull(createdUser.get("id"));
		assertEquals(email, createdUser.get("email"));
		assertEquals(firstName, createdUser.get("firstName"));
		assertEquals(lastName, createdUser.get("lastName"));

		// Verify the user was actually created by retrieving it
		UUID userId = UUID.fromString((String) createdUser.get("id"));
		ResultActions getResult = mockMvc.perform(get("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());

		String getResponseBody = getResult.andReturn().getResponse().getContentAsString();
		Map<String, Object> retrievedUser = objectMapper.readValue(getResponseBody, Map.class);

		assertEquals(createdUser.get("id"), retrievedUser.get("id"));
		assertEquals(email, retrievedUser.get("email"));
	}

	@Test
	public void testRegisterUserWithoutPassword() throws Exception {
		String email = "newuser2@example.com";
		String firstName = "New2";
		String lastName = "User2";

		String requestBody = objectMapper.writeValueAsString(Map.of(
				"email", email,
				"firstName", firstName,
				"lastName", lastName));

		ResultActions result = mockMvc.perform(post("/user/registerUser")
				.header("Authorization", "Bearer " + jwtAdminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated());

		// Validate the response contains the created user with correct data
		String responseBody = result.andReturn().getResponse().getContentAsString();
		Map<String, Object> createdUser = objectMapper.readValue(responseBody, Map.class);

		assertNotNull(createdUser.get("id"));
		assertEquals(email, createdUser.get("email"));
		assertEquals(firstName, createdUser.get("firstName"));
		assertEquals(lastName, createdUser.get("lastName"));

		// Verify the user was actually created by retrieving it
		UUID userId = UUID.fromString((String) createdUser.get("id"));
		ResultActions getResult = mockMvc.perform(get("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateUser() throws Exception {
		// First, create a user
		String email = "update@example.com";
		String initialfirstName = "Update";
		String initiallastName = "User";
		String updatedfirstName = "Updated";

		String createRequestBody = objectMapper.writeValueAsString(Map.of(
				"email", email,
				"password", "password",
				"firstName", initialfirstName,
				"lastName", initiallastName));

		ResultActions createResult = mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequestBody))
				.andExpect(status().isCreated());

		String createResponseBody = createResult.andReturn().getResponse().getContentAsString();
		Map<String, Object> createdUser = objectMapper.readValue(createResponseBody, Map.class);
		UUID userId = UUID.fromString((String) createdUser.get("id"));

		// Verify the user was created with the correct data
		assertEquals(email, createdUser.get("email"));
		assertEquals(initialfirstName, createdUser.get("firstName"));

		// Update the created user
		String updateRequestBody = objectMapper.writeValueAsString(Map.of(
				"id", userId.toString(),
				"email", email,
				"firstName", updatedfirstName,
				"lastName", initiallastName));

		ResultActions updateResult = mockMvc.perform(put("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateRequestBody))
				.andExpect(status().isOk());

		// Validate the update response
		String updateResponseBody = updateResult.andReturn().getResponse().getContentAsString();
		Map<String, Object> updatedUser = objectMapper.readValue(updateResponseBody, Map.class);

		assertEquals(userId.toString(), updatedUser.get("id"));
		assertEquals(email, updatedUser.get("email"));
		assertEquals(updatedfirstName, updatedUser.get("firstName"));

		// Verify the update was persisted by retrieving the user
		ResultActions getResult = mockMvc.perform(get("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isOk());

		String getResponseBody = getResult.andReturn().getResponse().getContentAsString();
		Map<String, Object> retrievedUser = objectMapper.readValue(getResponseBody, Map.class);

		assertEquals(updatedfirstName, retrievedUser.get("firstName"));
	}

	@Test
	public void testUpdateUserUnauthenticated() throws Exception {
		String requestBody = objectMapper.writeValueAsString(Map.of(
				"id", UUID.randomUUID(),
				"email", "update@example.com",
				"firstName", "Update",
				"lastName", "User"));

		mockMvc.perform(put("/user/" + UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isForbidden()); // technically 401 unauthorized wouldve been correct. But this is Noser Code
	}

	@Test
	public void testDeleteUserInvalidId() throws Exception {
		mockMvc.perform(delete("/user/invalid-uuid")
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteUser() throws Exception {
		// First, create a user
		String email = "delete@example.com";
		String createRequestBody = objectMapper.writeValueAsString(Map.of(
				"email", email,
				"password", "password",
				"firstName", "Delete",
				"lastName", "User"));

		ResultActions createResult = mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequestBody))
				.andExpect(status().isCreated());

		String createResponseBody = createResult.andReturn().getResponse().getContentAsString();
		Map<String, Object> createdUser = objectMapper.readValue(createResponseBody, Map.class);
		UUID userId = UUID.fromString((String) createdUser.get("id"));

		// Verify the user was created
		assertNotNull(userId);

		// Delete the created user
		mockMvc.perform(delete("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isNoContent());

		// Verify the user was actually deleted by trying to retrieve it
		mockMvc.perform(get("/user/" + userId)
				.header("Authorization", "Bearer " + jwtAdminToken))
				.andExpect(status().isNotFound());

		// Also ensure we can't authenticate with this user anymore
		String requestBody = objectMapper.writeValueAsString(Map.of(
				"email", email,
				"password", "password"));

		mockMvc.perform(post("/auth/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testDeleteUserUnauthenticated() throws Exception {
		mockMvc.perform(delete("/user/" + UUID.randomUUID()))
				.andExpect(status().isForbidden());
	}
}
