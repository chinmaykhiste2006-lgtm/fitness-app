package com.fitness.aiservice.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomRecommendation {

    private String type;
    private String heartRate;
    private String waterIntake;
    private String distance;
    private String eatBefore;
    private String eatAfter;
    private String whyThisRoutine;
    private Safety safety;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Safety {
        private List<String> dos;
        private List<String> donts;
    }
}
