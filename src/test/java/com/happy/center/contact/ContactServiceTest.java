package com.happy.center.contact;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContactServiceTest {

    @Test
    void normalizesVietnamesePhoneNumbers() {
        assertThat(ContactService.normalizePhone("0912 345 678")).isEqualTo("0912345678");
        assertThat(ContactService.normalizePhone("091.234.5678")).isEqualTo("0912345678");
        assertThat(ContactService.normalizePhone("+84912345678")).isEqualTo("0912345678");
        assertThat(ContactService.normalizePhone("+84 912-345-678")).isEqualTo("0912345678");
    }
}
