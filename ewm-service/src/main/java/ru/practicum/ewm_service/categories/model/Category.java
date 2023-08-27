package ru.practicum.ewm_service.categories.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 512, unique = true)
    private String name;
}
