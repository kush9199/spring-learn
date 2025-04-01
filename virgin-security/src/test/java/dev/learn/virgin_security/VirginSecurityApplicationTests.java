package dev.learn.virgin_security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learn.virgin_security.entity.Member;
import dev.learn.virgin_security.repository.MemberRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VirginSecurityApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@Order(1)
	public void registerMember() throws Exception {
		Member member = new Member();
		member.setUsername("admin");
		member.setPassword("admin");
		member.setFirstName("admin");
		member.setLastName("admin");

		mvc.perform(post("/api/v1/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(member)))
				.andExpect(status().isOk());

	}

}
