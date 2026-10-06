package com.happy.center.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Body of POST /api/contacts. Mirrors {@code ContactRequest} in the Angular app. */
public record CreateContactRequest(
        @NotNull(message = "Vui lòng chọn người học.")
        ContactAudience audience,

        @NotBlank(message = "Vui lòng nhập họ tên.")
        @Size(max = 100, message = "Họ tên tối đa 100 ký tự.")
        String fullName,

        @NotBlank(message = "Vui lòng nhập số điện thoại.")
        @Pattern(regexp = PHONE_REGEX, message = "Số điện thoại chưa đúng (10 số, bắt đầu bằng 0).")
        String phone,

        @Email(message = "Email chưa đúng định dạng.")
        @Size(max = 150, message = "Email tối đa 150 ký tự.")
        String email,

        @Size(max = 5, message = "Chọn tối đa 5 khóa học.")
        List<@NotNull Course> courses,

        @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự.")
        String note
) {
    /** 0xxxxxxxxx or +84xxxxxxxxx, optionally separated by spaces, dots or dashes. */
    public static final String PHONE_REGEX = "^(?:\\+84|0)(?:[\\s.-]?\\d){9}$";
}
