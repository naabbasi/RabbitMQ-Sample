package edu.learn.prod_cons;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"password"})
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
}
