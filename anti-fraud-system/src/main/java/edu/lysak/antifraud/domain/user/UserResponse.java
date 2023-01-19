package edu.lysak.antifraud.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Comparable<UserResponse> {
    private long id;
    private String name;
    private String username;
    private Role role;

    @Override
    public int compareTo(UserResponse other) {
        return Long.compare(this.id, other.id);
    }
}
