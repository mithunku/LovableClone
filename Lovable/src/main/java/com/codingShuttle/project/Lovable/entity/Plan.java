package com.codingShuttle.project.Lovable.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String stripePriceId; //this price id wiil be shared to stripe when we buy a plan
    Integer maxProjects;//max projects that you can create
    Integer maxTokensPerDay;
    Integer maxPreviews;//Max previews that are available since preview involve running code and it utilizes the resources so we cannot be free
    Boolean unlimitedAi; //unlimited accesee to LLM
    //if active is true plan is active else inactive
    Boolean active;

}
