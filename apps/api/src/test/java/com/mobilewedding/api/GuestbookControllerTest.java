package com.mobilewedding.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuestbookController.class)
class GuestbookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GuestbookRepository guestbookRepository;

    @Test
    void getAll_returnsEntries() throws Exception {
        GuestbookEntry entry = new GuestbookEntry();
        ReflectionTestUtils.setField(entry, "id", UUID.fromString("11111111-1111-1111-1111-111111111111"));
        ReflectionTestUtils.setField(entry, "name", "하객A");
        ReflectionTestUtils.setField(entry, "message", "축하합니다");
        ReflectionTestUtils.setField(entry, "attending", true);
        ReflectionTestUtils.setField(entry, "createdAt", OffsetDateTime.parse("2026-02-24T10:00:00+09:00"));

        given(guestbookRepository.findAllByOrderByCreatedAtDesc()).willReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/guestbook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("하객A"))
                .andExpect(jsonPath("$[0].message").value("축하합니다"))
                .andExpect(jsonPath("$[0].attending").value(true));
    }

    @Test
    void create_withValidPayload_savesTrimmedValues() throws Exception {
        given(guestbookRepository.save(any(GuestbookEntry.class))).willAnswer(invocation -> {
            GuestbookEntry input = invocation.getArgument(0);
            ReflectionTestUtils.setField(input, "id", UUID.fromString("22222222-2222-2222-2222-222222222222"));
            ReflectionTestUtils.setField(input, "createdAt", OffsetDateTime.parse("2026-02-24T11:00:00+09:00"));
            return input;
        });

        GuestbookDto.CreateRequest request = new GuestbookDto.CreateRequest("  하객B  ", "  행복하세요!  ", false);

        mockMvc.perform(post("/api/v1/guestbook")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("하객B"))
                .andExpect(jsonPath("$.message").value("행복하세요!"))
                .andExpect(jsonPath("$.attending").value(false));

        ArgumentCaptor<GuestbookEntry> captor = ArgumentCaptor.forClass(GuestbookEntry.class);
        verify(guestbookRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("하객B");
        assertThat(captor.getValue().getMessage()).isEqualTo("행복하세요!");
    }

    @Test
    void create_withBlankName_returnsBadRequest() throws Exception {
        GuestbookDto.CreateRequest request = new GuestbookDto.CreateRequest("", "축하해요", true);

        mockMvc.perform(post("/api/v1/guestbook")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(guestbookRepository);
    }
}
