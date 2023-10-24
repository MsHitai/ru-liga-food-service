package ru.liga.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.liga.model.enums.RestaurantStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurants_seq")
    @SequenceGenerator(name = "restaurants_seq", sequenceName = "restaurants_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "TEXT", name = "address")
    private String address;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @ToString.Exclude
    private List<Order> orders;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ?
                ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Restaurant that = (Restaurant) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass().hashCode() : getClass().hashCode();
    }
}
