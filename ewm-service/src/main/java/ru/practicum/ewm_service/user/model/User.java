package ru.practicum.ewm_service.user.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 512)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
}
