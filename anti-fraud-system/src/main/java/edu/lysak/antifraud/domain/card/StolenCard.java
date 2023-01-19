package edu.lysak.antifraud.domain.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stolen_card")
public class StolenCard implements Comparable<StolenCard> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Numpber should not be empty")
    @Column(name = "number")
    private String number;

    // TODO: 17.01.2023 is it okay fot entity to be comparable?
    @Override
    public int compareTo(StolenCard other) {
        return Long.compare(this.id, other.id);
    }
}
