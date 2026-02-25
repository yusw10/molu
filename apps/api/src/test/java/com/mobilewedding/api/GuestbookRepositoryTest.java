package com.mobilewedding.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    void findAllByOrderByCreatedAtDesc_returnsNewestFirst() {
        GuestbookEntry older = new GuestbookEntry();
        older.setName("첫번째");
        older.setMessage("먼저 작성");
        older.setAttending(false);
        guestbookRepository.saveAndFlush(older);

        GuestbookEntry newer = new GuestbookEntry();
        newer.setName("두번째");
        newer.setMessage("나중 작성");
        newer.setAttending(true);
        guestbookRepository.saveAndFlush(newer);

        List<GuestbookEntry> entries = guestbookRepository.findAllByOrderByCreatedAtDesc();

        assertThat(entries).hasSize(2);
        assertThat(entries.get(0).getName()).isEqualTo("두번째");
        assertThat(entries.get(1).getName()).isEqualTo("첫번째");
        assertThat(entries.get(0).getCreatedAt()).isNotNull();
    }
}
